package com.jfixby.tools.gdx.texturepacker.api.etc1;

import java.io.IOException;

import com.jfixby.cmns.api.file.File;

public interface AlphaChannelExtractor {

    AlphaChannelExtractionResult process(AlphaChannelExtractionSettings settings);

    AlphaChannelExtractionSettings newExtractionSettings();

    byte[] serialize() throws IOException;

    void saveAsPng(File folder);

}
