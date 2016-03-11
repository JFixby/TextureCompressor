
package com.jfixby.tools.gdx.texturepacker.etc1;

import com.jfixby.cmns.api.color.Color;
import com.jfixby.cmns.api.file.File;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1AtlasCompressorSettings;

class RedETC1AtlasCompressorSettings implements ETC1AtlasCompressorSettings {

    private File atlasFile;
    private Color transparentColor;
    private boolean deleteOriginalPNG = false;
    private boolean removeAlpha = true;
    private boolean zipCompressExtractedAlphaChannels = false;
    private boolean extractAlphaChannes = false;

    public void setRemoveAlpha(boolean removeAlpha) {
	this.removeAlpha = removeAlpha;
    }

    public void setAtlasFile(File atlasFile) {
	this.atlasFile = atlasFile;
    }

    public File getAtlasFile() {
	return atlasFile;
    }

    public Color getTransparentColor() {
	return transparentColor;
    }

    public void setTransparentColor(Color transparentColor) {
	this.transparentColor = transparentColor;
    }

    public boolean deleteOriginalPNG() {
	return deleteOriginalPNG;
    }

    public void setDeleteOriginalPNG(boolean deleteOriginalPNG) {
	this.deleteOriginalPNG = deleteOriginalPNG;
    }

    public boolean removeAlpha() {
	return removeAlpha;
    }

    @Override
    public boolean zipCompressExtractedAlphaChannels() {
	return zipCompressExtractedAlphaChannels;
    }

    @Override
    public boolean extractAlphaChannes() {
	return extractAlphaChannes;
    }

    @Override
    public void setZipCompressExtractedAlphaChannels(boolean zipCompressExtractedAlphaChannels) {
	this.zipCompressExtractedAlphaChannels = zipCompressExtractedAlphaChannels;
    }

    @Override
    public void setExtractAlphaChannes(boolean extractAlphaChannes) {
	this.extractAlphaChannes = extractAlphaChannes;
    }

}
