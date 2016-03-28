package com.jfixby.tools.gdx.texturepacker.api.indexed;

import java.io.IOException;

import com.jfixby.cmns.api.ComponentInstaller;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.image.ColorMap;

public class IndexedCompressor {

    public static final String INDEXED_COLOR_FILE_EXTENTION = ".r3-icolor";

    public static final int DEFAULT_RED_PALETTE_SIZE = 64;
    public static final int DEFAULT_GREEN_PALETTE_SIZE = 64;
    public static final int DEFAULT_BLUE_PALETTE_SIZE = 32;
    public static final int DEFAULT_ALPHA_PALETTE_SIZE = 128;

    static private ComponentInstaller<IndexedCompressorComponent> componentInstaller = new ComponentInstaller<IndexedCompressorComponent>(
	    "IndexedCompressor");

    public static final void installComponent(IndexedCompressorComponent component_to_install) {
	componentInstaller.installComponent(component_to_install);
    }

    public static final IndexedCompressorComponent invoke() {
	return componentInstaller.invokeComponent();
    }

    public static final IndexedCompressorComponent component() {
	return componentInstaller.getComponent();
    }

    public static IndexColorCompressionParams newCompressionParams() {
	return invoke().newCompressionSpecs();
    }

    public static void compress(IndexColorCompressionParams params) throws IOException {
	invoke().compress(params);
    }

    public static void compressFile(File originalFile, File output_file) throws IOException {
	invoke().compressFile(originalFile, output_file);
    }

    public static void deCompressFile(File compressedFile, File output_file) throws IOException {
	invoke().deCompressFile(compressedFile, output_file);
    }

    public static IndexColorDeCompressionParams newDeCompressionParams() {
	return invoke().newDeCompressionParams();
    }

    public static IndexColorDeCompressionResult deCompress(IndexColorDeCompressionParams params) throws IOException {
	return invoke().deCompress(params);
    }

    public static void indexImage(File originalFile, File indexedFile) throws IOException {
	invoke().indexImage(originalFile, indexedFile);
    }

    public static ColorMap indexImage(ColorMap originalImage) {
	return invoke().indexImage(originalImage);
    }

}
