package com.monkeyBankin.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

import com.monkeyBankin.managers.SceneManager.SceneType;

public class LoadingScene extends BaseScene
{

	@Override
	public void createScene() {
		
		setBackground(new Background(Color.WHITE));
		attachChild(new Text(400, 240, resourcesManager.fontLoading, "Loading...", vbom));
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		return;
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
