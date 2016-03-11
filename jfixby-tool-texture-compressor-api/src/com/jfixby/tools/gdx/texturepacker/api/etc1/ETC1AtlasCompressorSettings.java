package com.jfixby.tools.gdx.texturepacker.api.etc1;

import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.file.File;

public interface ETC1AtlasCompressorSettings {

    void setAtlasFile(File etc1AtlasFilePath);

    File getAtlasFile();

    void setDeleteOriginalPNG(boolean b);

    boolean deleteOriginalPNG();

    void setTransparentColor(Color transparentColor);

    Color getTransparentColor();

    void setRemoveAlpha(boolean removeAlpha);

    boolean removeAlpha();

    void setZipCompressExtractedAlphaChannels(boolean zipCompressExtractedAlphaChannels);

    boolean zipCompressExtractedAlphaChannels();

    void setExtractAlphaChannes(boolean extractAlphaChannes);

    boolean extractAlphaChannes();

}
