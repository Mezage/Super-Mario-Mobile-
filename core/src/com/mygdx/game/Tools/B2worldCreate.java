package com.mygdx.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Game_Sprites.Bricks;
import com.mygdx.game.Game_Sprites.Coins;
import com.mygdx.game.Game_Sprites.Enemy;
import com.mygdx.game.Game_Sprites.Goomba;
import com.mygdx.game.Game_Sprites.Turt;
import com.mygdx.game.Game_screen.GamingScreen;
import com.mygdx.game.MarkioBrothers;

public class B2worldCreate {
    private Array <Goomba> goombas;
    private Array <Turt> turts;

    public B2worldCreate(GamingScreen gamingScreen){
        World gameWorld = gamingScreen.getGameWorld();
        TiledMap Markio_map = gamingScreen.getMap();

        BodyDef bodyDef = new BodyDef();
        PolygonShape poly_shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        //index 1 because that's the index of layer in Tiles , start from 0
        //this creates ground
        for (MapObject mapObject: Markio_map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth()/2)/MarkioBrothers.pixelpm, (rectangle.getY() + rectangle.getHeight()/2)/MarkioBrothers.pixelpm);

            body = gameWorld.createBody(bodyDef);

            poly_shape.setAsBox(rectangle.getWidth()/2/MarkioBrothers.pixelpm,rectangle.getHeight()/2/MarkioBrothers.pixelpm);
            fixtureDef.shape = poly_shape;
            body.createFixture(fixtureDef);

        }
        //creates bricks
        for (MapObject mapObject: Markio_map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){


            new Bricks(gamingScreen,mapObject);
        }

        //creates coins
        for (MapObject mapObject: Markio_map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){


            new Coins(gamingScreen, mapObject);
        }

        //creates pipes
        for (MapObject mapObject: Markio_map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth()/2)/MarkioBrothers.pixelpm, (rectangle.getY() + rectangle.getHeight()/2)/MarkioBrothers.pixelpm);

            body = gameWorld.createBody(bodyDef);

            poly_shape.setAsBox(rectangle.getWidth()/2/MarkioBrothers.pixelpm,rectangle.getHeight()/2/MarkioBrothers.pixelpm);
            fixtureDef.shape = poly_shape;
            fixtureDef.filter.categoryBits = MarkioBrothers.ObjectBit;
            body.createFixture(fixtureDef);
        }

        //create goombas
        goombas = new Array<Goomba>();
        for (MapObject mapObject: Markio_map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

            goombas.add(new Goomba(gamingScreen, rectangle.getX()/MarkioBrothers.pixelpm, rectangle.getY()/MarkioBrothers.pixelpm));
        }
        //create all turts
        turts = new Array<Turt>();
        for (MapObject mapObject: Markio_map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();

            turts.add(new Turt(gamingScreen, rectangle.getX()/MarkioBrothers.pixelpm, rectangle.getY()/MarkioBrothers.pixelpm));
        }
    }

    public Array<Goomba> getGoombas() {
        return goombas;
    }


    public Array<Enemy> getEnemies(){
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(goombas);
        enemies.addAll(turts);
        return enemies;
    }
}
