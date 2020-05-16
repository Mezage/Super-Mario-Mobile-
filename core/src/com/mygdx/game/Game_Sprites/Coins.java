package com.mygdx.game.Game_Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Game_Scenes.HUD;
import com.mygdx.game.Game_Sprites.Goodies.GoodyDefinition;
import com.mygdx.game.Game_Sprites.Goodies.Mush;
import com.mygdx.game.Game_screen.GamingScreen;
import com.mygdx.game.MarkioBrothers;

public class Coins extends InteractiveTileObjects{
    private static TiledMapTileSet tileSet;
    private final int usedCoin = 28;

    public Coins(GamingScreen screen, MapObject mapObject){
        super(screen,mapObject);
        tileSet = tiledMap.getTileSets().getTileSet("tilesetgutter"); //may have to use tileset_gutter
        fixture.setUserData(this);
        setCategFilter(MarkioBrothers.CoinBit);
    }

    @Override
    public void HittingHead(Markio markio) {
        Gdx.app.log("Coin","Collision");
        if (getCell().getTile().getId() == usedCoin){}

        else {
            if (mapObject.getProperties().containsKey("mushroom")){
                screen.spawnGoody(new GoodyDefinition(new Vector2(tileBody.getPosition().x, tileBody.getPosition().y + 16/MarkioBrothers.pixelpm),
                    Mush.class));
            }
        }
        getCell().setTile(tileSet.getTile(usedCoin));           //makes coin brick used
        HUD.addScore(100);
    }
}
