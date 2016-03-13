
package com.jfixby.tools.gdx.texturepacker.etc1;

import java.io.IOException;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Page;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.jfixby.cmns.adopted.gdx.fs.ToGdxFileAdaptor;
import com.jfixby.cmns.api.debug.Debug;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.json.Json;
import com.jfixby.cmns.api.log.L;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1AtlasCompressionParams;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1Compressor;

public class GdxETC1AtlasCompressor {

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
	    String newPageFileName = pageFileName + ".etc1";
	    File compressedPageFile = pageFile.parent().child(newPageFileName);

	    // tell ETC1Compressor to process only related files, not the whole
	    // folder
	    ETC1Compressor.compressFile(pageFile, compressedPageFile, null, false);

	    atlasData = atlasData.replaceAll(oldPageFileName, newPageFileName);

	    L.d(" page " + i, pageFile);
	    L.d(" to", compressedPageFile);

	    result.addCompressedTextureNames(oldPageFileName, newPageFileName);
	    if (settings.deleteOriginalPNG()) {
		pageFile.delete();
	    }

	}

	atlasFile.writeString(atlasData);

	result.setCompressedAtlasFile(atlasFile);

	return result;
    }

}
