package org.gwtbootstrap3.extras.fullcalendar.client;

/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 - 2014 GwtBootstrap3
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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.ScriptInjector;

public class FullCalendarEntryPoint implements EntryPoint {

    @Override
    public void onModuleLoad() {
        injectStyle("gwtbootstrap3-fullcalendar-style", FullCalendarClientBundle.INSTANCE.fullCalendarCss().getText());
        injectStyle("gwtbootstrap3-fullcalendar-print-style",
                "@media print {" + FullCalendarClientBundle.INSTANCE.fullCalendarPrintCss().getText() + "}");

        if (!isMomentPresent()) {
            ScriptInjector.fromString(FullCalendarClientBundle.INSTANCE.getMomentJS().getText())
                    .setWindow(ScriptInjector.TOP_WINDOW).inject();
        }
        if (!isDragAndResizePresent()) {
            ScriptInjector.fromString(FullCalendarClientBundle.INSTANCE.getCustomDragResizeJS().getText())
                    .setWindow(ScriptInjector.TOP_WINDOW).inject();
        }
        if (!isCalendarPresent()) {
            ScriptInjector.fromString(FullCalendarClientBundle.INSTANCE.getFullCalendarJS().getText())
                    .setWindow(ScriptInjector.TOP_WINDOW).inject();
        }
    }

    private static native void injectStyle(String id, String css) /*-{
        if ($doc.getElementById(id)) return;
        var style = $doc.createElement('style');
        style.id = id;
        style.textContent = css;
        $doc.head.appendChild(style);
    }-*/;

    private static native boolean isCalendarPresent() /*-{
        return !!($wnd.jQuery && typeof $wnd.jQuery.fn.fullCalendar === 'function');
    }-*/;

    private static native boolean isDragAndResizePresent() /*-{
        return !!($wnd.jQuery && typeof $wnd.jQuery.fn.draggable === 'function'
                && typeof $wnd.jQuery.fn.resizable === 'function');
    }-*/;

    private static native boolean isMomentPresent() /*-{
        return typeof $wnd.moment === 'function';
    }-*/;
}
