package com.jfixby.tools.gdx.texturepacker.api.etc1;

import java.io.IOException;

public interface AlphaChannelExtractor {

    AlphaChannelExtractionResult process(AlphaChannelExtractionSettings settings);

    AlphaChannelExtractionSettings newExtractionSettings();

    byte[] serialize() throws IOException;

}
