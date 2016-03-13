package com.jfixby.tools.gdx.texturepacker.api.etc1;

import com.jfixby.cmns.api.collections.Collection;
import com.jfixby.cmns.api.file.File;

public interface ETC1AtlasCompressionResult {

    void print();

    File getCompressedAtlasFile();

    Collection<TextureFileRenaming> listRenamings();

}
