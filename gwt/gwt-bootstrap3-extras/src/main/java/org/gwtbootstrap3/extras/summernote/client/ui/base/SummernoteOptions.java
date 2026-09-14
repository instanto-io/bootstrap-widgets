package org.gwtbootstrap3.extras.summernote.client.ui.base;

/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2016 GwtBootstrap3
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayMixed;
import com.google.gwt.core.client.JsArrayString;

/**
 * This class represents Summernote options, that you can use to
 * customize the editor.
 *
 * @author Xiaodong SUN
 */
class SummernoteOptions {

    /**
     * Default constructor
     */
    final JavaScriptObject value = SummernoteJs.options();
    protected SummernoteOptions() {}

    /**
     * Creates a new instance of {@link SummernoteOptions}.
     *
     * @return a new instance of {@link SummernoteOptions}.
     */
    static SummernoteOptions newOptions() {
        return new SummernoteOptions();
    }

    /**
     * @see {@link SummernoteBase#setPlaceholder(String)}
     */
    final void setPlaceholder(String placeholder) { SummernoteJs.property(value, "placeholder", placeholder); }

    /**
     * @see {@link SummernoteBase#setFontNames(boolean)}
     */
    final void setFontNames(JsArrayString fontNames) { SummernoteJs.property(value, "fontNames", fontNames); }

    /**
     * @see {@link SummernoteBase#setFontNamesIgnoreCheck(boolean)}
     */
    final void setFontNamesIgnoreCheck(JsArrayString fontNamesIgnoreCheck) { SummernoteJs.property(value, "fontNamesIgnoreCheck", fontNamesIgnoreCheck); }

    /**
     * @see {@link SummernoteBase#setDialogsInBody(boolean)}
     */
    final void setDialogsInBody(boolean dialogsInBody) { SummernoteJs.property(value, "dialogsInBody", dialogsInBody); }

    /**
     * @see {@link SummernoteBase#setDialogsFade(boolean)}
     */
    final void setDialogsFade(boolean dialogsFade) { SummernoteJs.property(value, "dialogsFade", dialogsFade); }

    /**
     * @see {@link SummernoteBase#setDisableDragAndDrop(boolean)}
     */
    final void setDisableDragAndDrop(boolean disableDragAndDrop) { SummernoteJs.property(value, "disableDragAndDrop", disableDragAndDrop); }

    /**
     * @see {@link SummernoteBase#setShortcuts(boolean)}
     */
    final void setShortcuts(boolean shortcuts) { SummernoteJs.property(value, "shortcuts", shortcuts); }

    /**
     * @see {@link SummernoteBase#setShowToolbar(boolean)}
     */
    final void setShowToolbar(boolean showToolbar) { SummernoteJs.toolbarVisible(value, showToolbar); }

    /**
     * Creates a new toolbar group.
     *
     * @param name
     * @param buttons
     * @return
     */
    static final JsArrayMixed newToolbarGroup(String name, ToolbarButton... buttons) {
        JsArrayString arr = SummernoteJs.strings();
        for (ToolbarButton button : buttons) {
            arr.push(button.getId());
        }
        return getToolbarGroup(name, arr);
    }

    private static JsArrayMixed getToolbarGroup(String name, JsArrayString buttons) { return SummernoteJs.group(name, buttons); }

    /**
     * Builds the toolbar.
     *
     * @param toolbarGroups
     * @return
     */
    static JsArrayMixed buildToolbar(JsArrayMixed... toolbarGroups) {
        JsArrayMixed result = SummernoteJs.mixed();
        for (JsArrayMixed group : toolbarGroups) result.push(group);
        return result;
    }

    /**
     * @see {@link SummernoteBase#setToolbar(Toolbar)}
     */
    final void setToolbar(Toolbar toolbar) { SummernoteJs.property(value, "toolbar", toolbar.build()); }

    /**
     * @see {@link SummernoteBase#setDefaultHeight(int)}
     */
    final void setHeight(int height) { SummernoteJs.property(value, "height", height); }

    /**
     * @see {@link SummernoteBase#setMaxHeight(int)}
     */
    final void setMaxHeight(int maxHeight) { SummernoteJs.property(value, "maxHeight", maxHeight); }

    /**
     * @see {@link SummernoteBase#setMinHeight(int)}
     */
    final void setMinHeight(int minHeight) { SummernoteJs.property(value, "minHeight", minHeight); }

    /**
     * @see {@link SummernoteBase#setHasFocus(boolean)}
     */
    final void setFocus(boolean focus) { SummernoteJs.property(value, "focus", focus); }

    /**
     * @see {@link SummernoteBase#setLanguage(SummernoteLanguage)}
     */
    final void setLanguage(SummernoteLanguage language) { SummernoteJs.property(value, "lang", language.getCode()); }

    /**
     * @see {@link SummernoteBase#setAirMode(boolean)}
     */
    final void setAirMode(boolean airMode) { SummernoteJs.property(value, "airMode", airMode); }

    /**
     * @see {@link SummernoteBase#setHint(String, HintHandler)}
     */
    final void setHint(String match, HintHandler handler) { SummernoteJs.hint(value, match, handler); }

}
