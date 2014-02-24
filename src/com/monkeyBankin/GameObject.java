package com.monkeyBankin;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.hardware.SensorManager;
import android.location.Address;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.monkeyBankin.managers.ResourcesManager;


public class GameObject extends Sprite
{

	private Body objectBody;
	public PhysicsHandler mPhysicsHandler;
	public ContactListener playerContact;
	
	public static tipo TIPO_BASE;
	public enum tipo{FIJA,FALSA};
	
	public GameObject(tipo TIPO_BASE,float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,PhysicsWorld physicsWorld,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		// TODO Auto-generated constructor stub
		
		this.TIPO_BASE=TIPO_BASE;
		createPhysics(physicsWorld);

//		createContactListener();
	}
	
	public GameObject(tipo TIPO_BASE,float pX, float pY, ITextureRegion base_region, PhysicsWorld physicsWorld,
			VertexBufferObjectManager vbom) {
		// TODO Auto-generated constructor stub
		super(pX, pY, base_region, vbom);
		
		this.TIPO_BASE=TIPO_BASE;
		createPhysics(physicsWorld);
	}

	public Body getObjectBody() {
		return objectBody;
	}

	public void setObjectBody(Body playerBody) {
		this.objectBody = playerBody;
	}

	public void createPhysics(PhysicsWorld physicsWorld)
	{
		final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0, 0f);
		objectBody = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.StaticBody, objectFixtureDef);
		
		if(TIPO_BASE==tipo.FIJA) objectBody.setUserData("Base");
		else objectBody.setUserData("Base_falsa");
		
		mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.mPhysicsHandler);
		
	    physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, objectBody, true, false)
	    {
	        @Override
	        public void onUpdate(float pSecondsElapsed) //VACIO
	        {
	            super.onUpdate(pSecondsElapsed);
	            ResourcesManager.getInstance().camera.onUpdate(0.1f);
	        }
	    });
	}
	
//	public void createContactListener() //VACIO
//	{
//	}
}
