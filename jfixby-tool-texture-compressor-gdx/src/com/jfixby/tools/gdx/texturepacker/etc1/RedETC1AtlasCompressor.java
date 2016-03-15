
package com.jfixby.tools.gdx.texturepacker.etc1;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Page;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.jfixby.cmns.adopted.gdx.atlas.CompressedFokkerAtlas;
import com.jfixby.cmns.adopted.gdx.fs.ToGdxFileAdaptor;
import com.jfixby.cmns.api.debug.Debug;
import com.jfixby.cmns.api.desktop.ImageAWT;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.FileOutputStream;
import com.jfixby.cmns.api.image.EditableColorMap;
import com.jfixby.cmns.api.image.EditableGrayMap;
import com.jfixby.cmns.api.io.GZipOutputStream;
import com.jfixby.cmns.api.io.IO;
import com.jfixby.cmns.api.json.Json;
import com.jfixby.cmns.api.log.L;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1AtlasCompressionParams;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1Compressor;

public class RedETC1AtlasCompressor {

    static RedETC1AtlasCompressionResult doCompress(ETC1AtlasCompressionParams settings) throws IOException {
	Json.invoke();
	File atlasFile = Debug.checkNull("atlas_file_path_string", settings.getAtlasFile());

	L.d("compressing atlas to ETC1", atlasFile);
	GdxNativesLoader.load();

	RedETC1AtlasCompressionResult result = new RedETC1AtlasCompressionResult();

	result.setGdxAtlasFile(atlasFile);

	File atlasFolder = atlasFile.parent();

	FileHandle gdxAtlasFile = new ToGdxFileAdaptor(atlasFile);
	FileHandle gdxAtlasFolder = new ToGdxFileAdaptor(atlasFolder);
	TextureAtlas.TextureAtlasData data = new TextureAtlas.TextureAtlasData(gdxAtlasFile, gdxAtlasFolder, false);

	Array<Page> pages = data.getPages();

	String atlasData = atlasFile.readToString();

	for (int i = 0; i < pages.size; i++) {
	    Page page_i = pages.get(i);
	    ToGdxFileAdaptor gdxFile = (ToGdxFileAdaptor) page_i.textureFile;
	    File pageFile = gdxFile.getFixbyFile();
	    String oldPageFileName = pageFile.getName();
	    String pageFileName = pageFile.nameWithoutExtension();
	    String etc1PageFileName = pageFileName + ".etc1";
	    String alphaChannelname = pageFileName + CompressedFokkerAtlas.ALPHA_CHANNEL_FILE_EXTENTION;
	    File compressedPageFile = pageFile.parent().child(etc1PageFileName);
	    File alphaChannelFile = pageFile.parent().child(alphaChannelname);
	    splitAndSaveAlphaChannel(pageFile, alphaChannelFile);

	    // tell ETC1Compressor to process only related files, not
	    // the whole
	    // folder
	    ETC1Compressor.compressFile(pageFile, compressedPageFile, null, !false);

	    atlasData = atlasData.replaceAll(oldPageFileName, etc1PageFileName);

	    L.d(" page " + i, pageFile);
	    L.d(" to", compressedPageFile);

	    result.addCompressedTextureNames(oldPageFileName, etc1PageFileName);
	    if (settings.deleteOriginalPNG()) {
		pageFile.delete();
	    }

	}

	atlasFile.writeString(atlasData);

	result.setCompressedAtlasFile(atlasFile);

	return result;
    }

    private static void splitAndSaveAlphaChannel(File inputFile, File outputFile) throws IOException {
	BufferedImage awtImage = ImageAWT.readFromFile(inputFile);
	EditableColorMap colorMap = ImageAWT.newAWTColorMap(awtImage);
	EditableGrayMap alpha = colorMap.getAlpha();

	// ColorMapSpecs spces = ImageProcessing.newColorMapSpecs();
	// spces.setColorMapDimentions(colorMap.getWidth(),
	// colorMap.getHeight());
	// spces.setRed(alpha);
	// spces.setGreen(alpha);
	// spces.setBlue(alpha);
	// ColorMap outputColorMap = ImageProcessing.newColorMap(spces);
	// BufferedImage resultingImage =
	// ImageAWT.toAWTImage(outputColorMap);
	BufferedImage resultingImage = ImageAWT.toAWTImage(alpha);

	// L.d("writing", alphaChannelFile);
	FileOutputStream fileStream = outputFile.newOutputStream();
	GZipOutputStream zip = IO.newGZipStream(fileStream);

	ImageAWT.writeToStream(resultingImage, zip, "png", BufferedImage.TYPE_BYTE_GRAY);

	// File testFile = outputFile.parent().child(outputFile.getName() +
	// ".png");
	// ImageAWT.writeToFile(resultingImage, testFile, "png",
	// BufferedImage.TYPE_BYTE_GRAY);

	zip.flush();
	zip.close();
	fileStream.flush();
	fileStream.close();

	// restoreCheck(outputFile);

    }

    // private static void restoreCheck(File outputFile) throws IOException {
    // File restoredFile = outputFile.parent().child(outputFile.getName() +
    // "-restored.png");
    // FileInputStream fileStream = outputFile.newInputStream();
    // GZipInputStream zip = IO.newGZipStream(fileStream);
    // BufferedImage restoredImage = ImageAWT.readFromStream(zip);
    // fileStream.close();
    // zip.close();
    // ImageAWT.writeToFile(restoredImage, restoredFile, "png",
    // BufferedImage.TYPE_BYTE_GRAY);
    // }

}
