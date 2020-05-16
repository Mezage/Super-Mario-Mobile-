package com.mygdx.game.Game_Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Game_Scenes.HUD;
import com.mygdx.game.Game_screen.GamingScreen;
import com.mygdx.game.MarkioBrothers;

public class Goomba extends Enemy{
    private float stateTime;
    private Animation <TextureRegion> walkingAnim;
    private Array <TextureRegion> frames;

    //placeholders to destroy body, cant destroy body midgame without
    // calculation errors for other colliding body
    private boolean set2Destroy;
    private boolean destroyedEnemy;

    public Goomba(GamingScreen gamingScreen, float x, float y) {
        super(gamingScreen, x, y);
        frames = new Array<TextureRegion>();
        for (int j = 0; j < 2; j++){
            frames.add(new TextureRegion(gamingScreen.getTextureAtlas().findRegion("goomba"),j*16, 0,16,16));
        }
        walkingAnim = new Animation<TextureRegion>(0.4f,frames);
        stateTime = 0;
        setBounds(getX(),getY(),16/MarkioBrothers.pixelpm,16/MarkioBrothers.pixelpm);
        set2Destroy = false;
        destroyedEnemy = false;
    }

    public void update(float delatTime){
        stateTime += delatTime;

        //will remove enemy body from Box2D world
        if (set2Destroy && !destroyedEnemy){
            gWorld.destroyBody(BodyB2);
            destroyedEnemy = true;
            setRegion(new TextureRegion(gScreen.getTextureAtlas().findRegion("goomba"),32,0,16,16));
            stateTime = 0;
            HUD.addScore(200);
        }
        else if (!destroyedEnemy) {
            BodyB2.setLinearVelocity(speed);
            setPosition(BodyB2.getPosition().x - getWidth() / 2, BodyB2.getPosition().y - getHeight() / 2);
            setRegion(walkingAnim.getKeyFrame(stateTime, true));
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(),getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        BodyB2 = gWorld.createBody(bodyDef);

        FixtureDef fix_def = new FixtureDef();
        CircleShape cShape = new CircleShape();
        cShape.setRadius(6/MarkioBrothers.pixelpm);

        fix_def.filter.categoryBits = MarkioBrothers.EnemyBit;
        fix_def.filter.maskBits = MarkioBrothers.GroundBit | MarkioBrothers.CoinBit |
                MarkioBrothers.BrickBit | MarkioBrothers.EnemyBit | MarkioBrothers.ObjectBit |
                MarkioBrothers.MarkioBit;                              //what goomba can collide with

        fix_def.shape = cShape;
        BodyB2.createFixture(fix_def).setUserData(this);

        //Goomba head created
        PolygonShape goomHead = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-5, 8).scl(1/MarkioBrothers.pixelpm);
        vertices[1] = new Vector2(5, 8).scl(1/MarkioBrothers.pixelpm);
        vertices[2] = new Vector2(-3, 3).scl(1/MarkioBrothers.pixelpm);
        vertices[3] = new Vector2(3, 3).scl(1/MarkioBrothers.pixelpm);
        goomHead.set(vertices);

        fix_def.shape = goomHead;
        fix_def.restitution = 0.5f;         //gives head bounciness for markio to jump on
        fix_def.filter.categoryBits = MarkioBrothers.EnemyHeadBit;
        BodyB2.createFixture(fix_def).setUserData(this);
    }

    @Override
    public void headHit(Markio markio) {
        set2Destroy = true;
    }

    @Override
    public void enemyHit(Enemy enemy) {
        if (enemy instanceof Turt && ((Turt)enemy).currState == Turt.State.MOBILE_SHELL)
            set2Destroy = true;
        else
            revSpeed(true,false);
    }

    public void draw(Batch batch){
        if (!destroyedEnemy || stateTime < 1){
            super.draw(batch);
        }
    }
}
