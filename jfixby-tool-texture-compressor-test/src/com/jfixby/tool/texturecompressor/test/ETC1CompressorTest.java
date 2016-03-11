package com.jfixby.tool.texturecompressor.test;

import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.color.Colors;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.LocalFileSystem;
import com.jfixby.red.desktop.DesktopAssembler;
import com.jfixby.tools.gdx.texturepacker.etc1.GdxETC1Compressor;

public class ETC1CompressorTest {

    public static void main(String[] args) {

	DesktopAssembler.setup();

	Color transparentColor = Colors.FUCHSIA();
	File home = LocalFileSystem.ApplicationHome();
	File input_folder = home.child("input");
	File output_folder = home.child("output");
	File inputFile = input_folder.child("etc1-test.png");
	File outputFile = output_folder.child(inputFile.nameWithoutExtension() + ".etc1");
	GdxETC1Compressor.compressFile(inputFile, outputFile, transparentColor, true);

    }

}
