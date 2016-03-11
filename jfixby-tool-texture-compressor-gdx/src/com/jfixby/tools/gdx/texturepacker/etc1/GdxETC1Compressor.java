
package com.jfixby.tools.gdx.texturepacker.etc1;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.ETC1;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.color.Colors;
import com.jfixby.cmns.api.file.File;

public class GdxETC1Compressor {
    public static Pixmap removeAlphaChannel(Pixmap pixmap) {
	int W = pixmap.getWidth();
	int H = pixmap.getHeight();
	for (int x = 0; x < W; x++) {
	    for (int y = 0; y < H; y++) {
		int value = pixmap.getPixel(x, y);
		value = value | 0x000000ff;
		pixmap.drawPixel(x, y, value);
	    }
	}
	return pixmap;

    }

    public static void compressFile(File inputFile, File outputFile, Color transparentColor, boolean removeAlpha) {
	GdxNativesLoader.load();
	System.out.println("Processing " + inputFile);
	if (transparentColor == null) {
	    transparentColor = Colors.FUCHSIA();
	}
	com.badlogic.gdx.graphics.Color gdxTransparentColor = new com.badlogic.gdx.graphics.Color(
		transparentColor.red(), transparentColor.green(), transparentColor.blue(), transparentColor.alpha());
	Pixmap pixmap = new Pixmap(new FileHandle(inputFile.toJavaFile()));

	if (removeAlpha) {
	    pixmap = removeAlphaChannel(pixmap);
	}

	if (pixmap.getFormat() != Format.RGB888 && pixmap.getFormat() != Format.RGB565) {
	    System.out.println("Converting from " + pixmap.getFormat() + " to RGB888!");
	    Pixmap tmp = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), Format.RGB888);
	    tmp.setColor(gdxTransparentColor);
	    tmp.fill();
	    tmp.drawPixmap(pixmap, 0, 0, 0, 0, pixmap.getWidth(), pixmap.getHeight());
	    pixmap.dispose();
	    pixmap = tmp;
	}
	ETC1.encodeImagePKM(pixmap).write(new FileHandle(outputFile.toJavaFile()));
	pixmap.dispose();
    }
}
