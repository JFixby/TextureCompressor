package com.jfixby.tools.gdx.texturepacker.etc1;

import com.jfixby.tools.gdx.texturepacker.api.etc1.AlphaChannelExtractorSpecs;

 class RedAlphaChannelExtractorSpecs implements AlphaChannelExtractorSpecs {

    private boolean useZIPCompression;

    public boolean useZIPCompression() {
	return useZIPCompression;
    }

    @Override
    public void setUseZIPCompression(boolean useZIPCompression) {
	this.useZIPCompression = useZIPCompression;
    }

}
