package com.jfixby.tools.gdx.texturepacker.api.indexed;

import com.jfixby.cmns.api.color.Color;

public interface ColorIndexMap {

    int getWidth();

    int getHeight();

    int getColorIndexAt(float x, float y);

    Color colorOf(int color_index);

}
