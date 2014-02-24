package com.monkeyBankin;

import org.andengine.engine.Engine;
import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.ui.activity.BaseGameActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.monkeyBankin.managers.PhysicsManager;
import com.monkeyBankin.managers.ResourcesManager;
import com.monkeyBankin.managers.SceneManager;
import com.monkeyBankin.managers.SceneManager.SceneType;

public class GameActivity extends BaseGameActivity {
	// -----------------------------
	// CONSTANTS
	// ----------------------------

	private final static float SPLASH_TIME = 5f;

	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private BoundCamera camera;
	private Vibrator vibrator;
	
	public SharedPreferences preferences;
	public static final String RECORDS = "records";
	public SharedPreferences.Editor preferencesEditor;
	
	
	@Override
	protected void onCreate(Bundle pSavedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(pSavedInstanceState);
		
		preferences = getSharedPreferences(RECORDS, 0);
		preferencesEditor = preferences.edit();
	}

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		return new FixedStepEngine(pEngineOptions, 60);
		
	}

	@Override
	public EngineOptions onCreateEngineOptions() {

		camera = new BoundCamera(0, 0, ResourcesManager.INITIAL_WIDTH,
				ResourcesManager.INITIAL_HEIGHT);
		EngineOptions eo = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						ResourcesManager.INITIAL_WIDTH,
						ResourcesManager.INITIAL_HEIGHT), camera);
		
		// LOAD VIBRATOR
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		eo.getTouchOptions().setNeedsMultiTouch(true);
		eo.getAudioOptions().setNeedsMusic(true);
		eo.getAudioOptions().setNeedsSound(true);
		
		return eo;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {

		ResourcesManager.prepareManager(mEngine, this, camera, vibrator,preferences,preferencesEditor,
				this.getVertexBufferObjectManager());
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {

		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {

		mEngine.registerUpdateHandler(new TimerHandler(SPLASH_TIME,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						Log.d("ESQ","GameActivity.onPopulateScene.TimerCallBack.onTimePassed");
						mEngine.unregisterUpdateHandler(pTimerHandler);

						// load menu resources, create menu scene
						// set menu scene using scene manager
						SceneManager.getInstance().loadMenuScene(mEngine);
						SceneManager.getInstance().setScene(
								SceneType.SCENE_MENU);
						SceneManager.getInstance().disposeSplashScene();

					}
				}));
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{  
	    if (keyCode == KeyEvent.KEYCODE_BACK)
	    {
	        SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
	    }
	    return false; 
	}

	@Override
	public synchronized void onResumeGame() 
	{
		if(ResourcesManager.getInstance().mMusic != null && !ResourcesManager.getInstance().mMusic.isPlaying()){
		ResourcesManager.getInstance().mMusic.play();
		}
		super.onResumeGame();
	}
	
	@Override
	public synchronized void onPauseGame() 
	{
		if(ResourcesManager.getInstance().mMusic != null && ResourcesManager.getInstance().mMusic.isPlaying()){
		ResourcesManager.getInstance().mMusic.pause();
		}
		super.onPauseGame();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(this.isGameLoaded())
		{
			System.exit(0);
		}
	}
}
