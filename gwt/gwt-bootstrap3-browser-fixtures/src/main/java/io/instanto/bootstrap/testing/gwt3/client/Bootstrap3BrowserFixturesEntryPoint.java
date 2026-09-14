package io.instanto.bootstrap.testing.gwt3.client;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.CheckBoxButton;
import org.gwtbootstrap3.client.ui.Collapse;
import org.gwtbootstrap3.client.ui.DropDown;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.InputGroup;
import org.gwtbootstrap3.client.ui.InputGroupAddon;
import org.gwtbootstrap3.client.ui.InputGroupButton;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.NavTabs;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelGroup;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.Popover;
import org.gwtbootstrap3.client.ui.Radio;
import org.gwtbootstrap3.client.ui.RadioButton;
import org.gwtbootstrap3.client.ui.TabContent;
import org.gwtbootstrap3.client.ui.TabListItem;
import org.gwtbootstrap3.client.ui.TabPane;
import org.gwtbootstrap3.client.ui.TabPanel;
import org.gwtbootstrap3.client.ui.SuggestBox;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.Tooltip;
import org.gwtbootstrap3.client.ui.VerticalButtonGroup;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonGroupSize;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.InputGroupSize;
import org.gwtbootstrap3.client.ui.constants.Placement;
import org.gwtbootstrap3.client.ui.constants.TabPosition;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.constants.Trigger;
import org.gwtbootstrap3.client.ui.form.validator.BlankValidator;
import org.gwtbootstrap3.client.ui.theme.StandardThemes;
import org.gwtbootstrap3.client.ui.theme.Theme;
import org.gwtbootstrap3.client.ui.theme.Themes;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.gwtbootstrap3.extras.select.client.ui.Select;
import org.gwtbootstrap3.themes.client.BootswatchThemes;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/** Browser-facing fixtures compiled by GWT and driven through Chrome DevTools. */
public final class Bootstrap3BrowserFixturesEntryPoint implements EntryPoint {
    private static final String CLICK_COUNT = "data-click-count";
    private static final String CHANGE_COUNT = "data-change-count";

    @Override
    public void onModuleLoad() {
        RootPanel root = RootPanel.get("fixtures");
        initializeThemes();
        root.add(new HTML("<h1>GWT Bootstrap 3 interaction fixtures</h1>"));
        root.add(toggleFixture());
        root.add(disabledToggleFixture());
        root.add(checkboxFixture());
        root.add(radioFixture());
        root.add(dropdownFixture());
        root.add(splitDropdownFixture());
        root.add(dropupFixture());
        root.add(alignedDropdownFixture());
        root.add(checkboxLabelFixture());
        root.add(radioLabelFixture());
        root.add(textLabelFixture());
        root.add(textValueFixture());
        root.add(programmaticTextValueFixture());
        root.add(validationFixture());
        root.add(listSelectionFixture());
        root.add(radioGroupFixture());
        root.add(formSubmissionFixture());
        root.add(collapseFixture());
        root.add(accordionFixture());
        root.add(detachedCollapseFixture());
        root.add(basicLifecycleFixture());
        root.add(remountLifecycleFixture());
        root.add(handlerLifecycleFixture());
        root.add(textInputGroupFixture());
        root.add(buttonInputGroupFixture());
        root.add(sizingInputGroupFixture());
        root.add(basicSuggestBoxFixture());
        root.add(suggestBoxLifecycleFixture());
        root.add(themeSwitcherFixture());
        root.add(darkThemeFixture());
        root.add(themePersistenceFixture());
        root.add(basicModalFixture());
        root.add(keyboardModalFixture());
        root.add(exclusiveModalFixture());
        root.add(tooltipOptionsFixture());
        root.add(tooltipDisposalFixture());
        root.add(popoverHtmlFixture());
        root.add(popoverProgrammaticFixture());
        root.add(pluginLifecycleFixture());
        root.add(basicTabsFixture());
        root.add(disabledTabsFixture());
        root.add(programmaticTabsFixture());
        root.add(fadingTabsFixture());
        root.add(positionedTabsFixture());
        root.add(loadingFixture());
        root.add(programmaticCheckboxFixture());
        root.add(buttonTypesFixture());
        root.add(buttonSizesFixture());
        root.add(basicButtonGroupFixture());
        root.add(buttonGroupSizesFixture());
        root.add(verticalButtonGroupFixture());
        root.add(nestedButtonGroupFixture());
        root.add(buttonGroupRemovalFixture());
        root.add(bootstrapSelectFixture());

        Document.get().getBody().setAttribute("data-fixtures-ready", "true");
        markReady();
    }

    private Widget toggleFixture() {
        Button button = tagged(new Button("Toggle"), "behaviour/toggle-button/basic");
        button.setDataToggle(Toggle.BUTTON);
        countClicks(button, button);
        return fixture("Toggle button", button);
    }

    private Widget disabledToggleFixture() {
        Button button = tagged(new Button("Disabled"), "behaviour/toggle-button/disabled");
        button.setDataToggle(Toggle.BUTTON);
        button.setEnabled(false);
        countClicks(button, button);
        return fixture("Disabled toggle button", button);
    }

    private Widget checkboxFixture() {
        ButtonGroup group = tagged(new ButtonGroup(), "behaviour/check-box-buttons/independent");
        group.setDataToggle(Toggle.BUTTONS);
        addCheckbox(group, "First", "behaviour/check-box-button/first");
        addCheckbox(group, "Second", "behaviour/check-box-button/second");
        addCheckbox(group, "Third", "behaviour/check-box-button/third");
        return fixture("Checkbox buttons", group);
    }

    private Widget radioFixture() {
        ButtonGroup group = tagged(new ButtonGroup(), "behaviour/radio-buttons/exclusive");
        group.setDataToggle(Toggle.BUTTONS);
        group.setName("touch-radio-fixture");

        RadioButton first = tagged(new RadioButton("touch-radio-fixture", "First"),
                "behaviour/radio-button/first");
        RadioButton second = tagged(new RadioButton("touch-radio-fixture", "Second"),
                "behaviour/radio-button/second");
        first.setActive(true);
        second.setActive(false);
        initializeCounter(first.getElement(), CHANGE_COUNT);
        initializeCounter(second.getElement(), CHANGE_COUNT);
        initializeValue(first, true);
        initializeValue(second, false);
        countClicks(first, first);
        countClicks(second, second);

        first.addValueChangeHandler(event -> {
            increment(first.getElement(), CHANGE_COUNT);
            initializeValue(first, first.getValue());
            initializeValue(second, second.getValue());
        });
        second.addValueChangeHandler(event -> {
            increment(second.getElement(), CHANGE_COUNT);
            initializeValue(first, first.getValue());
            initializeValue(second, second.getValue());
        });
        group.add(first);
        group.add(second);
        return fixture("Radio buttons", group);
    }

    private Widget dropdownFixture() {
        FlowPanel container = new FlowPanel();
        DropDown dropdown = tagged(new DropDown(), "behaviour/dropdown/basic");
        Anchor toggle = tagged(new Anchor("Touch menu", "#"), "behaviour/dropdown/toggle");
        toggle.setDataToggle(Toggle.DROPDOWN);
        countClicks(toggle, toggle);

        DropDownMenu menu = new DropDownMenu();
        AnchorListItem action = tagged(new AnchorListItem("Action"), "behaviour/dropdown/action");
        initializeCounter(action.getElement(), CLICK_COUNT);
        action.addClickHandler(event -> increment(action.getElement(), CLICK_COUNT));
        menu.add(action);
        AnchorListItem disabled = tagged(new AnchorListItem("Disabled"),
                "behaviour/dropdown/disabled-item");
        disabled.setHref("#disabled-action");
        disabled.setEnabled(false);
        initializeCounter(disabled.getElement(), CLICK_COUNT);
        disabled.addClickHandler(event -> increment(disabled.getElement(), CLICK_COUNT));
        menu.add(disabled);
        dropdown.add(toggle);
        dropdown.add(menu);

        Button outside = tagged(new Button("Outside"), "behaviour/dropdown/outside");
        outside.getElement().getStyle().setProperty("marginTop", "80px");
        container.add(dropdown);
        container.add(outside);
        return fixture("Dropdown", container);
    }

    private Widget splitDropdownFixture() {
        ButtonGroup group = tagged(new ButtonGroup(), "behaviour/dropdown/split-button");
        Button primary = tagged(new Button("Primary action"), "behaviour/dropdown/split-primary");
        countClicks(primary, primary);
        Button toggle = tagged(new Button(), "behaviour/dropdown/split-toggle");
        toggle.setDataToggle(Toggle.DROPDOWN);
        toggle.getElement().setAttribute("aria-label", "Primary action menu");
        DropDownMenu menu = tagged(new DropDownMenu(), "behaviour/dropdown/split-menu");
        menu.add(new AnchorListItem("Menu action"));
        group.add(primary);
        group.add(toggle);
        group.add(menu);
        return fixture("Split dropdown", group);
    }

    private Widget dropupFixture() {
        ButtonGroup group = tagged(new ButtonGroup(), "behaviour/dropdown/dropup");
        group.setDropUp(true);
        Button toggle = tagged(new Button("Open upward"), "behaviour/dropdown/dropup-toggle");
        toggle.setDataToggle(Toggle.DROPDOWN);
        DropDownMenu menu = tagged(new DropDownMenu(), "behaviour/dropdown/dropup-menu");
        menu.add(new AnchorListItem("First action"));
        menu.add(new AnchorListItem("Second action"));
        group.add(toggle);
        group.add(menu);
        return fixture("Dropup", group);
    }

    private Widget alignedDropdownFixture() {
        ButtonGroup group = tagged(new ButtonGroup(), "behaviour/dropdown/aligned-width");
        group.setWidth("220px");
        Button toggle = tagged(new Button("Constrained menu"),
                "behaviour/dropdown/aligned-width-toggle");
        toggle.setDataToggle(Toggle.DROPDOWN);
        toggle.setWidth("100%");
        DropDownMenu menu = tagged(new DropDownMenu(),
                "behaviour/dropdown/aligned-width-menu");
        menu.setWidth("100%");
        menu.add(new AnchorListItem("Aligned menu item"));
        group.add(toggle);
        group.add(menu);
        return fixture("Aligned dropdown width", group);
    }

    private Widget checkboxLabelFixture() {
        CheckBox checkbox = tagged(new CheckBox("Accept"), "behaviour/form/checkbox-label");
        initializeCounter(checkbox.getElement(), CHANGE_COUNT);
        initializeValue(checkbox, false);
        checkbox.addValueChangeHandler(event -> {
            increment(checkbox.getElement(), CHANGE_COUNT);
            initializeValue(checkbox, event.getValue());
            checkbox.getElement().setAttribute("data-source-match",
                    Boolean.toString(event.getSource() == checkbox));
        });
        return fixture("Checkbox label", checkbox);
    }

    private Widget radioLabelFixture() {
        Radio radio = tagged(new Radio("form-radio-label", "Select"),
                "behaviour/form/radio-label");
        initializeCounter(radio.getElement(), CHANGE_COUNT);
        initializeValue(radio, false);
        radio.addValueChangeHandler(event -> {
            increment(radio.getElement(), CHANGE_COUNT);
            initializeValue(radio, event.getValue());
            radio.getElement().setAttribute("data-source-match",
                    Boolean.toString(event.getSource() == radio));
        });
        return fixture("Radio label", radio);
    }

    private Widget textLabelFixture() {
        FormGroup group = tagged(new FormGroup(), "behaviour/form/text-label");
        FormLabel label = tagged(new FormLabel(), "behaviour/form/text-label/label");
        label.setText("Account name");
        TextBox textBox = tagged(new TextBox(), "behaviour/form/text-label/control");
        textBox.setId("form-text-label-control");
        label.setFor(textBox.getId());
        group.add(label);
        group.add(textBox);
        return fixture("Text field label", group);
    }

    private Widget textValueFixture() {
        TextBox textBox = tagged(new TextBox(), "behaviour/form/text-value");
        textBox.setValue("initial");
        initializeCounter(textBox.getElement(), CHANGE_COUNT);
        textBox.getElement().setAttribute("data-value", textBox.getValue());
        textBox.addValueChangeHandler(event -> {
            increment(textBox.getElement(), CHANGE_COUNT);
            textBox.getElement().setAttribute("data-value", event.getValue());
            textBox.getElement().setAttribute("data-source-match",
                    Boolean.toString(event.getSource() == textBox));
        });
        return fixture("Text field value", textBox);
    }

    private Widget programmaticTextValueFixture() {
        FlowPanel container = new FlowPanel();
        TextBox textBox = tagged(new TextBox(), "behaviour/form/values");
        textBox.setValue("initial");
        initializeCounter(textBox.getElement(), CHANGE_COUNT);
        textBox.getElement().setAttribute("data-value", textBox.getValue());
        textBox.addValueChangeHandler(event -> {
            increment(textBox.getElement(), CHANGE_COUNT);
            textBox.getElement().setAttribute("data-value", event.getValue());
            textBox.getElement().setAttribute("data-source-match",
                    Boolean.toString(event.getSource() == textBox));
        });
        Button silent = tagged(new Button("Set silently"), "behaviour/form/values/set-silent");
        silent.addClickHandler(event -> {
            textBox.setValue("silent", false);
            textBox.getElement().setAttribute("data-value", textBox.getValue());
        });
        Button firing = tagged(new Button("Set with event"), "behaviour/form/values/set-firing");
        firing.addClickHandler(event -> {
            textBox.setValue("firing", true);
            textBox.getElement().setAttribute("data-value", textBox.getValue());
        });
        container.add(textBox);
        container.add(silent);
        container.add(firing);
        return fixture("Programmatic text values", container);
    }

    private Widget validationFixture() {
        FormGroup group = tagged(new FormGroup(), "behaviour/form/validation");
        FormLabel label = new FormLabel();
        label.setText("Required value");
        TextBox textBox = tagged(new TextBox(), "behaviour/form/validation/control");
        textBox.setId("form-validation-control");
        label.setFor(textBox.getId());
        textBox.setValidators(new BlankValidator<String>("Required"));
        HelpBlock message = tagged(new HelpBlock(), "behaviour/form/validation/message");
        message.getElement().setId("form-validation-message");
        Button validate = tagged(new Button("Validate"), "behaviour/form/validation/action");
        validate.addClickHandler(event -> group.getElement().setAttribute(
                "data-validation-result", Boolean.toString(textBox.validate(true))));
        Button clear = tagged(new Button("Set valid"), "behaviour/form/validation/clear");
        clear.addClickHandler(event -> {
            textBox.setValue("valid");
            group.getElement().setAttribute("data-validation-result",
                    Boolean.toString(textBox.validate(true)));
        });
        group.add(label);
        group.add(textBox);
        group.add(message);
        group.add(validate);
        group.add(clear);
        return fixture("Validation", group);
    }

    private Widget formSubmissionFixture() {
        Form form = tagged(new Form(), "behaviour/form/submission");
        initializeCounter(form.getElement(), "data-submit-count");
        form.addSubmitHandler(event -> {
            increment(form.getElement(), "data-submit-count");
            form.getElement().setAttribute("data-source-match",
                    Boolean.toString(event.getSource() == form));
            event.cancel();
        });
        Button submit = tagged(new Button("Submit"), "behaviour/form/submission/action");
        submit.addClickHandler(event -> form.submit());
        form.add(submit);
        form.getElement().setAttribute("data-frame-name", form.getTarget());
        return fixture("Cancelled form submission", form);
    }

    private Widget listSelectionFixture() {
        ListBox listBox = tagged(new ListBox(), "behaviour/form/list-selection");
        listBox.addItem("France");
        listBox.addItem("Germany");
        listBox.addItem("Spain");
        initializeCounter(listBox.getElement(), CHANGE_COUNT);
        listBox.getElement().setAttribute("data-value", listBox.getItemText(0));
        listBox.addChangeHandler(event -> {
            increment(listBox.getElement(), CHANGE_COUNT);
            listBox.getElement().setAttribute("data-value",
                    listBox.getItemText(listBox.getSelectedIndex()));
            listBox.getElement().setAttribute("data-source-match",
                    Boolean.toString(event.getSource() == listBox));
        });
        return fixture("List selection", listBox);
    }

    private Widget radioGroupFixture() {
        FlowPanel group = tagged(new FlowPanel(), "behaviour/form/radio-group");
        Radio first = tagged(new Radio("form-radio-group", "First"),
                "behaviour/form/radio-group/first");
        first.setFormValue("first");
        Radio second = tagged(new Radio("form-radio-group", "Second"),
                "behaviour/form/radio-group/second");
        second.setFormValue("second");
        group.getElement().setAttribute("data-value", "");
        first.addValueChangeHandler(event -> recordRadioGroupValue(group, first, second));
        second.addValueChangeHandler(event -> recordRadioGroupValue(group, first, second));
        group.add(first);
        group.add(second);
        return fixture("Radio group", group);
    }

    private void recordRadioGroupValue(FlowPanel group, Radio first, Radio second) {
        if (first.getValue()) {
            group.getElement().setAttribute("data-value", first.getFormValue());
        } else if (second.getValue()) {
            group.getElement().setAttribute("data-value", second.getFormValue());
        }
    }

    private Widget collapseFixture() {
        FlowPanel container = new FlowPanel();
        Button target = tagged(new Button("Touch collapse"), "behaviour/collapse/toggle");
        Collapse collapse = tagged(new Collapse(), "behaviour/collapse/basic");
        collapse.getElement().setId("touch-collapse-content");
        collapse.setToggle(false);
        collapse.add(new HTML("<p>Collapsed content</p>"));
        collapse.getElement().setAttribute("data-event-order", "");
        collapse.addShowHandler(event -> appendEvent(collapse.getElement(), "show"));
        collapse.addShownHandler(event -> appendEvent(collapse.getElement(), "shown"));
        collapse.addHideHandler(event -> appendEvent(collapse.getElement(), "hide"));
        collapse.addHiddenHandler(event -> appendEvent(collapse.getElement(), "hidden"));

        target.setDataToggle(Toggle.COLLAPSE);
        target.setDataTargetWidget(collapse);
        countClicks(target, target);
        container.add(target);
        container.add(collapse);
        return fixture("Collapse", container);
    }

    private Widget accordionFixture() {
        PanelGroup accordion = tagged(new PanelGroup(), "behaviour/collapse/accordion");
        accordion.getElement().setId("behaviour-collapse-accordion");
        accordion.add(accordionItem("first", true));
        accordion.add(accordionItem("second", false));
        accordion.add(accordionItem("third", false));
        return fixture("Accordion", accordion);
    }

    private Widget accordionItem(String name, boolean open) {
        String collapseId = "behaviour-accordion-" + name;
        Panel panel = new Panel();
        PanelHeader header = new PanelHeader();
        Anchor toggle = tagged(new Anchor(capitalize(name), "#" + collapseId),
                "behaviour/collapse/accordion/" + name + "-toggle");
        toggle.setDataToggle(Toggle.COLLAPSE);
        toggle.setDataParent("#behaviour-collapse-accordion");
        toggle.getElement().setAttribute("aria-controls", collapseId);
        toggle.getElement().setAttribute("aria-expanded", Boolean.toString(open));
        header.add(toggle);

        PanelCollapse collapse = tagged(new PanelCollapse(),
                "behaviour/collapse/accordion/" + name + "-panel");
        collapse.getElement().setId(collapseId);
        collapse.setIn(open);
        PanelBody body = new PanelBody();
        body.add(new HTML("<p>" + capitalize(name) + " accordion content</p>"));
        collapse.add(body);
        panel.add(header);
        panel.add(collapse);
        return panel;
    }

    private Widget detachedCollapseFixture() {
        FlowPanel state = tagged(new FlowPanel(), "behaviour/collapse/detach");
        FlowPanel host = tagged(new FlowPanel(), "behaviour/collapse/detach/host");
        Collapse collapse = tagged(new Collapse(), "behaviour/collapse/detach/panel");
        collapse.getElement().setId("behaviour-collapse-detach-panel");
        collapse.setToggle(false);
        collapse.getElement().setAttribute("data-event-order", "");
        collapse.addShowHandler(event -> appendEvent(collapse.getElement(), "show"));
        collapse.addShownHandler(event -> appendEvent(collapse.getElement(), "shown"));
        collapse.add(new HTML("<p>Remounted collapse content</p>"));
        host.add(collapse);

        Button remount = tagged(new Button("Remount collapse"),
                "behaviour/collapse/detach/remount");
        remount.addClickHandler(event -> {
            host.remove(collapse);
            host.add(collapse);
            state.getElement().setAttribute("data-remounted", "true");
        });
        Button toggle = tagged(new Button("Open remounted collapse"),
                "behaviour/collapse/detach/toggle");
        toggle.setDataToggle(Toggle.COLLAPSE);
        toggle.setDataTargetWidget(collapse);
        state.add(remount);
        state.add(toggle);
        state.add(host);
        return fixture("Detached collapse", state);
    }

    private Widget basicLifecycleFixture() {
        FlowPanel state = tagged(new FlowPanel(), "behaviour/lifecycle/basic");
        FlowPanel host = tagged(new FlowPanel(), "behaviour/lifecycle/basic/host");
        Button widget = tagged(new Button("Lifecycle widget"),
                "behaviour/lifecycle/basic/widget");
        widget.addAttachHandler(event -> recordLifecycle(state, host, widget));
        host.add(widget);
        Button remove = tagged(new Button("Remove widget"),
                "behaviour/lifecycle/basic/remove");
        remove.addClickHandler(event -> {
            host.remove(widget);
            recordLifecycle(state, host, widget);
        });
        state.add(remove);
        state.add(host);
        return fixture("Basic widget lifecycle", state);
    }

    private Widget remountLifecycleFixture() {
        FlowPanel state = tagged(new FlowPanel(), "behaviour/lifecycle/remount");
        FlowPanel oldHost = tagged(new FlowPanel(), "behaviour/lifecycle/remount/old-host");
        FlowPanel freshHost = tagged(new FlowPanel(), "behaviour/lifecycle/remount/fresh-host");
        Button widget = tagged(new Button("Remountable widget"),
                "behaviour/lifecycle/remount/widget");
        initializeCounter(widget.getElement(), "data-attach-count");
        widget.addAttachHandler(event -> {
            if (event.isAttached()) {
                increment(widget.getElement(), "data-attach-count");
            }
        });
        oldHost.add(widget);
        Button remount = tagged(new Button("Move to fresh host"),
                "behaviour/lifecycle/remount/action");
        remount.addClickHandler(event -> {
            oldHost.remove(widget);
            freshHost.add(widget);
            state.getElement().setAttribute("data-parent-match",
                    Boolean.toString(widget.getParent() == freshHost));
            state.getElement().setAttribute("data-new-attach-count",
                    Integer.toString(Integer.parseInt(widget.getElement()
                            .getAttribute("data-attach-count")) - 1));
        });
        state.add(remount);
        state.add(oldHost);
        state.add(freshHost);
        return fixture("Widget remount", state);
    }

    private Widget handlerLifecycleFixture() {
        FlowPanel state = tagged(new FlowPanel(), "behaviour/lifecycle/handlers");
        FlowPanel firstHost = tagged(new FlowPanel(), "behaviour/lifecycle/handlers/first-host");
        FlowPanel secondHost = tagged(new FlowPanel(), "behaviour/lifecycle/handlers/second-host");
        Button widget = tagged(new Button("Activate after remounts"),
                "behaviour/lifecycle/handlers/widget");
        countClicks(widget, widget);
        firstHost.add(widget);
        final FlowPanel[] currentHost = {firstHost};
        Button remount = tagged(new Button("Remount three times"),
                "behaviour/lifecycle/handlers/remount");
        remount.addClickHandler(event -> {
            for (int i = 0; i < 3; i++) {
                FlowPanel nextHost = currentHost[0] == firstHost ? secondHost : firstHost;
                currentHost[0].remove(widget);
                nextHost.add(widget);
                currentHost[0] = nextHost;
            }
            state.getElement().setAttribute("data-current-host",
                    currentHost[0] == firstHost ? "first" : "second");
        });
        state.add(remount);
        state.add(firstHost);
        state.add(secondHost);
        return fixture("Repeated widget remount", state);
    }

    private void recordLifecycle(FlowPanel state, FlowPanel host, Widget widget) {
        state.getElement().setAttribute("data-parent-match",
                Boolean.toString(widget.getParent() == host));
        state.getElement().setAttribute("data-attached", Boolean.toString(widget.isAttached()));
        state.getElement().setAttribute("data-dom-parent",
                Boolean.toString(widget.getElement().getParentElement() == host.getElement()));
    }

    private Widget textInputGroupFixture() {
        InputGroup group = tagged(new InputGroup(), "behaviour/input-group/text-addons");
        InputGroupAddon prefix = tagged(new InputGroupAddon(),
                "behaviour/input-group/text-addons/prefix");
        prefix.setText("Prefix");
        TextBox control = tagged(new TextBox(), "behaviour/input-group/text-addons/control");
        control.setPlaceholder("Grouped value");
        InputGroupAddon suffix = tagged(new InputGroupAddon(),
                "behaviour/input-group/text-addons/suffix");
        suffix.setText("Suffix");
        group.add(prefix);
        group.add(control);
        group.add(suffix);
        return fixture("Text input group", group);
    }

    private Widget buttonInputGroupFixture() {
        InputGroup group = tagged(new InputGroup(), "behaviour/input-group/button-addon");
        InputGroupButton addon = tagged(new InputGroupButton(),
                "behaviour/input-group/button-addon/container");
        Button button = tagged(new Button("Apply"), "behaviour/input-group/button-addon/action");
        countClicks(button, button);
        addon.add(button);
        TextBox control = tagged(new TextBox(), "behaviour/input-group/button-addon/control");
        control.setValue("original");
        group.add(addon);
        group.add(control);
        return fixture("Button input group", group);
    }

    private Widget sizingInputGroupFixture() {
        FlowPanel state = new FlowPanel();
        InputGroup group = tagged(new InputGroup(), "behaviour/input-group/sizing");
        group.add(new InputGroupAddon());
        group.add(new TextBox());
        Button large = tagged(new Button("Large"), "behaviour/input-group/sizing/large");
        large.addClickHandler(event -> {
            group.setSize(InputGroupSize.LARGE);
            group.getElement().setAttribute("data-reported-size", group.getSize().name());
        });
        Button reset = tagged(new Button("Default"), "behaviour/input-group/sizing/default");
        reset.addClickHandler(event -> {
            group.setSize(InputGroupSize.DEFAULT);
            group.getElement().setAttribute("data-reported-size", group.getSize().name());
        });
        state.add(group);
        state.add(large);
        state.add(reset);
        return fixture("Input group sizing", state);
    }

    private Widget basicSuggestBoxFixture() {
        MultiWordSuggestOracle oracle = countryOracle();
        SuggestBox suggestBox = tagged(new SuggestBox(oracle), "behaviour/suggest-box/basic");
        suggestBox.setPlaceholder("Country");
        recordSuggestions(suggestBox);
        return fixture("Basic suggest box", suggestBox);
    }

    private Widget suggestBoxLifecycleFixture() {
        FlowPanel state = tagged(new FlowPanel(), "behaviour/suggest-box/lifecycle");
        FlowPanel host = tagged(new FlowPanel(), "behaviour/suggest-box/lifecycle/host");
        SuggestBox suggestBox = tagged(new SuggestBox(countryOracle()),
                "behaviour/suggest-box/lifecycle/control");
        recordSuggestions(suggestBox);
        host.add(suggestBox);
        Button detach = tagged(new Button("Detach"), "behaviour/suggest-box/lifecycle/detach");
        detach.addClickHandler(event -> host.remove(suggestBox));
        Button remount = tagged(new Button("Remount"), "behaviour/suggest-box/lifecycle/remount");
        remount.addClickHandler(event -> host.add(suggestBox));
        state.add(detach);
        state.add(remount);
        state.add(host);
        return fixture("Suggest box lifecycle", state);
    }

    private MultiWordSuggestOracle countryOracle() {
        MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
        oracle.add("United Kingdom");
        oracle.add("United States");
        oracle.add("France");
        oracle.add("Germany");
        return oracle;
    }

    private void recordSuggestions(SuggestBox suggestBox) {
        initializeCounter(suggestBox.getElement(), "data-suggestion-count");
        suggestBox.addSelectionHandler(event -> {
            increment(suggestBox.getElement(), "data-suggestion-count");
            suggestBox.getElement().setAttribute("data-selected-value",
                    event.getSelectedItem().getReplacementString());
            suggestBox.getElement().setAttribute("data-source-match",
                    Boolean.toString(event.getSource() == suggestBox));
        });
    }

    private void initializeThemes() {
        Themes.register(StandardThemes.all());
        Themes.register(BootswatchThemes.all());
        Themes.restore(Themes.get(StandardThemes.BOOTSTRAP));
    }

    private Widget themeSwitcherFixture() {
        FlowPanel state = tagged(new FlowPanel(), "behaviour/themes/switcher");
        Button standard = tagged(new Button("Bootstrap"),
                "behaviour/themes/switcher/standard");
        standard.addClickHandler(event -> {
            Themes.apply(StandardThemes.BOOTSTRAP);
            recordTheme(state);
        });
        Button flatly = tagged(new Button("Flatly"), "behaviour/themes/switcher/flatly");
        flatly.addClickHandler(event -> {
            Themes.apply("flatly");
            recordTheme(state);
        });
        state.add(standard);
        state.add(flatly);
        recordTheme(state);
        return fixture("Theme switcher", state);
    }

    private Widget darkThemeFixture() {
        FlowPanel state = tagged(new FlowPanel(), "behaviour/themes/dark");
        Button darkly = tagged(new Button("Darkly"), "behaviour/themes/dark/darkly");
        darkly.addClickHandler(event -> {
            Themes.apply("darkly");
            recordTheme(state);
        });
        Button flatly = tagged(new Button("Flatly"), "behaviour/themes/dark/flatly");
        flatly.addClickHandler(event -> {
            Themes.apply("flatly");
            recordTheme(state);
        });
        state.add(darkly);
        state.add(flatly);
        recordTheme(state);
        return fixture("Dark theme reporting", state);
    }

    private Widget themePersistenceFixture() {
        FlowPanel state = tagged(new FlowPanel(), "behaviour/themes/persistence");
        Button united = tagged(new Button("United"), "behaviour/themes/persistence/united");
        united.addClickHandler(event -> {
            Themes.apply("united");
            recordTheme(state);
        });
        state.add(united);
        recordTheme(state);
        return fixture("Theme persistence", state);
    }

    private void recordTheme(FlowPanel state) {
        Theme current = Themes.getCurrent();
        Element link = Document.get().getElementById(Themes.LINK_ID);
        state.getElement().setAttribute("data-current-theme",
                current == null ? "" : current.getName());
        state.getElement().setAttribute("data-dark", Boolean.toString(Themes.isDark()));
        state.getElement().setAttribute("data-theme-url",
                link == null ? "" : link.getAttribute("href"));
    }

    private Widget basicModalFixture() {
        FlowPanel state = tagged(new FlowPanel(), "behaviour/modal/basic");
        Modal modal = tagged(new Modal(), "behaviour/modal/basic/dialog");
        modal.getElement().setId("behaviour-basic-modal");
        modal.setTitle("Basic modal title");
        modal.setClosable(false);
        modal.add(new HTML("<p>Basic modal content</p>"));
        modal.getElement().setAttribute("data-event-order", "");
        modal.addShowHandler(event -> appendEvent(modal.getElement(), "show"));
        modal.addShownHandler(event -> appendEvent(modal.getElement(), "shown"));
        modal.addHideHandler(event -> appendEvent(modal.getElement(), "hide"));
        modal.addHiddenHandler(event -> appendEvent(modal.getElement(), "hidden"));
        ModalFooter footer = new ModalFooter();
        Button dismiss = tagged(new Button("Close"), "behaviour/modal/basic/dismiss");
        dismiss.setDataDismiss(ButtonDismiss.MODAL);
        footer.add(dismiss);
        modal.add(footer);

        Button target = tagged(new Button("Open basic modal"), "behaviour/modal/basic/target");
        target.setDataToggle(Toggle.MODAL);
        target.setDataTargetWidget(modal);
        state.add(target);
        state.add(modal);
        return fixture("Basic modal", state);
    }

    private Widget keyboardModalFixture() {
        FlowPanel state = tagged(new FlowPanel(), "behaviour/modal/keyboard");
        Modal modal = tagged(new Modal(), "behaviour/modal/keyboard/dialog");
        modal.getElement().setId("behaviour-keyboard-modal");
        modal.setTitle("Keyboard modal");
        modal.setDataKeyboard(true);
        modal.add(new HTML("<p>Press Escape to close</p>"));
        Button target = tagged(new Button("Open keyboard modal"),
                "behaviour/modal/keyboard/target");
        target.setDataToggle(Toggle.MODAL);
        target.setDataTargetWidget(modal);
        state.add(target);
        state.add(modal);
        return fixture("Keyboard modal", state);
    }

    private Widget exclusiveModalFixture() {
        FlowPanel state = tagged(new FlowPanel(), "behaviour/modal/exclusive");
        Modal first = tagged(new Modal(), "behaviour/modal/exclusive/first");
        first.getElement().setId("behaviour-exclusive-first");
        first.setTitle("First modal");
        Modal second = tagged(new Modal(), "behaviour/modal/exclusive/second");
        second.getElement().setId("behaviour-exclusive-second");
        second.setTitle("Second modal");
        second.setHideOtherModals(true);

        ModalFooter firstFooter = new ModalFooter();
        Button openSecond = tagged(new Button("Open second modal"),
                "behaviour/modal/exclusive/open-second");
        openSecond.addClickHandler(event -> second.show());
        firstFooter.add(openSecond);
        first.add(firstFooter);
        second.add(new HTML("<p>Exclusive modal content</p>"));

        Button target = tagged(new Button("Open first modal"),
                "behaviour/modal/exclusive/open-first");
        target.setDataToggle(Toggle.MODAL);
        target.setDataTargetWidget(first);
        state.add(target);
        state.add(first);
        state.add(second);
        return fixture("Exclusive modals", state);
    }

    private Widget tooltipOptionsFixture() {
        Button target = tagged(new Button("Show delayed tooltip"),
                "behaviour/tooltip/options");
        target.getElement().setAttribute("data-event-order", "");
        Tooltip tooltip = new Tooltip(target, "Delayed bottom tooltip");
        tooltip.setTrigger(Trigger.CLICK);
        tooltip.setPlacement(Placement.BOTTOM);
        tooltip.setShowDelayMs(100);
        tooltip.setHideDelayMs(100);
        tooltip.setContainer("body");
        tooltip.addShowHandler(event -> appendEvent(target.getElement(), "show"));
        tooltip.addShownHandler(event -> appendEvent(target.getElement(), "shown"));
        return fixture("Tooltip options", tooltip.asWidget());
    }

    private Widget tooltipDisposalFixture() {
        FlowPanel state = tagged(new FlowPanel(), "behaviour/tooltip/disposal");
        FlowPanel host = tagged(new FlowPanel(), "behaviour/tooltip/disposal/host");
        Button target = tagged(new Button("Disposable tooltip"),
                "behaviour/tooltip/disposal/target");
        target.getElement().setAttribute("data-event-order", "");
        Tooltip tooltip = new Tooltip(target, "Disposable tooltip content");
        tooltip.setTrigger(Trigger.CLICK);
        tooltip.setPlacement(Placement.BOTTOM);
        tooltip.setContainer("body");
        tooltip.addShowHandler(event -> appendEvent(target.getElement(), "show"));
        tooltip.addShownHandler(event -> appendEvent(target.getElement(), "shown"));
        host.add(tooltip.asWidget());

        Button destroy = tagged(new Button("Destroy and detach"),
                "behaviour/tooltip/disposal/destroy");
        destroy.addClickHandler(event -> {
            tooltip.destroy();
            host.remove(target);
            state.getElement().setAttribute("data-destroyed", "true");
        });
        Button remount = tagged(new Button("Remount"),
                "behaviour/tooltip/disposal/remount");
        remount.addClickHandler(event -> host.add(target));
        state.add(destroy);
        state.add(remount);
        state.add(host);
        return fixture("Tooltip disposal", state);
    }

    private Widget popoverHtmlFixture() {
        Button target = tagged(new Button("Show HTML popover"), "behaviour/popover/html");
        target.getElement().setAttribute("data-event-order", "");
        Popover popover = new Popover(target, "Trusted title",
                "<strong data-popover-content=\"trusted\">Trusted content</strong>");
        popover.setTrigger(Trigger.CLICK);
        popover.setPlacement(Placement.RIGHT);
        popover.setIsHtml(true);
        popover.setContainer("body");
        popover.addShowHandler(event -> appendEvent(target.getElement(), "show"));
        popover.addShownHandler(event -> appendEvent(target.getElement(), "shown"));
        return fixture("HTML popover", popover.asWidget());
    }

    private Widget popoverProgrammaticFixture() {
        FlowPanel state = tagged(new FlowPanel(), "behaviour/popover/programmatic");
        Button target = tagged(new Button("Programmatic popover target"),
                "behaviour/popover/programmatic/target");
        Popover popover = new Popover(target, "Programmatic", "Popover content");
        popover.setTrigger(Trigger.MANUAL);
        popover.setContainer("body");
        Button show = tagged(new Button("Show"), "behaviour/popover/programmatic/show");
        show.addClickHandler(event -> popover.show());
        Button toggle = tagged(new Button("Toggle"), "behaviour/popover/programmatic/toggle");
        toggle.addClickHandler(event -> popover.toggle());
        Button showHide = tagged(new Button("Show then hide"),
                "behaviour/popover/programmatic/show-hide");
        showHide.addClickHandler(event -> {
            popover.show();
            popover.hide();
        });
        state.add(popover.asWidget());
        state.add(show);
        state.add(toggle);
        state.add(showHide);
        return fixture("Programmatic popover", state);
    }

    private Widget pluginLifecycleFixture() {
        FlowPanel state = tagged(new FlowPanel(), "behaviour/lifecycle/plugin");
        FlowPanel host = tagged(new FlowPanel(), "behaviour/lifecycle/plugin/host");
        Button target = tagged(new Button("Lifecycle tooltip"),
                "behaviour/lifecycle/plugin/target");
        Tooltip tooltip = new Tooltip(target, "Lifecycle tooltip content");
        tooltip.setTrigger(Trigger.MANUAL);
        tooltip.setContainer("body");
        host.add(tooltip.asWidget());
        Button show = tagged(new Button("Show tooltip"), "behaviour/lifecycle/plugin/show");
        show.addClickHandler(event -> tooltip.show());
        Button detach = tagged(new Button("Detach tooltip"),
                "behaviour/lifecycle/plugin/detach");
        detach.addClickHandler(event -> host.remove(target));
        Button remount = tagged(new Button("Remount tooltip"),
                "behaviour/lifecycle/plugin/remount");
        remount.addClickHandler(event -> {
            host.add(target);
            tooltip.show();
        });
        state.add(show);
        state.add(detach);
        state.add(remount);
        state.add(host);
        return fixture("Plugin lifecycle", state);
    }

    private Widget basicTabsFixture() {
        return fixture("Basic tabs", createTabSet("basic-tabs", "behaviour/tabs/basic",
                false, false, TabPosition.TOP, false));
    }

    private Widget disabledTabsFixture() {
        return fixture("Disabled tabs", createTabSet("disabled-tabs",
                "behaviour/tabs/disabled", true, false, TabPosition.TOP, false));
    }

    private Widget programmaticTabsFixture() {
        return fixture("Programmatic tabs", createTabSet("programmatic-tabs",
                "behaviour/tabs/programmatic", false, false, TabPosition.TOP, true));
    }

    private Widget fadingTabsFixture() {
        return fixture("Fading tabs", createTabSet("fading-tabs", "behaviour/tabs/fade",
                false, true, TabPosition.TOP, false));
    }

    private Widget positionedTabsFixture() {
        FlowPanel positions = tagged(new FlowPanel(), "behaviour/tabs/positions");
        positions.add(createTabSet("left-tabs", "behaviour/tabs/positions/left",
                false, false, TabPosition.LEFT, false));
        positions.add(createTabSet("right-tabs", "behaviour/tabs/positions/right",
                false, false, TabPosition.RIGHT, false));
        positions.add(createTabSet("below-tabs", "behaviour/tabs/positions/below",
                false, false, TabPosition.BELOW, false));
        return fixture("Positioned tabs", positions);
    }

    private Widget createTabSet(String prefix, String fixtureId, boolean disableThird,
            boolean fade, TabPosition position, boolean programmatic) {
        FlowPanel container = tagged(new FlowPanel(), fixtureId);
        container.getElement().setAttribute("data-event-order", "");
        TabPanel panel = new TabPanel();
        panel.setTabPosition(position);
        NavTabs tabs = new NavTabs();
        tabs.getElement().setAttribute("role", "tablist");
        TabContent content = new TabContent();

        TabListItem first = createTab(prefix, fixtureId, "first", true, true);
        TabListItem second = createTab(prefix, fixtureId, "second", false, true);
        TabListItem third = createTab(prefix, fixtureId, "third", false, !disableThird);
        tabs.add(first);
        tabs.add(second);
        tabs.add(third);

        content.add(createTabPane(prefix, fixtureId, "first", true, fade));
        content.add(createTabPane(prefix, fixtureId, "second", false, fade));
        content.add(createTabPane(prefix, fixtureId, "third", false, fade));
        if (position == TabPosition.BELOW) {
            panel.add(content);
            panel.add(tabs);
        } else {
            panel.add(tabs);
            panel.add(content);
        }
        recordTabEvents(container, first, second, third);
        container.add(panel);

        if (programmatic) {
            Button showSecond = tagged(new Button("Show second tab"),
                    fixtureId + "/show-second");
            showSecond.addClickHandler(event -> second.showTab());
            container.add(showSecond);
        }
        return container;
    }

    private TabListItem createTab(String prefix, String fixtureId, String name,
            boolean active, boolean enabled) {
        TabListItem tab = new TabListItem(capitalize(name));
        tab.setDataTarget("#" + prefix + "-" + name + "-pane");
        tab.setActive(active);
        tab.setEnabled(enabled);
        Element control = tab.getElement().getFirstChildElement();
        control.setAttribute("data-testid", fixtureId + "/" + name + "-tab");
        control.setAttribute("aria-controls", prefix + "-" + name + "-pane");
        return tab;
    }

    private TabPane createTabPane(String prefix, String fixtureId, String name,
            boolean active, boolean fade) {
        TabPane pane = tagged(new TabPane(), fixtureId + "/" + name + "-pane");
        pane.getElement().setId(prefix + "-" + name + "-pane");
        pane.setFade(fade);
        pane.setActive(active);
        pane.setIn(fade && active);
        pane.add(new HTML("<p>" + capitalize(name) + " pane</p>"));
        return pane;
    }

    private void recordTabEvents(FlowPanel state, TabListItem... tabs) {
        for (TabListItem tab : tabs) {
            tab.addShowHandler(event -> appendEvent(state.getElement(), "show"));
            tab.addShownHandler(event -> appendEvent(state.getElement(), "shown"));
        }
    }

    private String capitalize(String value) {
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }

    private Widget loadingFixture() {
        Button button = tagged(new Button("Save"), "behaviour/button/loading");
        button.setDataLoadingText("Saving...");
        countClicks(button, button);
        button.addClickHandler(event -> {
            button.state().loading();
            new Timer() {
                @Override
                public void run() {
                    button.state().reset();
                }
            }.schedule(750);
        });
        return fixture("Loading button", button);
    }

    private Widget programmaticCheckboxFixture() {
        FlowPanel container = new FlowPanel();
        CheckBoxButton button = tagged(new CheckBoxButton("Programmatic"),
                "behaviour/check-box-button/basic");
        initializeCounter(button.getElement(), CHANGE_COUNT);
        initializeValue(button, false);
        button.addValueChangeHandler(event -> {
            increment(button.getElement(), CHANGE_COUNT);
            initializeValue(button, event.getValue());
        });

        Button silent = tagged(new Button("Set true silently"),
                "behaviour/check-box-button/set-silent");
        silent.addClickHandler(event -> {
            button.setValue(true, false);
            initializeValue(button, button.getValue());
        });
        Button firing = tagged(new Button("Set false with event"),
                "behaviour/check-box-button/set-firing");
        firing.addClickHandler(event -> {
            button.setValue(false, true);
            initializeValue(button, button.getValue());
        });

        container.add(button);
        container.add(silent);
        container.add(firing);
        return fixture("Programmatic checkbox button", container);
    }

    private Widget buttonTypesFixture() {
        FlowPanel buttons = tagged(new FlowPanel(), "behaviour/button/types");
        for (ButtonType type : ButtonType.values()) {
            Button button = new Button(type.name());
            button.setType(type);
            button.getElement().setAttribute("data-assigned-type", type.name());
            button.getElement().setAttribute("data-reported-type", button.getType().name());
            button.getElement().setAttribute("data-expected-class", type.getCssName());
            buttons.add(button);
        }
        return fixture("Button types", buttons);
    }

    private Widget buttonSizesFixture() {
        FlowPanel container = new FlowPanel();
        Button button = tagged(new Button("Sized"), "behaviour/button/sizes");
        button.setSize(ButtonSize.LARGE);
        recordButtonSize(button);
        Button change = tagged(new Button("Use small size"), "behaviour/button/sizes/change");
        change.addClickHandler(event -> {
            button.setSize(ButtonSize.SMALL);
            recordButtonSize(button);
        });
        container.add(button);
        container.add(change);
        return fixture("Button sizes", container);
    }

    private Widget basicButtonGroupFixture() {
        ButtonGroup group = tagged(new ButtonGroup(), "behaviour/button-group/basic");
        group.add(new Button("First"));
        group.add(new Button("Second"));
        group.add(new Button("Third"));
        return fixture("Basic button group", group);
    }

    private Widget buttonGroupSizesFixture() {
        FlowPanel container = new FlowPanel();
        ButtonGroup group = tagged(new ButtonGroup(), "behaviour/button-group/sizes");
        group.setDataToggle(Toggle.BUTTONS);
        group.setSize(ButtonGroupSize.LARGE);
        addPresetCheckbox(group, "First", true);
        addPresetCheckbox(group, "Second", false);
        addPresetCheckbox(group, "Third", true);
        recordButtonGroupSize(group);

        Button change = tagged(new Button("Use small group size"),
                "behaviour/button-group/sizes/change");
        change.addClickHandler(event -> {
            group.setSize(ButtonGroupSize.SMALL);
            recordButtonGroupSize(group);
        });
        container.add(group);
        container.add(change);
        return fixture("Button group sizes", container);
    }

    private Widget verticalButtonGroupFixture() {
        VerticalButtonGroup group = tagged(new VerticalButtonGroup(),
                "behaviour/button-group/vertical");
        group.add(new Button("First"));
        group.add(new Button("Second"));
        group.add(new Button("Third"));
        return fixture("Vertical button group", group);
    }

    private Widget nestedButtonGroupFixture() {
        ButtonGroup group = tagged(new ButtonGroup(), "behaviour/button-group/nested-dropdown");
        Button sibling = tagged(new Button("Sibling"),
                "behaviour/button-group/nested-dropdown/sibling");
        countClicks(sibling, sibling);
        Button toggle = tagged(new Button("More"),
                "behaviour/button-group/nested-dropdown/toggle");
        toggle.addStyleName("dropdown-toggle");
        toggle.setDataToggle(Toggle.DROPDOWN);
        DropDownMenu menu = tagged(new DropDownMenu(),
                "behaviour/button-group/nested-dropdown/menu");
        menu.add(new AnchorListItem("Nested action"));
        group.add(sibling);
        group.add(toggle);
        group.add(menu);
        return fixture("Nested button group dropdown", group);
    }

    private Widget buttonGroupRemovalFixture() {
        FlowPanel container = new FlowPanel();
        ButtonGroup group = tagged(new ButtonGroup(), "behaviour/button-group/removal");
        Button first = new Button("First");
        Button middle = tagged(new Button("Second"), "behaviour/button-group/removal/middle");
        Button third = new Button("Third");
        group.add(first);
        group.add(middle);
        group.add(third);
        group.getElement().setAttribute("data-removed-parent-null", "false");

        Button remove = tagged(new Button("Remove middle"), "behaviour/button-group/removal/action");
        remove.addClickHandler(event -> {
            group.remove(middle);
            group.getElement().setAttribute("data-removed-parent-null",
                    Boolean.toString(middle.getParent() == null));
        });
        container.add(group);
        container.add(remove);
        return fixture("Button group removal", container);
    }

    private Widget bootstrapSelectFixture() {
        FlowPanel state = tagged(new FlowPanel(), "behaviour/bootstrap-select/basic");
        Select select = tagged(new Select(), "behaviour/bootstrap-select/basic/control");
        addOption(select, "Mustard", "mustard");
        addOption(select, "Ketchup", "ketchup");
        addOption(select, "Relish", "relish");
        initializeCounter(state.getElement(), CHANGE_COUNT);
        state.getElement().setAttribute("data-value", "mustard");
        select.addValueChangeHandler(event -> {
            increment(state.getElement(), CHANGE_COUNT);
            state.getElement().setAttribute("data-value", event.getValue());
            state.getElement().setAttribute("data-source-match",
                    Boolean.toString(event.getSource() == select));
        });
        state.add(select);
        return fixture("Bootstrap Select", state);
    }

    private void addOption(Select select, String text, String value) {
        Option option = new Option();
        option.setText(text);
        option.setValue(value);
        select.add(option);
    }

    private void addPresetCheckbox(ButtonGroup group, String label, boolean value) {
        CheckBoxButton button = new CheckBoxButton(label);
        button.setValue(value);
        initializeValue(button, value);
        group.add(button);
    }

    private void recordButtonSize(Button button) {
        button.getElement().setAttribute("data-reported-size", button.getSize().name());
    }

    private void recordButtonGroupSize(ButtonGroup group) {
        group.getElement().setAttribute("data-reported-size", group.getSize().name());
    }

    private void addCheckbox(ButtonGroup group, String label, String fixtureId) {
        CheckBoxButton button = tagged(new CheckBoxButton(label), fixtureId);
        initializeCounter(button.getElement(), CHANGE_COUNT);
        initializeValue(button, false);
        countClicks(button, button);
        button.addValueChangeHandler(event -> {
            increment(button.getElement(), CHANGE_COUNT);
            initializeValue(button, event.getValue());
        });
        group.add(button);
    }

    private FlowPanel fixture(String title, Widget content) {
        FlowPanel fixture = new FlowPanel();
        fixture.setStyleName("fixture");
        fixture.add(new HTML("<h2>" + title + "</h2>"));
        fixture.add(content);
        return fixture;
    }

    private <T extends Widget> T tagged(T widget, String fixtureId) {
        widget.getElement().setAttribute("data-testid", fixtureId);
        return widget;
    }

    private void countClicks(com.google.gwt.event.dom.client.HasClickHandlers source, Widget state) {
        initializeCounter(state.getElement(), CLICK_COUNT);
        source.addClickHandler(event -> increment(state.getElement(), CLICK_COUNT));
    }

    private void initializeValue(com.google.gwt.user.client.ui.HasValue<Boolean> widget, boolean value) {
        ((Widget) widget).getElement().setAttribute("data-value", Boolean.toString(value));
    }

    private void initializeCounter(Element element, String attribute) {
        element.setAttribute(attribute, "0");
    }

    private void increment(Element element, String attribute) {
        String current = element.getAttribute(attribute);
        int value = current == null || current.isEmpty() ? 0 : Integer.parseInt(current);
        element.setAttribute(attribute, Integer.toString(value + 1));
    }

    private void appendEvent(Element element, String event) {
        String current = element.getAttribute("data-event-order");
        element.setAttribute("data-event-order", current.isEmpty() ? event : current + "," + event);
    }

    private static native void markReady() /*-{
        $wnd.__bootstrapWidgetFixturesReady = true;
    }-*/;
}
