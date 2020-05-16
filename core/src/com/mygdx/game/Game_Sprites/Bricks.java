package com.mygdx.game.Game_Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Game_Scenes.HUD;
import com.mygdx.game.Game_screen.GamingScreen;
import com.mygdx.game.MarkioBrothers;


public class Bricks extends InteractiveTileObjects {
    public Bricks (GamingScreen screen, MapObject mapObject){
        super(screen,mapObject);
        fixture.setUserData(this);
        setCategFilter(MarkioBrothers.BrickBit);
    }

    @Override
    public void HittingHead(Markio markio) {
        if (markio.isBig()){
            Gdx.app.log("Brick","Collision");
            setCategFilter(MarkioBrothers.DestroyedBit);
            getCell().setTile(null);
            HUD.addScore(200);
        }
    }
}
