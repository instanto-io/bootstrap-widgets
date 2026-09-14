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
import io.instanto.bootstrap5.client.ui.base.helper.StyleHelper;
import io.instanto.bootstrap5.client.ui.constants.ImageType;
import io.instanto.bootstrap5.client.ui.constants.Styles;

public class ImageAnchor extends Anchor implements HasType<ImageType> {

    private final Image image = new Image();

    public ImageAnchor() {
        add(image);
    }

    public ImageAnchor(String href, String imageUrl) {
        this();
        setHref(href == null ? "#" : href);
        setUrl(imageUrl);
    }

    @Override
    public void setType(final ImageType type) {
        StyleHelper.addUniqueEnumStyleName(this, ImageType.class, type == null ? ImageType.DEFAULT : type);
    }

    @Override
    public ImageType getType() {
        return ImageType.fromStyleName(getStyleName());
    }

    public void setResponsive(final boolean responsive) {
        StyleHelper.toggleStyleName(image, responsive, Styles.IMG_RESPONSIVE);
    }

    public void setAsMediaObject(final boolean asMediaObject) {
        StyleHelper.toggleStyleName(image, asMediaObject, Styles.MEDIA_OBJECT);
    }

    public void setUrl(final String url) {
        image.setUrl(url);
    }

    public String getUrl() {
        return image.getUrl();
    }

    public void setAlt(final String alt) {
        image.setAltText(alt);
    }

    public String getAlt() {
        return image.getAltText();
    }

    public Image getImage() {
        return image;
    }
}
