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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.jfixby.cmns.adopted.gdx.json.GdxJson;
import com.jfixby.cmns.api.assets.AssetID;
import com.jfixby.cmns.api.assets.Names;
import com.jfixby.cmns.api.collections.Mapping;
import com.jfixby.cmns.api.debug.Debug;
import com.jfixby.cmns.api.debug.DebugTimer;
import com.jfixby.cmns.api.file.File;
import com.jfixby.cmns.api.file.LocalFileSystem;
import com.jfixby.cmns.api.json.Json;
import com.jfixby.cmns.api.log.L;
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

    public ETC1AtlasCompressorTest(File regularAtlasFile, File compressedAtlasFile) {
	this.regularAtlasFile = regularAtlasFile;
	this.compressedAtlasFile = compressedAtlasFile;
    }

    public static void main(String[] args) throws Exception {
	DesktopAssembler.setup();
	TexturePacker.installComponent(new GdxTexturePacker());
	ETC1Compressor.installComponent(new GdxETC1());
	Json.installComponent(new GdxJson());
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
	    // Color fuxia = new com.badlogic.gdx.graphics.Color(1f, 0f, 1f,
	    // 1f);
	    // settings.setTransparentColor(fuxia);
	    settings.setDeleteOriginalPNG(true);

	    L.d();
	    ETC1AtlasCompressionResult compressionResult = ETC1Compressor.compressAtlas(settings);
	    L.d();
	    compressionResult.print();

	    compressedAtlasFile = compressionResult.getCompressedAtlasFile();

	}

	L.d("Showing compressed sprites");
	new LwjglApplication(new ETC1AtlasCompressorTest(regularAtlasFile, compressedAtlasFile), "", 1600, 768);

    }

    private static AtlasPackingResult prepareTestAtlas(File input_raster_folder, File output_atlas_folder,
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

    private TextureAtlas regularAtlas;
    private Array<Sprite> regularSprites;

    private TextureAtlas etc1Atlas;
    private Array<Sprite> etc1Sprites;
    // private FokkerCompressedAtlas compressed_atlas;
    // private ShaderProgram gdxShader;

    private RedFokkerShader fokkerShader;

    public void create() {
	batch = new SpriteBatch();
	// gdxShader = loadShader();

	DebugTimer timer = Debug.newTimer();

	timer.reset();
	regularAtlas = new TextureAtlas(this.regularAtlasFile.toJavaFile().getAbsolutePath());
	regularSprites = regularAtlas.createSprites();
	timer.printTime("Regular Texture Atlas");

	timer.reset();
	etc1Atlas = new TextureAtlas(this.compressedAtlasFile.toJavaFile().getAbsolutePath());
	etc1Sprites = etc1Atlas.createSprites();
	timer.printTime("Regular Texture Atlas");

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
	for (int i = 0; i < regularSprites.size; i++) {
	    Sprite sprite = regularSprites.get(i);
	    sprite.setX(x);
	    sprite.setY(10);
	    x = x + sprite.getWidth() * 0.9f;
	    y = Math.max(y, sprite.getHeight());
	}
	x = 10;
	for (int i = 0; i < etc1Sprites.size; i++) {
	    Sprite sprite = etc1Sprites.get(i);
	    sprite.setX(x);
	    sprite.setY(y * 1.1f);
	    x = x + sprite.getWidth() * 0.9f;
	}

    }

    private ShaderProgram loadShader() {

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

	return (ShaderProgram) fokkerShader.getGdxShaderProgram();
    }

    private void activateShader() {
	Mapping<String, ShaderParameter> params = fokkerShader.listParameters();
	params.print("shader params");
	// Err.reportError("here");
	// fokkerShader.setFloatParameterValue(params.getValueAt(0).getName(),
	// Screen.getScreenWidth());
	// fokkerShader.setFloatParameterValue(params.getValueAt(1).getName(),
	// Screen.getScreenHeight());
	fokkerShader.setFloatParameterValue(params.getValueAt(2).getName(), 1f);
	fokkerShader.setIntParameterValue(params.getValueAt(3).getName(), 0);
	fokkerShader.setIntParameterValue(params.getValueAt(4).getName(), 1);

	// shader.setFloatParameterValue("test", 0.5);

	fokkerShader.setupValues();
    }

    public void render() {
	final float gray = 0.5f;
	Gdx.gl.glClearColor(gray, gray, gray, 1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	int x = 20, y = 20;
	batch.begin();
	for (Sprite sprite : regularSprites) {
	    sprite.draw(batch);
	}
	batch.end();

	// if (compressed_atlas.getLoadMode() ==
	// ATLAS_LOAD_MODE.SECOND_ALPHA_TEXTURE_SHADER) {
	// activateShader();
	// batch.setShader(gdxShader);
	//
	// }
	batch.begin();
	for (Sprite sprite : etc1Sprites) {
	    sprite.draw(batch);
	}
	batch.end();
	batch.setShader(null);
    }

    public void resize(int width, int height) {
	float m = 0.6f;
	batch.setProjectionMatrix(
		new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth() * m, Gdx.graphics.getHeight() * m));
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

}
