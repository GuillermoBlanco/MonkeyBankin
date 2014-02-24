package com.monkeyBankin.scenes;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.AnimatedSpriteMenuItem;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.color.Color;

import android.location.Address;

import com.monkeyBankin.R.menu;
import com.monkeyBankin.managers.ResourcesManager;
import com.monkeyBankin.managers.SceneManager;
import com.monkeyBankin.managers.SceneManager.SceneType;

public class MenuMonkeyScene extends BaseScene implements IOnMenuItemClickListener {

	public MenuScene     menuScene;
	
	private final int     MENU_PLAY = 0;
	private final int     MENU_OPT = 1;
	
	public SpriteMenuItem btnPlaySprite, btnSettSprite;
	public SpriteBackground backMenu;
	public AnimatedSpriteMenuItem btnPlayASprite, btnInfoASprite;

	@Override
	public void createScene() {

		menuScene = new MenuScene(ResourcesManager.getInstance().camera);
		setChildScene(menuScene);
		
		createBackground();
		createElements();		

	}

	private void createElements() {

		
		btnPlayASprite= new AnimatedSpriteMenuItem(MENU_PLAY, ResourcesManager.getInstance().menu_btt, ResourcesManager.getInstance().vbom);
		btnInfoASprite= new AnimatedSpriteMenuItem(MENU_OPT, ResourcesManager.getInstance().menu_btt, ResourcesManager.getInstance().vbom);
		
		btnPlayASprite.animate(new long[] {100,0}, 0, 1, false);
		btnInfoASprite.animate(new long[] {100,0}, 2, 3, false);
		
		btnPlayASprite.setPosition((ResourcesManager.getInstance().INITIAL_WIDTH/2) -200, 250);
		btnInfoASprite.setPosition((ResourcesManager.getInstance().INITIAL_WIDTH/2) -200, 350);
		
		menuScene.addMenuItem(btnPlayASprite);
		menuScene.addMenuItem(btnInfoASprite);

		menuScene.setBackgroundEnabled(false);
		menuScene.setOnMenuItemClickListener(this);

	}

	@Override
	public void onBackKeyPressed() 
	{
			System.exit(0);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_MENU;
	}

	@Override
	public void disposeScene() {
		camera.setHUD(null);
		camera.setChaseEntity(null); // TODO
		camera.setCenter(200, 400);

	}


	private void createBackground() {

		attachChild(new Sprite(0, 0, ResourcesManager.getInstance().menu_back, ResourcesManager.getInstance().vbom)
		{
			
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				// TODO Auto-generated method stub
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		// TODO Auto-generated method stub
		  switch (pMenuItem.getID())
		  {
		   case MENU_PLAY:
		    // when click play
			btnPlayASprite.animate(new long[] {0,100}, 0, 1, false);
			SceneManager.getInstance().loadGameScene(resourcesManager.engine);
		    return true;
		   case MENU_OPT:
		    // when click options
			btnInfoASprite.animate(new long[] {0,300}, 2, 3, false);
			SceneManager.getInstance().gameToast("El récord de altura es: "+ResourcesManager.getInstance().preferences.getInt("altura", 0));
		    return true;
		   default:
			return false;
		  }
		
	}

}
