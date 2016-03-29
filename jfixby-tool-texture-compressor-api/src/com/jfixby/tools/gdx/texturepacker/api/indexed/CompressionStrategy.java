package com.jfixby.tools.gdx.texturepacker.api.indexed;

public enum CompressionStrategy {

    SEPARATE_CHANNELS(53), MERGE_CHANNELS(31);

    private int byteFlag;

    CompressionStrategy(int byteFlag) {
	this.byteFlag = (byte) byteFlag;
    }

    public static CompressionStrategy checkDefault(CompressionStrategy compressionStrategy) {
	if (compressionStrategy != null) {
	    return compressionStrategy;
	}
	return SEPARATE_CHANNELS;
    }

    public int byteFlag() {
	return byteFlag;
    }

    public static CompressionStrategy valueOf(int readByte) {
	for (int i = 0; i < values().length; i++) {
	    if (values()[i].byteFlag == readByte) {
		return values()[i];
	    }
	}
	return null;
    }

}
