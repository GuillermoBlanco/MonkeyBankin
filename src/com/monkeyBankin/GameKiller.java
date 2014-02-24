package com.monkeyBankin;

import java.sql.Timestamp;
import java.util.Date;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.primitive.Vector2;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Transform;
import com.monkeyBankin.managers.ResourcesManager;

public class GameKiller extends Rectangle
{
	private Body killerBody;
	public PhysicsHandler mPhysicsHandler;
	
	public final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0f);

	
	public GameKiller(float pX, float pY, float pWidth, float pHeight,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
		// TODO Auto-generated constructor stub
		
		setColor(Color.BLUE);
		setAlpha(0.2f);
	}
	
	public void createPhysics(PhysicsWorld physicsWorld)
	{

		killerBody = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, playerFixtureDef);
		killerBody.setUserData("Killer");
		
		mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.mPhysicsHandler);
		
        
	    physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, killerBody, true, false)
	    {
	        @Override
	        public void onUpdate(float pSecondsElapsed) //OnUpdateCamera 0.1
	        {
	            super.onUpdate(pSecondsElapsed);
	            ResourcesManager.getInstance().camera.onUpdate(0.1f);
	            
//	            setY(getY()-(float)((new Timestamp(new Date().getDate()).getTime() - GameManager.timer)*0.2));	            
	            
	        }
	    });
	    
	    killerBody.setGravityScale(0);
	}
	
	public Body getKillerBody() {
		return killerBody;
	}
}
