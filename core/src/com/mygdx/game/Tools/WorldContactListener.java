package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.Game_Sprites.Enemy;
import com.mygdx.game.Game_Sprites.Goodies.Goody;
import com.mygdx.game.Game_Sprites.InteractiveTileObjects;
import com.mygdx.game.Game_Sprites.Markio;
import com.mygdx.game.MarkioBrothers;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (cDef){
            case MarkioBrothers.MarkHeadBit | MarkioBrothers.BrickBit:
            case MarkioBrothers.MarkHeadBit | MarkioBrothers.CoinBit:
                if (fixtureA.getFilterData().categoryBits == MarkioBrothers.MarkHeadBit){
                    ((InteractiveTileObjects)fixtureB.getUserData()).HittingHead((Markio) fixtureA.getUserData());
                }else {
                    ((InteractiveTileObjects)fixtureA.getUserData()).HittingHead((Markio) fixtureB.getUserData());
                }
                break;
            case MarkioBrothers.EnemyHeadBit | MarkioBrothers.MarkioBit:
                if (fixtureA.getFilterData().categoryBits == MarkioBrothers.EnemyHeadBit){
                    ((Enemy)fixtureA.getUserData()).headHit((Markio) fixtureB.getUserData()) ;
                }
                else {
                    ((Enemy)fixtureB.getUserData()).headHit((Markio) fixtureA.getUserData()) ;
                }
                break;
            case MarkioBrothers.EnemyBit | MarkioBrothers.ObjectBit :
                if (fixtureA.getFilterData().categoryBits == MarkioBrothers.EnemyBit){
                    ((Enemy)fixtureA.getUserData()).revSpeed(true,false);
                }
                else {
                    ((Enemy)fixtureB.getUserData()).revSpeed(true,false);
                }
                break;
            case MarkioBrothers.EnemyBit | MarkioBrothers.EnemyBit:
                ((Enemy)fixtureA.getUserData()).enemyHit((Enemy)fixtureB.getUserData());
                ((Enemy)fixtureB.getUserData()).enemyHit((Enemy)fixtureA.getUserData());
                break;

            case MarkioBrothers.MarkioBit | MarkioBrothers.EnemyBit:
                Gdx.app.log("Markio", "Died");
                if (fixtureA.getFilterData().categoryBits == MarkioBrothers.MarkioBit){
                    ((Markio) fixtureA.getUserData()).hit((Enemy)fixtureB.getUserData());
                }else {
                    ((Markio) fixtureB.getUserData()).hit((Enemy)fixtureA.getUserData());
                }
                break;
            case MarkioBrothers.GoodyBit | MarkioBrothers.ObjectBit :
                if (fixtureA.getFilterData().categoryBits == MarkioBrothers.GoodyBit){
                    ((Goody)fixtureA.getUserData()).revSpeed(true,false);
                }
                else {
                    ((Goody)fixtureB.getUserData()).revSpeed(true,false);
                }
                break;

            case MarkioBrothers.GoodyBit | MarkioBrothers.MarkioBit :
                if (fixtureA.getFilterData().categoryBits == MarkioBrothers.GoodyBit){
                    ((Goody)fixtureA.getUserData()).useUp((Markio) fixtureB.getUserData());
                }
                else {
                    ((Goody)fixtureB.getUserData()).useUp((Markio) fixtureA.getUserData());
                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
