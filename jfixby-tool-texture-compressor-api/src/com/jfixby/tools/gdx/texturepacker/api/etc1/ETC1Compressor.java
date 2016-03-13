package com.jfixby.tools.gdx.texturepacker.api.etc1;

import java.io.IOException;

import com.jfixby.cmns.api.ComponentInstaller;
import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.file.File;

public class ETC1Compressor {
    // public static final String COMPRESSED_ATLAS_FILE_EXTENTION =
    // ".r3-compressed-atlas";
    // public static final String EXTRACTED_ALPHA_CHANNELS_FILE_EXTENTION =
    // ".r3-alpha";
    // public static final String ETC1AtlasCompression =
    // "R3.libGDX.ETC1AtlasCompression";

    static private ComponentInstaller<ETC1CompressorComponent> componentInstaller = new ComponentInstaller<ETC1CompressorComponent>(
	    "ETC1Compressor");

    public static final void installComponent(ETC1CompressorComponent component_to_install) {
	componentInstaller.installComponent(component_to_install);
    }

    public static final ETC1CompressorComponent invoke() {
	return componentInstaller.invokeComponent();
    }

    public static final ETC1CompressorComponent component() {
	return componentInstaller.getComponent();
    }

    public static void compressFile(File originalFile, File compressedFile, Color transparentColor,
	    boolean discardAlpha) {
	invoke().compressFile(originalFile, compressedFile, transparentColor, discardAlpha);
    }

    public static void deCompressFile(File compressedFile, File restoredFile) throws IOException {
	invoke().deCompressFile(compressedFile, restoredFile);
    }

    public static ETC1DeCompressionParams newDeCompressionParams() {
	return invoke().newDeCompressionParams();
    }

    public static ETC1DeCompressionResult deCompress(ETC1DeCompressionParams params) {
	return invoke().deCompress(params);
    }

    public static void compress(ETC1CompressionParams params) throws IOException {
	invoke().compress(params);
    }

    public static ETC1CompressionParams newCompressionParams() {
	return invoke().newCompressionParams();
    }

    public static ETC1AtlasCompressionParams newAtlasCompressionSettings() {
	return invoke().newAtlasCompressionParams();
    }

    public static ETC1AtlasCompressionResult compressAtlas(ETC1AtlasCompressionParams atlasCompressionSettings)
	    throws IOException {
	return invoke().compressAtlas(atlasCompressionSettings);
    }

}
