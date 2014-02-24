package com.monkeyBankin.managers;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.util.color.Color;

import android.hardware.SensorManager;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class PhysicsManager 
{
	public FixedStepPhysicsWorld mPhysicsFixedWorld;
	
	final FixtureDef WALL_FIXTURE_DEF =	PhysicsFactory.createFixtureDef(0, 0 ,0.5f);
	
	public Body groundWallBody;
	public Body leftWallBody;
	public Body rightWallBody;
	
	public Rectangle ground;
	public Rectangle left;
	public Rectangle right;

	
	public PhysicsManager() {
		// TODO Auto-generated constructor stub
		ResourcesManager resourceManager = ResourcesManager.getInstance();
		
		mPhysicsFixedWorld = new FixedStepPhysicsWorld(60,new Vector2(0f,SensorManager.GRAVITY_EARTH) ,true, 8, 3);
		
		ground = new Rectangle(0, resourceManager.INITIAL_HEIGHT-50,resourceManager.INITIAL_WIDTH, 100,resourceManager.vbom);
		left = new Rectangle(0, -(resourceManager.INITIAL_HEIGHT*2), 2,resourceManager.INITIAL_HEIGHT*4, resourceManager.vbom);
		right = new Rectangle(resourceManager.INITIAL_WIDTH - 6f,-(resourceManager.INITIAL_HEIGHT*2), 8f,resourceManager.INITIAL_HEIGHT*4, resourceManager.vbom);

		ground.setColor(Color.BLACK);
		left.setColor(Color.BLACK);
		right.setColor(Color.BLACK);
	
		groundWallBody = PhysicsFactory.createBoxBody(this.mPhysicsFixedWorld, ground,BodyType.StaticBody, WALL_FIXTURE_DEF);
		leftWallBody = PhysicsFactory.createBoxBody(this.mPhysicsFixedWorld, left, BodyType.StaticBody, WALL_FIXTURE_DEF);
		rightWallBody = PhysicsFactory.createBoxBody(this.mPhysicsFixedWorld, right,BodyType.StaticBody, WALL_FIXTURE_DEF);
		
		groundWallBody.setUserData("Base");
		leftWallBody.setUserData("Pared");
		rightWallBody.setUserData("Pared");
		
		SceneManager.getInstance().getCurrentScene().attachChild(ground);
		SceneManager.getInstance().getCurrentScene().attachChild(left);
		SceneManager.getInstance().getCurrentScene().attachChild(right);
		
	}
	
}

