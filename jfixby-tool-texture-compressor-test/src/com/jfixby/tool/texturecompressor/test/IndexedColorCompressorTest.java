package com.jfixby.tool.texturecompressor.test;

import com.jfixby.red.desktop.DesktopAssembler;
import com.jfixby.tools.gdx.texturepacker.api.indexed.IndexedCompressor;
import com.jfixby.tools.texturepacker.red.indexed.RedIndexedCompressor;

public class IndexedColorCompressorTest {

    public static void main(String[] args) {
	DesktopAssembler.setup();
	IndexedCompressor.installComponent(new RedIndexedCompressor());

    }

}
