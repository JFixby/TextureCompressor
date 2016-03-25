package com.jfixby.tools.gdx.texturepacker.api.indexed;

import java.io.IOException;

import com.jfixby.cmns.api.file.File;

public interface IndexedCompressorComponent {

    IndexColorCompressionParams newCompressionSpecs();

    void compress(IndexColorCompressionParams params) throws IOException;

    void compressFile(File originalFile, File output_file) throws IOException;

    void deCompressFile(File compressedFile, File output_file) throws IOException;

    IndexColorDeCompressionParams newDeCompressionParams();

    IndexColorDeCompressionResult deCompress(IndexColorDeCompressionParams params) throws IOException;
}