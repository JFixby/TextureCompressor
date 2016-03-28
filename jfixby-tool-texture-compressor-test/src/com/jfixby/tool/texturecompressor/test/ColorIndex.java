package com.jfixby.tool.texturecompressor.test;

import java.io.IOException;
import java.util.List;

import com.jfixby.cmns.api.collections.Collections;
import com.jfixby.cmns.api.collections.Histogramm;
import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.color.ColorProjector;
import com.jfixby.cmns.api.color.Colors;
import com.jfixby.cmns.api.color.GraySet;
import com.jfixby.cmns.api.desktop.ImageAWT;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.LocalFileSystem;
import com.jfixby.cmns.api.image.ColorMap;
import com.jfixby.cmns.api.image.ImageProcessing;
import com.jfixby.cmns.api.image.PixelByPixelAction;
import com.jfixby.cmns.api.log.L;
import com.jfixby.red.desktop.DesktopAssembler;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class ColorIndex {

    public static void main(String[] args) throws IOException {
	DesktopAssembler.setup();
	IndexedCompressor.installComponent(new RedIndexedCompressor());

	File home = LocalFileSystem.ApplicationHome();

	File input_folder = home.child("input");
	File output_folder = home.child("indexed");
	output_folder.makeFolder();

	File originalFile = input_folder.child("fox.png");
	File indexedFile = output_folder.child(originalFile.nameWithoutExtension() + "-cindex.png");
	File noAlphaFile = output_folder.child(originalFile.nameWithoutExtension() + "-no-alpha.png");

	ColorMap original_image = ImageAWT.readAWTColorMap(originalFile);

	// original_image = ImageProcessing.removeAlpha(original_image);
	// ImageAWT.writeToFile(original_image, noAlphaFile, "png");

	final GraySet green_palette = Colors.newUniformGraySet(IndexedCompressor.DEFAULT_GREEN_PALETTE_SIZE / 2);
	final GraySet red_palette = Colors.newUniformGraySet(IndexedCompressor.DEFAULT_RED_PALETTE_SIZE / 2);
	final GraySet blue_palette = Colors.newUniformGraySet(IndexedCompressor.DEFAULT_BLUE_PALETTE_SIZE / 2);
	// final ColorSet palette = Colors.newColorsSet();

	// L.d("generating color space");
	// for (int r = 0; r < red_palette.size(); r++) {
	// for (int g = 0; g < green_palette.size(); g++) {
	// for (int b = 0; b < blue_palette.size(); b++) {
	// float red = red_palette.getValue(r);
	// float gren = red_palette.getValue(g);
	// float blue = red_palette.getValue(b);
	// CustomColor color = Colors.newColor(red, gren, blue);
	// palette.add(color);
	// }
	// }
	// }

	final ColorProjector projector = new ColorProjector() {

	    @Override
	    public Color findClosest(Color other) {
		Color color = Colors.newColor(//
			red_palette.findClosest(other.red()), //
			green_palette.findClosest(other.green()), //
			blue_palette.findClosest(other.blue())//
		);
		return color;
	    }
	};

	L.d("collecting color stats");
	final Histogramm<Color> colorStats = Collections.newHistogramm();
	PixelByPixelAction action = new PixelByPixelAction() {
	    @Override
	    public boolean scanPixelAt(ColorMap image, int x, int y, Color value) {

		colorStats.add(projector.findClosest(value));
		return false;
	    }
	};
	ImageProcessing.scanImage(original_image, action);
	L.d("sorting colors", colorStats.size());
	colorStats.sortNumbers();
	L.d("sorted", colorStats.size());
	colorStats.print("colorStats");
	// colors.cutToSize(256);
	// Sys.exit();

	List<Color> java_list = colorStats.toJavaList();

	// long N = IntegerMath.min(256 * 128, java_list.size());
	// for (int i = 0; i < N; i++) {
	// Color color = java_list.get(i);
	// palette.add(color);
	// }

	// Collections.arrayCopy(colors, 0, palette, 256 * 256);

	// palette.print("palette");
	L.d("writing", indexedFile);
	ImageAWT.writeToFile(indexImage(original_image, projector), indexedFile, "png");
	L.d("DONE");
    }

    private static ColorMap indexImage(ColorMap readAWTColorMap, ColorProjector palette) {

	int W = readAWTColorMap.getWidth();
	int H = readAWTColorMap.getHeight();

	ColorMap indexedImage = ImageProcessing.newColorMap(ImageProcessing.index(readAWTColorMap, palette), W, H);

	return indexedImage;
    }

}
