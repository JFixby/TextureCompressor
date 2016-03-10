
package com.jfixby.tools.gdx.texturepacker.etc1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.debug.Debug;
import com.jfixby.cmns.api.file.File;
import com.jfixby.tools.gdx.texturepacker.api.etc1.AlphaChannelExtractionResult;
import com.jfixby.tools.gdx.texturepacker.api.etc1.AlphaChannelExtractionSettings;
import com.jfixby.tools.gdx.texturepacker.api.etc1.AlphaChannelExtractor;
import com.jfixby.tools.gdx.texturepacker.api.etc1.AlphaChannelExtractorSpecs;

class RedAlphaChannelExtractor implements AlphaChannelExtractor {

    private Color transparentColor;
    final ArrayList<RedAlphaInfoPage> pages = new ArrayList<RedAlphaInfoPage>();
    private boolean zip;

    public RedAlphaChannelExtractor(AlphaChannelExtractorSpecs alphaExtractorSpecs) {
	zip = alphaExtractorSpecs.useZIPCompression();

    }

    private static void extractAlphaChannel(File file, RedAlphaChannelExtractor alphaInfo, String newPageFileName) {
	FileHandle pageFile = new FileHandle(file.toJavaFile());
	Pixmap pixmap = new Pixmap(pageFile);
	int W = pixmap.getWidth();
	int H = pixmap.getHeight();
	alphaInfo.beginFile(newPageFileName, W, H);
	for (int x = 0; x < W; x++) {
	    for (int y = 0; y < H; y++) {
		int value = pixmap.getPixel(x, y);
		int alpha = value & 0x000000ff;
		alphaInfo.addAlphaValue(alpha);
	    }
	}
	alphaInfo.endFile(newPageFileName);
	pixmap.dispose();
    }

    public void setTransparentColor(Color transparentColor) {
	this.transparentColor = transparentColor;
    }

    public void beginFile(String newPageFileName, int w, int h) {
	RedAlphaInfoPage newPage = new RedAlphaInfoPage(newPageFileName, w, h);
	pages.add(newPage);
    }

    public void addAlphaValue(int alpha) {
	pages.get(pages.size() - 1).addAlphaValue(alpha);
    }

    public void endFile(String newPageFileName) {
	pages.get(pages.size() - 1).checkValid(newPageFileName);
    }

    public byte[] toByteArray() throws IOException {
	ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	for (int i = 0; i < pages.size(); i++) {
	    writePage(buffer, pages.get(i));
	}
	buffer.flush();
	buffer.close();
	byte[] bytes = buffer.toByteArray();
	if (zip) {
	    bytes = compressZIP(bytes);
	}
	return bytes;

    }

    public static RedAlphaChannelExtractor deserialize(byte[] alphas_bytes) {
	throw new Error();
    }

    private byte[] compressZIP(byte[] bytes) throws IOException {
	ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	GZIPOutputStream zip = new GZIPOutputStream(buffer);
	zip.write(bytes);
	zip.flush();
	zip.close();
	buffer.close();
	return buffer.toByteArray();
    }

    private void writePage(ByteArrayOutputStream buffer, RedAlphaInfoPage alphaInfoPage) throws IOException {
	alphaInfoPage.writeTo(buffer);
    }

    @Override
    public AlphaChannelExtractionResult process(AlphaChannelExtractionSettings settings) {
	GdxNativesLoader.load();
	RedAlphaChannelExtractionResult result = new RedAlphaChannelExtractionResult();

	File input_png = settings.getInputFile();
	input_png = Debug.checkNull("input png", input_png);

	String tag = settings.getNameTag();
	tag = Debug.checkNull("name tag", tag);
	tag = Debug.checkEmpty("name tag", tag);

	extractAlphaChannel(input_png, this, tag);

	return result;
    }

    @Override
    public AlphaChannelExtractionSettings newExtractionSettings() {
	return new RedAlphaChannelExtractionSettings();
    }

    @Override
    public byte[] serialize() throws IOException {
	return this.toByteArray();
    }

    // RedAlphaChannelExtractor alphaInfo = new RedAlphaChannelExtractor();
    // alphaInfo.setTransparentColor(transparentColor);
    // collectAlphaInfo(pageFile, alphaInfo, newPageFileName);
    // byte[] alphaInfoBytes = alphaInfo.toByteArray();
    // String alphaInfoFileName = atlasFile.getName() + ".alpha-info";
    // File alphaInfoFile = atlasFolder.child(alphaInfoFileName);
    // alphaInfoFile.writeBytes(alphaInfoBytes);

}
