package com.jfixby.tool.texturecompressor.test;

import java.io.IOException;

import com.jfixby.scarabei.api.color.ColorSet;
import com.jfixby.scarabei.api.color.Colors;
import com.jfixby.scarabei.api.color.GraySet;
import com.jfixby.scarabei.api.desktop.DesktopSetup;
import com.jfixby.scarabei.api.desktop.ImageAWT;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.image.ColorMap;
import com.jfixby.scarabei.api.image.GrayIndexedλImage;
import com.jfixby.scarabei.api.image.GrayMap;
import com.jfixby.scarabei.api.image.ImageProcessing;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class ComparePalettes {

    public static void main(String[] args) throws IOException {
	DesktopSetup.deploy();
	IndexedCompressor.installComponent(new RedIndexedCompressor());

	File home = LocalFileSystem.ApplicationHome();
	File input_folder = home.child("input");
	File output_folder = home.child("indexed");

	final ColorMap image_hkse = ImageAWT.readAWTColorMap(input_folder.child("hks-e.png"));
	final ColorMap image_fox = ImageAWT.readAWTColorMap(input_folder.child("fox.png"));

	final ColorSet palette_hks_e = Colors.newColorsSet(image_hkse);
	final ColorSet palette_fox = Colors.newColorsSet(image_fox);

	GraySet red_hkse_palette = Colors.newGraySet(image_hkse.getRed());
	GraySet green_hkse_palette = Colors.newGraySet(image_hkse.getGreen());
	GraySet blue_hkse_palette = Colors.newGraySet(image_hkse.getBlue());

	red_hkse_palette.sort();
	green_hkse_palette.sort();
	blue_hkse_palette.sort();

	green_hkse_palette.addAll(0, 1);

	GrayMap red = image_fox.getRed();
	GrayMap green = image_fox.getGreen();
	GrayMap blue = image_fox.getBlue();
	GrayMap alpha = image_fox.getAlpha();

	int W = image_fox.getWidth();
	int H = image_fox.getHeight();

	GrayIndexedλImage red_indexed = ImageProcessing.index(red, green_hkse_palette);
	GrayIndexedλImage green_indexed = ImageProcessing.index(green, green_hkse_palette);
	GrayIndexedλImage blue_indexed = ImageProcessing.index(blue, green_hkse_palette);

	ColorMap colormap = ImageProcessing.newColorMap(W, H, alpha, red_indexed, green_indexed, blue_indexed);
	ImageAWT.writeToFile(colormap, output_folder.child("fox-projected.png"), "png");

	// red_hkse_palette.print("red_hkse_palette");
	// green_hkse_palette.print("green_hkse_palette");
	// blue_hkse_palette.print("blue_hkse_palette");

	// Colors.newGraySet()

	// palette_hks_e.print("palette_hks_e");
	// L.d("palette_fox", palette_fox.size());

    }

}
