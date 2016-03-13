
package com.jfixby.tools.gdx.texturepacker.etc1;

import java.util.ArrayList;

import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.log.L;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1AtlasCompressionResult;

class RedETC1AtlasCompressionResult implements ETC1AtlasCompressionResult {

    private File gdxAtlas;
    private ArrayList<RedTextureFileRenaming> textures = new ArrayList<RedTextureFileRenaming>();

    private File compressedAtlasFile;

    public void setGdxAtlasFile(File atlas) {
	this.gdxAtlas = atlas;
    }

    public void setCompressedAtlasFile(File compressedAtlasFile) {
	this.compressedAtlasFile = compressedAtlasFile;
    }

    public void addCompressedTextureNames(String oldPageFileName, String newPageFileName) {
	RedTextureFileRenaming renaming = new RedTextureFileRenaming(oldPageFileName, newPageFileName);
	textures.add(renaming);
    }

    public void print() {
	L.d("AtlasETC1CompressionResult[" + textures.size() + "]");
	L.d(" gdx atlas file", gdxAtlas);
	for (int i = 0; i < textures.size(); i++) {
	    RedTextureFileRenaming renaming = textures.get(i);
	    L.d("   " + i, renaming);
	}

    }

    public File getGdxAtlasFile() {
	return this.gdxAtlas;
    }

    @Override
    public File getCompressedAtlasFile() {
	return this.compressedAtlasFile;
    }

}
