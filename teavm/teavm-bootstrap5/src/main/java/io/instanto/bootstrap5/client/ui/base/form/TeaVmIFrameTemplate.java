/*
 * Copyright 2026 Carl Stainton
 * Licensed under the Apache License, Version 2.0.
 */
package io.instanto.bootstrap5.client.ui.base.form;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/** TeaVM deferred-binding implementation for the form submission iframe. */
public final class TeaVmIFrameTemplate implements AbstractForm.IFrameTemplate {

    @Override
    public SafeHtml get(String name) {
        return new SafeHtmlBuilder()
                .appendHtmlConstant("<iframe src=\"javascript:''\" name='")
                .appendEscaped(name)
                .appendHtmlConstant("' tabindex='-1' title='Form submit helper frame'"
                        + " style='position:absolute;width:0;height:0;border:0'></iframe>")
                .toSafeHtml();
    }
}
