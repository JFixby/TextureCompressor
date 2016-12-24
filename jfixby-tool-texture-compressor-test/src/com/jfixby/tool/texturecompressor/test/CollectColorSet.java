
package com.jfixby.tool.texturecompressor.test;

import java.io.IOException;

import com.jfixby.scarabei.api.color.Color;
import com.jfixby.scarabei.api.color.ColorSet;
import com.jfixby.scarabei.api.color.Colors;
import com.jfixby.scarabei.api.desktop.DesktopSetup;
import com.jfixby.scarabei.api.desktop.ImageAWT;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.image.ArrayColorMap;
import com.jfixby.scarabei.api.image.ColorMap;
import com.jfixby.scarabei.api.image.ColoredλImage;
import com.jfixby.scarabei.api.image.ImageProcessing;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class CollectColorSet {

	public static void main (final String[] args) throws IOException {
		DesktopSetup.deploy();
		IndexedCompressor.installComponent(new RedIndexedCompressor());

		final File home = LocalFileSystem.ApplicationHome();
		final File input_folder = home.child("input");
		final File output_folder = home.child("indexed");
// File originalFile = input_folder.child("hks-e.png");
		final File originalFile = input_folder.child("fox.png");
		final ArrayColorMap image = ImageAWT.readAWTColorMap(originalFile);

		final ColorSet palette = Colors.newColorsSet(image);

		palette.print(originalFile.getName());

		ColoredλImage paletteImage = new ColoredλImage() {
			@Override
			public Color valueAt (final float x, final float y) {
				return palette.getElementAt((int)x);
			}
		};

		paletteImage = ImageProcessing.scale(paletteImage, 8, 8);
		final ColorMap colors = ImageProcessing.newColorMap(paletteImage, palette.size() * 8, palette.size() * 2);
		ImageAWT.writeToFile(colors, output_folder.child("palette.png"), "png");

	}

}
