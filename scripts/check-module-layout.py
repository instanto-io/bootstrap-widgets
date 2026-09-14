#!/usr/bin/env python3
"""Validate the path-sensitive GWT/TeaVM shared-source seams."""

from pathlib import Path
import re
import sys
import xml.etree.ElementTree as ET


ROOT = Path(__file__).resolve().parent.parent
NS = {"m": "http://maven.apache.org/POM/4.0.0"}

TRACKS = {
    "Bootstrap 3": {
        "pom": ROOT / "teavm/teavm-bootstrap3/pom.xml",
        "sources": {
            "${project.build.directory}/shared-sources",
            "${project.build.directory}/generated-sources/teavm-modules",
        },
        "unpacked": {
            "gwt-bootstrap3",
            "gwt-bootstrap3-themes",
            "gwt-bootstrap3-showcase",
        },
        "source_roots": (
            ROOT / "gwt/gwt-bootstrap3/src/main/java",
            ROOT / "gwt/gwt-bootstrap3-themes/src/main/java",
            ROOT / "gwt/gwt-bootstrap3-showcase/src/main/java",
            # Only the ported extras are compiled, but the seam check has to find the
            # GWT halves of their seams, which live here.
            ROOT / "gwt/gwt-bootstrap3-extras/src/main/java",
        ),
        "excludes": {
            "org/gwtbootstrap3/demo/client/ExtrasPages.java",
            "org/gwtbootstrap3/demo/client/ShowcaseScripts.java",
            "org/gwtbootstrap3/demo/client/ui/PrettyPre.java",
            "org/gwtbootstrap3/demo/client/GwtBootstrap3DemoClientBundle.java",
            "org/gwtbootstrap3/demo/client/application/extras/AnimateView.java",
            "org/gwtbootstrap3/demo/client/application/extras/BootboxJSView.java",
            "org/gwtbootstrap3/demo/client/application/extras/CardView.java",
            "org/gwtbootstrap3/demo/client/application/extras/DatePickerView.java",
            "org/gwtbootstrap3/demo/client/application/extras/DateTimePickerView.java",
            "org/gwtbootstrap3/demo/client/application/extras/FullCalendarView.java",
            "org/gwtbootstrap3/demo/client/application/extras/GalleryView.java",
            "org/gwtbootstrap3/demo/client/application/extras/MarkdownView.java",
            "org/gwtbootstrap3/demo/client/application/extras/NotifyView.java",
            "org/gwtbootstrap3/demo/client/application/extras/OfflineView.java",
            "org/gwtbootstrap3/demo/client/application/extras/TagsInputView.java",
            "org/gwtbootstrap3/demo/client/application/extras/TypeaheadView.java",
            "org/gwtbootstrap3/demo/client/application/css/validation/**",
            "org/gwtbootstrap3/extras/animate/client/ui/AnimateJs.java",
            "org/gwtbootstrap3/extras/toggleswitch/client/ui/base/ToggleSwitchJs.java",
            "org/gwtbootstrap3/extras/toggleswitch/client/ToggleSwitchClientBundle.java",
            "org/gwtbootstrap3/extras/toggleswitch/client/ToggleSwitchEntryPoint.java",
            "org/gwtbootstrap3/extras/markdown/client/Markdown.java",
            "org/gwtbootstrap3/extras/slider/client/ui/base/SliderJs.java",
            "org/gwtbootstrap3/extras/slider/client/SliderClientBundle.java",
            "org/gwtbootstrap3/extras/slider/client/SliderEntryPoint.java",
            "org/gwtbootstrap3/extras/select/client/ui/SelectJs.java",
            "org/gwtbootstrap3/extras/select/client/SelectEntryPoint.java",
            "org/gwtbootstrap3/extras/summernote/client/ui/base/SummernoteJs.java",
            "org/gwtbootstrap3/extras/summernote/client/event/SummernoteImageUploadEvent.java",
            "org/gwtbootstrap3/extras/summernote/client/SummernoteEntryPoint.java",
            "org/gwtbootstrap3/extras/markdown/client/MarkdownClientBundle.java",
            "org/gwtbootstrap3/extras/markdown/client/MarkdownEntryPoint.java",
            "org/gwtbootstrap3/extras/markdown/client/ui/TextAreaSelection.java",
            "org/gwtbootstrap3/client/GwtBootstrap3EntryPoint.java",
            "org/gwtbootstrap3/client/GwtBootstrap3ClientBundle.java",
            "org/gwtbootstrap3/client/shared/js/JQuery.java",
            "org/gwtbootstrap3/client/ui/base/TooltipOptions.java",
            "org/gwtbootstrap3/client/ui/base/CarouselOptions.java",
        },
        "replacements": {
            "org/gwtbootstrap3/client/TeaVmBootstrap3EntryPoint.java",
            "org/gwtbootstrap3/client/Bootstrap3Resources.java",
            "org/gwtbootstrap3/client/shared/js/JQuery.java",
            "org/gwtbootstrap3/client/ui/base/TooltipOptions.java",
            "org/gwtbootstrap3/client/ui/base/CarouselOptions.java",
            "org/gwtbootstrap3/demo/client/ExtrasPages.java",
            "org/gwtbootstrap3/demo/client/ShowcaseScripts.java",
            "org/gwtbootstrap3/demo/client/ui/PrettyPre.java",
        },
    },
    "Bootstrap 5": {
        "pom": ROOT / "teavm/teavm-bootstrap5/pom.xml",
        "sources": {
            "${project.build.directory}/generated-sources/teavm-modules",
            "${project.build.directory}/shared-sources",
        },
        "unpacked": {
            "gwt-bootstrap5",
            "gwt-bootstrap5-themes",
            "gwt-bootstrap5-extras",
            "gwt-bootstrap5-showcase",
        },
                "source_roots": (
            ROOT / "gwt/gwt-bootstrap5/src/main/java",
            ROOT / "gwt/gwt-bootstrap5-themes/src/main/java",
            ROOT / "gwt/gwt-bootstrap5-extras/src/main/java",
            ROOT / "gwt/gwt-bootstrap5-showcase/src/main/java",
        ),
        "excludes": {
            "io/instanto/bootstrap5/client/GwtBootstrap5EntryPoint.java",
            "io/instanto/bootstrap5/client/ui/base/BootstrapEventBridge.java",
            "io/instanto/bootstrap5/client/ui/base/BootstrapComponent.java",
            "io/instanto/bootstrap5/client/ui/base/InputEvents.java",
            "io/instanto/bootstrap5/extras/datepicker/client/DatePickerClientBundle.java",
            "io/instanto/bootstrap5/extras/datepicker/client/DatePickerEntryPoint.java",
            "io/instanto/bootstrap5/extras/datepicker/client/ui/DatePickerJs.java",
            "io/instanto/bootstrap5/extras/markdown/client/Markdown.java",
            "io/instanto/bootstrap5/extras/markdown/client/MarkdownClientBundle.java",
            "io/instanto/bootstrap5/extras/markdown/client/MarkdownEntryPoint.java",
            "io/instanto/bootstrap5/extras/markdown/client/ui/TextAreaSelection.java",
            "io/instanto/bootstrap5/extras/richtext/client/RichTextClientBundle.java",
            "io/instanto/bootstrap5/extras/richtext/client/RichTextEntryPoint.java",
            "io/instanto/bootstrap5/extras/richtext/client/ui/QuillJs.java",
            "io/instanto/bootstrap5/extras/slider/client/SliderClientBundle.java",
            "io/instanto/bootstrap5/extras/slider/client/SliderEntryPoint.java",
            "io/instanto/bootstrap5/extras/slider/client/ui/SliderJs.java",
            "io/instanto/bootstrap5/extras/select/client/SelectClientBundle.java",
            "io/instanto/bootstrap5/extras/select/client/SelectEntryPoint.java",
            "io/instanto/bootstrap5/extras/select/client/ui/SelectJs.java",
            "io/instanto/bootstrap5/extras/grid/client/GridClientBundle.java",
            "io/instanto/bootstrap5/extras/grid/client/GridEntryPoint.java",
            "io/instanto/bootstrap5/extras/grid/client/ui/GridJs.java",
            "io/instanto/bootstrap5/extras/sortable/client/SortableClientBundle.java",
            "io/instanto/bootstrap5/extras/sortable/client/SortableEntryPoint.java",
            "io/instanto/bootstrap5/extras/sortable/client/ui/SortableJs.java",
            "io/instanto/bootstrap5/extras/dashboard/client/DashboardClientBundle.java",
            "io/instanto/bootstrap5/extras/dashboard/client/DashboardEntryPoint.java",
            "io/instanto/bootstrap5/extras/dashboard/client/ui/DashboardJs.java",
            "io/instanto/bootstrap5/extras/gallery/client/GalleryClientBundle.java",
            "io/instanto/bootstrap5/extras/gallery/client/GalleryEntryPoint.java",
            "io/instanto/bootstrap5/extras/gallery/client/ui/GalleryJs.java",
        },
        "replacements": {
            "io/instanto/bootstrap5/client/TeaVmBootstrap5EntryPoint.java",
            "io/instanto/bootstrap5/client/ui/base/BootstrapEventBridge.java",
            "io/instanto/bootstrap5/client/ui/base/BootstrapComponent.java",
            "io/instanto/bootstrap5/client/ui/base/InputEvents.java",
            "io/instanto/bootstrap5/extras/datepicker/client/ui/DatePickerJs.java",
            "io/instanto/bootstrap5/extras/markdown/client/Markdown.java",
            "io/instanto/bootstrap5/extras/markdown/client/ui/TextAreaSelection.java",
            "io/instanto/bootstrap5/extras/select/client/ui/SelectJs.java",
            "io/instanto/bootstrap5/extras/grid/client/ui/GridJs.java",
            "io/instanto/bootstrap5/extras/sortable/client/ui/SortableJs.java",
            "io/instanto/bootstrap5/extras/dashboard/client/ui/DashboardJs.java",
            "io/instanto/bootstrap5/extras/gallery/client/ui/GalleryJs.java",
        },
    },
}


def unpacked_artifacts(root):
    """The shared source artifacts a track unpacks to compile against.

    The modules used to name sibling directories. They now name artifacts, and the
    unpacking itself is configured once in the parent, so what a track declares is
    the list and that is what is worth checking.
    """
    found = set()
    for element in root.iter(f"{{{NS['m']}}}shared.source.artifacts"):
        found.update(part.strip() for part in (element.text or "").split(",") if part.strip())
    return found


def values(root, expression):
    return {node.text.strip() for node in root.findall(expression, NS) if node.text}


def check_track(name, track):
    problems = []
    pom = ET.parse(track["pom"]).getroot()
    sources = values(pom, ".//m:plugin[m:artifactId='build-helper-maven-plugin']//m:source")
    excludes = values(pom, ".//m:plugin[m:artifactId='maven-compiler-plugin']//m:exclude")

    if sources != track["sources"]:
        problems.append(f"{name} add-source entries are {sorted(sources)}, expected {sorted(track['sources'])}")
    if "unpacked" in track:
        unpacked = unpacked_artifacts(pom)
        if unpacked != track["unpacked"]:
            problems.append(
                f"{name} unpacks {sorted(unpacked)}, expected {sorted(track['unpacked'])}")
    if excludes != track["excludes"]:
        problems.append(f"{name} compiler exclusions are {sorted(excludes)}, expected {sorted(track['excludes'])}")

    for relative in sorted(track["excludes"]):
        found = any(
            any(source_root.glob(relative)) if "*" in relative
            else (source_root / relative).is_file()
            for source_root in track["source_roots"]
        )
        if not found:
            problems.append(f"{name} excluded GWT source is missing: {relative}")

    replacement_root = track["pom"].parent / "src/main/java"
    for relative in sorted(track["replacements"]):
        if not (replacement_root / relative).is_file():
            problems.append(f"{name} TeaVM replacement is missing: {relative}")
    return problems


def main():
    problems = []
    for name, track in TRACKS.items():
        problems.extend(check_track(name, track))

    element_panel = (ROOT / "gwt/gwt-bootstrap5/src/main/java/io/instanto/bootstrap5/client/ui/ElementPanel.java").read_text()
    if not re.search(r"implements\s+HasWidgets\s*,\s*HasHTML\b", element_panel):
        problems.append("ElementPanel must declare HasWidgets immediately before HasHTML for UiBinder")

    if problems:
        print("module layout check failed:", file=sys.stderr)
        for problem in problems:
            print(f"- {problem}", file=sys.stderr)
        return 1

    print("module layout and GWT/TeaVM source seams are consistent")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
