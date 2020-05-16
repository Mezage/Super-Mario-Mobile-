package com.mygdx.game.Game_Sprites.Goodies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.Game_Scenes.HUD;
import com.mygdx.game.Game_Sprites.Markio;
import com.mygdx.game.Game_screen.GamingScreen;
import com.mygdx.game.MarkioBrothers;

public class Mush extends Goody{
    public Mush(GamingScreen gScreen, float x, float y) {
        super(gScreen, x, y);
        setRegion(gScreen.getTextureAtlas().findRegion("mushroom"),0,0,16,16);
        speed = new Vector2(.7f,0);
    }

    @Override
    public void defineGoody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(),getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;        //dynamic means its affected by gravity and can move
        bod = gworld.createBody(bodyDef);

        FixtureDef fix_def = new FixtureDef();
        CircleShape cShape = new CircleShape();
        cShape.setRadius(6/ MarkioBrothers.pixelpm);                            //what mush can collide with
        fix_def.filter.categoryBits = MarkioBrothers.GoodyBit;              // Categorybits defines what this fixture is
        fix_def.filter.maskBits = MarkioBrothers.MarkioBit | MarkioBrothers.GroundBit | MarkioBrothers.ObjectBit |
                MarkioBrothers.BrickBit | MarkioBrothers.CoinBit;

        fix_def.shape = cShape;
        bod.createFixture(fix_def).setUserData(this);

    }

    @Override
    public void useUp(Markio markio) {
        destroy();
        markio.growMarkio();
        HUD.addScore(1000);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(bod.getPosition().x - getWidth()/2, bod.getPosition().y - getHeight()/2);
        speed.y = bod.getLinearVelocity().y;
        bod.setLinearVelocity(speed);
    }
}
