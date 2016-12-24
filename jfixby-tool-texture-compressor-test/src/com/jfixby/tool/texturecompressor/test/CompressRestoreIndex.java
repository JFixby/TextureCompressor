package com.jfixby.tool.texturecompressor.test;

import java.io.IOException;

import com.jfixby.scarabei.api.desktop.DesktopSetup;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class CompressRestoreIndex {

    public static void main(String[] args) throws IOException {
	DesktopSetup.deploy();
	IndexedCompressor.installComponent(new RedIndexedCompressor());

	File home = LocalFileSystem.ApplicationHome();
	File input_folder = home.child("indexed");
	File output_folder = home.child("indexed");

	File originalFile = input_folder.child("fox.png");

	File compressedFile = output_folder
		.child(originalFile.nameWithoutExtension() + IndexedCompressor.INDEXED_COLOR_FILE_EXTENTION);
	IndexedCompressor.compressFile(originalFile, compressedFile);

	L.d("write", compressedFile);

	L.d("restoring", compressedFile);

	File restoredFile = output_folder.child(originalFile.nameWithoutExtension() + "-restored.png");
	IndexedCompressor.deCompressFile(compressedFile, restoredFile);
	L.d("writing", restoredFile);

	File indexedFile = output_folder.child(originalFile.nameWithoutExtension() + "-indexed.png");
	IndexedCompressor.indexImage(originalFile, indexedFile);

    }

}
