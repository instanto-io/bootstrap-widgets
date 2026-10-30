package org.gwtbootstrap3.extras.datepicker.client.ui.base.constants;

/**
 * The date picker's display, calendar and input options. Grouping these related
 * capabilities keeps the widget's contract readable while preserving each
 * individual interface for existing callers and UiBinder properties.
 */
public interface HasDatePickerOptions extends HasAutoClose, HasDaysOfWeekDisabled, HasEndDate,
        HasForceParse, HasFormat, HasHighlightToday, HasKeyboardNavigation, HasLanguage,
        HasMinView, HasPosition, HasShowTodayButton, HasShowClearButton, HasStartDate,
        HasStartView, HasViewSelect, HasWeekStart {
}
