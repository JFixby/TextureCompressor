package com.jfixby.tool.texturecompressor.test;

import java.io.IOException;

import com.jfixby.cmns.api.color.ColorSet;
import com.jfixby.cmns.api.color.Colors;
import com.jfixby.cmns.api.color.GraySet;
import com.jfixby.cmns.api.desktop.ImageAWT;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.FileOutputStream;
import com.jfixby.cmns.api.file.LocalFileSystem;
import com.jfixby.cmns.api.image.ArrayColorMap;
import com.jfixby.cmns.api.image.GrayMap;
import com.jfixby.red.desktop.DesktopAssembler;
import com.jfixby.tools.gdx.texturepacker.api.indexed.CompressionStrategy;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexColorCompressionParams;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.gdx.texturepacker.api.indexed.RGBOrder;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class IndexCompressImage {

    public static void main(String[] args) throws IOException {

	DesktopAssembler.setup();
	IndexedCompressor.installComponent(new RedIndexedCompressor());

	File home = LocalFileSystem.ApplicationHome();
	File input_folder = home.child("input");
	File output_folder = home.child("indexed");

	File originalFile = input_folder.child("etc1-test.png");

	final ArrayColorMap image = ImageAWT.readAWTColorMap(originalFile);

	GrayMap red = image.getRed();
	GrayMap green = image.getGreen();
	GrayMap blue = image.getBlue();
	GrayMap alpha = image.getAlpha();

	GraySet red_palette = Colors.newUniformGraySet((int) (16));// -4bit
	GraySet green_palette = Colors.newUniformGraySet((int) (32));// -2bit
	GraySet blue_palette = Colors.newUniformGraySet((int) (16));// -4bit
	ColorSet colorPalette = Colors.newColorsSet(image);

	int W = image.getWidth();
	int H = image.getHeight();

	IndexColorCompressionParams params = IndexedCompressor.newCompressionParams();

	File output_file = output_folder.child(originalFile.getName() + IndexedCompressor.INDEXED_COLOR_FILE_EXTENTION);
	FileOutputStream os = output_file.newOutputStream();

	params.setInputImage(image);
	params.setOutputStream(os);
	params.setRGBOrder(RGBOrder.RGB);

	params.setRedPalette(red_palette);
	params.setGreenPalette(green_palette);
	params.setBluePalette(blue_palette);

	// params.setColorPalette(colorPalette);

	params.setCompressionStrategy(CompressionStrategy.SEPARATE_CHANNELS);

	IndexedCompressor.compress(params);

	os.forceClose();

    }

}
