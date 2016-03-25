package com.jfixby.tools.gdx.texturepacker.api.indexed;

import com.jfixby.cmns.api.color.GraySet;

public enum RGBOrder {

    RGB(1, 2, 3), //
    RBG(1, 3, 2), //
    GBR(3, 1, 2), //
    GRB(2, 1, 3), //
    BGR(3, 2, 1), //
    BRG(2, 3, 1),//
    ;

    private int indexRed;
    private int indexGreen;
    private int indexBlue;

    RGBOrder(int indexRed, int indexGreen, int indexBlue) {
	this.indexRed = indexRed;
	this.indexGreen = indexGreen;
	this.indexBlue = indexBlue;
    }

    final public static RGBOrder checkDefault(final RGBOrder rgbOrder) {
	if (rgbOrder != null) {
	    return rgbOrder;
	}
	return RGB;
    }

    public GraySet[] permutation(GraySet[] rgb) {
	GraySet[] result = new GraySet[3];
	result[indexRed] = rgb[0];
	result[indexGreen] = rgb[1];
	result[indexBlue] = rgb[2];
	return result;
    }

}
