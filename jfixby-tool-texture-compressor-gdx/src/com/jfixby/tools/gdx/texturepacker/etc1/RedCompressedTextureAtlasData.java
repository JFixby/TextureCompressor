package com.jfixby.tools.gdx.texturepacker.etc1;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.ClampToEdge;
import static com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;
import com.jfixby.cmns.api.collections.Collections;
import com.jfixby.cmns.api.err.Err;
import com.jfixby.cmns.api.log.L;

public class RedCompressedTextureAtlasData {
    final Array<Page> pages = new Array();
    final Array<Region> regions = new Array();
    AlphaPages alphaPages;

    public RedCompressedTextureAtlasData(FileHandle packFile, FileHandle imagesDir, boolean flip) {
	BufferedReader reader = new BufferedReader(new InputStreamReader(packFile.read()), 64);
	try {
	    Page pageImage = null;
	    while (true) {
		String line = reader.readLine();
		if (line == null)
		    break;
		if (line.trim().length() == 0)
		    pageImage = null;
		else if (pageImage == null) {
		    FileHandle file = imagesDir.child(line);

		    float width = 0, height = 0;
		    if (RedCompressedTextureAtlas.readTuple(reader) == 2) { // size
									    // is
									    // only
									    // optional
			// for an atlas packed
			// with an old
			// TexturePacker.
			width = Integer.parseInt(RedCompressedTextureAtlas.tuple[0]);
			height = Integer.parseInt(RedCompressedTextureAtlas.tuple[1]);
			RedCompressedTextureAtlas.readTuple(reader);
		    }
		    Format format = Format.valueOf(RedCompressedTextureAtlas.tuple[0]);

		    RedCompressedTextureAtlas.readTuple(reader);
		    TextureFilter min = TextureFilter.valueOf(RedCompressedTextureAtlas.tuple[0]);
		    TextureFilter max = TextureFilter.valueOf(RedCompressedTextureAtlas.tuple[1]);

		    String direction = RedCompressedTextureAtlas.readValue(reader);
		    TextureWrap repeatX = ClampToEdge;
		    TextureWrap repeatY = ClampToEdge;
		    if (direction.equals("x"))
			repeatX = Repeat;
		    else if (direction.equals("y"))
			repeatY = Repeat;
		    else if (direction.equals("xy")) {
			repeatX = Repeat;
			repeatY = Repeat;
		    }

		    pageImage = new Page(file, width, height, min.isMipMap(), format, min, max, repeatX, repeatY);
		    pages.add(pageImage);
		} else {
		    boolean rotate = Boolean.valueOf(RedCompressedTextureAtlas.readValue(reader));

		    RedCompressedTextureAtlas.readTuple(reader);
		    int left = Integer.parseInt(RedCompressedTextureAtlas.tuple[0]);
		    int top = Integer.parseInt(RedCompressedTextureAtlas.tuple[1]);

		    RedCompressedTextureAtlas.readTuple(reader);
		    int width = Integer.parseInt(RedCompressedTextureAtlas.tuple[0]);
		    int height = Integer.parseInt(RedCompressedTextureAtlas.tuple[1]);

		    Region region = new Region();
		    region.page = pageImage;
		    region.left = left;
		    region.top = top;
		    region.width = width;
		    region.height = height;
		    region.name = line;
		    region.rotate = rotate;

		    if (RedCompressedTextureAtlas.readTuple(reader) == 4) { // split
									    // is
									    // optional
			region.splits = new int[] { Integer.parseInt(RedCompressedTextureAtlas.tuple[0]),
				Integer.parseInt(RedCompressedTextureAtlas.tuple[1]),
				Integer.parseInt(RedCompressedTextureAtlas.tuple[2]),
				Integer.parseInt(RedCompressedTextureAtlas.tuple[3]) };

			if (RedCompressedTextureAtlas.readTuple(reader) == 4) { // pad
										// is
										// optional,
			    // but only present
			    // with splits
			    region.pads = new int[] { Integer.parseInt(RedCompressedTextureAtlas.tuple[0]),
				    Integer.parseInt(RedCompressedTextureAtlas.tuple[1]),
				    Integer.parseInt(RedCompressedTextureAtlas.tuple[2]),
				    Integer.parseInt(RedCompressedTextureAtlas.tuple[3]) };

			    RedCompressedTextureAtlas.readTuple(reader);
			}
		    }

		    region.originalWidth = Integer.parseInt(RedCompressedTextureAtlas.tuple[0]);
		    region.originalHeight = Integer.parseInt(RedCompressedTextureAtlas.tuple[1]);

		    RedCompressedTextureAtlas.readTuple(reader);
		    region.offsetX = Integer.parseInt(RedCompressedTextureAtlas.tuple[0]);
		    region.offsetY = Integer.parseInt(RedCompressedTextureAtlas.tuple[1]);

		    region.index = Integer.parseInt(RedCompressedTextureAtlas.readValue(reader));

		    if (flip)
			region.flip = true;

		    regions.add(region);
		}
	    }
	} catch (Exception ex) {
	    throw new GdxRuntimeException("Error reading pack file: " + packFile, ex);
	} finally {
	    StreamUtils.closeQuietly(reader);
	}

	regions.sort(RedCompressedTextureAtlas.indexComparator);
    }

    public Array<Page> getPages() {
	return pages;
    }

    public Array<Region> getRegions() {
	return regions;
    }

    public AlphaPages getAlphaChannels() {
	return alphaPages;
    }

    public void bindAlphaChannels(AlphaPages alphaPages) {
	this.alphaPages = alphaPages;
    }

//    public Page findPageByTexture(Texture texture) {
//	for (int i = 0; i < this.pages.size; i++) {
//	    Page page = this.pages.get(i);
//	    if (page.getTexture() == texture) {
//		return page;
//	    }
//	}
//	Collections.newList(this.pages).print("pages");
//	L.d("Texture", texture);
//	Err.reportError("Texture not found");
//	return null;
//    }
}