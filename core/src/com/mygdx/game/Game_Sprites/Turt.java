package com.mygdx.game.Game_Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Game_screen.GamingScreen;
import com.mygdx.game.MarkioBrothers;

public class Turt extends Enemy{
    public enum State {WALKING, STANDING_SHELL, MOBILE_SHELL,DEAD}
    public State currState;
    public State prevState;

    public static final int leftKickSpeed = -2;
    public static final int rightKickSpeed = 2;

    private float stateTime;
    private float deathSpinAngle;
    private Animation<TextureRegion> walkingAnim;
    private Array<TextureRegion> frames;
    private TextureRegion shell;
    private boolean set2Destroy;
    private boolean destroyedEnemy;

    public Turt(GamingScreen gamingScreen, float x, float y) {
        super(gamingScreen, x, y);
        frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(gamingScreen.getTextureAtlas().findRegion("turtle"),0,0,16,24));
        frames.add(new TextureRegion(gamingScreen.getTextureAtlas().findRegion("turtle"),16,0,16,24));
        shell = new TextureRegion(gamingScreen.getTextureAtlas().findRegion("turtle"),64,0,16,24);
        walkingAnim = new Animation<TextureRegion>(0.2f,frames);

        currState = prevState = State.WALKING;
        deathSpinAngle = 0;
        setBounds(getX(),getY(), 16/ MarkioBrothers.pixelpm, 24/MarkioBrothers.pixelpm);

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
                MarkioBrothers.MarkioBit;                                      //what turtle can collide with

        fix_def.shape = cShape;
        BodyB2.createFixture(fix_def).setUserData(this);

        //turtle head created
        PolygonShape turtHead = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-5, 8).scl(1/MarkioBrothers.pixelpm);
        vertices[1] = new Vector2(5, 8).scl(1/MarkioBrothers.pixelpm);
        vertices[2] = new Vector2(-3, 3).scl(1/MarkioBrothers.pixelpm);
        vertices[3] = new Vector2(3, 3).scl(1/MarkioBrothers.pixelpm);
        turtHead.set(vertices);

        fix_def.shape = turtHead;
        fix_def.restitution = 1.5f;         //gives head bounciness for markio to jump on
        fix_def.filter.categoryBits = MarkioBrothers.EnemyHeadBit;
        BodyB2.createFixture(fix_def).setUserData(this);

    }

    @Override
    public void headHit(Markio markio) {
        if (currState != State.STANDING_SHELL){
            currState = State.STANDING_SHELL;
            speed.x = 0;
        }else{
            kick(markio.getX() <= this.getX() ? rightKickSpeed : leftKickSpeed);
        }
    }

    @Override
    public void enemyHit(Enemy enemy) {
        if (enemy instanceof Turt){
            if (((Turt)enemy).currState == State.MOBILE_SHELL && currState != State.MOBILE_SHELL){
                murdered();
            }else if (currState == State.MOBILE_SHELL && ((Turt)enemy).currState == State.WALKING)
                return;
            else
                revSpeed(true,false);
        }
        else if (currState != State.MOBILE_SHELL)
            revSpeed(true,false);
    }

    public void kick(int velocity){
        speed.x = velocity;
        currState = State.MOBILE_SHELL;
    }

    public State getCurrState(){
        return currState;
    }

    public TextureRegion getFrame(float detaT){
        TextureRegion textureRegion;

        switch (currState){
            case STANDING_SHELL:
            case MOBILE_SHELL:
                textureRegion = shell;
                break;
            case WALKING:
            default:
                textureRegion = walkingAnim.getKeyFrame(stateTime,true);
                break;
        }
        //making sure that the image is oriented in the right direction as walking direction
        if (speed.x > 0 && textureRegion.isFlipX() == false){
            textureRegion.flip(true,false);
        }
        if (speed.x < 0 && textureRegion.isFlipX() == true){
            textureRegion.flip(true,false);
        }
        stateTime = currState == prevState ? stateTime + detaT: 0;
        prevState = currState;
        return textureRegion;
    }

    public void murdered(){
        currState = State.DEAD;
        Filter filter = new Filter();
        filter.maskBits = MarkioBrothers.NothingBit;

        for (Fixture fixture : BodyB2.getFixtureList())
            fixture.setFilterData(filter);
        BodyB2.applyLinearImpulse(new Vector2(0,5f),BodyB2.getWorldCenter(), true);
    }

    public void draw(Batch batch){
        if (!destroyedEnemy)
            super.draw(batch);
    }

    @Override
    public void update(float dt) {
        setRegion(getFrame(dt));
        if (currState == State.STANDING_SHELL && stateTime>5){
            currState = State.WALKING;
            speed.x = 1;                            //making him walk right
        }
        setPosition(BodyB2.getPosition().x - getWidth()/2, BodyB2.getPosition().y - 8/MarkioBrothers.pixelpm);

        if (currState == State.DEAD){
            deathSpinAngle += 3;
            rotate(deathSpinAngle);
            if (stateTime > 5 && !destroyedEnemy){
                gWorld.destroyBody(BodyB2);
                destroyedEnemy = true;
            }
        }else
            BodyB2.setLinearVelocity(speed);
    }
}
