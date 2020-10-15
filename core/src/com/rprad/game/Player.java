package com.rprad.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Player extends Sprite {
    enum State{Dashing, Falling, Dead}
    private State state;
    private static final float JUMP_DISTANCE = 100f;
    private int numJumps;
    private Body body;
    private World world;
    Sprite sprite;
    private PlayScreen rscreen;
    private Animation<TextureRegion> dash;
    private Sprite baseSprite;
    public Player(PlayScreen screen){
        this.rscreen = screen;
        numJumps = 2;
        sprite = new Sprite(new Texture("sprite-idle.png"));
        sprite.setPosition((Gdx.graphics.getWidth() - sprite.getWidth()) / 2f, (Gdx.graphics.getHeight() - sprite.getHeight()) / 2f);
        // Body for box2d physics
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(sprite.getX(), sprite.getY());
        body = rscreen.getWorld().createBody(bdef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2, sprite.getHeight() / 2);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 1f;
        Fixture fixture = body.createFixture(fdef);
        shape.dispose();
    }

    public void handleInput(float dt){
        if(Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)){
            if (Gdx.input.isKeyPressed(Input.Keys.W)){
                if(Gdx.input.isKeyPressed(Input.Keys.D)){
                    if(numJumps > 0){
                        numJumps--;
                        System.out.println("Jumps: " + numJumps);

                    }
                }
                else{
//                    Check if character's jumps <= 0
//                    If not, apply up-dash
                    if(numJumps > 0){
                        System.out.println("Top dash");
                        numJumps--;
                        System.out.println("Jumps: " + numJumps);

                    }
                }
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.S)){
                if (Gdx.input.isKeyPressed(Input.Keys.D)){
                    if(numJumps > 0){
                        body.applyLinearImpulse(new Vector2(JUMP_DISTANCE, JUMP_DISTANCE), body.getPosition(), true);
                        numJumps--;
                        System.out.println("Jumps: " + numJumps);
                    }
                }
                else {
                    if(numJumps > 0){
                         numJumps--;
                         System.out.println("Jumps: " + numJumps);
                    }
                }
            }
        }
        update();
    }
    public void update(){
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
    }
    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }
    public void dispose(){

    }
}
