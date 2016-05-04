package com.jfixby.tool.texturecompressor.test;

import java.io.IOException;

import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.color.ColorSet;
import com.jfixby.cmns.api.color.Colors;
import com.jfixby.cmns.api.desktop.ImageAWT;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.LocalFileSystem;
import com.jfixby.cmns.api.image.ArrayColorMap;
import com.jfixby.cmns.api.image.ColorMap;
import com.jfixby.cmns.api.image.ColoredλImage;
import com.jfixby.cmns.api.image.ImageProcessing;
import com.jfixby.red.desktop.DesktopSetup;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class CollectColorSet {

    public static void main(String[] args) throws IOException {
	DesktopSetup.deploy();
	IndexedCompressor.installComponent(new RedIndexedCompressor());

	File home = LocalFileSystem.ApplicationHome();
	File input_folder = home.child("input");
	File output_folder = home.child("indexed");
//	File originalFile = input_folder.child("hks-e.png");
	File originalFile = input_folder.child("fox.png");
	final ArrayColorMap image = ImageAWT.readAWTColorMap(originalFile);

	final ColorSet palette = Colors.newColorsSet(image);

	palette.print(originalFile.getName());

	ColoredλImage paletteImage = new ColoredλImage() {
	    @Override
	    public Color valueAt(float x, float y) {
		return palette.getElementAt((int) x);
	    }
	};

	paletteImage = ImageProcessing.scale(paletteImage, 8, 8);
	ColorMap colors = ImageProcessing.newColorMap(paletteImage, palette.size() * 8, palette.size() * 2);
	ImageAWT.writeToFile(colors, output_folder.child("palette.png"), "png");

    }

}
