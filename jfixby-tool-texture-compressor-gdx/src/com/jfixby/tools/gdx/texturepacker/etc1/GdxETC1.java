
package com.jfixby.tools.gdx.texturepacker.etc1;

import java.io.IOException;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.ETC1;
import com.badlogic.gdx.graphics.glutils.ETC1Data;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.color.Colors;
import com.jfixby.cmns.api.color.CustomColor;
import com.jfixby.cmns.api.desktop.ImageAWT;
import com.jfixby.cmns.api.err.Err;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.FileInputStream;
import com.jfixby.cmns.api.file.FileOutputStream;
import com.jfixby.cmns.api.image.ArrayColorMap;
import com.jfixby.cmns.api.image.ArrayColorMapSpecs;
import com.jfixby.cmns.api.image.ColorMap;
import com.jfixby.cmns.api.image.EditableColorMap;
import com.jfixby.cmns.api.image.ImageProcessing;
import com.jfixby.cmns.api.io.IO;
import com.jfixby.cmns.api.io.InputStream;
import com.jfixby.cmns.api.io.OutputStream;
import com.jfixby.cmns.api.log.L;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1AtlasCompressionParams;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1AtlasCompressionResult;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1CompressionParams;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1Compressor;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1CompressorComponent;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1DeCompressionParams;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1DeCompressionResult;

public class GdxETC1 implements ETC1CompressorComponent {
    @Override
    public void compressFile(File inputFile, File outputFile, Color transparentColor, boolean discardAlpha) {
	FileOutputStream os = null;
	try {
	    EditableColorMap image = ImageAWT.readAWTColorMap(inputFile);
	    ETC1CompressionParams params = ETC1Compressor.newCompressionParams();
	    params.setInputImage(image);

	    L.d("writing", outputFile);
	    os = outputFile.newOutputStream();

	    params.setOutputStream(os);
	    params.setTransparentColor(transparentColor);
	    params.setDiscardAlpha(discardAlpha);

	    ETC1Compressor.compress(params);

	    os.flush();

	} catch (IOException e) {
	    Err.reportError(e);
	} finally {
	    IO.forceClose(os);
	}
    }

    @Override
    public GdxETC1CompressionParams newCompressionParams() {
	return new GdxETC1CompressionParams();
    }

    @Override
    public ETC1DeCompressionParams newDeCompressionParams() {
	return new GdxETC1DeCompressionParams();
    }

    @Override
    public void compress(ETC1CompressionParams params) throws IOException {
	ColorMap image = params.getInputImage();
	OutputStream os = params.getOutputStream();
	Color transparent = params.getTransparentColor();
	if (transparent == null) {
	    transparent = Colors.FUCHSIA();
	}
	boolean discardAlpha = params.discardAlpha();

	int width = image.getWidth();
	int height = image.getHeight();

	com.badlogic.gdx.graphics.Color gdxColor = new com.badlogic.gdx.graphics.Color(transparent.red(),
		transparent.green(), transparent.blue(), transparent.alpha());
	GdxNativesLoader.load();
	if (discardAlpha) {
	    Pixmap.setBlending(Blending.None);
	} else {
	    Pixmap.setBlending(Blending.SourceOver);
	}
	Pixmap pixmap = new Pixmap(width, height, Format.RGB888);
	pixmap.setColor(gdxColor);
	pixmap.fill();
	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {
		Color color = image.valueAt(x, y);
		float alpha = color.alpha();
		if (discardAlpha) {
		    alpha = 1f;
		}
		gdxColor.set(color.red(), color.green(), color.blue(), alpha);
		int intColor = com.badlogic.gdx.graphics.Color.rgba8888(gdxColor);
		pixmap.drawPixel(x, y, intColor);
	    }
	}
	ETC1Data pkm = ETC1.encodeImagePKM(pixmap);

	java.io.OutputStream javaStream = os.toJavaOutputStream();
	try {
	    pkm.write(javaStream);
	    javaStream.flush();
	} catch (Exception e) {
	    throw new IOException(e);
	} finally {
	    pkm.dispose();
	    IO.forceClose(os);
	}
    }

    @Override
    public void deCompressFile(File compressedFile, File restoredFile) throws IOException {
	ETC1DeCompressionParams params = ETC1Compressor.newDeCompressionParams();
	FileInputStream is = compressedFile.newInputStream();
	params.setInputStream(is);

	ETC1DeCompressionResult result = ETC1Compressor.deCompress(params);
	is.close();

	ColorMap image = result.getImage();
	L.d("writing", restoredFile);
	ImageAWT.writeToFile(image, restoredFile, "png");
    }

    @Override
    public ETC1DeCompressionResult deCompress(ETC1DeCompressionParams params) {
	GdxETC1DeCompressionResult result = new GdxETC1DeCompressionResult();
	InputStream inputStream = params.getInputStream();
	java.io.InputStream javaInputStream = inputStream.toJavaInputStream();

	ETC1Data etc1Data = ETC1Data.read(javaInputStream);

	GdxNativesLoader.load();
	Pixmap etc1Pixmap = ETC1.decodeImage(etc1Data, Format.RGB888);
	etc1Data.dispose();

	int width = etc1Pixmap.getWidth();
	int height = etc1Pixmap.getHeight();
	ArrayColorMapSpecs lambda_specs = ImageProcessing.newArrayColorMapSpecs();
	lambda_specs.setWidth(width);
	lambda_specs.setHeight(height);

	ArrayColorMap image = ImageProcessing.newArrayColorMap(lambda_specs);
	com.badlogic.gdx.graphics.Color gdxColor = new com.badlogic.gdx.graphics.Color();

	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {
		int intColor = etc1Pixmap.getPixel(x, y);

		com.badlogic.gdx.graphics.Color.rgba8888ToColor(gdxColor, intColor);
		CustomColor color = Colors.newColor();
		color.setAlpha(gdxColor.a);
		color.setRed(gdxColor.r);
		color.setGreen(gdxColor.g);
		color.setBlue(gdxColor.b);

		image.setValue(x, y, color);

	    }
	}

	result.setImage(image);

	return result;

    }

    @Override
    public ETC1AtlasCompressionParams newAtlasCompressionParams() {
	return new RedETC1AtlasCompressionParams();
    }

    @Override
    public ETC1AtlasCompressionResult compressAtlas(ETC1AtlasCompressionParams params) throws IOException {
	return RedETC1AtlasCompressor.doCompress(params);
    }

}
