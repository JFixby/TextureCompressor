
package com.jfixby.tools.gdx.texturepacker.etc1;

import java.io.IOException;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Page;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.jfixby.cmns.adopted.gdx.fs.ToGdxFileAdaptor;
import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.color.Colors;
import com.jfixby.cmns.api.debug.Debug;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.json.Json;
import com.jfixby.cmns.api.log.L;
import com.jfixby.tools.gdx.texturepacker.api.etc1.CompressedAtlasDescriptor;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1AtlasCompressionResult;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1AtlasCompressorSettings;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1Compressor;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1CompressorComponent;

public class RedETC1AtlasCompressor implements ETC1CompressorComponent {

    public static RedETC1AtlasCompressorSettings newCompressionSettings() {
	return new RedETC1AtlasCompressorSettings();
    }

    static RedETC1AtlasCompressionResult doCompress(ETC1AtlasCompressorSettings settings) throws IOException {
	Json.invoke();
	File atlasFile = Debug.checkNull("atlas_file_path_string", settings.getAtlasFile());

	L.d("compressing atlas to ETC1", atlasFile);
	GdxNativesLoader.load();

	RedETC1AtlasCompressionResult result = new RedETC1AtlasCompressionResult();

	CompressedAtlasDescriptor outputAtlas = new CompressedAtlasDescriptor();

	result.setGdxAtlasFile(atlasFile);

	File atlasFolder = atlasFile.parent();

	FileHandle gdxAtlasFile = new ToGdxFileAdaptor(atlasFile);
	FileHandle gdxAtlasFolder = new ToGdxFileAdaptor(atlasFolder);
	TextureAtlas.TextureAtlasData data = new TextureAtlas.TextureAtlasData(gdxAtlasFile, gdxAtlasFolder, false);

	String outputAtlasName = atlasFile.nameWithoutExtension() + ETC1Compressor.COMPRESSED_ATLAS_FILE_EXTENTION;
	File outputAtlasFile = atlasFolder.child(outputAtlasName);

	Array<Page> pages = data.getPages();

	String atlasData = atlasFile.readToString();

	Color transparentColor = settings.getTransparentColor();

	boolean removeAlpha = settings.removeAlpha();

	transparentColor = checkNullCollorSetDefault(transparentColor);
	result.setTransparentColor(transparentColor);

	for (int i = 0; i < pages.size; i++) {
	    Page page_i = pages.get(i);
	    ToGdxFileAdaptor gdxFile = (ToGdxFileAdaptor) page_i.textureFile;
	    File pageFile = gdxFile.getFixbyFile();
	    String oldPageFileName = pageFile.getName();
	    String pageFileName = pageFile.nameWithoutExtension();
	    String newPageFileName = pageFileName + ".etc1";

	    // tell ETC1Compressor to process only related files, not the whole
	    // folder
	    compressTexture(atlasFolder, oldPageFileName, newPageFileName, transparentColor, removeAlpha);

	    atlasData = atlasData.replaceAll(oldPageFileName, newPageFileName);

	    File compressedPageFile = pageFile.parent().child(newPageFileName);
	    L.d("  page " + i, pageFile);
	    L.d("   to", compressedPageFile);

	    result.addCompressedTextureNames(oldPageFileName, newPageFileName);
	    if (settings.deleteOriginalPNG()) {
		pageFile.delete();
	    }

	}

	atlasFile.writeString(atlasData);

	outputAtlas.gdx_atlas_file_name = atlasFile.getName();
	outputAtlas.compression_method = ETC1Compressor.ETC1AtlasCompression;
	// outputAtlas.alpha_channes_are_zip_compressed = useZip;

	outputAtlasFile.writeString(Json.serializeToString(outputAtlas));
	result.setCompressedAtlasFile(outputAtlasFile);

	return result;
    }

    private static Color checkNullCollorSetDefault(Color color) {
	if (color == null) {
	    return Colors.FUCHSIA();
	}
	return color;
    }

    private static void compressTexture(File atlasFolder, String oldPageFileName, String newPageFileName,
	    Color transparentColor, boolean removeAlpha) {
	File oldPageFile = atlasFolder.child(oldPageFileName);
	File newPageFile = atlasFolder.child(newPageFileName);
	GdxETC1Compressor.compressFile(oldPageFile, newPageFile, transparentColor, removeAlpha);
    }

    @Override
    public ETC1AtlasCompressorSettings newAtlasCompressionSettings() {
	return new RedETC1AtlasCompressorSettings();
    }

    @Override
    public ETC1AtlasCompressionResult compress(ETC1AtlasCompressorSettings settings) throws IOException {
	return doCompress(settings);
    }

}
