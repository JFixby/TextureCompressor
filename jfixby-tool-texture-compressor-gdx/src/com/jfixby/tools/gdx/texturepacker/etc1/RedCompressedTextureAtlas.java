package com.jfixby.tools.gdx.texturepacker.etc1;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.jfixby.cmns.api.log.L;

/**
 * Loads images from texture atlases created by TexturePacker.<br>
 * <br>
 * A TextureAtlas must be disposed to free up the resources consumed by the
 * backing textures.
 * 
 * @author Nathan Sweet
 */
public class RedCompressedTextureAtlas implements Disposable {
    static final String[] tuple = new String[4];

    private final ObjectSet<TextureContainer> textures = new ObjectSet(4);
    private final Array<AtlasRegion> regions = new Array();

    /** Creates an empty atlas to which regions can be added. */
    public RedCompressedTextureAtlas() {
    }

    /**
     * Loads the specified pack file using {@link FileType#Internal}, using the
     * parent directory of the pack file to find the page images.
     */
    public RedCompressedTextureAtlas(String internalPackFile) {
	this(Gdx.files.internal(internalPackFile));
    }

    /**
     * Loads the specified pack file, using the parent directory of the pack
     * file to find the page images.
     */
    public RedCompressedTextureAtlas(FileHandle packFile) {
	this(packFile, packFile.parent());
    }

    /**
     * @param flip
     *            If true, all regions loaded will be flipped for use with a
     *            perspective where 0,0 is the upper left corner.
     * @see #TextureAtlas(FileHandle)
     */
    public RedCompressedTextureAtlas(FileHandle packFile, boolean flip) {
	this(packFile, packFile.parent(), flip);
    }

    public RedCompressedTextureAtlas(FileHandle packFile, FileHandle imagesDir) {
	this(packFile, imagesDir, false);
    }

    /**
     * @param flip
     *            If true, all regions loaded will be flipped for use with a
     *            perspective where 0,0 is the upper left corner.
     */
    public RedCompressedTextureAtlas(FileHandle packFile, FileHandle imagesDir, boolean flip) {
	this(new RedCompressedTextureAtlasData(packFile, imagesDir, flip));
    }

    /**
     * @param data
     *            May be null.
     */
    public RedCompressedTextureAtlas(RedCompressedTextureAtlasData data) {
	if (data != null)
	    load(data);
    }

    private void load(RedCompressedTextureAtlasData data) {
	ObjectMap<Page, Texture> pageToTexture = new ObjectMap<Page, Texture>();
	for (Page page : data.pages) {
	    Texture texture = null;
	    if (true) {
		texture = loadTextureB(page, data);
		texture.setFilter(page.minFilter, page.magFilter);
		texture.setWrap(page.uWrap, page.vWrap);
	    } else {
		// texture = page.getTexture();
		// texture.setFilter(page.minFilter, page.magFilter);
		// texture.setWrap(page.uWrap, page.vWrap);
	    }
	    textures.add(new TextureContainer(texture, page.getTextureFile()));
	    pageToTexture.put(page, texture);
	}

	for (Region region : data.regions) {
	    int width = region.width;
	    int height = region.height;
	    AtlasRegion atlasRegion = new AtlasRegion(pageToTexture.get(region.page), region.left, region.top,
		    region.rotate ? height : width, region.rotate ? width : height);
	    atlasRegion.index = region.index;
	    atlasRegion.name = region.name;
	    atlasRegion.offsetX = region.offsetX;
	    atlasRegion.offsetY = region.offsetY;
	    atlasRegion.originalHeight = region.originalHeight;
	    atlasRegion.originalWidth = region.originalWidth;
	    atlasRegion.rotate = region.rotate;
	    atlasRegion.splits = region.splits;
	    atlasRegion.pads = region.pads;
	    if (region.flip)
		atlasRegion.flip(false, true);
	    regions.add(atlasRegion);
	}
    }

    private Texture loadTextureA(Page page, RedCompressedTextureAtlasData data) {
	Texture newTexture = null;
	newTexture = new Texture(page.getTextureFile(), page.format, page.useMipMaps);
	// >>

	return newTexture;
    }

    private Texture loadTextureB(Page page, RedCompressedTextureAtlasData data) {
	// GdxNativesLoader.load();
	// >>

	FileHandle textureFile = page.getTextureFile();
	// ETC1Data etc1Data = new ETC1Data(textureFile);

	// Pixmap etc1Pixmap = ETC1.decodeImage(etc1Data, Format.RGB888);

	final float W = page.getWidth();
	final float H = page.getHeight();
	String name = textureFile.name();
	AlphaPage alphaPage = data.alphaPages.findAlphaPage(name);
	alphaPage.checkValid(name);
	alphaPage.checkValid((int) W, (int) H);
	Pixmap mergedPixmap = new Pixmap((int) W, (int) H, Format.RGBA8888);
	mergedPixmap.setColor(0x0000ffff);
	 mergedPixmap.fill();
	// mergedPixmap.drawPixmap(etc1Pixmap, 0, 0);
	Pixmap.setBlending(Pixmap.Blending.None);
	// mergedPixmap.setBlending(Blending.SourceOver);
	for (int x = 0; x < W; x++) {
	    for (int y = 0; y < H; y++) {
		final float alpha = alphaPage.getAlphaValue(x, y) * 1 + 0;
		// final int color_int = etc1Pixmap.getPixel(x, y);
		// Color color = new Color(color_int);
		// color.a = alpha * 1 + 0 * 0.5f;
		// mergedPixmap.setColor(color);
		mergedPixmap.setColor(0xffffffff);
		mergedPixmap.drawPixel(x, y);
	    }
	}
	L.d();
	page.format = Format.RGBA8888;
	PixmapTextureData textureData = new PixmapTextureData(mergedPixmap, Format.RGBA8888, false, false, false);
	final Texture newTexture = new Texture(textureData);
	// etc1Pixmap.dispose();
	mergedPixmap.dispose();

	return newTexture;
    }

    // /**
    // * Adds a region to the atlas. The specified texture will be disposed when
    // * the atlas is disposed.
    // */
    // public AtlasRegion addRegion(String name, Texture texture, int x, int y,
    // int width, int height) {
    // textures.add(texture);
    // AtlasRegion region = new AtlasRegion(texture, x, y, width, height);
    // region.name = name;
    // region.originalWidth = width;
    // region.originalHeight = height;
    // region.index = -1;
    // regions.add(region);
    // return region;
    // }
    //
    // /**
    // * Adds a region to the atlas. The texture for the specified region will
    // be
    // * disposed when the atlas is disposed.
    // */
    // public AtlasRegion addRegion(String name, TextureRegion textureRegion) {
    // return addRegion(name, textureRegion.getTexture(),
    // textureRegion.getRegionX(), textureRegion.getRegionY(),
    // textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
    // }

    /** Returns all regions in the atlas. */
    public Array<AtlasRegion> getRegions() {
	return regions;
    }

    /**
     * Returns the first region found with the specified name. This method uses
     * string comparison to find the region, so the result should be cached
     * rather than calling this method multiple times.
     * 
     * @return The region, or null.
     */
    public AtlasRegion findRegion(String name) {
	for (int i = 0, n = regions.size; i < n; i++)
	    if (regions.get(i).name.equals(name))
		return regions.get(i);
	return null;
    }

    /**
     * Returns the first region found with the specified name and index. This
     * method uses string comparison to find the region, so the result should be
     * cached rather than calling this method multiple times.
     * 
     * @return The region, or null.
     */
    public AtlasRegion findRegion(String name, int index) {
	for (int i = 0, n = regions.size; i < n; i++) {
	    AtlasRegion region = regions.get(i);
	    if (!region.name.equals(name))
		continue;
	    if (region.index != index)
		continue;
	    return region;
	}
	return null;
    }

    /**
     * Returns all regions with the specified name, ordered by smallest to
     * largest {@link AtlasRegion#index index}. This method uses string
     * comparison to find the regions, so the result should be cached rather
     * than calling this method multiple times.
     */
    public Array<AtlasRegion> findRegions(String name) {
	Array<AtlasRegion> matched = new Array();
	for (int i = 0, n = regions.size; i < n; i++) {
	    AtlasRegion region = regions.get(i);
	    if (region.name.equals(name))
		matched.add(new AtlasRegion(region));
	}
	return matched;
    }

    /**
     * Returns all regions in the atlas as sprites. This method creates a new
     * sprite for each region, so the result should be stored rather than
     * calling this method multiple times.
     * 
     * @see #createSprite(String)
     */
    public Array<Sprite> createSprites() {
	Array sprites = new Array(regions.size);
	for (int i = 0, n = regions.size; i < n; i++)
	    sprites.add(newSprite(regions.get(i)));
	return sprites;
    }

    /**
     * Returns the first region found with the specified name as a sprite. If
     * whitespace was stripped from the region when it was packed, the sprite is
     * automatically positioned as if whitespace had not been stripped. This
     * method uses string comparison to find the region and constructs a new
     * sprite, so the result should be cached rather than calling this method
     * multiple times.
     * 
     * @return The sprite, or null.
     */
    public Sprite createSprite(String name) {
	for (int i = 0, n = regions.size; i < n; i++)
	    if (regions.get(i).name.equals(name))
		return newSprite(regions.get(i));
	return null;
    }

    /**
     * Returns the first region found with the specified name and index as a
     * sprite. This method uses string comparison to find the region and
     * constructs a new sprite, so the result should be cached rather than
     * calling this method multiple times.
     * 
     * @return The sprite, or null.
     * @see #createSprite(String)
     */
    public Sprite createSprite(String name, int index) {
	for (int i = 0, n = regions.size; i < n; i++) {
	    AtlasRegion region = regions.get(i);
	    if (!region.name.equals(name))
		continue;
	    if (region.index != index)
		continue;
	    return newSprite(regions.get(i));
	}
	return null;
    }

    /**
     * Returns all regions with the specified name as sprites, ordered by
     * smallest to largest {@link AtlasRegion#index index}. This method uses
     * string comparison to find the regions and constructs new sprites, so the
     * result should be cached rather than calling this method multiple times.
     * 
     * @see #createSprite(String)
     */
    public Array<Sprite> createSprites(String name) {
	Array<Sprite> matched = new Array();
	for (int i = 0, n = regions.size; i < n; i++) {
	    AtlasRegion region = regions.get(i);
	    if (region.name.equals(name))
		matched.add(newSprite(region));
	}
	return matched;
    }

    private Sprite newSprite(AtlasRegion region) {
	if (region.packedWidth == region.originalWidth && region.packedHeight == region.originalHeight) {
	    if (region.rotate) {
		Sprite sprite = new Sprite(region);
		sprite.setBounds(0, 0, region.getRegionHeight(), region.getRegionWidth());
		sprite.rotate90(true);
		return sprite;
	    }
	    return new Sprite(region);
	}
	return new AtlasSprite(region);
    }

    /**
     * Returns the first region found with the specified name as a
     * {@link NinePatch}. The region must have been packed with ninepatch
     * splits. This method uses string comparison to find the region and
     * constructs a new ninepatch, so the result should be cached rather than
     * calling this method multiple times.
     * 
     * @return The ninepatch, or null.
     */
    public NinePatch createPatch(String name) {
	for (int i = 0, n = regions.size; i < n; i++) {
	    AtlasRegion region = regions.get(i);
	    if (region.name.equals(name)) {
		int[] splits = region.splits;
		if (splits == null)
		    throw new IllegalArgumentException("Region does not have ninepatch splits: " + name);
		NinePatch patch = new NinePatch(region, splits[0], splits[1], splits[2], splits[3]);
		if (region.pads != null)
		    patch.setPadding(region.pads[0], region.pads[1], region.pads[2], region.pads[3]);
		return patch;
	    }
	}
	return null;
    }

    /** @return the textures of the pages, unordered */
    public ObjectSet<TextureContainer> getTextures() {
	return textures;
    }

    /**
     * Releases all resources associated with this TextureAtlas instance. This
     * releases all the textures backing all TextureRegions and Sprites, which
     * should no longer be used after calling dispose.
     */
    public void dispose() {
	for (TextureContainer texture : textures)
	    texture.getTexture().dispose();
	textures.clear();
    }

    static final Comparator<Region> indexComparator = new Comparator<Region>() {
	public int compare(Region region1, Region region2) {
	    int i1 = region1.index;
	    if (i1 == -1)
		i1 = Integer.MAX_VALUE;
	    int i2 = region2.index;
	    if (i2 == -1)
		i2 = Integer.MAX_VALUE;
	    return i1 - i2;
	}
    };

    static String readValue(BufferedReader reader) throws IOException {
	String line = reader.readLine();
	int colon = line.indexOf(':');
	if (colon == -1)
	    throw new GdxRuntimeException("Invalid line: " + line);
	return line.substring(colon + 1).trim();
    }

    /** Returns the number of tuple values read (1, 2 or 4). */
    static int readTuple(BufferedReader reader) throws IOException {
	String line = reader.readLine();
	int colon = line.indexOf(':');
	if (colon == -1)
	    throw new GdxRuntimeException("Invalid line: " + line);
	int i = 0, lastMatch = colon + 1;
	for (i = 0; i < 3; i++) {
	    int comma = line.indexOf(',', lastMatch);
	    if (comma == -1)
		break;
	    tuple[i] = line.substring(lastMatch, comma).trim();
	    lastMatch = comma + 1;
	}
	tuple[i] = line.substring(lastMatch).trim();
	return i + 1;
    }

    /**
     * Describes the region of a packed image and provides information about the
     * original image before it was packed.
     */
    static public class AtlasRegion extends TextureRegion {
	/**
	 * The number at the end of the original image file name, or -1 if none.
	 * <br>
	 * <br>
	 * When sprites are packed, if the original file name ends with a
	 * number, it is stored as the index and is not considered as part of
	 * the sprite's name. This is useful for keeping animation frames in
	 * order.
	 * 
	 * @see RedCompressedTextureAtlas#findRegions(String)
	 */
	public int index;

	/**
	 * The name of the original image file, up to the first underscore.
	 * Underscores denote special instructions to the texture packer.
	 */
	public String name;

	/**
	 * The offset from the left of the original image to the left of the
	 * packed image, after whitespace was removed for packing.
	 */
	public float offsetX;

	/**
	 * The offset from the bottom of the original image to the bottom of the
	 * packed image, after whitespace was removed for packing.
	 */
	public float offsetY;

	/** The width of the image, after whitespace was removed for packing. */
	public int packedWidth;

	/**
	 * The height of the image, after whitespace was removed for packing.
	 */
	public int packedHeight;

	/**
	 * The width of the image, before whitespace was removed and rotation
	 * was applied for packing.
	 */
	public int originalWidth;

	/**
	 * The height of the image, before whitespace was removed for packing.
	 */
	public int originalHeight;

	/**
	 * If true, the region has been rotated 90 degrees counter clockwise.
	 */
	public boolean rotate;

	/**
	 * The ninepatch splits, or null if not a ninepatch. Has 4 elements:
	 * left, right, top, bottom.
	 */
	public int[] splits;

	/**
	 * The ninepatch pads, or null if not a ninepatch or the has no padding.
	 * Has 4 elements: left, right, top, bottom.
	 */
	public int[] pads;

	public AtlasRegion(Texture texture, int x, int y, int width, int height) {
	    super(texture, x, y, width, height);
	    originalWidth = width;
	    originalHeight = height;
	    packedWidth = width;
	    packedHeight = height;
	}

	public AtlasRegion(AtlasRegion region) {
	    setRegion(region);
	    index = region.index;
	    name = region.name;
	    offsetX = region.offsetX;
	    offsetY = region.offsetY;
	    packedWidth = region.packedWidth;
	    packedHeight = region.packedHeight;
	    originalWidth = region.originalWidth;
	    originalHeight = region.originalHeight;
	    rotate = region.rotate;
	    splits = region.splits;
	}

	@Override
	/**
	 * Flips the region, adjusting the offset so the image appears to be
	 * flip as if no whitespace has been removed for packing.
	 */
	public void flip(boolean x, boolean y) {
	    super.flip(x, y);
	    if (x)
		offsetX = originalWidth - offsetX - getRotatedPackedWidth();
	    if (y)
		offsetY = originalHeight - offsetY - getRotatedPackedHeight();
	}

	/**
	 * Returns the packed width considering the rotate value, if it is true
	 * then it returns the packedHeight, otherwise it returns the
	 * packedWidth.
	 */
	public float getRotatedPackedWidth() {
	    return rotate ? packedHeight : packedWidth;
	}

	/**
	 * Returns the packed height considering the rotate value, if it is true
	 * then it returns the packedWidth, otherwise it returns the
	 * packedHeight.
	 */
	public float getRotatedPackedHeight() {
	    return rotate ? packedWidth : packedHeight;
	}

	public String toString() {
	    return name;
	}
    }

    /**
     * A sprite that, if whitespace was stripped from the region when it was
     * packed, is automatically positioned as if whitespace had not been
     * stripped.
     */
    static public class AtlasSprite extends Sprite {
	final AtlasRegion region;
	float originalOffsetX, originalOffsetY;

	public AtlasSprite(AtlasRegion region) {
	    this.region = new AtlasRegion(region);
	    originalOffsetX = region.offsetX;
	    originalOffsetY = region.offsetY;
	    setRegion(region);
	    setOrigin(region.originalWidth / 2f, region.originalHeight / 2f);
	    int width = region.getRegionWidth();
	    int height = region.getRegionHeight();
	    if (region.rotate) {
		super.rotate90(true);
		super.setBounds(region.offsetX, region.offsetY, height, width);
	    } else
		super.setBounds(region.offsetX, region.offsetY, width, height);
	    setColor(1, 1, 1, 1);
	}

	public AtlasSprite(AtlasSprite sprite) {
	    region = sprite.region;
	    this.originalOffsetX = sprite.originalOffsetX;
	    this.originalOffsetY = sprite.originalOffsetY;
	    set(sprite);
	}

	@Override
	public void setPosition(float x, float y) {
	    super.setPosition(x + region.offsetX, y + region.offsetY);
	}

	@Override
	public void setX(float x) {
	    super.setX(x + region.offsetX);
	}

	@Override
	public void setY(float y) {
	    super.setY(y + region.offsetY);
	}

	@Override
	public void setBounds(float x, float y, float width, float height) {
	    float widthRatio = width / region.originalWidth;
	    float heightRatio = height / region.originalHeight;
	    region.offsetX = originalOffsetX * widthRatio;
	    region.offsetY = originalOffsetY * heightRatio;
	    int packedWidth = region.rotate ? region.packedHeight : region.packedWidth;
	    int packedHeight = region.rotate ? region.packedWidth : region.packedHeight;
	    super.setBounds(x + region.offsetX, y + region.offsetY, packedWidth * widthRatio,
		    packedHeight * heightRatio);
	}

	@Override
	public void setSize(float width, float height) {
	    setBounds(getX(), getY(), width, height);
	}

	@Override
	public void setOrigin(float originX, float originY) {
	    super.setOrigin(originX - region.offsetX, originY - region.offsetY);
	}

	@Override
	public void setOriginCenter() {
	    super.setOrigin(getWidth() / 2 - region.offsetX, getHeight() / 2 - region.offsetY);
	}

	@Override
	public void flip(boolean x, boolean y) {
	    // Flip texture.
	    if (region.rotate)
		super.flip(y, x);
	    else
		super.flip(x, y);

	    float oldOriginX = getOriginX();
	    float oldOriginY = getOriginY();
	    float oldOffsetX = region.offsetX;
	    float oldOffsetY = region.offsetY;

	    float widthRatio = getWidthRatio();
	    float heightRatio = getHeightRatio();

	    region.offsetX = originalOffsetX;
	    region.offsetY = originalOffsetY;
	    region.flip(x, y); // Updates x and y offsets.
	    originalOffsetX = region.offsetX;
	    originalOffsetY = region.offsetY;
	    region.offsetX *= widthRatio;
	    region.offsetY *= heightRatio;

	    // Update position and origin with new offsets.
	    translate(region.offsetX - oldOffsetX, region.offsetY - oldOffsetY);
	    setOrigin(oldOriginX, oldOriginY);
	}

	@Override
	public void rotate90(boolean clockwise) {
	    // Rotate texture.
	    super.rotate90(clockwise);

	    float oldOriginX = getOriginX();
	    float oldOriginY = getOriginY();
	    float oldOffsetX = region.offsetX;
	    float oldOffsetY = region.offsetY;

	    float widthRatio = getWidthRatio();
	    float heightRatio = getHeightRatio();

	    if (clockwise) {
		region.offsetX = oldOffsetY;
		region.offsetY = region.originalHeight * heightRatio - oldOffsetX - region.packedWidth * widthRatio;
	    } else {
		region.offsetX = region.originalWidth * widthRatio - oldOffsetY - region.packedHeight * heightRatio;
		region.offsetY = oldOffsetX;
	    }

	    // Update position and origin with new offsets.
	    translate(region.offsetX - oldOffsetX, region.offsetY - oldOffsetY);
	    setOrigin(oldOriginX, oldOriginY);
	}

	@Override
	public float getX() {
	    return super.getX() - region.offsetX;
	}

	@Override
	public float getY() {
	    return super.getY() - region.offsetY;
	}

	@Override
	public float getOriginX() {
	    return super.getOriginX() + region.offsetX;
	}

	@Override
	public float getOriginY() {
	    return super.getOriginY() + region.offsetY;
	}

	@Override
	public float getWidth() {
	    return super.getWidth() / region.getRotatedPackedWidth() * region.originalWidth;
	}

	@Override
	public float getHeight() {
	    return super.getHeight() / region.getRotatedPackedHeight() * region.originalHeight;
	}

	public float getWidthRatio() {
	    return super.getWidth() / region.getRotatedPackedWidth();
	}

	public float getHeightRatio() {
	    return super.getHeight() / region.getRotatedPackedHeight();
	}

	public AtlasRegion getAtlasRegion() {
	    return region;
	}

	public String toString() {
	    return region.toString();
	}
    }

}
