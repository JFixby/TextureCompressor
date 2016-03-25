package com.jfixby.tools.gdx.texturepacker.api.indexed;

public enum CompressionStrategy {

    SEPARATE_CHANNELS, MERGE_CHANNELS;

    public static CompressionStrategy checkDefault(CompressionStrategy compressionStrategy) {
	if (compressionStrategy != null) {
	    return compressionStrategy;
	}
	return SEPARATE_CHANNELS;
    }

}
