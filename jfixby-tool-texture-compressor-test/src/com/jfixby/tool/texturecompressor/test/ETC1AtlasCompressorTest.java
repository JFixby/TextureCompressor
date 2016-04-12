/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.jfixby.tool.texturecompressor.test;

import java.io.IOException;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.jfixby.cmns.adopted.gdx.atlas.ATLAS_LOAD_MODE;
import com.jfixby.cmns.adopted.gdx.atlas.CompressedFokkerAtlas;
import com.jfixby.cmns.adopted.gdx.atlas.CompressedGdxTextureAtlas;
import com.jfixby.cmns.adopted.gdx.atlas.GdxSprite;
import com.jfixby.cmns.adopted.gdx.atlas.legacy.GdxTextureAtlas;
import com.jfixby.cmns.adopted.gdx.json.RedJson;
import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.assets.Names;
import com.jfixby.cmns.api.collections.Mapping;
import com.jfixby.cmns.api.debug.Debug;
import com.jfixby.cmns.api.debug.DebugTimer;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.LocalFileSystem;
import com.jfixby.cmns.api.json.Json;
import com.jfixby.cmns.api.log.L;
import com.jfixby.cmns.api.sys.Sys;
import com.jfixby.r3.api.shader.ShaderAsset;
import com.jfixby.r3.api.shader.ShaderParameter;
import com.jfixby.r3.shader.fokker.FokkerShaderPackageReader;
import com.jfixby.rana.api.pkg.ResourcesManager;
import com.jfixby.red.desktop.DesktopAssembler;
import com.jfixby.red.engine.core.unit.shader.RedFokkerShader;
import com.jfixby.red.triplane.resources.fsbased.RedResourcesManager;
import com.jfixby.tools.gdx.texturepacker.GdxTexturePacker;
import com.jfixby.tools.gdx.texturepacker.api.AtlasPackingResult;
import com.jfixby.tools.gdx.texturepacker.api.Packer;
import com.jfixby.tools.gdx.texturepacker.api.TexturePacker;
import com.jfixby.tools.gdx.texturepacker.api.TexturePackingSpecs;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1AtlasCompressionParams;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1AtlasCompressionResult;
import com.jfixby.tools.gdx.texturepacker.api.etc1.ETC1Compressor;
import com.jfixby.tools.gdx.texturepacker.etc1.GdxETC1;

public class ETC1AtlasCompressorTest implements ApplicationListener {

	private File regularAtlasFile;
	private File compressedAtlasFile;

	public ETC1AtlasCompressorTest (File regularAtlasFile, File compressedAtlasFile) {
		this.regularAtlasFile = regularAtlasFile;
		this.compressedAtlasFile = compressedAtlasFile;
	}

	public static void main (String[] args) throws Exception {
		DesktopAssembler.setup();
		TexturePacker.installComponent(new GdxTexturePacker());
		ETC1Compressor.installComponent(new GdxETC1());
		Json.installComponent(new RedJson());
		RedResourcesManager res_manager = new RedResourcesManager();
		ResourcesManager.installComponent(res_manager);

		File homeFolder = LocalFileSystem.ApplicationHome();
		File spritesFolder = homeFolder.child("sprites");
		// .child("benchmark");
		File regularAtlasFolder = homeFolder.child("atlas");
		File etc1AtlasFolder = homeFolder.child("atlas-etc1");

		final String atlasFilename = "atlas_test";

		prepareTestAtlas(spritesFolder, regularAtlasFolder, atlasFilename);
		AtlasPackingResult atlas_packing_result = prepareTestAtlas(spritesFolder, etc1AtlasFolder, atlasFilename);

		// ETC1Compressor.

		String outputAtlasFilename = atlas_packing_result.getAtlasOutputFile().getName();
		File regularAtlasFile = regularAtlasFolder.child(outputAtlasFilename);
		File etc1AtlasFile = etc1AtlasFolder.child(outputAtlasFilename);
		File compressedAtlasFile = null;

		boolean COMPRESS = true;

		if (COMPRESS) {
			ETC1AtlasCompressionParams settings = ETC1Compressor.newAtlasCompressionSettings();
			settings.setAtlasFile(etc1AtlasFile);
			settings.setDeleteOriginalPNG(true);
			ETC1AtlasCompressionResult compressionResult = ETC1Compressor.compressAtlas(settings);
			compressionResult.print();
			compressedAtlasFile = compressionResult.getCompressedAtlasFile();
		}

		L.d("Showing compressed sprites");
		new LwjglApplication(new ETC1AtlasCompressorTest(regularAtlasFile, compressedAtlasFile), "", 1600, 768);

	}

	private static AtlasPackingResult prepareTestAtlas (File input_raster_folder, File output_atlas_folder,
		String outputAtlasFilename) throws IOException {
		L.d("input_raster_folder", input_raster_folder);
		L.d("output_atlas_folder", output_atlas_folder);
		L.d("outputAtlasFilename", outputAtlasFilename);

		TexturePackingSpecs specs = TexturePacker.newPackingSpecs();
		specs.setDebugMode(!true);
		specs.setInputRasterFolder(input_raster_folder);
		output_atlas_folder.makeFolder();
		specs.setOutputAtlasFolder(output_atlas_folder);
		specs.setOutputAtlasFileName(outputAtlasFilename);
		Packer packer = TexturePacker.newPacker(specs);
		AtlasPackingResult result = packer.pack();
		result.print();
		return result;

	}

	/// -------------------------------------------------------------------------------------------------

	SpriteBatch batch;

	private GdxTextureAtlas regularAtlas;
	private Array<Sprite> regularSprites;

	private CompressedGdxTextureAtlas etc1Atlas;
	private Array<GdxSprite> etc1Sprites;
	private Array<GdxSprite> etc1AlphaSprites;
	// private FokkerCompressedAtlas compressed_atlas;
	private ShaderProgram gdxShader;

	private RedFokkerShader fokkerShader;

	private CompressedFokkerAtlas compressed_atlas;
	private Mapping<String, ShaderParameter> params;

	public void create () {
		batch = new SpriteBatch();
		gdxShader = loadShader();

		DebugTimer timer = Debug.newTimer();

		timer.reset();
		regularAtlas = new GdxTextureAtlas(this.regularAtlasFile.toJavaFile().getAbsolutePath());
		timer.printTime("Regular Texture Atlas");
		regularSprites = regularAtlas.createSprites();

		timer.reset();
		compressed_atlas = new CompressedFokkerAtlas(this.compressedAtlasFile);

		compressed_atlas.setLoadMode(ATLAS_LOAD_MODE.SECOND_ALPHA_TEXTURE_SHADER);
		try {
			compressed_atlas.load(false);
		} catch (IOException e) {
			e.printStackTrace();
			Sys.exit();
		}
		timer.printTime("Compressed Texture Atlas");
		// Sys.exit();
		this.etc1Atlas = this.compressed_atlas.getGdxAtlas();
		// etc1Atlas = new
		// GdxTextureAtlas(this.compressedAtlasFile.toJavaFile().getAbsolutePath());
		this.etc1Sprites = this.etc1Atlas.createSprites();
		this.etc1AlphaSprites = this.etc1Atlas.createAlphaSprites();

		// FokkerCompressedAtlasReader atlas_reader = new
		// FokkerCompressedAtlasReader();
		//
		// try {
		// timer.reset();
		// compressed_atlas = atlas_reader.read(this.compressedAtlasFile);
		// compressed_atlas.setLoadMode(ATLAS_LOAD_MODE.SECOND_ALPHA_TEXTURE_SHADER);
		// compressed_atlas.load();
		// etc1Atlas = compressed_atlas.getGdxAtlas();
		// etc1Sprites = etc1Atlas.createSprites();
		// timer.printTime("ETC1 Texture Atlas");
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		float x = 10;
		float y = 10;
		float alpha = 0.3f;
		for (int i = 0; i < regularSprites.size; i++) {
			Sprite sprite = regularSprites.get(i);
			sprite.setX(x);
			sprite.setY(10);
			sprite.setAlpha(alpha);
			x = x + sprite.getWidth() * 0.9f;
			y = Math.max(y, sprite.getHeight());
		}
		x = 10;
		for (int i = 0; i < etc1Sprites.size; i++) {
			GdxSprite sprite = etc1Sprites.get(i);
			sprite.setX(x);
			sprite.setY(y * 1.1f);
			sprite.setAlpha(alpha);
			x = x + sprite.getWidth() * 0.9f;
		}

	}

	private ShaderProgram loadShader () {

		FokkerShaderPackageReader reader = new FokkerShaderPackageReader();

		File shader_root_file = LocalFileSystem.newFile(
			"D:\\[DATA]\\[RED-ASSETS]\\Art-Private\\tinto-assets\\content\\bank-tinto\\com.jfixby.r3.fokker.shader.photoshop\\content\\r3.shader.info");
		try {
			reader.readRootFile(shader_root_file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		AssetID asset_id = Names.newAssetID("com.jfixby.r3.fokker.shader.photoshop.test");
		ShaderAsset asset = reader.findStructure(asset_id);
		fokkerShader = new RedFokkerShader(asset);

		activateShader();

		return (ShaderProgram)fokkerShader.getGdxShaderProgram();
	}

	private void activateShader () {
		params = fokkerShader.listParameters();
		params.print("shader params");
		// Err.reportError("here");
		// fokkerShader.setFloatParameterValue(params.getValueAt(0).getName(),
		// Screen.getScreenWidth());
		// fokkerShader.setFloatParameterValue(params.getValueAt(1).getName(),
		// Screen.getScreenHeight());
		fokkerShader.setFloatParameterValue(params.getValueAt(2).getName(), 1f);
		fokkerShader.setIntParameterValue(params.getValueAt(3).getName(), 0);
		fokkerShader.setIntParameterValue(params.getValueAt(4).getName(), 1);
		fokkerShader.setIntParameterValue(params.getValueAt(5).getName(), 2);

		// shader.setFloatParameterValue("test", 0.5);
		fokkerShader.printParameterValues();
		// Sys.exit();
		fokkerShader.setupValues();
	}

	public void render () {
		final float gray = 0.5f;
		Gdx.gl.glClearColor(gray, gray, gray, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float alpha = 0;
		alpha = alpha();
		int x = 20, y = 20;
		batch.begin();
		for (Sprite sprite : regularSprites) {
			sprite.setAlpha(alpha);
			sprite.draw(batch);
		}
		batch.end();

		for (int i = 0; i < etc1Sprites.size; i++) {

			final GdxSprite sprite = etc1Sprites.get(i);
			final GdxSprite alphaSprite = etc1AlphaSprites.get(i);

			if (compressed_atlas.getLoadMode() == ATLAS_LOAD_MODE.SECOND_ALPHA_TEXTURE_SHADER) {

				// final float alpha = 0.5f;
				fokkerShader.setFloatParameterValue(params.getValueAt(2).getName(), alpha);
				fokkerShader.setupValues();

				batch.setShader(gdxShader);

			}

			batch.begin();

			alphaSprite.getTexture().bind(2);
			// alphaSprite.getTexture().bind(1);
			sprite.getTexture().bind(0);

			final Texture texture = sprite.getTexture();
			batch.draw(texture, sprite.getVertices(), 0, SPRITE_SIZE);

			sprite.draw(batch);

			batch.end();
			batch.setShader(null);
		}

	}

	private float alpha () {
		return (float)(0.5 + Math.sin((System.currentTimeMillis() / 1000d) * 1d) / 2) / 2f;
	}

	static final int VERTEX_SIZE = 2 + 1 + 2;
	static final int SPRITE_SIZE = 4 * VERTEX_SIZE;

	public void resize (int width, int height) {
		float m = 0.6f;
		batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth() * m, Gdx.graphics.getHeight() * m));
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}

}
