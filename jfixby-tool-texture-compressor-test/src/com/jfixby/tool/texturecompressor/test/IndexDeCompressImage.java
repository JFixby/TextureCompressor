package com.jfixby.tool.texturecompressor.test;

import java.io.IOException;

import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class IndexDeCompressImage {

    public static void main(String[] args) throws IOException {

	ScarabeiDesktop.deploy();
	IndexedCompressor.installComponent(new RedIndexedCompressor());

	File home = LocalFileSystem.ApplicationHome();
	File input_folder = home.child("indexed");
	File output_folder = home.child("indexed");

	File compressedFile = input_folder.child("fox.r3-icolor");
	L.d("restoring", compressedFile);

	File output_file = output_folder.child(compressedFile.nameWithoutExtension() + "-restored.png");
	IndexedCompressor.deCompressFile(compressedFile, output_file);
	L.d("writing", output_file);

    }

}
