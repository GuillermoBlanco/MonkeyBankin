package com.monkeyBankin.managers;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class GameManager 
{
	int mCurrentScore, mBirdCount, mEnemyCount;
	
	public static int INITIAL_SCORE=0;
	public static int INITIAL_BIRD_COUNT=2;
	public static int INITIAL_ENEMY_COUNT=0;
//	public static long timer;
	
	private static GameManager INSTANCE;
	
	public GameManager ()
	{
//		timer = new Timestamp(new Date().getDate()).getTime();
	}
	
	public static GameManager getInstance()
	{
		if(INSTANCE == null){
		INSTANCE = new GameManager();
		}
		return INSTANCE;
	}
	
	public int getCurrentScore(){
	return this.mCurrentScore;
	}

	public int getBirdCount(){
	return this.mBirdCount;
	}

	public void incrementScore(int pIncrementBy){
	mCurrentScore += pIncrementBy;
	}

	public void decrementBirdCount(){
	mBirdCount -=1;
	}
	
	public void resetGame(){
	this.mCurrentScore = GameManager.INITIAL_SCORE;
	this.mBirdCount = GameManager.INITIAL_BIRD_COUNT;
	this.mEnemyCount = GameManager.INITIAL_ENEMY_COUNT;
	}
}
