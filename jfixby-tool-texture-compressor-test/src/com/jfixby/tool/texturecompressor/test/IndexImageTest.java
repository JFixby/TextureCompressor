package com.jfixby.tool.texturecompressor.test;

import java.io.IOException;

import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class IndexImageTest {

    public static void main(String[] args) throws IOException {
	ScarabeiDesktop.deploy();
	IndexedCompressor.installComponent(new RedIndexedCompressor());

	File home = LocalFileSystem.ApplicationHome();
	File input_folder = home.child("input");
	File output_folder = home.child("indexed");
	File originalFile = input_folder.child("fox.png");
	File processing = output_folder.child("index.png");

	home.getFileSystem().copyFileToFile(originalFile, processing);
	L.d("indexing", processing);
	IndexedCompressor.indexImage(processing, processing);

    }

}
