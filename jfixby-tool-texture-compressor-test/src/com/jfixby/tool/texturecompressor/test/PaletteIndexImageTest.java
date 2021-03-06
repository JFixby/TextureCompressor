package com.jfixby.tool.texturecompressor.test;

import java.io.IOException;

import com.jfixby.scarabei.api.color.CachedColorProjector;
import com.jfixby.scarabei.api.color.ColorSet;
import com.jfixby.scarabei.api.color.Colors;
import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.desktop.ImageAWT;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.image.ArrayColorMap;
import com.jfixby.scarabei.api.image.ColorMap;
import com.jfixby.scarabei.api.image.ColoredλImage;
import com.jfixby.scarabei.api.image.ImageProcessing;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class PaletteIndexImageTest {

    public static void main(String[] args) throws IOException {
	ScarabeiDesktop.deploy();
	IndexedCompressor.installComponent(new RedIndexedCompressor());

	File home = LocalFileSystem.ApplicationHome();
	File input_folder = home.child("input");
	File output_folder = home.child("indexed");
	File paletteFile = input_folder.child("hks-e.png");
	File originalFile = input_folder.child("fox.png");
	final ArrayColorMap palette_image = ImageAWT.readAWTColorMap(paletteFile);
	final ColorSet palette = Colors.newColorsSet(palette_image);

	final ArrayColorMap image = ImageAWT.readAWTColorMap(originalFile);

	int W = image.getWidth();
	int H = image.getHeight();

	CachedColorProjector colorProjector = Colors.colorProjectorCache(palette);

	ColoredλImage indexed = ImageProcessing.index(image, colorProjector);
	ColorMap colormap = ImageProcessing.newColorMap(indexed, W, H);

	ImageAWT.writeToFile(colormap, output_folder.child("indexed.png"), "png");
	palette.print("palette");
    }

}
