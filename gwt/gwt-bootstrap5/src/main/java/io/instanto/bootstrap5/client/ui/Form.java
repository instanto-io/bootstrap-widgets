/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 - 2018 GwtBootstrap3
 * %%
 * Modified from the GwtBootstrap3 original for the Bootstrap 5 track of
 * GWT Bootstrap: moved to the io.instanto.bootstrap5 namespace and
 * re-targeted at Bootstrap 5 markup, class names and JavaScript APIs. The
 * GwtBootstrap3 copyright above is retained as required by the Apache
 * License 2.0; the namespace changed, the attribution did not.
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
package io.instanto.bootstrap5.client.ui;

import io.instanto.bootstrap5.client.ui.base.HasType;
import io.instanto.bootstrap5.client.ui.base.form.AbstractForm;
import io.instanto.bootstrap5.client.ui.base.helper.StyleHelper;
import io.instanto.bootstrap5.client.ui.constants.FormType;

/**
 * A Bootstrap 5 form.
 *
 * <p>Extends {@link AbstractForm}, so it carries GWT's form submission
 * behaviour -- action, method, encoding, {@code submit()} and the submit
 * handlers -- along with the validation the widget library layers on top of it.
 * Bootstrap 5 removed the {@code .form-inline} and {@code .form-horizontal}
 * classes; {@link FormType} now names the library classes that reproduce those
 * layouts with grid and flex utilities.</p>
 */
public class Form extends AbstractForm implements HasType<FormType> {

    public Form() {
        this(FormType.DEFAULT);
    }

    public Form(final FormType type) {
        setType(type);
    }

    @Override
    public void setType(final FormType type) {
        StyleHelper.addUniqueEnumStyleName(this, FormType.class, type);
    }

    @Override
    public FormType getType() {
        return FormType.fromStyleName(getStyleName());
    }

    /** Equivalent to {@code setType(FormType.INLINE)} / {@code FormType.DEFAULT}. */
    public void setInline(final boolean inline) {
        setType(inline ? FormType.INLINE : FormType.DEFAULT);
    }

    public boolean isInline() {
        return FormType.INLINE == getType();
    }
}
