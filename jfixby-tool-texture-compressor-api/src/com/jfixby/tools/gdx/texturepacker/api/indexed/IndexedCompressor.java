package com.jfixby.tools.gdx.texturepacker.api.indexed;

import com.jfixby.cmns.api.ComponentInstaller;

public class IndexedCompressor {

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

}
