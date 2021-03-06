package com.jfixby.tool.texturecompressor.test;

import java.io.IOException;

import com.jfixby.scarabei.api.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.api.file.FilesList;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class IndexAll {

    public static void main(String[] args) throws IOException {
	ScarabeiDesktop.deploy();
	IndexedCompressor.installComponent(new RedIndexedCompressor());

	File home = LocalFileSystem.ApplicationHome();
	File input_folder = home.child("input");
	File output_folder = home.child("indexed");
	output_folder.makeFolder();
	FilesList inputFiles = input_folder.listDirectChildren();

	for (int i = 0; i < inputFiles.size(); i++) {
	    File file_i = inputFiles.getElementAt(i);
	    if (file_i.extensionIs("png")) {

		L.d("processing", file_i);
		LocalFileSystem.copyFileToFolder(file_i, output_folder);
		IndexedCompressor.indexImage(file_i, output_folder.child(file_i.nameWithoutExtension() + "-"
			+ IndexedCompressor.DEFAULT_RED_PALETTE_SIZE + "-indexed.png"));
	    }
	}
	L.d("done;");

    }

}
