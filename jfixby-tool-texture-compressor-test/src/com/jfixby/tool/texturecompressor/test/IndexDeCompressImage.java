package com.jfixby.tool.texturecompressor.test;

import java.io.IOException;

import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.LocalFileSystem;
import com.jfixby.red.desktop.DesktopAssembler;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class IndexDeCompressImage {

    public static void main(String[] args) throws IOException {

	DesktopAssembler.setup();
	IndexedCompressor.installComponent(new RedIndexedCompressor());

	File home = LocalFileSystem.ApplicationHome();
	File input_folder = home.child("indexed");
	File output_folder = home.child("indexed");

	File compressedFile = input_folder.child("etc1-test.r3-icolor");

	File output_file = output_folder.child(compressedFile.nameWithoutExtension() + "-restored.png");
	IndexedCompressor.deCompressFile(compressedFile, output_file);

    }

}
