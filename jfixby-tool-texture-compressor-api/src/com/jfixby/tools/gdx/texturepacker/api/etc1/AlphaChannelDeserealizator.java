package com.jfixby.tools.gdx.texturepacker.api.etc1;

import java.io.IOException;

public interface AlphaChannelDeserealizator {

    AlphaPages deserialize(byte[] alphas_bytes, boolean zip) throws IOException;

}
