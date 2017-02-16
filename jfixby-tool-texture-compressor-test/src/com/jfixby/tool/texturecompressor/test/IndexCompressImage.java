package com.jfixby.tool.texturecompressor.test;

import java.io.IOException;

import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class IndexCompressImage {

    public static void main(String[] args) throws IOException {

	ScarabeiDesktop.deploy();
	IndexedCompressor.installComponent(new RedIndexedCompressor());

	File home = LocalFileSystem.ApplicationHome();
	File input_folder = home.child("input");
	File output_folder = home.child("indexed");

	File originalFile = input_folder.child("fox.png");

	L.d("compressing", originalFile);

	File output_file = output_folder
		.child(originalFile.nameWithoutExtension() + IndexedCompressor.INDEXED_COLOR_FILE_EXTENTION);
	IndexedCompressor.compressFile(originalFile, output_file);

	L.d("write", output_file);

    }

}
