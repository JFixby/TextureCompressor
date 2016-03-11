
package com.jfixby.tools.gdx.texturepacker.etc1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.jfixby.cmns.api.file.File;

class AlphaPage {

    @Override
    public String toString() {
	return "Page(" + newPageFileName + ") " + w + " x " + h + "";
    }

    int[] bytes;
    int pointer = 0;
    private final String newPageFileName;
    private int w;
    private int h;

    final public float getAlphaValue(final int x, final int y) {
	// this.pointer = x + this.w * y;
	this.pointer = y + this.h * x;
	return this.bytes[this.pointer] / 255f;
    }

    public AlphaPage(String newPageFileName, int w, int h) {
	bytes = new int[w * h];
	this.w = w;
	this.h = h;
	this.newPageFileName = newPageFileName;
    }

    public void addAlphaValue(int alpha, final int x, final int y) {
	this.pointer = y + this.h * x;
	bytes[pointer] = alpha;
    }

    public void writeTo(ByteArrayOutputStream buffer) throws IOException {
	checkValid(newPageFileName);
	ObjectOutputStream obj = new ObjectOutputStream(buffer);
	obj.writeObject(newPageFileName);
	obj.writeInt(w);
	obj.writeInt(h);
	obj.writeObject(bytes);
	obj.close();
    }

    public static void writePage(ByteArrayOutputStream buffer, AlphaPage page) throws IOException {
	page.writeTo(buffer);
    }

    public static AlphaPage readPage(ByteArrayInputStream input) throws IOException {

	ObjectInputStream is = new ObjectInputStream(input);
	String newPageFileName;
	try {
	    newPageFileName = (String) is.readObject();

	    int w = is.readInt();
	    int h = is.readInt();
	    int[] bytes = (int[]) is.readObject();
	    AlphaPage page = new AlphaPage(newPageFileName, w, h);
	    page.bytes = bytes;
	    return page;
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	    throw new IOException(e);
	}
    }

    public String getName() {
	return this.newPageFileName;
    }

    public void saveAsPng(File png_file) {
	Pixmap pixmap = new Pixmap(w, h, Format.RGB888);
	for (int x = 0; x < w; x++) {
	    for (int y = 0; y < h; y++) {

		// int color = 0x000000ff;
		float alpha = this.getAlphaValue(x, y);
		// color = color | (alpha << 8);
		// pixmap.setColor(color);

		Color color = new Color(alpha, alpha, alpha, 1);
		pixmap.setColor(color);
		pixmap.drawPixel(x, y);
	    }
	}
	FileHandle gdx_file = new FileHandle(png_file.toJavaFile());
	PixmapIO.writePNG(gdx_file, pixmap);
    }

    public void checkValid(String newPageFileName) {
	if (!this.newPageFileName.equals(newPageFileName)) {
	    throw new Error("AlphaInfoPage<" + this.newPageFileName + "> is corrupted");
	}

    }

    public void checkValid(int w, int h) {
	if (w != this.w) {
	    throw new Error("AlphaInfoPage<" + this.newPageFileName + "> is corrupted");
	}
	if (h != this.h) {
	    throw new Error("AlphaInfoPage<" + this.newPageFileName + "> is corrupted");
	}
    }

}
