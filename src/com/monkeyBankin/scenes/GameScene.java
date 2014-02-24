package com.monkeyBankin.scenes;

import java.sql.Timestamp;
import java.util.Date;

import javax.xml.validation.TypeInfoProvider;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSCounter;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.monkeyBankin.GameKiller;
import com.monkeyBankin.GameObject;
import com.monkeyBankin.GamePlayer;
import com.monkeyBankin.managers.GameManager;
import com.monkeyBankin.managers.PhysicsManager;
import com.monkeyBankin.managers.ResourcesManager;
import com.monkeyBankin.managers.SceneManager;
import com.monkeyBankin.managers.SceneManager.SceneType;

public class GameScene extends BaseScene implements IOnSceneTouchListener {

	private HUD gameHUD;
	private ButtonSprite btn_a;
	
	public PhysicsManager mPhysicsWorld;
	
	public TimerHandler avanceKiller;
	public final float RAPIDO=0.75f;
	public final float LENTO=0.5f;
	
	public GamePlayer monkey;
	public GameKiller killer;
	public Sprite banana;
	public Sprite base,base1,base2,base3,base4,base5,base6,base7,base8;
	
	private static final float initX=100, initY=(ResourcesManager.getInstance().INITIAL_HEIGHT)-300;
	private static final int LAYER_BG = 0, LAYER_FG = 1;
	
	public int altura=0;
	public int alturaMax;
	public Text puntuacio,vidas;
	public FPSCounter fpsCounter;
	
	public boolean die=false;
	public boolean goal=false;

	@Override
	public void createScene() 
	{
		attachChild(new Entity());
		attachChild(new Entity());
		
		createPhysics();
		createBackground();
		createHUD();
		createPlayer();
		createKiller();
		createGoal();

		ResourcesManager.getInstance().camera.setBounds(0, -(ResourcesManager.getInstance().INITIAL_HEIGHT*2), ResourcesManager.getInstance().INITIAL_WIDTH, ResourcesManager.getInstance().INITIAL_HEIGHT);
		ResourcesManager.getInstance().camera.setBoundsEnabled(true);
		ResourcesManager.getInstance().mMusic.play();
		setOnSceneTouchListener(this);
	}
	
	private void createPhysics()
	{
		mPhysicsWorld = new PhysicsManager();
		registerUpdateHandler(mPhysicsWorld.mPhysicsFixedWorld);
		
		mPhysicsWorld.mPhysicsFixedWorld.setContactListener(new ContactListener() {
			 
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub				
			}
			
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub

			}
			
			@Override
			public void endContact(Contact contact) {
				// TODO Auto-generated method stub
				final Fixture x1 = contact.getFixtureA();
	            final Fixture x2 = contact.getFixtureB();
	            
	            // PLAYER Y BASE
	            if (x2.getBody().getUserData().equals("Player")&&(x1.getBody().getUserData().equals("Base") || x1.getBody().getUserData().equals("Base_falsa")))
	            {
	                Log.i("ACTION!!", "PLAYER IS ON AIR");
	                monkey.contacts--;
	                System.out.println(monkey.contacts+"");
	            }
			}
			
			@Override
			public void beginContact(Contact contact) {
				// TODO Auto-generated method stub
				final Fixture x1 = contact.getFixtureA();
	            final Fixture x2 = contact.getFixtureB();
	            
	            // PLAYER Y BASE
	            if (x2.getBody().getUserData().equals("Player")&&x1.getBody().getUserData().equals("Base"))
	            {
	                Log.i("CONTACT!!", "PLAYER AT STATIC");
	                if(monkey.isJumping)
	                {
	                	monkey.isJumping=false;
	                }
	                monkey.contacts++;
	                System.out.println(monkey.contacts+"");
	            }
	            
	            if (x2.getBody().getUserData().equals("Player")&&x1.getBody().getUserData().equals("Base_falsa"))
	            {
	                Log.i("CONTACT!!", "PLAYER AT STATIC FALSE");
	                if(monkey.isJumping)
	                {
	                	monkey.isJumping=false;
	                }
	                monkey.contacts++;
	                
	                //CAIDA DE LA BASE
	                engine.registerUpdateHandler(new TimerHandler(5f, new ITimerCallback()
	                {                                    
	                    public void onTimePassed(final TimerHandler pTimerHandler)
	                    {
	                        pTimerHandler.reset();
	                        engine.unregisterUpdateHandler(pTimerHandler);
	                        x1.getBody().setType(BodyType.DynamicBody);
	                    }
	                }));
	            }
	            
//	            // PLAYER Y KILLER (IF KILLER_PHYSICS)
//	            if (x2.getBody().getUserData().equals("Killer")&&x1.getBody().getUserData().equals("Player"))
//	            {
//	            	monkey.onDie();
//	            }
			}
		});

	}

	private void createKiller()
	{
		killer = new GameKiller(0, ResourcesManager.INITIAL_HEIGHT-20, ResourcesManager.INITIAL_WIDTH, ResourcesManager.INITIAL_HEIGHT, vbom)
		{
			@Override
		    protected void onManagedUpdate(final float pSecondsElapsed) 
		    {
		       	super.onManagedUpdate(pSecondsElapsed);
		       	
		       	//KILL PLAYER
		       	if (collidesWith(monkey)&& !die)
		       	{
		       		die=true;
		       		System.out.println("COLLIDES");
		       		monkey.onDie();
		       		engine.unregisterUpdateHandler(avanceKiller);
		       		if (ResourcesManager.getInstance().preferences.getInt("altura", 0)<alturaMax) 
		       		{
		       			ResourcesManager.getInstance().preferencesEditor.putInt("altura", alturaMax);
		       			ResourcesManager.getInstance().preferencesEditor.commit();
		       			
		       			SceneManager.getInstance().gameToast("Has marcado un nuevo record!!!\nAltura m�xima: "+alturaMax+"\n\nInt�ntalo de nuevo!!");
		       		}
		       		if (GameManager.getInstance().getBirdCount()<1)
		       		{
		       			registerUpdateHandler(new TimerHandler(5f, new ITimerCallback() {
		    				@Override
		    				public void onTimePassed(TimerHandler pTimerHandler) { 
		    					unregisterUpdateHandler(pTimerHandler);
				       			disposeScene();
				       			SceneManager.getInstance().loadGameOverScene(resourcesManager.engine);
		    				}
		    			}));

		       		}
		       		
		       	}

		}};

//		//KILLER WITH NO PHYSICS
//		killer.createPhysics(mPhysicsWorld.mPhysicsFixedWorld);
		
		avanceKiller = new TimerHandler(LENTO, new ITimerCallback()
        {                                    
            public void onTimePassed(final TimerHandler pTimerHandler)
            {
                pTimerHandler.reset();
//              killer.setY(killer.getY()-(float)((new Timestamp(new Date().getDate()).getTime() - GameManager.timer)*0.2));
//            	System.out.println(""+(killer.getY()-(float)((new Timestamp(new Date().getDate()).getTime() - GameManager.timer)*0.2)));
//              
                //DESPLAZAMIENTO KILLER
                killer.setY(killer.getY()-1);
            }
        });
		
//		// CAMBIAR VELOCIDAD KILLER
//		avanceKiller.setTimerSeconds(RAPIDO);
		
		engine.registerUpdateHandler(avanceKiller);    	            
		this.getChildByIndex(LAYER_FG).attachChild(killer);
	}
	
	private void createPlayer() 
	{	
		monkey = new GamePlayer(initX, initY,
				ResourcesManager.getInstance().monkey_region,ResourcesManager.getInstance().camera,mPhysicsWorld.mPhysicsFixedWorld,ResourcesManager.getInstance().vbom);
        monkey.setStop();
		camera.setChaseEntity(monkey);
		
		this.getChildByIndex(LAYER_FG).attachChild(monkey);
	}
	
	private void createGoal()
	{
		banana= new Sprite(150, -550, ResourcesManager.getInstance().banana_region, ResourcesManager.getInstance().vbom){
			@Override
		    protected void onManagedUpdate(final float pSecondsElapsed) 
		    {
		       	super.onManagedUpdate(pSecondsElapsed);
		       	
		       	//KILL PLAYER
		       	if (collidesWith(monkey)&&!goal)
		       	{
		       		monkey.getPlayerBody().setActive(false);
		       		monkey.setScale(3);
		       		
		       		goal=true;
		       		System.out.println("GOAL!!!");
		       		engine.unregisterUpdateHandler(avanceKiller);
	       			
		       		SceneManager.getInstance().gameToast("Has alcanzado la banana!!!!!");

		       		if (GameManager.getInstance().getBirdCount()<1)
		       		{
		       			registerUpdateHandler(new TimerHandler(5f, new ITimerCallback() {
		    				@Override
		    				public void onTimePassed(TimerHandler pTimerHandler) { 
		    					unregisterUpdateHandler(pTimerHandler);
				       			disposeScene();
				       			SceneManager.getInstance().loadGameOverScene(resourcesManager.engine);
		    				}
		    			}));
		       		}
		       	}
		}};
		
		getChildByIndex(LAYER_FG).attachChild(banana);
	}

	private void createHUD() {
	
			gameHUD = new HUD();
	
			puntuacio = new Text(20, 20, ResourcesManager.getInstance().font, "",
					100, ResourcesManager.getInstance().vbom);
	//		puntuacio.setText("Score: "+GameManager.getInstance().getCurrentScore()+" points"+"\nLives: "+GameManager.getInstance().getBirdCount());
			
	//		vidas = new Text(500, 20, ResourcesManager.getInstance().font, "",
	//				100, ResourcesManager.getInstance().vbom);
	//		puntuacio.setText("Lives: "+GameManager.getInstance().getBirdCount());
			
			fpsCounter = new FPSCounter();
			engine.registerUpdateHandler(fpsCounter);
			registerUpdateHandler(new TimerHandler(1/20f, new ITimerCallback() {
				@Override
				public void onTimePassed(TimerHandler pTimerHandler) { //Actualiza cada frames
					pTimerHandler.reset();
	//				puntuacio.setText("FPS: " + fpsCounter.getFPS());
					
					int altura = ((int) (ResourcesManager.getInstance().INITIAL_HEIGHT-camera.getCenterY()))/20;
					puntuacio.setText(" Altura ______"+altura+" mts\n|\n|\n|\n|\n|__");
					if (altura>alturaMax) alturaMax=altura;
//					Log.d("ESQ", "onTimePAssed "+pTimerHandler.getTimerSecondsElapsed());
				}
			}));
				
			createControls();
			
	        btn_a = new ButtonSprite(this.camera.getWidth() - 80, this.camera.getHeight() - 80, ResourcesManager.getInstance().onScreenButtonARegion, vbom, new OnClickListener(){
	
	            @Override
	            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
	            	System.out.println("SALTA!!!!!!");
	            	
	            	if(monkey.contacts>0)
		            {
	            		Log.i("ACTION!!", "PLAYER IS JUMPING");
	            		monkey.jump();
		            }
	            }
	            
	            
	        });
	        
	        
	        gameHUD.attachChild(btn_a);
			gameHUD.attachChild(puntuacio);
	//		gameHUD.attachChild(vidas);
			
			gameHUD.registerTouchArea(btn_a);
			camera.setHUD(gameHUD);
		}

	private void createControls()
	{
	    final DigitalOnScreenControl digitalOnScreenControl = new DigitalOnScreenControl(10, this.camera.getHeight() - ResourcesManager.getInstance().mOnScreenControlBaseTextureRegion.getHeight() - 10, this.camera, ResourcesManager.getInstance().mOnScreenControlBaseTextureRegion, ResourcesManager.getInstance().mOnScreenControlKnobTextureRegion, 0.1f, vbom, new IOnScreenControlListener() {
	
	        @Override
	        public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
	
	        	if (monkey.contacts>0)
	        	{
	                if(pValueX != 0){
	                	
	                	monkey.setRunning(pValueX);
	                }
	                else
	                {
	                	monkey.setStop();
	                	monkey.isRunning=false;
	                }
	        	}         
	        }
	    });
	
	    digitalOnScreenControl.getControlBase().setAlpha(0.5f);
	    digitalOnScreenControl.getControlBase().setScaleCenter(0, 128);
	    digitalOnScreenControl.getControlBase().setScale(1f);
	    digitalOnScreenControl.getControlKnob().setScale(1f);
	    digitalOnScreenControl.refreshControlKnobPosition();
	
	    setChildScene(digitalOnScreenControl);
	}
	
	@Override
	public void onBackKeyPressed() 
	{
		disposeScene();
		SceneManager.getInstance().loadMenuScene(resourcesManager.engine);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene() {
		camera.setHUD(null);
		camera.setChaseEntity(null); // TODO
		camera.setCenter(200, 400);
		
		ResourcesManager.getInstance().mMusic.play();
	}
	private void createBackground() { //Background objects (BASE)

		setBackground(new Background(Color.YELLOW));
		
		base = new GameObject(GameObject.TIPO_BASE.FIJA,275, 300f, resourcesManager.base_region,mPhysicsWorld.mPhysicsFixedWorld, vbom);
		this.getChildByIndex(LAYER_FG).attachChild(base);
		
		base1 = new GameObject(GameObject.TIPO_BASE.FIJA,600, 180, resourcesManager.base_region,mPhysicsWorld.mPhysicsFixedWorld, vbom);
		this.getChildByIndex(LAYER_FG).attachChild(base1);
		
		base2 = new GameObject(GameObject.TIPO_BASE.FIJA,275, 80, resourcesManager.base_region,mPhysicsWorld.mPhysicsFixedWorld, vbom);
		this.getChildByIndex(LAYER_FG).attachChild(base2);

		base3 = new GameObject(GameObject.TIPO_BASE.FIJA,0, 180f, resourcesManager.base_region,mPhysicsWorld.mPhysicsFixedWorld, vbom);
		this.getChildByIndex(LAYER_FG).attachChild(base3);
		
		base4 = new GameObject(GameObject.TIPO_BASE.FIJA,600, -20, resourcesManager.base_region,mPhysicsWorld.mPhysicsFixedWorld, vbom);
		this.getChildByIndex(LAYER_FG).attachChild(base4);
		
		base5 = new GameObject(GameObject.TIPO_BASE.FIJA,300, -120, resourcesManager.base_region,mPhysicsWorld.mPhysicsFixedWorld, vbom);
		this.getChildByIndex(LAYER_FG).attachChild(base5);
		
		base6 = new GameObject(GameObject.TIPO_BASE.FIJA,80, -200f, resourcesManager.base_region,mPhysicsWorld.mPhysicsFixedWorld, vbom);
		this.getChildByIndex(LAYER_FG).attachChild(base6);

		base7 = new GameObject(GameObject.TIPO_BASE.FIJA,400, -300f, resourcesManager.base_region,mPhysicsWorld.mPhysicsFixedWorld, vbom);
		this.getChildByIndex(LAYER_FG).attachChild(base7);
		
		base8 = new GameObject(GameObject.TIPO_BASE.FIJA,150, -450f, resourcesManager.base_region,mPhysicsWorld.mPhysicsFixedWorld, vbom);
		this.getChildByIndex(LAYER_FG).attachChild(base8);
//		
//		base4 = new GameObject(GameObject.TIPO_BASE.FALSA,300, 650f, resourcesManager.base_region,mPhysicsWorld.mPhysicsFixedWorld, vbom);
//		this.getChildByIndex(LAYER_FG).attachChild(base4);

	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		return false;
	}
}
