/*
 * Copyright (C) 2026 Carl Stainton
 * Licensed under the Apache License, Version 2.0.
 * https://www.apache.org/licenses/LICENSE-2.0
 */
package io.instanto.bootstrap5.client.ui;

import io.instanto.bootstrap5.client.ui.html.Div;

/** The animation belongs on the skeleton's parent, as required by Bootstrap's CSS. */
public class PlaceholderContainer extends Div {
    public enum Animation { NONE, GLOW, WAVE }
    private Animation animation = Animation.NONE;

    public PlaceholderContainer() { getElement().setAttribute("aria-hidden", "true"); }
    public void setAnimation(Animation animation) {
        if (animation == null) animation = Animation.NONE;
        removeStyleName("placeholder-glow");
        removeStyleName("placeholder-wave");
        this.animation = animation;
        if (animation == Animation.GLOW) addStyleName("placeholder-glow");
        if (animation == Animation.WAVE) addStyleName("placeholder-wave");
    }
    public Animation getAnimation() { return animation; }
}
