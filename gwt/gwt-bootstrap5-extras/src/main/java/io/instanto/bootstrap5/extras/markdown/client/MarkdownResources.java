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
package io.instanto.bootstrap5.extras.markdown.client;

import com.google.gwt.core.client.GWT;

/**
 * How the Markdown scripts arrive on GWT: compiled into the application through {@link
 * MarkdownClientBundle} and injected by {@link MarkdownEntryPoint} before it runs.
 *
 * <p>TeaVM builds leave this class out. The resources plugin generates a {@code MarkdownResources}
 * with the same methods there, which fetches the scripts by URL, so {@link Markdown} calls one API
 * and each platform supplies its own loading.
 */
public final class MarkdownResources {

  /** Whether the library is usable on the page. */
  public interface Presence {
    boolean isPresent();
  }

  private MarkdownResources() {}

  /** Nothing to fetch: the entry point has already injected the scripts. */
  public static void ensureInjected() {}

  /**
   * Runs an action now if the library is on the page.
   *
   * <p>If it is not, waiting will not help -- nothing else is going to load it -- so this says so
   * rather than failing later inside the library.
   */
  public static void whenReady(final Presence presence, final Runnable action) {
    if (presence.isPresent()) {
      action.run();
    } else {
      GWT.log("Markdown: the parser is not on the page; the module did not load it");
    }
  }

  /** Whether the library is usable now. */
  public static boolean isReady(final Presence presence) {
    return presence.isPresent();
  }
}
