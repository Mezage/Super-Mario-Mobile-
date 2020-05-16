package com.mygdx.game;

import android.os.Bundle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.mygdx.game.MarkioBrothers;


public class AndroidLauncher extends AndroidApplication{
	@Override
    protected void onCreate(Bundle savedInstanceState){
	    super.onCreate(savedInstanceState);
	    AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
	    initialize(new MarkioBrothers(),config);
    }

}