package com.jfixby.tools.gdx.texturepacker.api.etc1;

import java.io.IOException;

import com.jfixby.cmns.api.ComponentInstaller;

public class ETC1Compressor {
    public static final String COMPRESSED_ATLAS_FILE_EXTENTION = ".r3-compressed-atlas";
    public static final String EXTRACTED_ALPHA_CHANNELS_FILE_EXTENTION = ".r3-alpha";
    public static final String ETC1AtlasCompression = "R3.libGDX.ETC1AtlasCompression";

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

    public static ETC1AtlasCompressorSettings newAtlasCompressionSettings() {
	return invoke().newAtlasCompressionSettings();
    }

    public static ETC1AtlasCompressionResult compressAtlas(ETC1AtlasCompressorSettings atlasCompressionSettings)
	    throws IOException {
	return invoke().compress(atlasCompressionSettings);
    }

}
