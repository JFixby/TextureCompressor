package com.jfixby.tools.gdx.texturepacker.api.etc1;

import com.jfixby.cmns.api.file.File;

public interface ETC1AtlasCompressionParams {

    void setAtlasFile(File etc1AtlasFilePath);

    File getAtlasFile();

    void setDeleteOriginalPNG(boolean b);

    boolean deleteOriginalPNG();

}
