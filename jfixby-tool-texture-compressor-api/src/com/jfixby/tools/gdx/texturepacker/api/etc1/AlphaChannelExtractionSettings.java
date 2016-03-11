package com.jfixby.tools.gdx.texturepacker.api.etc1;

import com.jfixby.cmns.api.file.File;

public interface AlphaChannelExtractionSettings {

    void setInputFile(File input_png);

    void setNameTag(String name);

    File getInputFile();

    String getNameTag();

}
