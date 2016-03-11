package com.jfixby.tools.gdx.texturepacker.etc1;

import java.io.IOException;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ETC1;
import com.badlogic.gdx.graphics.glutils.ETC1.ETC1Data;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.jfixby.cmns.adopted.gdx.fs.ToGdxFileAdaptor;
import com.jfixby.cmns.api.collections.Collections;
import com.jfixby.cmns.api.collections.List;
import com.jfixby.cmns.api.debug.Debug;
import com.jfixby.cmns.api.err.Err;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.log.L;
import com.jfixby.tools.gdx.texturepacker.api.etc1.CompressedAtlasDescriptor;
import com.jfixby.tools.gdx.texturepacker.etc1.RedCompressedTextureAtlas.AtlasRegion;

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

    public RedCompressedTextureAtlas getGdxAtlas() {
	return gdx_atlas;
    }

    boolean loaded = false;

    private String gdx_atlas_name;

    private RedCompressedTextureAtlas gdx_atlas;

    private boolean zip;

    private ATLAS_LOAD_MODE mode = ATLAS_LOAD_MODE.MERGED_ALPHA_CHANNEL;

    public void load() throws IOException {
	if (loaded) {
	    Err.reportError("This atlas is already loaded: " + this.descriptorFile);
	}
	File gdx_atlas_file = descriptorFile.parent().child(gdx_atlas_name);
	ToGdxFileAdaptor adaptor = new ToGdxFileAdaptor(gdx_atlas_file);

	File alphas_file = descriptorFile.parent().child(alpha_channes_file_name);
	byte[] alphas_bytes = alphas_file.readBytes();

	AlphaPages alphaPages = RedAlphaChannelExtractor.deserialize(alphas_bytes, zip);
	RedCompressedTextureAtlasData atlas_data = new RedCompressedTextureAtlasData(adaptor, adaptor.parent(), false);
	atlas_data.bindAlphaChannels(alphaPages);

	gdx_atlas = new RedCompressedTextureAtlas(atlas_data);
	// ,lkkkkkkkk
	// L.d(alphaPages);
	//
	if (this.mode == ATLAS_LOAD_MODE.MERGED_ALPHA_CHANNEL) {
	    loadMergedAlphaChannel(alphaPages);
	}
	if (this.mode == ATLAS_LOAD_MODE.SECOND_ALPHA_TEXTURE_SHADER) {
//	    Err.reportError("Mode is not supported yet: " + mode);

	}
	if (this.mode == ATLAS_LOAD_MODE.FUXIA_ALPHA_COLOR_SHADER) {
	    Err.reportError("Mode is not supported yet: " + mode);
	}
	// Sys.exit();

	loaded = true;
    }

    private void loadMergedAlphaChannel(AlphaPages alphaPages) {
	List<TextureContainer> gdxPagesList = Collections.newList();
	ObjectSet<TextureContainer> gdxTextures = gdx_atlas.getTextures();
	gdxPagesList.addAll(gdxTextures);
	gdxPagesList.print("gdxPagesList");
	//

	for (int i = 0; i < gdxPagesList.size(); i++) {
	    TextureContainer container = gdxPagesList.getElementAt(i);
	    final Texture oldGdxTexture = container.getTexture();

	    final Texture newGdxTexture = newTexture(oldGdxTexture, container, alphaPages);
	    oldGdxTexture.dispose();
	    container.setTexture(newGdxTexture);

	    fixRegions(oldGdxTexture, newGdxTexture, gdx_atlas);

	}
    }

    private Texture newTexture(Texture texture, TextureContainer container, AlphaPages alphaPages) {

	ETC1Data etc1Data = new ETC1Data(container.getTextureFile());
	Pixmap etc1Pixmap = ETC1.decodeImage(etc1Data, Format.RGB888);
	final float W = texture.getWidth();
	final float H = texture.getHeight();
	String name = container.getTextureFile().name();
	AlphaPage alphaPage = alphaPages.findAlphaPage(name);
	alphaPage.checkValid(name);
	alphaPage.checkValid((int) W, (int) H);
	Pixmap mergedPixmap = new Pixmap((int) W, (int) H, Format.RGBA8888);
	// mergedPixmap.setColor(0x0000ff00);
	// mergedPixmap.fill();
	// mergedPixmap.drawPixmap(etc1Pixmap, 0, 0);
	Pixmap.setBlending(Pixmap.Blending.None);
	// mergedPixmap.setBlending(Blending.SourceOver);
	for (int x = 0; x < W; x++) {
	    for (int y = 0; y < H; y++) {
		final float alpha = alphaPage.getAlphaValue(x, y) * 1 + 0;
		final int color_int = etc1Pixmap.getPixel(x, y);
		Color color = new Color(color_int);
		color.a = alpha * 1 + 0 * 0.5f;
		mergedPixmap.setColor(color);
		mergedPixmap.drawPixel(x, y);
	    }
	}
	L.d();

	final Texture newTexture = new Texture(mergedPixmap);
	etc1Pixmap.dispose();
	mergedPixmap.dispose();
	texture.dispose();
	return newTexture;
    }

    static private void fixRegions(Texture oldGdxTexture, Texture newGdxTexture, RedCompressedTextureAtlas gdx_atlas) {
	Array<AtlasRegion> regions = gdx_atlas.getRegions();
	for (int i = 0; i < regions.size; i++) {
	    AtlasRegion region = regions.get(i);
	    Texture regionTexture = region.getTexture();
	    if (regionTexture == oldGdxTexture) {
		region.setTexture(newGdxTexture);
	    }
	}
    }

    public void setLoadMode(ATLAS_LOAD_MODE mode) {
	if (this.loaded) {
	    Err.reportError("ATLAS_LOAD_MODE can be changed only for unloaded atlas");
	}
	this.mode = Debug.checkNull("mode", mode);
    }

    public ATLAS_LOAD_MODE getLoadMode() {
	return mode;
    }

}
