package com.jfixby.tool.texturecompressor.test;

import java.io.IOException;

import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.color.Colors;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.LocalFileSystem;
import com.jfixby.red.desktop.DesktopAssembler;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1Compressor;
import com.jfixby.tools.gdx.texturepacker.etc1.GdxETC1;

public class ETC1CompressorTest {

    public static void main(String[] args) throws IOException {

	DesktopAssembler.setup();
	ETC1Compressor.installComponent(new GdxETC1());
	Color transparentColor = Colors.FUCHSIA();
	File home = LocalFileSystem.ApplicationHome();
	File input_folder = home.child("etc1");
	File output_folder = home.child("etc1");
	File originalFile = input_folder.child("original_image.png");
	File restoredFile = input_folder.child("restored_image.png");

	File compressedFile = output_folder.child("etc1_compressed.etc1");
	boolean discardAlpha = !false;
	ETC1Compressor.compressFile(originalFile, compressedFile, transparentColor, discardAlpha);
	ETC1Compressor.deCompressFile(compressedFile, restoredFile);

    }

}
