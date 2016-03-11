
package com.jfixby.tools.gdx.texturepacker.etc1;

import java.util.ArrayList;

import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.log.L;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1AtlasCompressionResult;

class RedETC1AtlasCompressionResult implements ETC1AtlasCompressionResult {

    private File atlas;
    private ArrayList<RedTextureFileRenaming> textures = new ArrayList<RedTextureFileRenaming>();

    private Color transparentColor;

    

    public void setAtlasFile(File atlas) {
	this.atlas = atlas;
    }

    public void setTransparentColor(Color transparentColor) {
	this.transparentColor = transparentColor;
    }

    public void addCompressedTextureNames(String oldPageFileName, String newPageFileName) {
	RedTextureFileRenaming renaming = new RedTextureFileRenaming(oldPageFileName, newPageFileName);
	textures.add(renaming);
    }

    public void print() {
	L.d("AtlasETC1CompressionResult[" + textures.size() + "]");
	L.d(" atlas file", atlas);
	L.d(" transparent color", transparentColor);
	for (int i = 0; i < textures.size(); i++) {
	    RedTextureFileRenaming renaming = textures.get(i);
	    L.d("   " + i, renaming);
	}

    }

    public File getAtlasPath() {
	return this.atlas;
    }

}
