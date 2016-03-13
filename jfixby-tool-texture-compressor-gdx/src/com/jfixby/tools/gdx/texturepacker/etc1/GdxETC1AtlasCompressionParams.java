
package com.jfixby.tools.gdx.texturepacker.etc1;

import com.jfixby.cmns.api.file.File;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1AtlasCompressionParams;

class GdxETC1AtlasCompressionParams implements ETC1AtlasCompressionParams {

    private File atlasFile;
    private boolean deleteOriginalPNG = false;

    public void setAtlasFile(File atlasFile) {
	this.atlasFile = atlasFile;
    }

    public File getAtlasFile() {
	return atlasFile;
    }

    public boolean deleteOriginalPNG() {
	return deleteOriginalPNG;
    }

    public void setDeleteOriginalPNG(boolean deleteOriginalPNG) {
	this.deleteOriginalPNG = deleteOriginalPNG;
    }

}
