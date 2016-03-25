package com.jfixby.tools.gdx.texturepacker.api.indexed;

import java.io.IOException;

public interface IndexedCompressorComponent {

    IndexColorCompressionParams newCompressionSpecs();

    void compress(IndexColorCompressionParams params) throws IOException;
}