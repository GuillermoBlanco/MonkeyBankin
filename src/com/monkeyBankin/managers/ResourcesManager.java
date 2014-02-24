package com.monkeyBankin.managers;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.music.MusicManager;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.audio.sound.SoundManager;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Vibrator;

import com.monkeyBankin.GameActivity;

public class ResourcesManager {
	// -----------------------------
	// CONSTANTS
	// ----------------------------
	public int WIDTH;
	public int HEIGHT;
	public static final int INITIAL_WIDTH = 800;
	public static final int INITIAL_HEIGHT = 480;

	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private static final ResourcesManager INSTANCE = new ResourcesManager();

	public Engine engine;
	public GameActivity activity;
	public BoundCamera camera;
	public Vibrator vibrator;
	public VertexBufferObjectManager vbom;
	public SharedPreferences preferences;
	public SharedPreferences.Editor preferencesEditor;
	
	public Font font,fontLoading;

	// ---------------------------------------------
	// SOUNDS & MUSIC
	// ---------------------------------------------

	public Music mMusic;
	public Sound monkeySound;
	
	// ---------------------------------------------
	// TEXTURES & TEXTURE REGIONS
	// ---------------------------------------------

	public ITextureRegion splash_region,options_region,base_region;

	// Texture Atlas
	public BitmapTextureAtlas mOnScreenControlTexture,onScreenButtonA;
	public BuildableBitmapTextureAtlas gameTextureAtlas,menuTextureAtlas,splashTextureAtlas;


	// Game Texture Regions
	//	public BitmapTextureAtlasTextureRegionFactory mOnScreenControlBaseTextureRegion,mOnScreenControlKnobTextureRegion, onScreenButtonARegion;

	public TextureRegion btn_play, btn_settings,mOnScreenControlBaseTextureRegion,mOnScreenControlKnobTextureRegion, onScreenButtonARegion;
	public ITextureRegion menu_back,banana_region;
	public TiledTextureRegion monkey_region,menu_btt;
	
	
	// ---------------------------------------------
	// PHYSICS
	// ---------------------------------------------
	
	public PhysicsManager mPhysicsWorld;
	
	
	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	public void loadMenuResources() {
		loadMenuGraphics();
		loadMenuFonts();
//		loadMenuAudio();
	}
	
	
	
	private void loadMenuGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		menuTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);

		// RegionTextures
		menu_btt = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				menuTextureAtlas, activity, "botones.png",4,1);
		menu_back=BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				menuTextureAtlas, activity, "back.png");
		
		try {
			this.menuTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.menuTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	private void loadMenuFonts()
	{
	    FontFactory.setAssetBasePath("font/");
	    final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	    
	    fontLoading = FontFactory.createStrokeFromAsset(engine.getFontManager(), mainFontTexture, activity.getAssets(), "AverageMonoBold.ttf", 50f, true, Color.WHITE_ABGR_PACKED_INT, 2f, Color.BLACK_ARGB_PACKED_INT, false);
	    font = FontFactory.createStrokeFromAsset(engine.getFontManager(), mainFontTexture, activity.getAssets(), "AverageMonoBold.ttf", 20f, true, Color.WHITE_ABGR_PACKED_INT, 2f, Color.BLACK_ARGB_PACKED_INT, false);
	    fontLoading.load();
	    font.load();
	}
	
	public void unloadMenuScreen() {
		menuTextureAtlas.unload();
		btn_play = btn_settings = null;
	}
	
	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------
	
	public void loadGameResources() {
		loadGameGraphics();
		loadGameFonts();
		loadGameAudio();
	}

	private void loadGameGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		gameTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 3072, 512,
				TextureOptions.BILINEAR);

		//Crea el contenedor para el joystick en pantalla
        this.mOnScreenControlTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
        //Carga las imagenes en el contenedor anterirormente definido, en este contenedor tendremos 2 texturas o imagenes
        this.mOnScreenControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mOnScreenControlTexture, activity, "onscreen_control_base.png", 0, 0);
        this.mOnScreenControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mOnScreenControlTexture, activity, "onscreen_control_knob.png", 128, 0);
        //Carga la textura
        this.mOnScreenControlTexture.load();
       
        this.onScreenButtonA = new BitmapTextureAtlas(activity.getTextureManager(), 48, 48, TextureOptions.NEAREST);
        this.onScreenButtonARegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(onScreenButtonA, activity, "button_a.png", 0, 0);
        this.onScreenButtonA.load();
  
		
		// RegionTextures

        banana_region=BitmapTextureAtlasTextureRegionFactory.createFromAsset(
        		gameTextureAtlas, activity, "banana.png");
        monkey_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				gameTextureAtlas, activity, "mono.png",17,1);
		base_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				gameTextureAtlas, activity, "base_madera.png");
		try {
			this.gameTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.gameTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}
	
	private void loadGameFonts() {
		font = FontFactory.create(engine.getFontManager(), engine.
				getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT,
				Typeface.NORMAL), 32f, true, Color.BLACK_ARGB_PACKED_INT);
		font.load();
	}
	
	public void unloadGameScreen()
	{
		gameTextureAtlas.unload();
		mOnScreenControlTexture.unload();
		onScreenButtonA.unload();
		
		monkey_region=null;
		banana_region=null;
		base_region=null;
		mOnScreenControlBaseTextureRegion=null;
		mOnScreenControlKnobTextureRegion=null;
		onScreenButtonARegion=null;
	}

	private void loadGameAudio() 
	{
		SoundFactory.setAssetBasePath("sfx/");
		MusicFactory.setAssetBasePath("sfx/");
		
		try {
			monkeySound = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "monkey.mp3");
			mMusic = MusicFactory.createMusicFromAsset(activity.getMusicManager(), activity, "fondo_jungla.mp3");
			mMusic.setLooping(true);
			mMusic.setVolume(0.5f);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}


	public void loadSplashScreen() {

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		splashTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 512, 512,
				TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png");
		try {
			splashTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
					0, 1, 0));
			splashTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	public void unloadSplashScreen() {
		splashTextureAtlas.unload();
		splash_region = null;
	}

	public static void prepareManager(Engine engine, GameActivity activity,
			BoundCamera camera, Vibrator vibrator,SharedPreferences preferences,SharedPreferences.Editor preferencesEditor, VertexBufferObjectManager vbom) {
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vibrator = vibrator;
		getInstance().vbom = vbom;
		getInstance().preferences=preferences;
		getInstance().preferencesEditor=preferencesEditor;
	}

	// ---------------------------------------------
	// GETTERS AND SETTERS
	// ---------------------------------------------

	public static ResourcesManager getInstance() {
		return INSTANCE;
	}
}