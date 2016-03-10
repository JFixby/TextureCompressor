package com.jfixby.tools.gdx.texturepacker.etc1;

import java.io.IOException;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.jfixby.cmns.adopted.gdx.fs.ToGdxFileAdaptor;
import com.jfixby.cmns.api.debug.Debug;
import com.jfixby.cmns.api.err.Err;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.log.L;
import com.jfixby.tools.gdx.texturepacker.api.etc1.CompressedAtlasDescriptor;

public class RedCompressedAtlas {

    private CompressedAtlasDescriptor descriptor;

    private File descriptorFile;

    private String alpha_channes_file_name;

    public RedCompressedAtlas(RedCompressedAtlasSpecs specs) {
	this.descriptor = Debug.checkNull(specs.getDescriptor());
	this.descriptorFile = Debug.checkNull(specs.getFile());
	gdx_atlas_name = Debug.checkEmpty("descriptor.gdx_atlas_file_name", descriptor.gdx_atlas_file_name);
	gdx_atlas_name = Debug.checkNull("descriptor.gdx_atlas_file_name", descriptor.gdx_atlas_file_name);

	alpha_channes_file_name = Debug.checkEmpty("descriptor.alpha_channes_file_name",
		descriptor.alpha_channes_file_name);
	alpha_channes_file_name = Debug.checkNull("descriptor.alpha_channes_file_name",
		descriptor.alpha_channes_file_name);
	zip = descriptor.alpha_channes_are_zip_compressed;
    }

    public TextureAtlas getGdxAtlas() {
	return gdx_atlas;
    }

    boolean loaded = false;

    private String gdx_atlas_name;

    private TextureAtlas gdx_atlas;

    private boolean zip;

    public void load() throws IOException {
	if (loaded) {
	    Err.reportError("This atlas is already loaded: " + this.descriptorFile);
	}
	File gdx_atlas_file = descriptorFile.parent().child(gdx_atlas_name);
	ToGdxFileAdaptor adaptor = new ToGdxFileAdaptor(gdx_atlas_file);
	gdx_atlas = new TextureAtlas(adaptor);

	File alphas_file = descriptorFile.parent().child(alpha_channes_file_name);
	byte[] alphas_bytes = alphas_file.readBytes();

	Pages pages = RedAlphaChannelExtractor.deserialize(alphas_bytes, zip);
	L.d(pages);

	loaded = true;
    }

}
