package com.jfixby.tools.gdx.texturepacker.etc1;

import com.jfixby.cmns.api.io.InputStream;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1DeCompressionParams;

public class GdxETC1DeCompressionParams implements ETC1DeCompressionParams {

    private InputStream inputStream;

    GdxETC1DeCompressionParams() {
    }

    public InputStream getInputStream() {
	return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
	this.inputStream = inputStream;
    }

}
