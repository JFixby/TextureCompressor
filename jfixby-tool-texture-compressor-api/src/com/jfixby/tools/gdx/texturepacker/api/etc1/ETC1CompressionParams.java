package com.jfixby.tools.gdx.texturepacker.api.etc1;

import com.jfixby.scarabei.api.color.Color;
import com.jfixby.scarabei.api.image.ColorMap;
import com.jfixby.scarabei.api.io.OutputStream;

public interface ETC1CompressionParams {

    ColorMap getInputImage();

    OutputStream getOutputStream();

    Color getTransparentColor();

    boolean discardAlpha();

    void setInputImage(ColorMap image);

    void setOutputStream(OutputStream os);

    void setTransparentColor(Color transparentColor);

    void setDiscardAlpha(boolean discardAlpha);

}
