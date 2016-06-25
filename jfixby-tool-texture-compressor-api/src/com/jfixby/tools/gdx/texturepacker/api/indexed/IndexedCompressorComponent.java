
package com.jfixby.tools.gdx.texturepacker.api.indexed;

import java.io.IOException;

import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.image.ColorMap;
import com.jfixby.cmns.api.io.InputStream;

public interface IndexedCompressorComponent {

	IndexColorCompressionParams newCompressionSpecs ();

	void compress (IndexColorCompressionParams params) throws IOException;

	void compressFile (File originalFile, File outputFile) throws IOException;

	void deCompressFile (File compressedFile, File output_file) throws IOException;

	IndexColorDeCompressionParams newDeCompressionParams ();

	IndexColorDeCompressionResult deCompress (IndexColorDeCompressionParams params) throws IOException;

	void indexImage (File originalFile, File indexedFile) throws IOException;

	ColorMap indexImage (ColorMap originalImage);

	ColorMap deCompress (File compressedFile) throws IOException;

	ColorIndexMap readColorIndexMap (File compressedFile) throws IOException;

	ColorIndexMap readColorIndexMap (InputStream stream) throws IOException;
}
