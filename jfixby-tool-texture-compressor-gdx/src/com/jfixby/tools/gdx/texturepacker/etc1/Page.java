
package com.jfixby.tools.gdx.texturepacker.etc1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class Page {

    @Override
    public String toString() {
	return "Page(" + newPageFileName + ") " + w + " x " + h + "";
    }

    byte[] bytes;
    int pointer = 0;
    private String newPageFileName;
    private int w;
    private int h;

    public Page(String newPageFileName, int w, int h) {
	bytes = new byte[w * h];
	this.w = w;
	this.h = h;
	this.newPageFileName = newPageFileName;
    }

    public void addAlphaValue(int alpha) {
	bytes[pointer] = (byte) alpha;
	pointer++;
    }

    public void checkValid(String newPageFileName) {
	if (!this.newPageFileName.equals(newPageFileName)) {
	    throw new Error("AlphaInfoPage<" + this.newPageFileName + "> is corrupted");
	}
	if (pointer != bytes.length) {
	    throw new Error("AlphaInfoPage<" + this.newPageFileName + "> is corrupted");
	}
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

    public static void writePage(ByteArrayOutputStream buffer, Page page) throws IOException {
	page.writeTo(buffer);
    }

    public static Page readPage(ByteArrayInputStream input) throws IOException {

	ObjectInputStream is = new ObjectInputStream(input);
	String newPageFileName;
	try {
	    newPageFileName = (String) is.readObject();

	    int w = is.readInt();
	    int h = is.readInt();
	    byte[] bytes = (byte[]) is.readObject();
	    Page page = new Page(newPageFileName, w, h);
	    page.bytes = bytes;
	    return page;
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	    throw new IOException(e);
	}
    }

}
