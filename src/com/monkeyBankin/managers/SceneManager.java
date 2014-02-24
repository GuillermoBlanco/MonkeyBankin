package com.monkeyBankin.managers;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import android.util.Log;
import android.widget.Toast;

import com.monkeyBankin.scenes.BaseScene;
import com.monkeyBankin.scenes.GameOverScene;
import com.monkeyBankin.scenes.GameScene;
import com.monkeyBankin.scenes.LoadingScene;
import com.monkeyBankin.scenes.MenuMonkeyScene;
import com.monkeyBankin.scenes.SplashScene;

public class SceneManager {

	// ---------------------------------------------
	// SCENES
	// ---------------------------------------------

	private BaseScene splashScene;
	private BaseScene gameScene;
	private BaseScene menuScene;
	private BaseScene currentScene;
	private BaseScene loadingScene;
	private BaseScene gameoverScene;
	

	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------

	private static final SceneManager INSTANCE = new SceneManager();

	private SceneType currentSceneType = SceneType.SCENE_SPLASH;
	

	private Engine engine = ResourcesManager.getInstance().engine;

	public enum SceneType {
		SCENE_SPLASH, SCENE_MENU, SCENE_GAME, SCENE_LOADING,
	}

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	public void setScene(BaseScene scene) {
		engine.setScene(scene);
		currentScene = scene;
		currentSceneType = scene.getSceneType();
	}

	public void setScene(SceneType sceneType) {
		switch (sceneType) {

		case SCENE_GAME:
			setScene(gameScene);
			break;
		case SCENE_SPLASH:
			setScene(splashScene);
			break;
		case SCENE_MENU:
			setScene(menuScene);
			break;
		case SCENE_LOADING:
			setScene(loadingScene);
			break;
		default:
			break;
		}
	}

	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback) {

		ResourcesManager.getInstance().loadSplashScreen();
		splashScene = new SplashScene();
		currentScene = splashScene;

		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}
	
	public void disposeSplashScene() {
		ResourcesManager.getInstance().unloadSplashScreen();
		splashScene.disposeScene();
		splashScene = null;
	}
	
	private void disposeMenuScene() {
		ResourcesManager.getInstance().unloadMenuScreen();
		menuScene.disposeScene();
		menuScene = null;
	}
	
	private void disposeGameScene() {
		ResourcesManager.getInstance().unloadGameScreen();
		gameScene.disposeScene();
		gameScene = null;
	}

	public void loadGameScene(final Engine mEngine) {

		setScene(loadingScene);
		ResourcesManager.getInstance().unloadMenuScreen();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				// TODO Auto-generated method stub
				
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourcesManager.getInstance().loadGameResources();
				gameScene = new GameScene();
				setScene(gameScene);
			}
		}));
		
		

	}
	
	public void loadMenuScene(final Engine mEngine) {

		ResourcesManager.getInstance().loadMenuResources();
		loadingScene = new LoadingScene();
		menuScene = new MenuMonkeyScene();
		
		setScene(menuScene);

	}

	public void loadGameOverScene(final Engine mEngine) {

		//GAME_OVER_SCENE
		gameoverScene = new GameOverScene();
		setScene(gameoverScene);
		ResourcesManager.getInstance().unloadGameScreen();
		mEngine.registerUpdateHandler(new TimerHandler(3f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				// TODO Auto-generated method stub
				
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourcesManager.getInstance().loadMenuResources();
				menuScene = new MenuMonkeyScene();
				setScene(menuScene);
			}
		}));		

	}
	
	public void gameToast(final String msg)
	{
		ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(ResourcesManager.getInstance().activity, msg, Toast.LENGTH_LONG).show();
			}
		});
	}
	
	// ---------------------------------------------
	// GETTERS AND SETTERS
	// ---------------------------------------------

	public static SceneManager getInstance() {
		return INSTANCE;
	}

	public SceneType getCurrentSceneType() {
		return currentSceneType;
	}

	public BaseScene getCurrentScene() {
		return currentScene;
	}
	
	
	
	
	
	
	
	
	
	
}
