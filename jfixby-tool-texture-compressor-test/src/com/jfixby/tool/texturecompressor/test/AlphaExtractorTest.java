package com.jfixby.tool.texturecompressor.test;

import java.io.IOException;

import com.jfixby.cmns.api.file.ChildrenList;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.LocalFileSystem;
import com.jfixby.cmns.api.log.L;
import com.jfixby.red.desktop.DesktopAssembler;
import com.jfixby.tools.gdx.texturepacker.api.etc1.AlphaChannelExtractionResult;
import com.jfixby.tools.gdx.texturepacker.api.etc1.AlphaChannelExtractionSettings;
import com.jfixby.tools.gdx.texturepacker.api.etc1.AlphaChannelExtractor;
import com.jfixby.tools.gdx.texturepacker.api.etc1.AlphaChannelExtractorSpecs;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1Compressor;
import com.jfixby.tools.gdx.texturepacker.etc1.AlphaPages;
import com.jfixby.tools.gdx.texturepacker.etc1.RedAlphaChannelExtractor;
import com.jfixby.tools.gdx.texturepacker.etc1.RedETC1AtlasCompressor;

public class AlphaExtractorTest {

    public static void main(String[] args) throws IOException {

	DesktopAssembler.setup();
	ETC1Compressor.installComponent(new RedETC1AtlasCompressor());

	File home = LocalFileSystem.ApplicationHome();
	File input_folder = home.child("sprites");
	File output_folder = home.child("output");

	ChildrenList textures = input_folder.listChildren().filter(file -> file.extensionIs(".png"));

	AlphaChannelExtractorSpecs alphaExtractorSpecs = ETC1Compressor.newAlphaChannelExtractorSpecs();
	boolean zip = !false;
	alphaExtractorSpecs.setUseZIPCompression(zip);

	AlphaChannelExtractor alphaExtractor = ETC1Compressor.newAlphaChannelExtractor(alphaExtractorSpecs);

	for (int i = 0; i < textures.size(); i++) {
	    File input_png = textures.getElementAt(i);
	    AlphaChannelExtractionSettings settings = alphaExtractor.newExtractionSettings();
	    settings.setInputFile(input_png);
	    String name = input_png.getName();
	    settings.setNameTag(name);
	    AlphaChannelExtractionResult result = alphaExtractor.process(settings);
	    result.print();
	}

	byte[] bytes = alphaExtractor.serialize();
	File output_file = output_folder.child("AlphaExtractorTest.file");
	output_file.writeBytes(bytes);
	L.d("writing " + bytes.length / 1024 + " kBytes", output_file);

	alphaExtractor.saveAsPng(output_file.parent());

	AlphaPages deserialized = RedAlphaChannelExtractor.deserialize(bytes, zip);
	File out2 = output_folder.child("second");
	out2.makeFolder();
	RedAlphaChannelExtractor.savePagesAsPNG(out2, deserialized);

    }

}
