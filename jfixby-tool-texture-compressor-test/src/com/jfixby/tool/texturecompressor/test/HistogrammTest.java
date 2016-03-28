package com.jfixby.tool.texturecompressor.test;

import java.io.IOException;

import com.jfixby.cmns.api.collections.Collections;
import com.jfixby.cmns.api.collections.Histogramm;
import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.desktop.ImageAWT;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.LocalFileSystem;
import com.jfixby.cmns.api.image.ArrayColorMap;
import com.jfixby.cmns.api.image.ColorMap;
import com.jfixby.cmns.api.image.ColorMapSpecs;
import com.jfixby.cmns.api.image.GrayλImage;
import com.jfixby.cmns.api.image.ImageProcessing;
import com.jfixby.red.desktop.DesktopAssembler;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class HistogrammTest {

    public static void main(String[] args) throws IOException {
	DesktopAssembler.setup();
	IndexedCompressor.installComponent(new RedIndexedCompressor());

	File home = LocalFileSystem.ApplicationHome();
	File input_folder = home.child("input");
	File output_folder = home.child("indexed");
	File originalFile = input_folder.child("fox.png");

	// File compressedFile =
	// output_folder.child("index_compressed.r3-icolor");
	final Histogramm<Float> reds = Collections.newHistogramm();
	final Histogramm<Float> greens = Collections.newHistogramm();
	final Histogramm<Float> blues = Collections.newHistogramm();
//	final Histogramm<Float> alphas = Collections.newHistogramm();
	final ArrayColorMap image = ImageAWT.readAWTColorMap(originalFile);
	for (int i = 0; i < image.getWidth(); i++) {
	    for (int j = 0; j < image.getHeight(); j++) {
		Color color = image.valueAt(i, j);
		reds.addIf(color.red() * 255, color.red() > 0 && color.red() < 1);
		greens.addIf(color.green() * 255, color.green() > 0 && color.green() < 1);
		blues.addIf(color.blue() * 255, color.blue() > 0 && color.blue() < 1);
//		alphas.addIf(color.alpha() * 255, color.alpha() > 0 && color.alpha() < 1);
	    }
	}
	reds.sortValues();
	reds.print("reds");

	greens.sortValues();
	greens.print("greens");

	blues.sortValues();
	blues.print("blues");

//	alphas.sort();
//	alphas.print("alphas");

	GrayλImage redImage = histogrammImage(reds);
	GrayλImage greenImage = histogrammImage(greens);
	GrayλImage blueImage = histogrammImage(blues);
//	GrayλImage alphaImage = histogrammImage(alphas);

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
//	    ColorMapSpecs specs = ImageProcessing.newColorMapSpecs();
//	    specs.setColorMapDimentions(256, 256);
//	    specs.setRed(alphaImage);
//	    specs.setGreen(alphaImage);
//	    specs.setBlue(alphaImage);
//	    ColorMap colormap = ImageProcessing.newColorMap(specs);
//
//	    colormap = ImageProcessing.newColorMap(ImageProcessing.scale(colormap, 2, 2), 256 * 2, 256 * 2);
//
//	    ImageAWT.writeToFile(colormap, output_folder.child("histogramm-alpha.png"), "png");
	}

    }

    private static GrayλImage histogrammImage(final Histogramm<Float> histogramm) {
	return new GrayλImage() {
	    @Override
	    public float valueAt(float x, float y) {
		long num = histogramm.getNumberOf(1f * ImageProcessing.roundArgument(x));
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
