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
package io.instanto.bootstrap5.client.ui.form.validator;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Validation messages backed by a {@link ResourceBundle}.
 *
 * <p>Messages are resolved at runtime, so one build serves every language and the locale
 * can change without reloading.</p>
 *
 * <p>To add a language, ship {@code ValidationMessages_&lt;lang&gt;.properties} beside the
 * default and add its base name to {@code META-INF/services/java.util.ResourceBundle};
 * TeaVM inlines every bundle listed there at build time.</p>
 */
public class TeaVmValidationMessages implements ValidationMessages {

    private static final String BUNDLE =
            "io.instanto.bootstrap5.client.ui.form.validator.ValidationMessagesBundle";

    private Locale locale = Locale.getDefault();

    /** The locale used for subsequent lookups. */
    public void setLocale(final Locale locale) {
        this.locale = locale == null ? Locale.getDefault() : locale;
    }

    public Locale getLocale() {
        return locale;
    }

    @Override
    public String getString(final String methodName) {
        try {
            return ResourceBundle.getBundle(BUNDLE, locale).getString(methodName);
        } catch (final MissingResourceException absent) {
            throw absent;
        }
    }

    @Override
    public String org_gwtbootstrap5_validation_Blank_message() {
        return lookup("org_gwtbootstrap5_validation_Blank_message", "Field cannot be blank");
    }

    @Override
    public String org_gwtbootstrap5_validation_DecimalMax_message() {
        return lookup("org_gwtbootstrap5_validation_DecimalMax_message",
                "Value must be less than or equal to {1}");
    }

    @Override
    public String org_gwtbootstrap5_validation_DecimalMin_message() {
        return lookup("org_gwtbootstrap5_validation_DecimalMin_message",
                "Value must be greater than or equal to {1}");
    }

    @Override
    public String org_gwtbootstrap5_validation_FieldMatch_message() {
        return lookup("org_gwtbootstrap5_validation_FieldMatch_message", "{1} do not match");
    }

    @Override
    public String org_gwtbootstrap5_validation_Future_message() {
        return lookup("org_gwtbootstrap5_validation_Future_message", "Value must be in the future");
    }

    @Override
    public String org_gwtbootstrap5_validation_Past_message() {
        return lookup("org_gwtbootstrap5_validation_Past_message", "Value must be in the past");
    }

    @Override
    public String org_gwtbootstrap5_validation_RegEx_message() {
        return lookup("org_gwtbootstrap5_validation_RegEx_message", "Must match regex");
    }

    @Override
    public String org_gwtbootstrap5_validation_Size_message() {
        return lookup("org_gwtbootstrap5_validation_Size_message", "Size must be between {1} and {2}");
    }

    /** Falls back to the English default so a missing bundle degrades rather than throws. */
    private String lookup(final String key, final String fallback) {
        try {
            return ResourceBundle.getBundle(BUNDLE, locale).getString(key);
        } catch (final MissingResourceException absent) {
            return fallback;
        }
    }
}
