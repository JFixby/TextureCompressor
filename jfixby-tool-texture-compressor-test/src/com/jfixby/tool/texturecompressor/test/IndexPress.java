package com.jfixby.tool.texturecompressor.test;

import java.io.IOException;

import com.jfixby.cmns.api.color.Colors;
import com.jfixby.cmns.api.color.GraySet;
import com.jfixby.cmns.api.debug.Debug;
import com.jfixby.cmns.api.desktop.DesktopSetup;
import com.jfixby.cmns.api.desktop.ImageAWT;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.LocalFileSystem;
import com.jfixby.cmns.api.image.ArrayColorMap;
import com.jfixby.cmns.api.image.ColorMap;
import com.jfixby.cmns.api.image.GrayIndexedλImage;
import com.jfixby.cmns.api.image.GrayλImage;
import com.jfixby.cmns.api.image.ImageProcessing;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class IndexPress {

    public static void main(String[] args) throws IOException {
	DesktopSetup.deploy();
	IndexedCompressor.installComponent(new RedIndexedCompressor());

	File home = LocalFileSystem.ApplicationHome();
	File input_folder = home.child("indexed");
	File output_folder = home.child("indexed");
	File originalFile = input_folder.child("etc1-test.png");

	ArrayColorMap original = ImageAWT.readAWTColorMap(originalFile);
	int W = original.getWidth();
	int H = original.getHeight();

	GrayλImage alpha = original.getAlpha();
	GrayλImage green = original.getGreen();
	GrayλImage red = original.getRed();
	GrayλImage blue = original.getBlue();

	GraySet green_palette = Colors.newUniformGraySet(IndexedCompressor.DEFAULT_GREEN_PALETTE_SIZE);
	GraySet red_palette = Colors.newUniformGraySet(IndexedCompressor.DEFAULT_RED_PALETTE_SIZE);
	GraySet blue_palette = Colors.newUniformGraySet(IndexedCompressor.DEFAULT_BLUE_PALETTE_SIZE);

	final GrayIndexedλImage indexed_green = ImageProcessing.index(green, green_palette);

	final GrayIndexedλImage indexed_red = ImageProcessing.index(red, red_palette);

	final GrayIndexedλImage indexed_blue = ImageProcessing.index(blue, blue_palette);

	final GrayλImage nerged_red_blue = new GrayλImage() {

	    @Override
	    public float valueAt(float x, float y) {
		int redIndex = indexed_red.getPalette().indexOf(indexed_red.valueAt(x, y));
		int blueIndex = indexed_blue.getPalette().indexOf(indexed_blue.valueAt(x, y));
		Debug.checkTrue(redIndex < 16);
		Debug.checkTrue(blueIndex < 16);
		int combined = (blueIndex << 4) | redIndex;
		return combined * 1f / 255f;
	    }
	};

	green = new GrayλImage() {
	    @Override
	    public float valueAt(float x, float y) {
		return indexed_green.getPalette().indexOf(indexed_green.valueAt(x, y)) * 1f / 255f;
	    }
	};

	ColorMap compressed = ImageProcessing.newColorMap(W, H, null, nerged_red_blue, green, alpha);

	ImageAWT.writeToFile(compressed, output_folder.child(originalFile.nameWithoutExtension() + "-pressed.png"),
		"png");

    }

}
