package com.jfixby.tools.gdx.texturepacker.api.etc1;

import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.image.ColorMap;
import com.jfixby.cmns.api.io.OutputStream;

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
