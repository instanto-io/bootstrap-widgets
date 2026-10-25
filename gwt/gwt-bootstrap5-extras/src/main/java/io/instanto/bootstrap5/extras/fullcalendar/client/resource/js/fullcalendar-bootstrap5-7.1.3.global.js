/*!
FullCalendar Bootstrap 5 Plugin v7.1.3
Docs & License: https://fullcalendar.io/docs/bootstrap5
(c) 2026 Adam Shaw
*/
(function () {
    'use strict';

    function joinClassNames(...inputs) {
        return inputs.filter(Boolean).join(" ");
    }
    // usually 11px font / 12px line-height
    const xxsTextClass = "fc-bootstrap5-vQz";
    // outline
    const outlineWidthClass = "fc-bootstrap5-0Bj";
    const outlineWidthFocusClass = "fc-bootstrap5-uqo";
    const outlineOffsetClass = "fc-bootstrap5-3Xj";
    const outlineInsetClass = "fc-bootstrap5-fFh";
    const primaryOutlineColorClass = "fc-bootstrap5-93A";
    const navIconClass = "fc-bootstrap5-yLA fc-bootstrap5-kC0";
    // neutral buttons
    const strongSolidPressableClass = "fc-bootstrap5-d4c";
    const mutedHoverClass = "fc-bootstrap5-ECZ";
    const mutedHoverPressableClass = `${mutedHoverClass} fc-bootstrap5-PR8 fc-bootstrap5-NjO`;
    const faintHoverClass = "fc-bootstrap5-b9l";
    const faintHoverPressableClass = `${faintHoverClass} fc-bootstrap5-Dxv fc-bootstrap5-UM2`;
    // transparent resizer for mouse
    const blockPointerResizerClass = "fc-bootstrap5-1EY fc-bootstrap5-pps fc-bootstrap5-vs6";
    const rowPointerResizerClass = `${blockPointerResizerClass} fc-bootstrap5-AWB fc-bootstrap5-hza`;
    const columnPointerResizerClass = `${blockPointerResizerClass} fc-bootstrap5-MaV fc-bootstrap5-uuA`;
    // circle resizer for touch
    const blockTouchResizerClass = "fc-bootstrap5-1EY fc-bootstrap5-3wQ fc-bootstrap5-wsy fc-bootstrap5-lNM bg-body fc-bootstrap5-AAA";
    const rowTouchResizerClass = `${blockTouchResizerClass} fc-bootstrap5-ERR fc-bootstrap5-Dq8`;
    const columnTouchResizerClass = `${blockTouchResizerClass} fc-bootstrap5-1V6 fc-bootstrap5-F99`;
    const getDayClass = (info) => joinClassNames("fc-bootstrap5-wsy", info.isMajor ? "fc-bootstrap5-P7R" : "fc-bootstrap5-JO5", info.isDisabled ? "bg-body-tertiary" :
        info.isToday && "bg-warning fc-bootstrap5-pdS fc-bootstrap5-orx");
    const getSlotClass = (info) => joinClassNames("fc-bootstrap5-wsy fc-bootstrap5-JO5", info.isMinor && "fc-bootstrap5-TN2");
    const dayRowCommonClasses = {
        /* Day Row > List-Item Event
        ----------------------------------------------------------------------------------------------- */
        listItemEventClass: (info) => joinClassNames("fc-bootstrap5-Ika fc-bootstrap5-7A6 fc-bootstrap5-Fvv", info.isNarrow ? "fc-bootstrap5-cKZ" : "fc-bootstrap5-rVY", info.isSelected
            ? joinClassNames("bg-body-secondary", info.isDragging && "fc-bootstrap5-qNs")
            : (info.isInteractive ? mutedHoverPressableClass : mutedHoverClass)),
        listItemEventBeforeClass: (info) => joinClassNames("fc-bootstrap5-5JF", info.isNarrow ? "fc-bootstrap5-148" : "fc-bootstrap5-rVY"),
        listItemEventInnerClass: (info) => joinClassNames("fc-bootstrap5-dl1 fc-bootstrap5-1sP fc-bootstrap5-XpK", info.isNarrow ? "fc-bootstrap5-z5u" : "fc-bootstrap5-2rx", info.isNarrow ? xxsTextClass : "fc-bootstrap5-a3B"),
        listItemEventTimeClass: (info) => joinClassNames(info.isNarrow ? "fc-bootstrap5-F1o" : "fc-bootstrap5-oQ2", "fc-bootstrap5-TZ4 fc-bootstrap5-pKG fc-bootstrap5-1Zl"),
        listItemEventTitleClass: (info) => joinClassNames(info.isNarrow ? "fc-bootstrap5-F1o" : "fc-bootstrap5-oQ2", "fc-bootstrap5-DIS fc-bootstrap5-TZ4 fc-bootstrap5-pKG fc-bootstrap5-OLq"),
        /* Day Row > Row Event
        ----------------------------------------------------------------------------------------------- */
        rowEventClass: (info) => joinClassNames(info.isStart && joinClassNames("fc-bootstrap5-kmj", info.isNarrow ? "fc-bootstrap5-Jzj" : "fc-bootstrap5-Wga"), info.isEnd && joinClassNames("fc-bootstrap5-Skl", info.isNarrow ? "fc-bootstrap5-3e1" : "fc-bootstrap5-KYn")),
        rowEventInnerClass: (info) => info.isNarrow ? "fc-bootstrap5-z5u" : "fc-bootstrap5-2rx",
        rowEventTimeClass: (info) => info.isNarrow ? "fc-bootstrap5-a7i" : "fc-bootstrap5-C2j",
        rowEventTitleClass: (info) => info.isNarrow ? "fc-bootstrap5-oQ2" : "fc-bootstrap5-aCI",
        /* Day Row > More-Link
        ----------------------------------------------------------------------------------------------- */
        rowMoreLinkClass: (info) => joinClassNames("fc-bootstrap5-Ika fc-bootstrap5-wsy fc-bootstrap5-Fvv", info.isNarrow
            ? "fc-bootstrap5-cKZ border-primary"
            : "fc-bootstrap5-sI7 fc-bootstrap5-rVY fc-bootstrap5-d0j", mutedHoverPressableClass),
        rowMoreLinkInnerClass: (info) => joinClassNames("fc-bootstrap5-7A6", info.isNarrow ? xxsTextClass : "fc-bootstrap5-a3B"),
    };
    const continuationArrowClass = "fc-bootstrap5-rVY fc-bootstrap5-XM3 fc-bootstrap5-rif fc-bootstrap5-lMo";
    var plugin = {
        name: "theme-bootstrap5",
        optionDefaults: {
            className: "fc-theme-bootstrap5 fc-bootstrap5-yth fc-bootstrap5-n5m",
            viewClass: (info) => {
                const hasBorderTop = info.options.headerToolbar || !info.borderlessTop;
                const hasBorderBottom = info.options.footerToolbar || !info.borderlessBottom;
                const hasBorderX = !info.borderlessX;
                return joinClassNames("bg-body text-body fc-bootstrap5-JO5", hasBorderTop && "fc-bootstrap5-ku3", hasBorderBottom && "fc-bootstrap5-zi1", hasBorderX && "fc-bootstrap5-1Wx");
            },
            /* Toolbar
            --------------------------------------------------------------------------------------------- */
            toolbarClass: (info) => joinClassNames("fc-bootstrap5-dl1 fc-bootstrap5-1sP fc-bootstrap5-dNl fc-bootstrap5-XpK fc-bootstrap5-N2M fc-bootstrap5-wwb", info.borderlessX && "fc-bootstrap5-Apf"),
            toolbarSectionClass: "fc-bootstrap5-yi0 fc-bootstrap5-dl1 fc-bootstrap5-1sP fc-bootstrap5-XpK fc-bootstrap5-wwb",
            toolbarTitleClass: "fc-bootstrap5-AVD fc-bootstrap5-DIS",
            buttonGroupClass: "btn-group",
            buttonClass: (info) => joinClassNames("btn btn-primary fc-bootstrap5-4MR fc-bootstrap5-vwH fc-bootstrap5-cfp", info.isSelected && "active"),
            buttons: {
                prev: {
                    iconClass: `bi bi-chevron-left ${navIconClass}`,
                },
                next: {
                    iconClass: `bi bi-chevron-right ${navIconClass}`,
                },
                prevYear: {
                    iconClass: `bi bi-chevron-double-left ${navIconClass}`,
                },
                nextYear: {
                    iconClass: `bi bi-chevron-double-right ${navIconClass}`,
                },
            },
            /* Abstract Event
            --------------------------------------------------------------------------------------------- */
            eventColor: "var(--bs-primary)",
            eventContrastColor: "var(--bs-white)",
            eventClass: (info) => joinClassNames(info.isDragging && "fc-bootstrap5-n5m", info.event.url && "fc-bootstrap5-JiE", info.isSelected
                ? joinClassNames(outlineWidthClass, info.isDragging ? "fc-bootstrap5-1kP" : "fc-bootstrap5-tkw")
                : outlineWidthFocusClass, primaryOutlineColorClass),
            /* Background Event
            --------------------------------------------------------------------------------------------- */
            backgroundEventColor: "var(--bs-success)",
            backgroundEventClass: "fc-bootstrap5-uAw fc-bootstrap5-jsy fc-bootstrap5-DO7",
            backgroundEventTitleClass: (info) => joinClassNames("fc-bootstrap5-lMo fc-bootstrap5-L1Y", info.isNarrow
                ? `fc-bootstrap5-KUX ${xxsTextClass}`
                : "fc-bootstrap5-XJa fc-bootstrap5-a3B"),
            /* List-Item Event
            --------------------------------------------------------------------------------------------- */
            listItemEventClass: "fc-bootstrap5-XpK",
            listItemEventBeforeClass: "fc-bootstrap5-lNM fc-bootstrap5-AAA",
            listItemEventInnerClass: "text-body",
            /* Block Event
            --------------------------------------------------------------------------------------------- */
            blockEventClass: (info) => joinClassNames("fc-bootstrap5-bCs fc-bootstrap5-eYX fc-bootstrap5-d0j fc-bootstrap5-DO7 fc-bootstrap5-YjJ fc-bootstrap5-vwH", (info.isDragging && !info.isSelected) && "fc-bootstrap5-iTG", outlineOffsetClass),
            blockEventInnerClass: "fc-bootstrap5-i9F fc-bootstrap5-cfp",
            blockEventTimeClass: "fc-bootstrap5-TZ4 fc-bootstrap5-pKG fc-bootstrap5-1Zl",
            blockEventTitleClass: "fc-bootstrap5-TZ4 fc-bootstrap5-pKG fc-bootstrap5-OLq",
            /* Row Event
            --------------------------------------------------------------------------------------------- */
            rowEventClass: (info) => joinClassNames("fc-bootstrap5-Ika fc-bootstrap5-JIC", info.isStart && "fc-bootstrap5-kmj fc-bootstrap5-3J4", info.isEnd && "fc-bootstrap5-Skl fc-bootstrap5-USt"),
            rowEventBeforeClass: (info) => joinClassNames(info.isStartResizable && joinClassNames(info.isSelected ? rowTouchResizerClass : rowPointerResizerClass, "fc-bootstrap5-11a")),
            rowEventAfterClass: (info) => joinClassNames(info.isEndResizable && joinClassNames(info.isSelected ? rowTouchResizerClass : rowPointerResizerClass, "fc-bootstrap5-Tuc")),
            rowEventInnerClass: (info) => joinClassNames("fc-bootstrap5-dl1 fc-bootstrap5-1sP fc-bootstrap5-XpK", info.isNarrow ? xxsTextClass : "fc-bootstrap5-a3B"),
            rowEventTimeClass: "fc-bootstrap5-DIS",
            /* Column Event
            --------------------------------------------------------------------------------------------- */
            columnEventClass: (info) => joinClassNames("fc-bootstrap5-1Wx fc-bootstrap5-A3h fc-bootstrap5-Qj1", info.isStart && "fc-bootstrap5-ku3 fc-bootstrap5-Z7Q", info.isEnd && "fc-bootstrap5-Ika fc-bootstrap5-zi1 fc-bootstrap5-2qh"),
            columnEventBeforeClass: (info) => joinClassNames(info.isStartResizable && joinClassNames(info.isSelected ? columnTouchResizerClass : columnPointerResizerClass, "fc-bootstrap5-YDC")),
            columnEventAfterClass: (info) => joinClassNames(info.isEndResizable && joinClassNames(info.isSelected ? columnTouchResizerClass : columnPointerResizerClass, "fc-bootstrap5-fJL")),
            columnEventInnerClass: (info) => joinClassNames("fc-bootstrap5-dl1", info.isShort
                ? "fc-bootstrap5-iS4 fc-bootstrap5-1sP fc-bootstrap5-XpK fc-bootstrap5-NWN"
                : "fc-bootstrap5-aCI fc-bootstrap5-2rx fc-bootstrap5-sgX"),
            columnEventTimeClass: (info) => joinClassNames(!info.isShort && "fc-bootstrap5-166", xxsTextClass),
            columnEventTitleClass: (info) => joinClassNames(!info.isShort && "fc-bootstrap5-2rx", (info.isShort || info.isNarrow) ? xxsTextClass : "fc-bootstrap5-a3B"),
            /* More-Link
            --------------------------------------------------------------------------------------------- */
            moreLinkClass: `${outlineWidthFocusClass} ${primaryOutlineColorClass}`,
            moreLinkInnerClass: "fc-bootstrap5-TZ4 fc-bootstrap5-pKG",
            columnMoreLinkClass: `fc-bootstrap5-Ika fc-bootstrap5-Fvv fc-bootstrap5-wsy fc-bootstrap5-d0j fc-bootstrap5-4MR ${strongSolidPressableClass} fc-bootstrap5-vwH fc-bootstrap5-A3h fc-bootstrap5-Qj1 ${outlineOffsetClass}`,
            columnMoreLinkInnerClass: (info) => joinClassNames("fc-bootstrap5-KUX", info.isNarrow ? xxsTextClass : "fc-bootstrap5-a3B"),
            /* Day Header
            --------------------------------------------------------------------------------------------- */
            dayHeaderAlign: (info) => info.inPopover ? "start" : "center",
            dayHeaderClass: (info) => (info.inPopover
                ? "popover-header"
                : joinClassNames("fc-bootstrap5-E9P", info.isDisabled && "bg-body-tertiary", "fc-bootstrap5-wsy", info.isMajor ? "fc-bootstrap5-P7R" : "fc-bootstrap5-JO5")),
            dayHeaderInnerClass: (info) => (info.inPopover
                ? "fc-bootstrap5-dl1 fc-bootstrap5-sgX"
                : joinClassNames("fc-bootstrap5-rVY fc-bootstrap5-cJ3 fc-bootstrap5-dl1 fc-bootstrap5-sgX", info.isNarrow ? xxsTextClass : "fc-bootstrap5-9yp")),
            dayHeaderDividerClass: "fc-bootstrap5-zi1 fc-bootstrap5-JO5",
            /* Day Cell
            --------------------------------------------------------------------------------------------- */
            dayCellClass: getDayClass,
            dayCellTopClass: (info) => joinClassNames(info.isNarrow ? "fc-bootstrap5-84e" : "fc-bootstrap5-p7s", "fc-bootstrap5-dl1 fc-bootstrap5-1sP fc-bootstrap5-LMv"),
            dayCellTopInnerClass: (info) => joinClassNames("fc-bootstrap5-rVY fc-bootstrap5-TZ4", info.isNarrow
                ? `fc-bootstrap5-cJ3 ${xxsTextClass}`
                : "fc-bootstrap5-V9v fc-bootstrap5-9yp", info.isOther && "text-body-tertiary", info.monthText && "fc-bootstrap5-DIS"),
            dayCellInnerClass: (info) => info.inPopover ? "popover-body" : "",
            /* Popover
            --------------------------------------------------------------------------------------------- */
            popoverClass: "popover fc-bootstrap5-gMS bg-body text-body fc-bootstrap5-aNc fc-bootstrap5-n5m",
            popoverCloseClass: `fc-bootstrap5-bCs fc-bootstrap5-1EY fc-bootstrap5-Nkc fc-bootstrap5-aYN ${outlineWidthFocusClass} ${primaryOutlineColorClass} fc-bootstrap5-Z9U bi bi-x-lg`,
            /* Lane
            --------------------------------------------------------------------------------------------- */
            dayLaneClass: getDayClass,
            dayLaneInnerClass: (info) => (info.isStack
                ? "fc-bootstrap5-gMS"
                : info.isNarrow ? "fc-bootstrap5-148" : "fc-bootstrap5-Jzj fc-bootstrap5-B3G"),
            slotLaneClass: getSlotClass,
            /* List Day
            --------------------------------------------------------------------------------------------- */
            listDayHeaderClass: "fc-bootstrap5-zi1 fc-bootstrap5-JO5 bg-body-secondary fc-bootstrap5-nHS fc-bootstrap5-dl1 fc-bootstrap5-1sP fc-bootstrap5-XpK fc-bootstrap5-N2M",
            listDayHeaderInnerClass: "fc-bootstrap5-Apf fc-bootstrap5-dl6 fc-bootstrap5-9yp fc-bootstrap5-DIS",
            /* Single Month (in Multi-Month)
            --------------------------------------------------------------------------------------------- */
            singleMonthClass: (info) => joinClassNames(info.multiMonthColumns > 1 && "fc-bootstrap5-jD5", (info.multiMonthColumns === 1 && !info.isLast) && "fc-bootstrap5-zi1 fc-bootstrap5-JO5"),
            singleMonthHeaderClass: (info) => joinClassNames(info.multiMonthColumns > 1
                ? "fc-bootstrap5-cM0"
                : "fc-bootstrap5-dl6 fc-bootstrap5-zi1 fc-bootstrap5-JO5 bg-body", "fc-bootstrap5-XpK"),
            singleMonthHeaderInnerClass: "fc-bootstrap5-1Po fc-bootstrap5-DIS",
            /* Misc Table
            --------------------------------------------------------------------------------------------- */
            tableHeaderClass: "bg-body",
            fillerClass: "fc-bootstrap5-wsy fc-bootstrap5-JO5 fc-bootstrap5-lMo",
            dayHeaderRowClass: "fc-bootstrap5-wsy fc-bootstrap5-JO5",
            dayRowClass: "fc-bootstrap5-wsy fc-bootstrap5-JO5",
            slotHeaderRowClass: "fc-bootstrap5-wsy fc-bootstrap5-JO5",
            slotHeaderClass: getSlotClass,
            /* Misc Content
            --------------------------------------------------------------------------------------------- */
            navLinkClass: `fc-bootstrap5-Eu0 ${outlineWidthFocusClass} ${outlineInsetClass} ${primaryOutlineColorClass}`,
            inlineWeekNumberClass: (info) => joinClassNames("fc-bootstrap5-1EY fc-bootstrap5-n9G fc-bootstrap5-rbS fc-bootstrap5-C2g fc-bootstrap5-KUX fc-bootstrap5-HXA text-body-secondary bg-body-secondary", info.isNarrow ? xxsTextClass : "fc-bootstrap5-9yp"),
            nonBusinessHoursClass: "bg-body-tertiary",
            highlightClass: "bg-info fc-bootstrap5-pdS",
            /* Resource Day Header
            --------------------------------------------------------------------------------------------- */
            resourceDayHeaderAlign: "center",
            resourceDayHeaderClass: (info) => joinClassNames("fc-bootstrap5-wsy", info.isMajor ? "fc-bootstrap5-P7R" : "fc-bootstrap5-JO5"),
            resourceDayHeaderInnerClass: (info) => joinClassNames("fc-bootstrap5-rVY fc-bootstrap5-cJ3 fc-bootstrap5-dl1 fc-bootstrap5-sgX", info.isNarrow ? xxsTextClass : "fc-bootstrap5-9yp"),
            /* Resource Data Grid
            --------------------------------------------------------------------------------------------- */
            resourceColumnHeaderClass: "fc-bootstrap5-wsy fc-bootstrap5-JO5 fc-bootstrap5-E9P",
            resourceColumnHeaderInnerClass: "fc-bootstrap5-bvX fc-bootstrap5-9yp",
            resourceColumnResizerClass: "fc-bootstrap5-1EY fc-bootstrap5-AWB fc-bootstrap5-4Tv fc-bootstrap5-dnf",
            resourceGroupHeaderClass: "fc-bootstrap5-wsy fc-bootstrap5-JO5 bg-body-secondary",
            resourceGroupHeaderInnerClass: "fc-bootstrap5-bvX fc-bootstrap5-9yp",
            resourceCellClass: "fc-bootstrap5-wsy fc-bootstrap5-JO5",
            resourceCellInnerClass: "fc-bootstrap5-bvX fc-bootstrap5-9yp",
            resourceIndentClass: "fc-bootstrap5-Mde fc-bootstrap5-kp0 fc-bootstrap5-E9P",
            resourceExpanderClass: (info) => joinClassNames(`fc-bootstrap5-bCs ${outlineWidthFocusClass} ${primaryOutlineColorClass} bi bi-chevron-down`, !info.isExpanded && "fc-bootstrap5-KxI fc-bootstrap5-ZW3"),
            resourceHeaderRowClass: "fc-bootstrap5-wsy fc-bootstrap5-JO5",
            resourceRowClass: "fc-bootstrap5-wsy fc-bootstrap5-JO5",
            resourceColumnDividerClass: "fc-bootstrap5-1Wx fc-bootstrap5-JO5 fc-bootstrap5-a7i bg-body-secondary",
            /* Timeline Lane
            --------------------------------------------------------------------------------------------- */
            resourceGroupLaneClass: "fc-bootstrap5-wsy fc-bootstrap5-JO5 bg-body-secondary",
            resourceLaneClass: "fc-bootstrap5-wsy fc-bootstrap5-JO5",
            resourceLaneBottomClass: (info) => info.options.eventOverlap && "fc-bootstrap5-uuA",
            timelineBottomClass: "fc-bootstrap5-uuA",
        },
        views: {
            dayGrid: {
                ...dayRowCommonClasses,
                dayCellBottomClass: "fc-bootstrap5-toR",
            },
            multiMonth: {
                ...dayRowCommonClasses,
                dayCellBottomClass: "fc-bootstrap5-toR",
                tableClass: (info) => joinClassNames(info.multiMonthColumns > 1 && "fc-bootstrap5-JO5 fc-bootstrap5-wsy"),
            },
            timeGrid: {
                ...dayRowCommonClasses,
                dayCellBottomClass: "fc-bootstrap5-mhE",
                /* TimeGrid > Week Number Header
                ------------------------------------------------------------------------------------------- */
                weekNumberHeaderClass: "fc-bootstrap5-XpK fc-bootstrap5-LMv",
                weekNumberHeaderInnerClass: (info) => joinClassNames("fc-bootstrap5-rVY fc-bootstrap5-cJ3", info.isNarrow ? xxsTextClass : "fc-bootstrap5-9yp"),
                /* TimeGrid > All-Day Header
                ------------------------------------------------------------------------------------------- */
                allDayHeaderClass: "fc-bootstrap5-XpK fc-bootstrap5-LMv",
                allDayHeaderInnerClass: (info) => joinClassNames("fc-bootstrap5-rVY fc-bootstrap5-2tF fc-bootstrap5-2HE", info.isNarrow ? xxsTextClass : "fc-bootstrap5-9yp"),
                allDayDividerClass: "fc-bootstrap5-JIC fc-bootstrap5-JO5 fc-bootstrap5-8ub bg-body-secondary",
                /* TimeGrid > Slot Header
                ------------------------------------------------------------------------------------------- */
                slotHeaderClass: "fc-bootstrap5-LMv",
                slotHeaderInnerClass: (info) => joinClassNames("fc-bootstrap5-rVY fc-bootstrap5-cJ3", info.isNarrow ? xxsTextClass : "fc-bootstrap5-9yp"),
                slotHeaderDividerClass: "fc-bootstrap5-USt fc-bootstrap5-JO5",
                /* TimeGrid > Now-Indicator
                ------------------------------------------------------------------------------------------- */
                nowIndicatorHeaderClass: "fc-bootstrap5-rbS fc-bootstrap5-a10 fc-bootstrap5-XM3 fc-bootstrap5-rif fc-bootstrap5-jIH fc-bootstrap5-to7",
                nowIndicatorLineClass: "fc-bootstrap5-ku3 fc-bootstrap5-7uR",
            },
            list: {
                /* List-View > List-Item Event
                ------------------------------------------------------------------------------------------- */
                listDayClass: (info) => joinClassNames(!info.isLast && "fc-bootstrap5-zi1 fc-bootstrap5-JO5"),
                listItemEventClass: (info) => joinClassNames("fc-bootstrap5-bCs fc-bootstrap5-Apf fc-bootstrap5-dl6 fc-bootstrap5-wwb fc-bootstrap5-ku3 fc-bootstrap5-JO5", info.isInteractive
                    ? joinClassNames(faintHoverPressableClass, outlineInsetClass)
                    : faintHoverClass),
                listItemEventBeforeClass: "fc-bootstrap5-yDA",
                listItemEventInnerClass: "fc-bootstrap5-eF2",
                listItemEventTimeClass: "fc-bootstrap5-88I fc-bootstrap5-yi0 fc-bootstrap5-roZ fc-bootstrap5-kMV fc-bootstrap5-TZ4 fc-bootstrap5-pKG fc-bootstrap5-IPx fc-bootstrap5-9yp",
                listItemEventTitleClass: (info) => joinClassNames("fc-bootstrap5-1El fc-bootstrap5-2KU fc-bootstrap5-TZ4 fc-bootstrap5-pKG fc-bootstrap5-9yp", info.event.url && "fc-bootstrap5-Ogp"),
                /* No-Events Screen
                ------------------------------------------------------------------------------------------- */
                noEventsClass: "bg-body-secondary fc-bootstrap5-dl1 fc-bootstrap5-sgX fc-bootstrap5-XpK fc-bootstrap5-E9P",
                noEventsInnerClass: "fc-bootstrap5-OUe fc-bootstrap5-jGI fc-bootstrap5-P9h",
            },
            timeline: {
                /* Timeline > Row Event
                ------------------------------------------------------------------------------------------- */
                rowEventClass: (info) => joinClassNames(info.isEnd && "fc-bootstrap5-9hC", "fc-bootstrap5-XpK"),
                rowEventBeforeClass: (info) => (!info.isStart && `${continuationArrowClass} fc-bootstrap5-Bda fc-bootstrap5-5JV`),
                rowEventAfterClass: (info) => (!info.isEnd && `${continuationArrowClass} fc-bootstrap5-hhi fc-bootstrap5-LaM`),
                rowEventInnerClass: "fc-bootstrap5-2rx",
                rowEventTimeClass: "fc-bootstrap5-oQ2",
                rowEventTitleClass: "fc-bootstrap5-oQ2",
                /* Timeline > More-Link
                ------------------------------------------------------------------------------------------- */
                rowMoreLinkClass: `fc-bootstrap5-9hC fc-bootstrap5-Ika fc-bootstrap5-wsy fc-bootstrap5-d0j fc-bootstrap5-Fvv fc-bootstrap5-4MR ${strongSolidPressableClass} fc-bootstrap5-vwH`,
                rowMoreLinkInnerClass: "fc-bootstrap5-KUX fc-bootstrap5-a3B",
                /* Timeline > Slot Header
                ------------------------------------------------------------------------------------------- */
                slotHeaderAlign: (info) => info.isTime ? "start" : "center",
                slotHeaderClass: (info) => joinClassNames("fc-bootstrap5-E9P", !info.level && "fc-bootstrap5-pKG"),
                slotHeaderInnerClass: (info) => joinClassNames("fc-bootstrap5-fn8 fc-bootstrap5-V9v fc-bootstrap5-9yp", info.hasNavLink && "fc-bootstrap5-Eu0"),
                slotHeaderDividerClass: "fc-bootstrap5-zi1 fc-bootstrap5-JO5",
                /* Timeline > Now-Indicator
                ------------------------------------------------------------------------------------------- */
                nowIndicatorHeaderClass: "fc-bootstrap5-n9G fc-bootstrap5-J04 fc-bootstrap5-ybF fc-bootstrap5-Pqk fc-bootstrap5-bLA fc-bootstrap5-7uR",
                nowIndicatorLineClass: "fc-bootstrap5-3J4 fc-bootstrap5-7uR",
            },
        }
    };

    FullCalendar.globalPlugins.push(plugin);

})();
