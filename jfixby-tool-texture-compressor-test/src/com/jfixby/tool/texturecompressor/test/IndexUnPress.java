package com.jfixby.tool.texturecompressor.test;

import java.io.IOException;

import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.color.Colors;
import com.jfixby.cmns.api.color.GraySet;
import com.jfixby.cmns.api.desktop.ImageAWT;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.LocalFileSystem;
import com.jfixby.cmns.api.image.ArrayColorMap;
import com.jfixby.cmns.api.image.ColorMap;
import com.jfixby.cmns.api.image.GrayλImage;
import com.jfixby.cmns.api.image.ImageProcessing;
import com.jfixby.red.desktop.DesktopSetup;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class IndexUnPress {

    public static void main(String[] args) throws IOException {
	DesktopSetup.deploy();
	IndexedCompressor.installComponent(new RedIndexedCompressor());

	File home = LocalFileSystem.ApplicationHome();
	File input_folder = home.child("indexed");
	File output_folder = home.child("indexed");
	File originalFile = input_folder.child("etc1-test-pressed.png");

	final ArrayColorMap original = ImageAWT.readAWTColorMap(originalFile);
	int W = original.getWidth();
	int H = original.getHeight();

	final GraySet green_palette = Colors.newUniformGraySet(IndexedCompressor.DEFAULT_GREEN_PALETTE_SIZE);
	final GraySet red_palette = Colors.newUniformGraySet(IndexedCompressor.DEFAULT_RED_PALETTE_SIZE);
	final GraySet blue_palette = Colors.newUniformGraySet(IndexedCompressor.DEFAULT_BLUE_PALETTE_SIZE);

	GrayλImage new_alpha = new GrayλImage() {

	    @Override
	    public float valueAt(float x, float y) {
		Color pressedColor = original.valueAt(x, y);
		return pressedColor.blue();
	    }
	};
	GrayλImage new_red = new GrayλImage() {

	    @Override
	    public float valueAt(float x, float y) {
		Color pressedColor = original.valueAt(x, y);
		int combined_rgb_index = (int) (pressedColor.red() * 255);
		combined_rgb_index = 0x0f & combined_rgb_index;
		return red_palette.getValue(combined_rgb_index);
	    }
	};
	GrayλImage new_green = new GrayλImage() {

	    @Override
	    public float valueAt(float x, float y) {
		Color pressedColor = original.valueAt(x, y);
		int combined_rgb_index = (int) (pressedColor.green() * 255);
		return green_palette.getValue(combined_rgb_index);
	    }
	};
	GrayλImage new_blue = new GrayλImage() {

	    @Override
	    public float valueAt(float x, float y) {
		Color pressedColor = original.valueAt(x, y);
		int combined_rgb_index = (int) (pressedColor.red() * 255);
		combined_rgb_index = 0x0f & (combined_rgb_index >> 4);
		return blue_palette.getValue(combined_rgb_index);
	    }
	};

	;
	ColorMap compressed = ImageProcessing
		.newColorMap(ImageProcessing.merge(new_alpha, new_red, new_green, new_blue), W, H);

	ImageAWT.writeToFile(compressed, output_folder.child(originalFile.nameWithoutExtension() + "-unpressed.png"),
		"png");

    }

}
