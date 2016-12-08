package com.jfixby.tool.texturecompressor.test;

import java.io.IOException;

import com.jfixby.cmns.api.collections.Collections;
import com.jfixby.cmns.api.collections.Histogramm;
import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.color.ColorProjector;
import com.jfixby.cmns.api.color.Colors;
import com.jfixby.cmns.api.color.GraySet;
import com.jfixby.cmns.api.desktop.DesktopSetup;
import com.jfixby.cmns.api.desktop.ImageAWT;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.LocalFileSystem;
import com.jfixby.cmns.api.image.ArrayColorMap;
import com.jfixby.cmns.api.image.ColorMap;
import com.jfixby.cmns.api.image.ColorMapSpecs;
import com.jfixby.cmns.api.image.GrayλImage;
import com.jfixby.cmns.api.image.ImageProcessing;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class HistogrammTest {

    public static void main(String[] args) throws IOException {
	DesktopSetup.deploy();
	IndexedCompressor.installComponent(new RedIndexedCompressor());

	File home = LocalFileSystem.ApplicationHome();
	File input_folder = home.child("input");
	File output_folder = home.child("indexed");
	File originalFile = input_folder.child("fox.png");

	// File compressedFile =
	// output_folder.child("index_compressed.r3-icolor");
	final Histogramm<Integer> reds = Collections.newHistogramm();
	final Histogramm<Integer> greens = Collections.newHistogramm();
	final Histogramm<Integer> blues = Collections.newHistogramm();
	// final Histogramm<Float> alphas = Collections.newHistogramm();
	final ArrayColorMap image = ImageAWT.readAWTColorMap(originalFile);

	final GraySet green_palette = Colors.newUniformGraySet(IndexedCompressor.DEFAULT_GREEN_PALETTE_SIZE);
	final GraySet red_palette = Colors.newUniformGraySet(IndexedCompressor.DEFAULT_RED_PALETTE_SIZE/2);
	final GraySet blue_palette = Colors.newUniformGraySet(IndexedCompressor.DEFAULT_BLUE_PALETTE_SIZE/2);

	final ColorProjector projector = new ColorProjector() {

	    @Override
	    public Color findClosest(Color other) {
		Color color = Colors.newColor(//
			red_palette.findClosest(other.red()), //
			green_palette.findClosest(other.green()), //
			blue_palette.findClosest(other.blue())//
		);
		// L.d("" + other, color);
		return color;
	    }
	};

	for (int i = 0; i < image.getWidth(); i++) {
	    for (int j = 0; j < image.getHeight(); j++) {
		Color color = (image.valueAt(i, j));
		float red = red_palette.findClosest(color.red()); //
		float green = green_palette.findClosest(color.green()); //
		float blue = blue_palette.findClosest(color.blue());//

		reds.add(ImageProcessing.roundArgument(red * 255));
		greens.add(ImageProcessing.roundArgument(green * 255));
		blues.add(ImageProcessing.roundArgument(blue * 255));
		// alphas.addIf(color.alpha() * 255, color.alpha() > 0 &&
		// color.alpha() < 1);
	    }
	}
	reds.sortValues();
	reds.print("reds");

	greens.sortValues();
	greens.print("greens");

	blues.sortValues();
	blues.print("blues");

	// alphas.sort();
	// alphas.print("alphas");

	GrayλImage redImage = histogrammImage(reds);
	GrayλImage greenImage = histogrammImage(greens);
	GrayλImage blueImage = histogrammImage(blues);
	// GrayλImage alphaImage = histogrammImage(alphas);

	{
	    ColorMapSpecs specs = ImageProcessing.newColorMapSpecs();
	    specs.setColorMapDimentions(256, 256);
	    specs.setRed(redImage);
	    specs.setGreen(ImageProcessing.ZERO());
	    specs.setBlue(ImageProcessing.ZERO());
	    ColorMap colormap = ImageProcessing.newColorMap(specs);

	    colormap = ImageProcessing.newColorMap(ImageProcessing.scale(colormap, 2, 2), 256 * 2, 256 * 2);

	    ImageAWT.writeToFile(colormap, output_folder.child("histogramm-red.png"), "png");
	}
	{
	    ColorMapSpecs specs = ImageProcessing.newColorMapSpecs();
	    specs.setColorMapDimentions(256, 256);
	    specs.setRed(ImageProcessing.ZERO());
	    specs.setGreen(greenImage);
	    specs.setBlue(ImageProcessing.ZERO());
	    ColorMap colormap = ImageProcessing.newColorMap(specs);

	    colormap = ImageProcessing.newColorMap(ImageProcessing.scale(colormap, 2, 2), 256 * 2, 256 * 2);

	    ImageAWT.writeToFile(colormap, output_folder.child("histogramm-green.png"), "png");
	}
	{
	    ColorMapSpecs specs = ImageProcessing.newColorMapSpecs();
	    specs.setColorMapDimentions(256, 256);
	    specs.setRed(ImageProcessing.ZERO());
	    specs.setGreen(ImageProcessing.ZERO());
	    specs.setBlue(blueImage);
	    ColorMap colormap = ImageProcessing.newColorMap(specs);

	    colormap = ImageProcessing.newColorMap(ImageProcessing.scale(colormap, 2, 2), 256 * 2, 256 * 2);

	    ImageAWT.writeToFile(colormap, output_folder.child("histogramm-blue.png"), "png");
	}
	{
	    // ColorMapSpecs specs = ImageProcessing.newColorMapSpecs();
	    // specs.setColorMapDimentions(256, 256);
	    // specs.setRed(alphaImage);
	    // specs.setGreen(alphaImage);
	    // specs.setBlue(alphaImage);
	    // ColorMap colormap = ImageProcessing.newColorMap(specs);
	    //
	    // colormap =
	    // ImageProcessing.newColorMap(ImageProcessing.scale(colormap, 2,
	    // 2), 256 * 2, 256 * 2);
	    //
	    // ImageAWT.writeToFile(colormap,
	    // output_folder.child("histogramm-alpha.png"), "png");
	}

    }

    private static GrayλImage histogrammImage(final Histogramm<Integer> histogramm) {
	return new GrayλImage() {
	    @Override
	    public float valueAt(float x, float y) {
		long num = histogramm.getNumberOf(ImageProcessing.roundArgument(x));
		long max = histogramm.getMax();
		float value = 1f - (num * 1f / max);
		// float value = (num * 1f / max);
		value = value * 255f;
		if (y < value) {
		    return 0;
		}
		return 1;
	    }

	};
    }

}
