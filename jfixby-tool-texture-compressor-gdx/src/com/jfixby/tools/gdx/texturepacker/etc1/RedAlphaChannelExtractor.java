
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
import com.jfixby.cmns.api.err.Err;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.io.IO;
import com.jfixby.tools.gdx.texturepacker.api.etc1.AlphaChannelExtractionResult;
import com.jfixby.tools.gdx.texturepacker.api.etc1.AlphaChannelExtractionSettings;
import com.jfixby.tools.gdx.texturepacker.api.etc1.AlphaChannelExtractor;
import com.jfixby.tools.gdx.texturepacker.api.etc1.AlphaChannelExtractorSpecs;

public class RedAlphaChannelExtractor implements AlphaChannelExtractor {

    private Color transparentColor;
    final AlphaPages pages = new AlphaPages();
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
		alphaInfo.addAlphaValue(alpha, x, y);
	    }
	}
	alphaInfo.endFile(newPageFileName);
	pixmap.dispose();
    }

    public void setTransparentColor(Color transparentColor) {
	this.transparentColor = transparentColor;
    }

    public void beginFile(String newPageFileName, int w, int h) {
	AlphaPage newPage = new AlphaPage(newPageFileName, w, h);
	pages.add(newPage);
    }

    public void addAlphaValue(int alpha, int x, int y) {
	pages.get(pages.size() - 1).addAlphaValue(alpha, x, y);
    }

    public void endFile(String newPageFileName) {
	pages.get(pages.size() - 1).checkValid(newPageFileName);
    }

    private static byte[] unZIP(byte[] alphas_bytes) throws IOException {
	Err.reportError("Zip is not properly working yet");
	ByteArrayInputStream input = new ByteArrayInputStream(alphas_bytes);
	int len = IO.readInt(input);
	GZIPInputStream zip = new GZIPInputStream(input);
	byte[] buf = new byte[len];
	zip.read(buf, 0, len);
	return buf;
    }

    private byte[] compressZIP(byte[] bytes) throws IOException {
	Err.reportError("Zip is not properly working yet");
	ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	IO.writeInt(buffer, bytes.length);
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
	ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	IO.writeInt(buffer, pages.size());
	for (int i = 0; i < pages.size(); i++) {
	    AlphaPage.writePage(buffer, pages.get(i));
	}
	buffer.flush();
	buffer.close();
	byte[] bytes = buffer.toByteArray();
	if (zip) {
	    bytes = compressZIP(bytes);
	}
	return bytes;
    }

    public static AlphaPages deserialize(byte[] alphas_bytes, boolean zip) throws IOException {

	if (zip) {
	    alphas_bytes = unZIP(alphas_bytes);
	}

	ByteArrayInputStream input = new ByteArrayInputStream(alphas_bytes);
	AlphaPages pages = new AlphaPages();
	int pagesNumber = IO.readInt(input);
	for (int i = 0; i < pagesNumber; i++) {
	    AlphaPage page = AlphaPage.readPage(input);
	    pages.add(page);
	}
	return pages;

    }

    @Override
    public void saveAsPng(File folder) {
	savePagesAsPNG(folder, this.pages);
    }

    public static void savePagesAsPNG(File folder, AlphaPages pages) {
	for (int i = 0; i < pages.size(); i++) {
	    AlphaPage page = pages.get(i);
	    File png_file = folder.child(page.getName() + "-alpha.png");
	    page.saveAsPng(png_file);
	}
    }

    // RedAlphaChannelExtractor alphaInfo = new RedAlphaChannelExtractor();
    // alphaInfo.setTransparentColor(transparentColor);
    // collectAlphaInfo(pageFile, alphaInfo, newPageFileName);
    // byte[] alphaInfoBytes = alphaInfo.toByteArray();
    // String alphaInfoFileName = atlasFile.getName() + ".alpha-info";
    // File alphaInfoFile = atlasFolder.child(alphaInfoFileName);
    // alphaInfoFile.writeBytes(alphaInfoBytes);

}
