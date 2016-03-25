package com.jfixby.tools.gdx.texturepacker.api.indexed;

import java.io.IOException;

import com.jfixby.cmns.api.ComponentInstaller;

public class IndexedCompressor {

    public static final String INDEXED_COLOR_FILE_EXTENTION = ".r3-icolor";

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

}
