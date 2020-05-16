package com.mygdx.game.Game_Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Game_screen.GamingScreen;
import com.mygdx.game.MarkioBrothers;

public abstract class InteractiveTileObjects {
    protected World world;
    protected TiledMap tiledMap;
    protected TiledMapTile tile;
    protected Rectangle rectBounds;
    protected Body tileBody;
    protected GamingScreen screen;

    protected MapObject mapObject;
    protected Fixture fixture;

    public InteractiveTileObjects(GamingScreen screen, MapObject mapObject){
        this.mapObject = mapObject;
        this.screen = screen;
        this.world = screen.getGameWorld();
        this.tiledMap = screen.getMap();
        this.rectBounds = ((RectangleMapObject) mapObject).getRectangle();

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixDef = new FixtureDef();
        PolygonShape polyshape = new PolygonShape();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((rectBounds.getX() + rectBounds.getWidth()/2)/ MarkioBrothers.pixelpm, (rectBounds.getY() + rectBounds.getHeight()/2)/MarkioBrothers.pixelpm);

        tileBody = world.createBody(bodyDef);

        polyshape.setAsBox(rectBounds.getWidth()/2/MarkioBrothers.pixelpm,rectBounds.getHeight()/2/MarkioBrothers.pixelpm);
        fixDef.shape = polyshape;
        fixture = tileBody.createFixture(fixDef);
    }

    public abstract void HittingHead(Markio markio);
    public void setCategFilter (short filterBit){
        Filter filter = new Filter();           //if you don't have access to the fixture def
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(1);

        //box2D body is scaled down, so we have to scale back up
        return tileLayer.getCell((int)(tileBody.getPosition().x * MarkioBrothers.pixelpm / 16),
                (int) (tileBody.getPosition().y * MarkioBrothers.pixelpm /16));
    }

}
