
package com.jfixby.tools.gdx.texturepacker.etc1;

import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.collections.Collections;
import com.jfixby.cmns.api.collections.List;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.log.L;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1AtlasCompressionResult;
import com.jfixby.tools.gdx.texturepacker.api.etc1.TextureFileRenaming;

class RedETC1AtlasCompressionResult implements ETC1AtlasCompressionResult {

    private File gdxAtlas;
    private List<TextureFileRenaming> textures = Collections.newList();

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
	L.d("ETC1AtlasCompressionResult[" + textures.size() + "]");
	L.d(" gdx atlas file", gdxAtlas);
	for (int i = 0; i < textures.size(); i++) {
	    TextureFileRenaming renaming = textures.getElementAt(i);
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

    @Override
    public Collection<TextureFileRenaming> listRenamings() {
	return textures;
    }

}
