package com.mygdx.game.Game_Sprites.Goodies;

import com.badlogic.gdx.math.Vector2;

public class GoodyDefinition {
    public Vector2 pos;
    public Class<?> classType;

    public GoodyDefinition(Vector2 pos, Class<?> classType){
        this.classType = classType;
        this.pos = pos;
    }
}
