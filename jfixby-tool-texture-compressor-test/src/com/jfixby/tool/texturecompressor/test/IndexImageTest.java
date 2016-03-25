package com.jfixby.tool.texturecompressor.test;

import java.io.IOException;

import com.jfixby.cmns.api.color.Colors;
import com.jfixby.cmns.api.color.GraySet;
import com.jfixby.cmns.api.desktop.ImageAWT;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.LocalFileSystem;
import com.jfixby.cmns.api.image.ArrayColorMap;
import com.jfixby.cmns.api.image.ColorMap;
import com.jfixby.cmns.api.image.GrayIndexedλImage;
import com.jfixby.cmns.api.image.GrayMap;
import com.jfixby.cmns.api.image.ImageProcessing;
import com.jfixby.red.desktop.DesktopAssembler;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class IndexImageTest {

    public static void main(String[] args) throws IOException {
	DesktopAssembler.setup();
	IndexedCompressor.installComponent(new RedIndexedCompressor());

	File home = LocalFileSystem.ApplicationHome();
	File input_folder = home.child("input");
	File output_folder = home.child("indexed");
	File originalFile = input_folder.child("fox.png");
	final ArrayColorMap image = ImageAWT.readAWTColorMap(originalFile);

	GrayMap red = image.getRed();
	GrayMap green = image.getGreen();
	GrayMap blue = image.getBlue();
	GrayMap alpha = image.getAlpha();

	// 0.21 R + 0.72 G + 0.07 B
	int N = 16;
	// GraySet red_palette = Colors.newUniformGraySet((int) (+(1f - 0.21) *
	// N));
	// GraySet green_palette = Colors.newUniformGraySet((int) (+(1f - 0.72)
	// * N));
	// GraySet blue_palette = Colors.newUniformGraySet((int) (+(1f - 0.21) *
	// N));

	GraySet red_palette = Colors.newUniformGraySet((int) (15));// -4bit
	GraySet green_palette = Colors.newUniformGraySet((int) (31));// -2bit
	GraySet blue_palette = Colors.newUniformGraySet((int) (15));// -4bit

	// GraySet red_palette = Colors.newUniformGraySet((int) (N));
	// GraySet green_palette = Colors.newUniformGraySet((int) (N));
	// GraySet blue_palette = Colors.newUniformGraySet((int) (N));

	// GraySet alpha_palette = Colors.newUniformGraySet((int) (-(0f - 0.07)
	// * N));

	red_palette.print("red");
	green_palette.print("green");
	blue_palette.print("blue");
	// alpha_palette.print("alpha");

	int W = image.getWidth();
	int H = image.getHeight();

	GrayIndexedλImage red_indexed = ImageProcessing.index(red, red_palette);
	GrayIndexedλImage green_indexed = ImageProcessing.index(green, green_palette);
	GrayIndexedλImage blue_indexed = ImageProcessing.index(blue, blue_palette);
	// GrayIndexedλImage alpha_indexed = ImageProcessing.index(alpha,
	// alpha_palette);

	red = ImageProcessing.newGrayMap(red_indexed, W, H);
	green = ImageProcessing.newGrayMap(green_indexed, W, H);
	blue = ImageProcessing.newGrayMap(blue_indexed, W, H);
	// alpha = ImageProcessing.newGrayMap(alpha_indexed, W, H);

	{

	    ColorMap colormap = ImageProcessing.newColorMap(W, H, alpha, red, green, blue);

	    ImageAWT.writeToFile(colormap, output_folder.child("indexed.png"), "png");
	}

    }

}
