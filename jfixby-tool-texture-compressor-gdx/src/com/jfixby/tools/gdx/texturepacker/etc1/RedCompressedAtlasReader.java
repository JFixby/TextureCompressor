package com.jfixby.tools.gdx.texturepacker.etc1;

import java.io.IOException;

import com.jfixby.cmns.api.debug.Debug;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.json.Json;
import com.jfixby.tools.gdx.texturepacker.api.etc1.CompressedAtlasDescriptor;

public class RedCompressedAtlasReader {

    public RedCompressedAtlas read(File compressedAtlasFile) throws IOException {
	Debug.checkNull("compressedAtlasFile", compressedAtlasFile);

	CompressedAtlasDescriptor descriptor = Json.deserializeFromString(CompressedAtlasDescriptor.class,
		compressedAtlasFile.readToString());

	RedCompressedAtlasSpecs specs = new RedCompressedAtlasSpecs();
	specs.setDescriptor(descriptor);
	specs.setFile(compressedAtlasFile);
	RedCompressedAtlas atlas = new RedCompressedAtlas(specs);
	

	return atlas;
    }

}
