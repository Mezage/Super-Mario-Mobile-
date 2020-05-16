package com.mygdx.game.Game_Sprites.Goodies;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Game_Sprites.Markio;
import com.mygdx.game.Game_screen.GamingScreen;
import com.mygdx.game.MarkioBrothers;

public abstract class Goody extends Sprite {
    protected GamingScreen gScreen;
    protected World gworld;
    protected Vector2 speed;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body bod;

    public Goody (GamingScreen gScreen, float x, float y){
        this.gScreen = gScreen;
        gworld = gScreen.getGameWorld();
        setPosition(x,y);
        setBounds(getX(), getY(), 16/ MarkioBrothers.pixelpm, 16/MarkioBrothers.pixelpm);
        defineGoody();
        toDestroy = false;
        destroyed = false;
    }

    public abstract void defineGoody();
    public abstract void useUp(Markio markio);

    public void update(float dt){
        if (toDestroy && !destroyed){
            gworld.destroyBody(bod);
            destroyed = true;
        }
    }

    public void destroy(){
        toDestroy = true;
    }

    public void draw(Batch batch){
        if (!destroyed)
            super.draw(batch);
    }

    public void revSpeed (boolean x, boolean y){
        if (x)
            speed.x = -speed.x;
        if (y)
            speed.y = -speed.y;
    }
}
