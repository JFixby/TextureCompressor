package com.jfixby.tools.gdx.texturepacker.etc1;

import com.jfixby.cmns.api.file.File;
import com.jfixby.tools.gdx.texturepacker.api.etc1.CompressedAtlasDescriptor;

public class RedCompressedAtlasSpecs {

    private CompressedAtlasDescriptor descriptor;
    private File atlasDescriptorFile;

    public void setDescriptor(CompressedAtlasDescriptor descriptor) {
	this.descriptor = descriptor;
    }

    public CompressedAtlasDescriptor getDescriptor() {
	return descriptor;
    }

    public void setFile(File atlasDescriptorFile) {
	this.atlasDescriptorFile = atlasDescriptorFile;
    }

    public File getFile() {
	return atlasDescriptorFile;
    }

}
