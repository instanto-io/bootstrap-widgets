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
package io.instanto.bootstrap5.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.Properties;
import org.junit.Test;

/**
 * The resource plugin stages the assets where {@code META-INF/teavm-assets.properties} says, and
 * initialisation loads them from the default asset base. The two must name the same place, or an
 * application that only stages the assets and initialises gets an unstyled page.
 */
public class Bootstrap5ResourcesTest {

  @Test
  public void defaultAssetBaseIsWhereTheAssetsAreStaged() throws Exception {
    Properties declared = new Properties();
    try (InputStream in =
        Bootstrap5ResourcesTest.class
            .getClassLoader()
            .getResourceAsStream("META-INF/teavm-assets.properties")) {
      assertNotNull("META-INF/teavm-assets.properties is missing", in);
      declared.load(in);
    }
    assertEquals(declared.getProperty("target") + "/", Bootstrap5Resources.DEFAULT_ASSET_BASE);
  }
}
