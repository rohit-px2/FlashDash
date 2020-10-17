package com.rprad.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.Texture;
import java.util.Random;

public class Obstacle {
//    Each obstacle is made up of two walls, one of which sprouts from the top of the screen
//    and another of which sprouts from the bottom of the screen and there is a small gap between them.
    // 1 Wall would have a box2d body, a texture which we can draw
    private Body upperBody;
    private Body lowerBody;
    private Sprite lowerSprite;
    private Sprite upperSprite;

    public Obstacle (PlayScreen screen) {

    }
    public void update () {

    }
    public void render () {

    }
}