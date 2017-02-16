package com.jfixby.tool.texturecompressor.test;

import java.io.IOException;

import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.Histogramm;
import com.jfixby.scarabei.api.color.Color;
import com.jfixby.scarabei.api.color.Colors;
import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.desktop.ImageAWT;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.image.ColorMap;
import com.jfixby.scarabei.api.image.ColorMapSpecs;
import com.jfixby.scarabei.api.image.ColoredλImage;
import com.jfixby.scarabei.api.image.ImageProcessing;
import com.jfixby.scarabei.api.image.PixelByPixelAction;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class HistogrammColorTest {

    public static void main(String[] args) throws IOException {
	ScarabeiDesktop.deploy();
	IndexedCompressor.installComponent(new RedIndexedCompressor());

	File home = LocalFileSystem.ApplicationHome();
	File input_folder = home.child("input");
	File output_folder = home.child("indexed");
	File originalFile = input_folder.child("fox.png");

	ColorMap image = ImageAWT.readAWTColorMap(originalFile);
	// ColorSet colors = Colors.newColorsSet(image);

	// image = ImageProcessing.scaleTo(image, 1024, 1024);

	final Histogramm<Color> colors = Collections.newHistogramm();
	PixelByPixelAction action = new PixelByPixelAction() {
	    @Override
	    public boolean scanPixelAt(ColorMap image, int x, int y, Color value) {
		colors.add(value);
		// L.d(x + ", " + y, value);

		return false;
	    }
	};
	ImageProcessing.scanImage(image, action);

	colors.sortNumbers();
	colors.cutToSize(256);
	colors.print("colors");
	// colors.

	// Collections.newList(array)
	// Sys.exit();

	// alphas.sort();
	// alphas.print("alphas");

	ColorMap colormap = histogrammImage(colors);

	ImageAWT.writeToFile(colormap, output_folder.child("histogramm-color.png"), "png");
	L.d("DONE");
    }

    private static ColorMap histogrammImage(final Histogramm<Color> histogramm) {

	ColorMapSpecs specs = ImageProcessing.newColorMapSpecs();
	specs.setColorMapDimentions(histogramm.size(), 256);
	ColoredλImage lambda = new ColoredλImage() {
	    @Override
	    public Color valueAt(float x, float y) {
		long index = ImageProcessing.roundArgument(x);
		long num = histogramm.getNumberAt(index);
		long max = histogramm.getMax();
		float value = 1f - (num * 1f / max);
		// float value = (num * 1f / max);
		value = value * 255f;
		if (y < value) {
		    return Colors.BLACK();
		}
		return histogramm.getValueAt(index);
	    }
	};
	specs.setLambdaColoredImage(lambda);
	ColorMap colormap = ImageProcessing.newColorMap(specs);

	return colormap;
    }

}
