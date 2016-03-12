package com.jfixby.tools.gdx.texturepacker.api.etc1;

public interface AlphaPage {

    void checkValid(String name);

    void checkValid(int w, int h);

    float getAlphaValue(int x, int y);

}
