package com.jfixby.tools.gdx.texturepacker.etc1;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;

public class TextureContainer {

    private Texture texture;
    private FileHandle textureFile;

    public TextureContainer(Texture texture, FileHandle textureFile) {
	this.textureFile = textureFile;
	this.texture = texture;
    }

    public Texture getTexture() {
	return this.texture;
    }

    public void setTexture(Texture newGdxTexture) {
	this.texture = newGdxTexture;
    }

    public FileHandle getTextureFile() {
	return textureFile;
    }

}
