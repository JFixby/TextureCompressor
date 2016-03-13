package com.jfixby.tools.gdx.texturepacker.etc1;

import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.image.ColorMap;
import com.jfixby.cmns.api.io.OutputStream;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1CompressionParams;

public class GdxETC1CompressionParams implements ETC1CompressionParams {
    private ColorMap image;
    private OutputStream outputStream;
    private Color transparentColor;
    private boolean discardAlpha;

    GdxETC1CompressionParams() {
    }

    public void setInputImage(ColorMap image) {
	this.image = image;
    }

    public ColorMap getInputImage() {
	return image;
    }

    public OutputStream getOutputStream() {
	return outputStream;
    }

    public Color getTransparentColor() {
	return transparentColor;
    }

    public boolean discardAlpha() {
	return discardAlpha;
    }

    public void setOutputStream(OutputStream outputStream) {
	this.outputStream = outputStream;
    }

    public void setTransparentColor(Color transparentColor) {
	this.transparentColor = transparentColor;
    }

    public void setDiscardAlpha(boolean discardAlpha) {
	this.discardAlpha = discardAlpha;
    }

}
