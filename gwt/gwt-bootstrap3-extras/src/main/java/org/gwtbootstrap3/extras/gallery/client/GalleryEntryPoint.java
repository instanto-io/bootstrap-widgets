package org.gwtbootstrap3.extras.gallery.client;

/*
 * #%L
 * GwtBootstrap3
 * %%
 * Copyright (C) 2013 - 2016 GwtBootstrap3
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

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;

/**
 * @author Ben Dol
 */
public class GalleryEntryPoint implements EntryPoint {

    /**
     * Loads the gallery scripts, the second only once the first has arrived.
     *
     * <p>These were injected one after the other, which does not mean one after the
     * other: fromUrl is asynchronous, so both requests went out together and whichever
     * answered first ran first. bootstrap-image-gallery extends blueimp.Gallery and
     * reads it as it loads, so when it won the race it threw, and the jQuery gallery
     * plugin was never registered -- on every page, since a module's entry points all
     * run at startup whether or not anything on screen uses them.</p>
     */
    @Override
    public void onModuleLoad() {
        inject(GalleryClientBundle.BLUEIMP_JS, new Callback<Void, Exception>() {
            @Override
            public void onSuccess(final Void result) {
                inject(GalleryClientBundle.GALLERY_JS, null);
            }

            @Override
            public void onFailure(final Exception reason) {
                GWT.log("Gallery: " + GalleryClientBundle.BLUEIMP_JS + " failed to load; "
                        + GalleryClientBundle.GALLERY_JS + " needs it and was not loaded", reason);
            }
        });
    }

    private void inject(final String resource, final Callback<Void, Exception> callback) {
        ScriptInjector.fromUrl(GWT.getModuleBaseURL() + resource)
            .setRemoveTag(true)
            .setWindow(ScriptInjector.TOP_WINDOW)
            .setCallback(callback)
            .inject();
    }
}
