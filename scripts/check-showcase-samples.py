#!/usr/bin/env python3
"""Check the Bootstrap 3 showcase's code samples against the examples beside them.

Each panel in the showcase holds a live UiBinder example and, in its footer, a
hand-written snippet of the markup that produced it. The two are maintained
separately, so the snippet can drift: it has named widgets that do not exist and
carried tags that were never closed, both of which fail to compile the moment
somebody copies them.

Regenerating the snippets from the examples would remove the drift but also the
curation -- the elided text, the absent ui:field noise -- that makes them worth
reading. So this checks them instead, for the two things that actually went
wrong:

  * every widget a snippet names must exist in the library
  * every snippet must be well-formed: no unclosed tag, no unprefixed close

Run with no arguments from the repository root. Exits non-zero on any finding.
"""
import glob
import html
import os
import re
import sys
from collections import Counter

VIEWS = 'gwt/gwt-bootstrap3-showcase/src/main/java/**/*.ui.xml'
WIDGET_ROOTS = [
    'gwt/gwt-bootstrap3/src/main/java/org/gwtbootstrap3/client/ui',
    'gwt/gwt-bootstrap3-extras/src/main/java',
    'gwt/gwt-bootstrap3-showcase/src/main/java/org/gwtbootstrap3/demo/client/ui',
]


def known_widgets():
    names = set()
    for root in WIDGET_ROOTS:
        for dirpath, _, files in os.walk(root):
            for f in files:
                if f.endswith('.java'):
                    names.add(f[:-5])
    return names


def samples(text):
    return re.findall(r'<d:PrettyPre[^>]*>(.*?)</d:PrettyPre>', text, flags=re.S)


def check_snippet(snippet, widgets):
    problems = []
    body = html.unescape(snippet)

    opens, closes = Counter(), Counter()
    for m in re.finditer(r'<(b[a-z.]*:[A-Za-z]+)([^<>]*)>', body):
        name, rest = m.group(1), m.group(2)
        if not rest.rstrip().endswith('/'):
            opens[name] += 1
    for m in re.finditer(r'</(b[a-z.]*:[A-Za-z]+)>', body):
        closes[m.group(1)] += 1

    for name, count in opens.items():
        missing = count - closes.get(name, 0)
        if missing > 0:
            problems.append('%s never closed (%d time%s)'
                            % (name, missing, '' if missing == 1 else 's'))

    for stray in re.findall(r'</([A-Z][A-Za-z]+)>', body):
        problems.append('</%s> is missing its namespace prefix' % stray)

    for name in sorted(set(list(opens) + list(closes))):
        simple = name.split(':')[-1]
        if simple not in widgets:
            problems.append('%s is not a widget in this library' % name)

    return problems


def main():
    widgets = known_widgets()
    if not widgets:
        print('no widget sources found; run from the repository root', file=sys.stderr)
        return 2

    findings = 0
    scanned = 0
    for path in sorted(glob.glob(VIEWS, recursive=True)):
        with open(path, encoding='utf-8') as handle:
            text = handle.read()
        for index, snippet in enumerate(samples(text), start=1):
            scanned += 1
            for problem in check_snippet(snippet, widgets):
                findings += 1
                print('%s: sample %d: %s' % (path, index, problem))

    print('checked %d code samples' % scanned)
    if findings:
        print('%d problem%s found' % (findings, '' if findings == 1 else 's'))
        return 1
    print('all samples name real widgets and are well formed')
    return 0


if __name__ == '__main__':
    sys.exit(main())
