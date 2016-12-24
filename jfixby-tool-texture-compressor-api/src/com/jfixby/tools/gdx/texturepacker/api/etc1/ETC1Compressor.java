
package com.jfixby.tools.gdx.texturepacker.api.etc1;

import java.io.IOException;

import com.jfixby.scarabei.api.ComponentInstaller;
import com.jfixby.scarabei.api.color.Color;
import com.jfixby.scarabei.api.file.File;

public class ETC1Compressor {

	static private ComponentInstaller<ETC1CompressorComponent> componentInstaller = new ComponentInstaller<ETC1CompressorComponent>(
		"ETC1Compressor");

	public static final void installComponent (final ETC1CompressorComponent component_to_install) {
		componentInstaller.installComponent(component_to_install);
	}

	public static final ETC1CompressorComponent invoke () {
		return componentInstaller.invokeComponent();
	}

	public static final ETC1CompressorComponent component () {
		return componentInstaller.getComponent();
	}

	public static void compressFile (final File originalFile, final File compressedFile, final Color transparentColor,
		final boolean discardAlpha) {
		invoke().compressFile(originalFile, compressedFile, transparentColor, discardAlpha);
	}

	public static void deCompressFile (final File compressedFile, final File restoredFile) throws IOException {
		invoke().deCompressFile(compressedFile, restoredFile);
	}

	public static ETC1DeCompressionParams newDeCompressionParams () {
		return invoke().newDeCompressionParams();
	}

	public static ETC1DeCompressionResult deCompress (final ETC1DeCompressionParams params) throws IOException {
		return invoke().deCompress(params);
	}

	public static void compress (final ETC1CompressionParams params) throws IOException {
		invoke().compress(params);
	}

	public static ETC1CompressionParams newCompressionParams () {
		return invoke().newCompressionParams();
	}

	public static ETC1AtlasCompressionParams newAtlasCompressionSettings () {
		return invoke().newAtlasCompressionParams();
	}

	public static ETC1AtlasCompressionResult compressAtlas (final ETC1AtlasCompressionParams atlasCompressionSettings)
		throws IOException {
		return invoke().compressAtlas(atlasCompressionSettings);
	}

}
