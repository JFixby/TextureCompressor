
package com.jfixby.tools.gdx.texturepacker.etc1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
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
    final Pages pages = new Pages();
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
	Page newPage = new Page(newPageFileName, w, h);
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
	this.writeInt(buffer, pages.size());
	for (int i = 0; i < pages.size(); i++) {
	    Page.writePage(buffer, pages.get(i));
	}
	buffer.flush();
	buffer.close();
	byte[] bytes = buffer.toByteArray();
	if (zip) {
	    bytes = compressZIP(bytes);
	}
	return bytes;

    }

    public static Pages deserialize(byte[] alphas_bytes, boolean zip) throws IOException {

	if (zip) {
	    alphas_bytes = unZIP(alphas_bytes);
	}

	ByteArrayInputStream input = new ByteArrayInputStream(alphas_bytes);
	Pages pages = new Pages();
	int pagesNumber = readInt(input);
	for (int i = 0; i < pagesNumber; i++) {
	    Page page = Page.readPage(input);
	    pages.add(page);
	}
	return pages;

    }

    private static byte[] unZIP(byte[] alphas_bytes) throws IOException {
	ByteArrayInputStream input = new ByteArrayInputStream(alphas_bytes);
	int len = readInt(input);
	GZIPInputStream zip = new GZIPInputStream(input);
	byte[] buf = new byte[len];
	zip.read(buf, 0, len);
	return buf;
    }

    private static int readInt(ByteArrayInputStream input) {
	final int b0 = readByte(input);
	final int b1 = readByte(input);
	final int b2 = readByte(input);
	final int b3 = readByte(input);
	final int result = (b0 << 8 * 3) | (b1 << 8 * 2) | (b2 << 8 * 1) | (b3 << 8 * 0);
	return result;
    }

    private static int readByte(ByteArrayInputStream input) {
	return input.read();
    }

    private void writeInt(ByteArrayOutputStream buffer, int value) {
	final int b0 = (value >> 8 * 3) & 0xff;
	final int b1 = (value >> 8 * 2) & 0xff;
	final int b2 = (value >> 8 * 1) & 0xff;
	final int b3 = (value >> 8 * 0) & 0xff;
	buffer.write(b0);
	buffer.write(b1);
	buffer.write(b2);
	buffer.write(b3);
    }

    private byte[] compressZIP(byte[] bytes) throws IOException {
	ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	writeInt(buffer, bytes.length);
	GZIPOutputStream zip = new GZIPOutputStream(buffer);
	zip.write(bytes);
	zip.flush();
	zip.close();
	buffer.close();
	return buffer.toByteArray();
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
