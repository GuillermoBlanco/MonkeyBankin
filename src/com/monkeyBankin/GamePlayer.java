package com.monkeyBankin;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Transform;
import com.monkeyBankin.managers.GameManager;
import com.monkeyBankin.managers.ResourcesManager;


public class GamePlayer extends AnimatedSprite
{
	private Body playerBody;
	private PhysicsWorld physicsWorld;
	private PhysicsConnector playerPhysicsConector;
	public PhysicsHandler mPhysicsHandler;
	
	public final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(3, 0, 0f);
	public final FixtureDef playerOnDieFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0f);
	
	public final long[] PLAYER_STOP = new long[] {100,0};
	public final long[] PLAYER_RUN = new long[] { 150, 150, 150, 150, 150, 150, 150};
	public final long[] PLAYER_JUMP = new long[] {100,100,100,250,250,200,100,100};
	
	public boolean isJumping=false;
	public boolean isRunning=false;
	public boolean isFalling=false;
	
	public int contacts;
	
	public GamePlayer(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion, Camera camera,PhysicsWorld physicsWorld,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		// TODO Auto-generated constructor stub
		this.physicsWorld= physicsWorld;
		createPhysics();
	}
	
	public Body getPlayerBody() {
		return playerBody;
	}


	public void createPhysics()
	{
		
		playerBody = PhysicsFactory.createCircleBody(physicsWorld, this, BodyType.DynamicBody, playerFixtureDef);
		playerBody.setUserData("Player");
		playerBody.setLinearDamping(3f);
		
		mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.mPhysicsHandler);
	    
	    playerPhysicsConector = new PhysicsConnector(this, playerBody, true, false)
	    {
	        @Override
	        public void onUpdate(float pSecondsElapsed) //OnUpdateCamera 0.1
	        {
	            super.onUpdate(pSecondsElapsed);
	            ResourcesManager.getInstance().camera.onUpdate(0.1f);
	            
	        }
	    };
	    
	    physicsWorld.registerPhysicsConnector(playerPhysicsConector);
	}
	
	public void setRunning(float valueX)
	{
		if (!isRunning)
		{
			animate(PLAYER_RUN, 2, 8, true);
			if (valueX>0) setFlippedHorizontal(false); //Animacion derecha   
			else setFlippedHorizontal(true); //Animacion izquierda 

		}
	    
        Vector2 velocity = new Vector2(valueX * 3,0);
        getPlayerBody().setLinearVelocity(velocity);
        isRunning=true;
	}
	
	public void jump()
	{
		if (contacts > 0) 
		{
			animate(PLAYER_JUMP, 9, 16, false);
			
			if (getPlayerBody().getLinearVelocity().x>0) setFlippedHorizontal(false); //Animacion salto derecha   
			else setFlippedHorizontal(true);	//Animacion salto izquierda
			
			Vector2 velocity = new Vector2((getPlayerBody().getLinearVelocity().x)*10, -20);
			getPlayerBody().setLinearVelocity(velocity);
			isJumping=true;
		}
          
	}

	public void setStop()
	{
		animate(PLAYER_STOP, 0, 1, true);
		isRunning=false;
	}
	
	public void onDie()
	{
		physicsWorld.unregisterPhysicsConnector(playerPhysicsConector);
		
		registerEntityModifier(new MoveModifier(20f, getX(), getX(), getY(), getY()+380));
		
		GameManager.getInstance().decrementBirdCount();
		ResourcesManager.getInstance().vibrator.vibrate(100);
	}
}
