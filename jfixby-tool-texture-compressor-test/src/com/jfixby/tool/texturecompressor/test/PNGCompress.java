package com.jfixby.tool.texturecompressor.test;

import java.io.IOException;

import com.jfixby.scarabei.api.color.Color;
import com.jfixby.scarabei.api.color.Colors;
import com.jfixby.scarabei.api.color.CustomColor;
import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.desktop.ImageAWT;
import com.jfixby.scarabei.api.err.Err;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.image.ColorMap;
import com.jfixby.scarabei.api.image.ColoredλImage;
import com.jfixby.scarabei.api.image.ImageProcessing;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.tools.gdx.texturepacker.api.indexed.ColorIndexMap;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class PNGCompress {

    public static void main(String[] args) throws IOException {
	ScarabeiDesktop.deploy();
	IndexedCompressor.installComponent(new RedIndexedCompressor());

	File home = LocalFileSystem.ApplicationHome();
	File input_folder = home.child("indexed");
	File output_folder = home.child("indexed");

	File originalFile = input_folder.child("fox.png");

	File compressedFile = output_folder
		.child(originalFile.nameWithoutExtension() + IndexedCompressor.INDEXED_COLOR_FILE_EXTENTION);

	File pngEncodedFile = output_folder.child(originalFile.nameWithoutExtension() + "-encoded.png");
	File pngDecodedFile = output_folder.child(originalFile.nameWithoutExtension() + "-decoded.png");

	final ColorIndexMap indexMap = IndexedCompressor.readColorIndexMap(compressedFile);
	final int width = indexMap.getWidth();
	final int height = indexMap.getHeight();

	final ColoredλImage lambda = new ColoredλImage() {
	    @Override
	    public Color valueAt(float x, float y) {
		int color_index = indexMap.getColorIndexAt(x, y);
		if (color_index > 256 * 256) {
		    Err.reportError("bad color index: " + color_index);
		}
		CustomColor png_data = Colors.newColor(color_index << 8);
		png_data.setAlpha(1);

		return png_data;
	    }
	};
	ColorMap encoded = ImageProcessing.newColorMap(lambda, width, height);
	ImageAWT.writeToFile(encoded, pngEncodedFile, "png");

	final ColorMap reencoded = ImageAWT.readAWTColorMap(pngEncodedFile);

	final ColoredλImage decoded_lambda = new ColoredλImage() {
	    @Override
	    public Color valueAt(float x, float y) {
		Color encoded_color = reencoded.valueAt(x, y);

		int color_index = encoded_color.toInteger();
		color_index = color_index & 0x00ffffff;
		color_index = color_index >> 8;
		if (color_index > 256 * 256 || color_index < 0) {
		    Err.reportError("bad color index: " + color_index);
		}
		Color decoded_color = indexMap.colorOf(color_index);
		// decoded_color = decoded_color.customize().setAlpha(1);

		return decoded_color;
	    }
	};

	ColorMap decoded = ImageProcessing.newColorMap(decoded_lambda, width, height);
	ImageAWT.writeToFile(decoded, pngDecodedFile, "png");

	L.d("EXIT");
    }

}
