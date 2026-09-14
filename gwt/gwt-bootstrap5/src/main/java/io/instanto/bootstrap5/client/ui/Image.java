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
import io.instanto.bootstrap5.client.ui.base.HasResponsiveness;
import io.instanto.bootstrap5.client.ui.base.HasPull;
import io.instanto.bootstrap5.client.ui.constants.DeviceSize;
import io.instanto.bootstrap5.client.ui.constants.Pull;
import io.instanto.bootstrap5.client.ui.constants.Styles;

public class Image extends com.google.gwt.user.client.ui.Image implements HasType<ImageType>, HasResponsiveness, HasPull {

    public Image() {
        addStyleName("img-fluid");
    }

    public Image(String url) {
        super(url);
        addStyleName("img-fluid");
    }

    public void setRounded(boolean rounded) {
        setStyleName("rounded", rounded);
    }

    public void setCircle(boolean circle) {
        setStyleName("rounded-circle", circle);
    }

    public void setThumbnail(boolean thumbnail) {
        setStyleName("img-thumbnail", thumbnail);
    }

    @Override
    public void setType(ImageType type) {
        StyleHelper.addUniqueEnumStyleName(this, ImageType.class, type == null ? ImageType.DEFAULT : type);
    }

    @Override
    public ImageType getType() {
        return ImageType.fromStyleName(getStyleName());
    }

    /** Bootstrap 3 used .img-responsive; Bootstrap 5 renames it .img-fluid. */
    public void setResponsive(final boolean responsive) {
        setStyleName(Styles.IMG_RESPONSIVE, responsive);
    }

    public boolean isResponsive() {
        return StyleHelper.containsStyle(getStyleName(), Styles.IMG_RESPONSIVE);
    }

    @Override
    public void setPull(final Pull pull) {
        StyleHelper.addUniqueEnumStyleName(this, Pull.class, pull);
    }

    @Override
    public Pull getPull() {
        return Pull.fromStyleName(getStyleName());
    }

    @Override
    public void setVisibleOn(final DeviceSize deviceSize) {
        StyleHelper.setVisibleOn(this, deviceSize);
    }

    @Override
    public void setHiddenOn(final DeviceSize deviceSize) {
        StyleHelper.setHiddenOn(this, deviceSize);
    }

}
