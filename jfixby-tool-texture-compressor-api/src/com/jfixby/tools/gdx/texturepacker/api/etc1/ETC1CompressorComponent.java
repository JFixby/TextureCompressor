
package com.jfixby.tools.gdx.texturepacker.api.etc1;

import java.io.IOException;

import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.file.File;

public interface ETC1CompressorComponent {

	void compressFile (File originalFile, File compressedFile, Color transparentColor, boolean discardAlpha);

	void deCompressFile (File compressedFile, File restoredFile) throws IOException;

	ETC1DeCompressionParams newDeCompressionParams ();

	ETC1CompressionParams newCompressionParams ();

	void compress (ETC1CompressionParams params) throws IOException;

	ETC1DeCompressionResult deCompress (ETC1DeCompressionParams params) throws IOException;

	ETC1AtlasCompressionParams newAtlasCompressionParams ();

	ETC1AtlasCompressionResult compressAtlas (ETC1AtlasCompressionParams params) throws IOException;

}
