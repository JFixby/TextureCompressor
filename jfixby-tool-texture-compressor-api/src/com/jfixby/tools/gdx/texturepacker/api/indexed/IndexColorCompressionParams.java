package com.jfixby.tools.gdx.texturepacker.api.indexed;

import com.jfixby.cmns.api.color.ColorSet;
import com.jfixby.cmns.api.color.GraySet;
import com.jfixby.cmns.api.image.ColorMap;
import com.jfixby.cmns.api.io.OutputStream;

public interface IndexColorCompressionParams {

    void setInputImage(ColorMap image);

    ColorMap getInputImage();

    void setOutputStream(OutputStream os);

    OutputStream getOutputStream();

    void setRGBOrder(RGBOrder rgb);

    RGBOrder getRGBOrder();

    void setRedPalette(GraySet red_palette);

    GraySet getRedPalette();

    void setBluePalette(GraySet blue_palette);

    GraySet getBluePalette();

    void setGreenPalette(GraySet green_palette);

    GraySet getGreenPalette();

//    void setColorPalette(ColorSet colorPalette);

//    ColorSet getColorPalette();

    void setCompressionStrategy(CompressionStrategy channelByChannel);

    CompressionStrategy getCompressionStrategy();

}
