#!/usr/bin/env python3

import json
import os
import re
import shutil
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
SOURCE_MAP_URL = re.compile(rb"sourceMappingURL=([^\s]+)")

GWT_SHOWCASES = (
    ("gwt/gwt-bootstrap3-showcase", "GwtBootstrap3Demo"),
    ("gwt/gwt-bootstrap5-showcase", "GwtBootstrap5Showcase"),
    ("gwt/gwt-bootstrap3-browser-fixtures", "Bootstrap3BrowserFixtures"),
    ("gwt/gwt-bootstrap5-browser-fixtures", "Bootstrap5BrowserFixtures"),
)

SOURCE_MODULES = (
    "gwt/gwt-bootstrap3",
    "gwt/gwt-bootstrap3-extras",
    "gwt/gwt-bootstrap3-themes",
    "gwt/gwt-bootstrap5",
    "gwt/gwt-bootstrap5-extras",
    "gwt/gwt-bootstrap5-themes",
    "teavm/teavm-bootstrap3",
    "teavm/teavm-bootstrap5",
)


def require_one(paths, description):
    matches = list(paths)
    if len(matches) != 1:
        raise RuntimeError(f"Expected one {description}, found {len(matches)}")
    return matches[0]


def source_map_url(script):
    tail = script.read_bytes()[-1024:]
    matches = SOURCE_MAP_URL.findall(tail)
    if not matches:
        raise RuntimeError(f"No sourceMappingURL in {script}")
    return matches[-1].decode("utf-8")


def prepare_gwt_showcase(module_path, module_name):
    module = ROOT / module_path
    output = require_one(
        (path for path in (module / "target").glob(f"{module.name}-*") if path.is_dir()),
        f"exploded showcase under {module / 'target'}",
    ) / module_name
    central_maps = output / "symbolMaps"

    scripts = sorted(output.glob("*.cache.js"))
    scripts.extend(sorted((output / "deferredjs").glob("*/*.cache.js")))
    if not scripts:
        raise RuntimeError(f"No compiled GWT scripts under {output}")

    for script in scripts:
        relative_map = Path(source_map_url(script))
        target_map = script.parent / relative_map
        if not target_map.is_file():
            source_map = central_maps / relative_map.name
            if not source_map.is_file():
                raise RuntimeError(f"Missing source map for {script}: {source_map}")
            target_map.parent.mkdir(parents=True, exist_ok=True)
            shutil.copy2(source_map, target_map)

        source_map_data = json.loads(target_map.read_text(encoding="utf-8"))
        sources = source_map_data.get("sources", [])
        source_root = Path(os.path.relpath(output / "src", target_map.parent))
        source_map_data["sourceRoot"] = source_root.as_posix()
        target_map.write_text(
            json.dumps(source_map_data, separators=(",", ":")), encoding="utf-8"
        )
        missing = [source for source in sources if not (target_map.parent / source_root / source).is_file()]
        if not sources or missing:
            raise RuntimeError(f"GWT source map has {len(missing)} unresolved sources: {target_map}")

    print(f"GWT {module_name}: {len(scripts)} scripts have resolvable maps and Java sources")


def verify_teavm_showcase(module_path, script_name):
    output = ROOT / module_path / "target" / "teavm"
    script = output / script_name
    relative_map = Path(source_map_url(script))
    source_map = script.parent / relative_map
    data = json.loads(source_map.read_text(encoding="utf-8"))
    source_root = Path(data.get("sourceRoot", ""))
    sources = data.get("sources", [])
    missing = [source for source in sources if not (source_map.parent / source_root / source).is_file()]
    if not sources or missing:
        raise RuntimeError(
            f"TeaVM source map has {len(missing)} unresolved sources: {source_map}"
        )
    print(f"TeaVM {script_name}: {len(sources)} mapped Java sources are published")


def verify_source_jars():
    for module_path in SOURCE_MODULES:
        module = ROOT / module_path
        source_jar = require_one(
            (module / "target").glob(f"{module.name}-*-sources.jar"),
            f"source JAR for {module_path}",
        )
        if source_jar.stat().st_size == 0:
            raise RuntimeError(f"Empty source JAR: {source_jar}")
    print(f"Maven: {len(SOURCE_MODULES)} library source JARs are attached")


def main():
    for module_path, module_name in GWT_SHOWCASES:
        prepare_gwt_showcase(module_path, module_name)
    verify_teavm_showcase("teavm/teavm-bootstrap3", "teavm-bootstrap3-showcase.js")
    verify_teavm_showcase("teavm/teavm-bootstrap5", "teavm-bootstrap5-smoke.js")
    verify_source_jars()


if __name__ == "__main__":
    main()
