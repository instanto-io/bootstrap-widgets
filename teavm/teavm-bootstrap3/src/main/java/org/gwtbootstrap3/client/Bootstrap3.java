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
package org.gwtbootstrap3.client;

/**
 * Starts the Bootstrap 3 widgets on TeaVM.
 *
 * <p>One call, before the first widget is used. A GWT application never writes it,
 * because a GWT module descriptor declares the stylesheets a module needs and the
 * bootstrap injects them before any application code runs. TeaVM has no module system
 * to read that descriptor, so this stands in for it.</p>
 *
 * <pre>{@code
 * public static void main(String[] args) {
 *     Bootstrap3.initialise(() -> RootPanel.get().add(new FancyWidget()));
 * }
 * }</pre>
 *
 * <p>Widgets are then added through the ordinary GWT API this library emulates.
 * {@code RootPanel.get()} wraps the document body and {@code RootPanel.get(id)} wraps
 * any element already on the page, so a host element can come from wherever the
 * application likes.</p>
 *
 * <p>Attach a widget's element directly -- with appendChild, say -- and it will render
 * but never be told it was attached, so its onLoad never runs and anything that starts
 * there, a tooltip binding to its element or a dropdown registering its handlers,
 * silently never starts. RootPanel is what performs that attachment.</p>
 */
public final class Bootstrap3 {

    private static boolean initialised;

    private Bootstrap3() {
    }

    /** Starts loading the library. Use the callback overload before constructing widgets. */
    public static void initialise() {
        if (initialised) {
            return;
        }
        initialised = true;
        new TeaVmBootstrap3EntryPoint().onModuleLoad();
    }

    /** Starts the application once the vendored jQuery and Bootstrap scripts are usable. */
    public static void initialise(final Runnable ready) {
        initialise();
        NoThemeResourcesResources.whenReady(TeaVmBootstrap3EntryPoint::isBootstrapLoaded, ready);
    }
}
