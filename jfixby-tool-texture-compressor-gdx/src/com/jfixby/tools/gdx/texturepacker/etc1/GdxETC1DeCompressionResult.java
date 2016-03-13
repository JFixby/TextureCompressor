package com.jfixby.tools.gdx.texturepacker.etc1;

import com.jfixby.cmns.api.image.ColorMap;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1DeCompressionResult;

public class GdxETC1DeCompressionResult implements ETC1DeCompressionResult {

    private ColorMap image;

    public void setImage(ColorMap image) {
	this.image = image;
    }

    public ColorMap getImage() {
	return image;
    }

}
