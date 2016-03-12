
package com.jfixby.tools.gdx.texturepacker.etc1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.debug.Debug;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.io.IO;
import com.jfixby.tools.gdx.texturepacker.api.etc1.AlphaChannelDeserealizator;
import com.jfixby.tools.gdx.texturepacker.api.etc1.AlphaChannelExtractionResult;
import com.jfixby.tools.gdx.texturepacker.api.etc1.AlphaChannelExtractionSettings;
import com.jfixby.tools.gdx.texturepacker.api.etc1.AlphaChannelExtractor;
import com.jfixby.tools.gdx.texturepacker.api.etc1.AlphaChannelExtractorSpecs;
import com.jfixby.tools.gdx.texturepacker.api.etc1.AlphaPages;

public class RedAlphaChannelExtractor implements AlphaChannelExtractor, AlphaChannelDeserealizator {

    private Color transparentColor;
    final RedAlphaPages pages = new RedAlphaPages();
    private boolean zip;

    public RedAlphaChannelExtractor(AlphaChannelExtractorSpecs alphaExtractorSpecs) {
	zip = alphaExtractorSpecs.useZIPCompression();

    }

    public RedAlphaChannelExtractor() {
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
	RedAlphaPage newPage = new RedAlphaPage(newPageFileName, w, h);
	pages.add(newPage);
    }

    public void addAlphaValue(int alpha, int x, int y) {
	pages.get(pages.size() - 1).addAlphaValue(alpha, x, y);
    }

    public void endFile(String newPageFileName) {
	pages.get(pages.size() - 1).checkValid(newPageFileName);
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
	OutputStream os = null;
	ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	os = buffer;
	if (zip) {
	    os = new GZIPOutputStream(os);
	}
	IO.writeInt(os, pages.size());
	for (int i = 0; i < pages.size(); i++) {
	    RedAlphaPage.writePage(os, pages.get(i));
	}
	os.close();
	buffer.close();
	return buffer.toByteArray();
    }

    public RedAlphaPages deserialize(byte[] alphas_bytes, boolean zip) throws IOException {

	InputStream is = null;

	ByteArrayInputStream input = new ByteArrayInputStream(alphas_bytes);
	is = input;

	if (zip) {
	    is = new GZIPInputStream(is);
	}

	RedAlphaPages pages = new RedAlphaPages();
	int pagesNumber = IO.readInt(is);
	for (int i = 0; i < pagesNumber; i++) {
	    RedAlphaPage page = RedAlphaPage.readPage(is);
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
	    RedAlphaPage page = (RedAlphaPage) pages.get(i);
	    File png_file = folder.child(page.getName() + "-alpha.png");
	    page.saveAsPng(png_file);
	}
    }

}
