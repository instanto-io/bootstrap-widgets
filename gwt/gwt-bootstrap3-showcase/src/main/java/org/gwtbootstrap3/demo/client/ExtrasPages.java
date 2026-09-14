/*
 * #%L
 * GWT Bootstrap
 * %%
 * Copyright (C) 2026 Carl Stainton
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
package org.gwtbootstrap3.demo.client;

import com.google.gwt.user.client.ui.Widget;

import org.gwtbootstrap3.demo.client.place.NameTokens;

/**
 * The showcase pages that need gwt-bootstrap3-extras.
 *
 * <p>A seam, in the same sense as the rest of this repository: one file per backend,
 * the other excluded by the compiler plugin. The extras reach the browser through 352
 * JSNI methods, which only the GWT compiler understands, so this file is the GWT half
 * and teavm-bootstrap3 holds a counterpart for the ported extras pages.</p>
 *
 * <p>Splitting them out is what lets the entry point itself stay shared. It is the same
 * switch on both backends; only the set of pages it can reach differs.</p>
 */
final class ExtrasPages {

    private ExtrasPages() {
    }

    /** The page for a token, or null if it is not one of the extras pages. */
    static Widget forToken(final String token) {
        switch (token) {
            case NameTokens.ANIMATE:
                return new org.gwtbootstrap3.demo.client.application.extras.AnimateView();
            case NameTokens.BOOTBOX:
                return new org.gwtbootstrap3.demo.client.application.extras.BootboxJSView();
            case NameTokens.DATE_PICKER:
                return new org.gwtbootstrap3.demo.client.application.extras.DatePickerView();
            case NameTokens.DATETIME_PICKER:
                return new org.gwtbootstrap3.demo.client.application.extras.DateTimePickerView();
            case NameTokens.FORMS_WITH_VALIDATION:
                return new org.gwtbootstrap3.demo.client.application.css.validation.FormsWithValidationView();
            case NameTokens.FULL_CALENDAR:
                return new org.gwtbootstrap3.demo.client.application.extras.FullCalendarView();
            case NameTokens.GALLERY:
                return new org.gwtbootstrap3.demo.client.application.extras.GalleryView();
            case NameTokens.MARKDOWN:
                return new org.gwtbootstrap3.demo.client.application.extras.MarkdownView();
            case NameTokens.NOTIFY:
                return new org.gwtbootstrap3.demo.client.application.extras.NotifyView();
            case NameTokens.SELECT:
                return new org.gwtbootstrap3.demo.client.application.extras.BootstrapSelectView();
            case NameTokens.SLIDER:
                return new org.gwtbootstrap3.demo.client.application.extras.SliderView();
            case NameTokens.SUMMERNOTE:
                return new org.gwtbootstrap3.demo.client.application.extras.SummernoteView();
            case NameTokens.TAGSINPUT:
                return new org.gwtbootstrap3.demo.client.application.extras.TagsInputView();
            case NameTokens.TYPEAHEAD:
                return new org.gwtbootstrap3.demo.client.application.extras.TypeaheadView();
            default:
                return null;
        }
    }
}
