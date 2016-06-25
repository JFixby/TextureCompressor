
package com.jfixby.tools.gdx.texturepacker.api.indexed;

import java.io.IOException;

import com.jfixby.cmns.api.ComponentInstaller;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.image.ColorMap;
import com.jfixby.cmns.api.math.IntegerMath;

public class IndexedCompressor {

	public static final String INDEXED_COLOR_FILE_EXTENTION = ".r3-icolor";

	public static final int DEFAULT_RED_PALETTE_SIZE = (int)IntegerMath.power(2, 5);
	public static final int DEFAULT_GREEN_PALETTE_SIZE = (int)IntegerMath.power(2, 6);
	public static final int DEFAULT_BLUE_PALETTE_SIZE = (int)IntegerMath.power(2, 4);// 2^4
	public static final int DEFAULT_ALPHA_PALETTE_SIZE = (int)IntegerMath.power(2, 6);

	static private ComponentInstaller<IndexedCompressorComponent> componentInstaller = new ComponentInstaller<IndexedCompressorComponent>(
		"IndexedCompressor");

	public static final void installComponent (final IndexedCompressorComponent component_to_install) {
		componentInstaller.installComponent(component_to_install);
	}

	public static final IndexedCompressorComponent invoke () {
		return componentInstaller.invokeComponent();
	}

	public static final IndexedCompressorComponent component () {
		return componentInstaller.getComponent();
	}

	public static IndexColorCompressionParams newCompressionParams () {
		return invoke().newCompressionSpecs();
	}

	public static void compress (final IndexColorCompressionParams params) throws IOException {
		invoke().compress(params);
	}

	public static void compressFile (final File originalFile, final File outputFile) throws IOException {
		invoke().compressFile(originalFile, outputFile);
	}

	public static void deCompressFile (final File compressedFile, final File output_file) throws IOException {
		invoke().deCompressFile(compressedFile, output_file);
	}

	public static IndexColorDeCompressionParams newDeCompressionParams () {
		return invoke().newDeCompressionParams();
	}

	public static IndexColorDeCompressionResult deCompress (final IndexColorDeCompressionParams params) throws IOException {
		return invoke().deCompress(params);
	}

	public static void indexImage (final File originalFile, final File indexedFile) throws IOException {
		invoke().indexImage(originalFile, indexedFile);
	}

	public static ColorMap indexImage (final ColorMap originalImage) {
		return invoke().indexImage(originalImage);
	}

	public static ColorMap deCompress (final File compressedFile) throws IOException {
		return invoke().deCompress(compressedFile);
	}

	public static ColorIndexMap readColorIndexMap (final File compressedFile) throws IOException {
		return invoke().readColorIndexMap(compressedFile);
	}

}
