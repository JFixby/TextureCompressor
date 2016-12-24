package com.jfixby.tools.gdx.texturepacker.api.indexed;

import com.jfixby.scarabei.api.color.GraySet;
import com.jfixby.scarabei.api.image.ColorMap;
import com.jfixby.scarabei.api.io.OutputStream;

public interface IndexColorCompressionParams {

    void setInputImage(ColorMap image);

    ColorMap getInputImage();

    void setOutputStream(OutputStream os);

    OutputStream getOutputStream();

    void setRedPalette(GraySet red_palette);

    GraySet getRedPalette();

    void setBluePalette(GraySet blue_palette);

    GraySet getBluePalette();

    void setGreenPalette(GraySet green_palette);

    GraySet getGreenPalette();

    // void setColorPalette(ColorSet colorPalette);

    // ColorSet getColorPalette();

    void setCompressionStrategy(CompressionStrategy channelByChannel);

    CompressionStrategy getCompressionStrategy();

    // public void setUseGZip(boolean useGzip);
    //
    // public boolean useGZip();

}
