package com.jfixby.tool.texturecompressor.test;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.GdxNativesLoader;

public class PixmapTest {

    public static void main(String[] args) {
	
	GdxNativesLoader.load();
	Pixmap.setBlending(Pixmap.Blending.None);
	final float W = 400;
	final float H = 400;
	Pixmap pixmap = new Pixmap((int) W, (int) H, Format.RGBA8888);
	pixmap.setColor(0x0000FF00);// clear background, Alpha == 0
	pixmap.fill();
	for (int x = 0; x < W; x++) {
	    for (int y = 0; y < H; y++) {
		pixmap.setColor(0xffffff80);// set color White with Alpha=0.5
		pixmap.drawPixel(x, y);
	    }
	}
	FileHandle file = new FileHandle("1.png");
	System.out.println("writing: " + file);
	PixmapIO.writePNG(file, pixmap);
	pixmap.dispose();

    }

}
