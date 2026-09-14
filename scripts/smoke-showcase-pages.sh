#!/usr/bin/env bash
set -euo pipefail

root_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
pages_dir="${PAGES_OUTPUT_DIR:-${root_dir}/showcase-site/target/pages}"
port="${SHOWCASE_SMOKE_PORT:-8878}"

find_chrome() {
  local candidate
  for candidate in \
      "${CHROME_BIN:-}" \
      "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome" \
      "$(command -v google-chrome 2>/dev/null || true)" \
      "$(command -v google-chrome-stable 2>/dev/null || true)" \
      "$(command -v chromium 2>/dev/null || true)" \
      "$(command -v chromium-browser 2>/dev/null || true)"; do
    if [[ -n "${candidate}" && -x "${candidate}" ]]; then
      printf '%s\n' "${candidate}"
      return
    fi
  done
  echo "Chrome or Chromium is required for showcase smoke tests" >&2
  exit 1
}

test -s "${pages_dir}/showcase.html"
test -s "${pages_dir}/apidocs/index.html"
test -s "${pages_dir}/extras-apidocs/index.html"
test -s "${pages_dir}/bootstrap5/apidocs/index.html"
test -s "${pages_dir}/bootstrap5/extras-apidocs/index.html"
chrome_bin="$(find_chrome)"
server_log="${root_dir}/target/showcase-smoke-server.log"
mkdir -p "${root_dir}/target/showcase-smoke"

python3 -u -m http.server "${port}" --bind 127.0.0.1 --directory "${pages_dir}" >"${server_log}" 2>&1 &
server_pid=$!
trap 'kill "${server_pid}" 2>/dev/null || true' EXIT

for _ in {1..50}; do
  if curl -fsS "http://127.0.0.1:${port}/showcase.html" >/dev/null 2>&1; then
    break
  fi
  sleep 0.1
done
curl -fsS "http://127.0.0.1:${port}/showcase.html" >/dev/null

smoke_page() {
  local name="$1"
  local path="$2"
  local expected="$3"
  local output="${root_dir}/target/showcase-smoke/${name}.html"

  "${chrome_bin}" \
    --headless=new \
    --no-sandbox \
    --disable-dev-shm-usage \
    --disable-gpu \
    --virtual-time-budget=15000 \
    --dump-dom \
    "http://127.0.0.1:${port}/${path}" >"${output}" 2>/dev/null

  if ! grep -Fq "${expected}" "${output}"; then
    echo "${name} did not render expected text: ${expected}" >&2
    echo "Rendered output: ${output}" >&2
    exit 1
  fi
}

smoke_page gwt-bootstrap3 "showcase.html" "<h1>GWT Bootstrap Showcase"
smoke_page gwt-bootstrap3-progress "showcase.html#progressBars" "progress active progress-striped"
smoke_page gwt-bootstrap3-fixtures "fixtures/gwt-bootstrap3/index.html" "data-fixtures-ready=\"true\""
smoke_page gwt-bootstrap5 "bootstrap5/index.html" "<h1>Bootstrap Showcase"
smoke_page gwt-bootstrap5-progress "bootstrap5/index.html#progressBars" "progress-bar bg-success progress-bar-striped progress-bar-animated"
smoke_page gwt-bootstrap5-fixtures "fixtures/gwt-bootstrap5/index.html" "data-fixtures-ready=\"true\""
smoke_page teavm-bootstrap3 "teavm.html" "<h1>GWT Bootstrap Showcase"
smoke_page teavm-bootstrap5 "teavm-bootstrap5.html" "<h1>Bootstrap Showcase"

if grep -Eq '" [45][0-9][0-9] -$' "${server_log}"; then
  echo "A showcase requested an asset that returned an HTTP error" >&2
  grep -E '" [45][0-9][0-9] -$' "${server_log}" >&2
  exit 1
fi

echo "All four assembled showcases rendered successfully in Chromium"
