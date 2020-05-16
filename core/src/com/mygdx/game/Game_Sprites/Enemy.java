package com.mygdx.game.Game_Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Game_screen.GamingScreen;

public abstract class Enemy extends Sprite{
    protected World gWorld;
    protected GamingScreen gScreen;
    public Vector2 speed;
    public Body BodyB2;

    public Enemy(GamingScreen gamingScreen, float x, float y){
        gWorld = gamingScreen.getGameWorld();
        gScreen = gamingScreen;
        setPosition(x,y);
        defineEnemy();
        speed = new Vector2(1,-2);
        BodyB2.setActive(false);
    }

    public void revSpeed (boolean x, boolean y){
        if (x)
            speed.x = -speed.x;
        if (y)
            speed.y = -speed.y;
    }

    protected abstract void defineEnemy();
    public abstract void headHit(Markio markio);
    public abstract void enemyHit(Enemy enemy);
    public abstract void update(float dt);
}
