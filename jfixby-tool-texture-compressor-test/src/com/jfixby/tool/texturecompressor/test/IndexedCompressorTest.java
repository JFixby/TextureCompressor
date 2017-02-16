
package com.jfixby.tool.texturecompressor.test;

import java.io.IOException;

import com.jfixby.scarabei.api.color.Color;
import com.jfixby.scarabei.api.color.ColorProjector;
import com.jfixby.scarabei.api.color.Colors;
import com.jfixby.scarabei.api.color.GraySet;
import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.desktop.ImageAWT;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.image.ColorMap;
import com.jfixby.scarabei.api.image.ImageProcessing;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class IndexedCompressorTest {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		IndexedCompressor.installComponent(new RedIndexedCompressor());

		final File home = LocalFileSystem.ApplicationHome();

		final File input_folder = home.child("input");
		final File output_folder = home.child("indexed");

		output_folder.makeFolder();
		output_folder.clearFolder();

		final File inputFile = input_folder.child("fox.png");
		final File originalFile = output_folder.child(inputFile.nameWithoutExtension() + "-original.png");
		final File indexedFile = output_folder.child(inputFile.nameWithoutExtension() + "-cindex.png");
		final File noAlphaFile = output_folder.child(inputFile.nameWithoutExtension() + "-no-alpha.png");

		inputFile.getFileSystem().copyFileToFile(inputFile, originalFile);

		final ColorMap original_image = ImageAWT.readAWTColorMap(originalFile);

		final GraySet green_palette = Colors.newUniformGraySet(IndexedCompressor.DEFAULT_GREEN_PALETTE_SIZE);
		final GraySet red_palette = Colors.newUniformGraySet(IndexedCompressor.DEFAULT_RED_PALETTE_SIZE);
		final GraySet blue_palette = Colors.newUniformGraySet(IndexedCompressor.DEFAULT_BLUE_PALETTE_SIZE);

		final ColorProjector projector = new ColorProjector() {

			@Override
			public Color findClosest (final Color other) {
				final Color color = Colors.newColor(//
					red_palette.findClosest(other.red()), //
					green_palette.findClosest(other.green()), //
					blue_palette.findClosest(other.blue())//
				);
				return color;
			}
		};

		final ColorMap indexed = indexImage(original_image, projector);
		L.d("writing", indexedFile);
		ImageAWT.writeToFile(indexed, indexedFile, "png");

		final ColorMap noAlpha = ImageProcessing.removeAlpha(indexed);
		L.d("writing", noAlphaFile);
		ImageAWT.writeToFile(noAlpha, noAlphaFile, "png");
		L.d("DONE");
	}

	private static ColorMap indexImage (final ColorMap readAWTColorMap, final ColorProjector palette) {

		final int W = readAWTColorMap.getWidth();
		final int H = readAWTColorMap.getHeight();

		final ColorMap indexedImage = ImageProcessing.newColorMap(ImageProcessing.index(readAWTColorMap, palette), W, H);

		return indexedImage;
	}

}
