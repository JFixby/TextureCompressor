package com.jfixby.tools.gdx.texturepacker.etc1;

class RedTextureFileRenaming {

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