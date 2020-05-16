package com.mygdx.game.Game_Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Game_screen.GamingScreen;
import com.mygdx.game.MarkioBrothers;

public class Markio extends Sprite {
    public World world;
    public Body body_box2d;
    private TextureRegion standingMarkio;
    private TextureRegion bigMarkStanding;
    private TextureRegion bigMarkJumping;
    private TextureRegion markioDead;

    public enum State {FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD}
    public State currState;
    public State prevState;

    private Animation <TextureRegion> growMark;
    private Animation <TextureRegion> bigMarkRun;
    private Animation <TextureRegion> marRun;
    private TextureRegion marJump;
    private boolean runRight;           //changed from Boolean
    private boolean markIsBig;
    private boolean runGrowthAnim;
    private boolean DefineBigMarkTime;
    private boolean redefineMarkTime;
    private boolean markIsDead;
    private float stateTimer;           //to keep count of how much time has passed during any given state

    public Markio(GamingScreen gamingScreen){
        this.world = gamingScreen.getGameWorld();

        currState = State.STANDING;
        prevState = State.STANDING;
        stateTimer = 0;
        runRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        //getting and adding the little markio running animation
        for (int i = 1; i < 4; i++){
            frames.add(new TextureRegion(gamingScreen.getTextureAtlas().findRegion("little_mario"), i * 16, 0,16,16));           //the sprite images are 16x16
        }
        marRun = new Animation<TextureRegion>(0.1f, frames);

        frames.clear();                 //clear so we can use for jump animation

        //same but for big markio
        for (int i = 1; i < 4; i++){
            frames.add(new TextureRegion(gamingScreen.getTextureAtlas().findRegion("big_mario"), i * 16, 0,16,32));           //the sprite images are 16x16
        }
        bigMarkRun = new Animation<TextureRegion>(0.1f, frames);

        frames.clear();

        //for growing Markio animation
        frames.add(new TextureRegion(gamingScreen.getTextureAtlas().findRegion("big_mario"),240,0,16,32));
        frames.add(new TextureRegion(gamingScreen.getTextureAtlas().findRegion("big_mario"),0,0,16,32));
        frames.add(new TextureRegion(gamingScreen.getTextureAtlas().findRegion("big_mario"),240,0,16,32));
        frames.add(new TextureRegion(gamingScreen.getTextureAtlas().findRegion("big_mario"),0,0,16,32));
        growMark = new Animation<TextureRegion>(0.1f, frames);

        //getting and adding the jumping animation for little markio
        marJump = new TextureRegion(gamingScreen.getTextureAtlas().findRegion("little_mario"), 80,0,16,16);
        bigMarkJumping = new TextureRegion(gamingScreen.getTextureAtlas().findRegion("big_mario"), 80,0,16,32);

        //creates the texture regions for the standing markio image
        standingMarkio = new TextureRegion(gamingScreen.getTextureAtlas().findRegion("little_mario"),0,0,16,16);
        bigMarkStanding = new TextureRegion(gamingScreen.getTextureAtlas().findRegion("big_mario"), 0,0,16,32);

        //texture region for when he dies
        markioDead = new TextureRegion(gamingScreen.getTextureAtlas().findRegion("little_mario"), 96,0,16,16);

        defineMarkio();
        setBounds(0,0,16/MarkioBrothers.pixelpm,16/MarkioBrothers.pixelpm);
        setRegion(standingMarkio);                                                               //texture region associated with this sprite
    }

    public void update(float dt){
        if (markIsBig)
            setPosition(body_box2d.getPosition().x - getWidth() /2, body_box2d.getPosition().y - getHeight() /2 - 6/MarkioBrothers.pixelpm);
        else
            setPosition(body_box2d.getPosition().x - getWidth() /2, body_box2d.getPosition().y - getHeight() /2);
        setRegion(getFrame(dt));
        if (DefineBigMarkTime)
            defineBigMarkio();
        if (redefineMarkTime)
            redefineMarkio();
    }

    public TextureRegion getFrame(float delta_time){
        currState = getState();

        //checks the state of Markio
        TextureRegion textureRegion;
        switch (currState){
            case DEAD:
                textureRegion = markioDead;
                break;
            case GROWING:
                textureRegion = growMark.getKeyFrame(stateTimer);
                if (growMark.isAnimationFinished(stateTimer))
                    runGrowthAnim = false;
                break;
            case RUNNING:
                textureRegion = markIsBig ? bigMarkRun.getKeyFrame(stateTimer, true) : marRun.getKeyFrame(stateTimer, true);
                break;
            case JUMPING:
                textureRegion = markIsBig ? bigMarkJumping : marJump;
                break;
            case FALLING:
            case STANDING:
            default:
                textureRegion = markIsBig ? bigMarkStanding : standingMarkio;
                break;
        }

        //for the case that markio is standing still
        if ((body_box2d.getLinearVelocity().x < 0 || !runRight) && !textureRegion.isFlipX()){
            textureRegion.flip(true,false);
            runRight = false;
        }
        else if ((body_box2d.getLinearVelocity().x > 0 || runRight) && textureRegion.isFlipX()){
            textureRegion.flip(true,false);
            runRight = true;
        }
        stateTimer = currState == prevState ? stateTimer + delta_time: 0;
        prevState = currState;
        return textureRegion;
    }

    public State getState(){
        if (markIsDead)
            return State.DEAD;
        else if (runGrowthAnim)
            return State.GROWING;
        else if (body_box2d.getLinearVelocity().y > 0  || (body_box2d.getLinearVelocity().y < 0 && prevState == State.JUMPING)){
            return State.JUMPING;
        }
        else if (body_box2d.getLinearVelocity().y < 0){
            return State.FALLING;
        }
        else if (body_box2d.getLinearVelocity().x != 0){
            return State.RUNNING;
        }
        else
            return State.STANDING;
    }

    public void growMarkio(){
        markIsBig = true;
        runGrowthAnim = true;
        DefineBigMarkTime = true;
        setBounds(getX(),getY(),getWidth(),getHeight() * 2);        //have to change sprite dimensions to account for big markio
    }

    public boolean isBig(){
        return markIsBig;
    }

    public boolean isDead(){
        return markIsDead;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public void hit(Enemy enemy) {
        if (enemy instanceof Turt && ((Turt) enemy).getCurrState() == Turt.State.STANDING_SHELL) {
            ((Turt) enemy).kick(this.getX() <= enemy.getX() ? Turt.rightKickSpeed : Turt.leftKickSpeed);
        } else {
            if (markIsBig) {
                markIsBig = false;
                redefineMarkTime = true;
                setBounds(getX(), getY(), getWidth(), getHeight() / 2);
            } else {
                markIsDead = true;
                Filter fil = new Filter();
                fil.maskBits = MarkioBrothers.NothingBit;
                for (Fixture fix : body_box2d.getFixtureList())
                    fix.setFilterData(fil);
                body_box2d.applyLinearImpulse(new Vector2(0, 4f), body_box2d.getWorldCenter(), true);
            }
        }
    }

    public void defineBigMarkio(){
        Vector2 currPosition = body_box2d.getPosition();
        world.destroyBody(body_box2d);

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(currPosition.add(0,10/MarkioBrothers.pixelpm));
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body_box2d = world.createBody(bodyDef);

        FixtureDef fix_def = new FixtureDef();
        CircleShape cShape = new CircleShape();
        cShape.setRadius(6/MarkioBrothers.pixelpm);

        fix_def.filter.categoryBits = MarkioBrothers.MarkioBit;
        fix_def.filter.maskBits = MarkioBrothers.GroundBit |
                MarkioBrothers.CoinBit | MarkioBrothers.BrickBit | MarkioBrothers.EnemyBit |
                MarkioBrothers.ObjectBit | MarkioBrothers.EnemyHeadBit | MarkioBrothers.GoodyBit;                              //what markio can collide with

        fix_def.shape = cShape;
        body_box2d.createFixture(fix_def).setUserData(this);
        cShape.setPosition(new Vector2(0,-14/MarkioBrothers.pixelpm));
        body_box2d.createFixture(fix_def).setUserData(this);

        EdgeShape markioHead = new EdgeShape();
        markioHead.set(new Vector2(-2/MarkioBrothers.pixelpm, 6/MarkioBrothers.pixelpm), new Vector2(2/MarkioBrothers.pixelpm, 6/MarkioBrothers.pixelpm));
        fix_def.filter.categoryBits = MarkioBrothers.MarkHeadBit;
        fix_def.shape = markioHead;
        fix_def.isSensor = true;

        body_box2d.createFixture(fix_def).setUserData(this);
        DefineBigMarkTime = false;
    }

    public void defineMarkio(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32/ MarkioBrothers.pixelpm,32/MarkioBrothers.pixelpm);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body_box2d = world.createBody(bodyDef);

        FixtureDef fix_def = new FixtureDef();
        CircleShape cShape = new CircleShape();
        cShape.setRadius(6/MarkioBrothers.pixelpm);

        fix_def.filter.categoryBits = MarkioBrothers.MarkioBit;
        fix_def.filter.maskBits = MarkioBrothers.GroundBit |
                MarkioBrothers.CoinBit | MarkioBrothers.BrickBit | MarkioBrothers.EnemyBit |
                MarkioBrothers.ObjectBit | MarkioBrothers.EnemyHeadBit | MarkioBrothers.GoodyBit;                              //what markio can collide with

        fix_def.shape = cShape;
        body_box2d.createFixture(fix_def).setUserData(this);

        EdgeShape markioHead = new EdgeShape();
        markioHead.set(new Vector2(-2/MarkioBrothers.pixelpm, 6/MarkioBrothers.pixelpm), new Vector2(2/MarkioBrothers.pixelpm, 6/MarkioBrothers.pixelpm));
        fix_def.filter.categoryBits = MarkioBrothers.MarkHeadBit;
        fix_def.shape = markioHead;
        fix_def.isSensor = true;

        body_box2d.createFixture(fix_def).setUserData(this);
    }

    public void redefineMarkio(){
        Vector2 position = body_box2d.getPosition();
        world.destroyBody(body_box2d);

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position);           //placing new mario into same position of current body's position
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body_box2d = world.createBody(bodyDef);

        FixtureDef fix_def = new FixtureDef();
        CircleShape cShape = new CircleShape();
        cShape.setRadius(6/MarkioBrothers.pixelpm);

        fix_def.filter.categoryBits = MarkioBrothers.MarkioBit;
        fix_def.filter.maskBits = MarkioBrothers.GroundBit |
                MarkioBrothers.CoinBit | MarkioBrothers.BrickBit | MarkioBrothers.EnemyBit |
                MarkioBrothers.ObjectBit | MarkioBrothers.EnemyHeadBit | MarkioBrothers.GoodyBit;                              //what markio can collide with

        fix_def.shape = cShape;
        body_box2d.createFixture(fix_def).setUserData(this);

        EdgeShape markioHead = new EdgeShape();
        markioHead.set(new Vector2(-2/MarkioBrothers.pixelpm, 6/MarkioBrothers.pixelpm), new Vector2(2/MarkioBrothers.pixelpm, 6/MarkioBrothers.pixelpm));
        fix_def.filter.categoryBits = MarkioBrothers.MarkHeadBit;
        fix_def.shape = markioHead;
        fix_def.isSensor = true;

        body_box2d.createFixture(fix_def).setUserData(this);
        redefineMarkTime = false;
    }
}
