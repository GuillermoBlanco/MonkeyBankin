package com.monkeyBankin.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

import com.monkeyBankin.managers.ResourcesManager;
import com.monkeyBankin.managers.SceneManager.SceneType;

public class GameOverScene extends BaseScene {

	@Override
	public void createScene() {

		setBackground(new Background(Color.YELLOW));
		attachChild(new Text(400, 240, resourcesManager.fontLoading, "GAME OVER", vbom));
		
//		createBackground();
//		createElements();		

	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_LOADING;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		detachChildren();
	}
}
