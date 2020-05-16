package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Game_screen.GamingScreen;

public class MarkioBrothers extends Game {
	public SpriteBatch batch;				//holds all images and textures
    public static final int Virtual_H = 210;    //208
    public static final int Virtual_W = 400;
    public static final float pixelpm = 100;
    public static Integer lives = 3;

    //must all be powers of 2
	public static final short NothingBit = 0;
    public static final short GroundBit = 1;
    public static final short MarkioBit = 2;
    public static final short BrickBit = 4;
    public static final short CoinBit = 8;
    public static final short DestroyedBit = 16;
	public static final short ObjectBit = 32;
	public static final short EnemyBit = 64;
	public static final short EnemyHeadBit = 128;
	public static final short GoodyBit = 256;
	public static final short MarkHeadBit = 512;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new GamingScreen(this));
		//img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();		//may have to delete this
		batch.dispose();
		//img.dispose();
	}
}
