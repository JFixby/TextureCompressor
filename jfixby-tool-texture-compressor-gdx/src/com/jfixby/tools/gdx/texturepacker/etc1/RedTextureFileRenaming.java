package com.jfixby.tools.gdx.texturepacker.etc1;

import com.jfixby.tools.gdx.texturepacker.api.etc1.TextureFileRenaming;

class RedTextureFileRenaming implements TextureFileRenaming {

    private String oldPageFileName;
    private String newPageFileName;

    public RedTextureFileRenaming(String oldPageFileName, String newPageFileName) {
	this.oldPageFileName = oldPageFileName;
	this.newPageFileName = newPageFileName;
    }

    @Override
    public String toString() {
	return oldPageFileName + " :-> " + newPageFileName;
    }

}