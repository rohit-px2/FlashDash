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
    enum State{Idle, Dashing, Dead}
    private State state;
    private static final float JUMP_DISTANCE = 100f;
    private int numJumps;
    private Body body;
    private World world;
    Sprite sprite;
    private PlayScreen rscreen;
    private Animation<TextureRegion> dashAnimation;
    private Animation<TextureRegion> deathAnimation;
    private Sprite baseSprite;
    public Player(PlayScreen screen){
        this.state = State.Idle;
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
    public void move(float x, float y){
        if(state != State.Dashing) {
            this.state = State.Dashing;
            body.applyLinearImpulse(new Vector2(x*100000, y*100000), body.getPosition(), true);
        }
//        Move the character vertically, but not horizontally
        this.state = State.Idle;
    }
    public void handleInput(float dt){
        if(Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)){
            if (Gdx.input.isKeyPressed(Input.Keys.W)){
                if(Gdx.input.isKeyPressed(Input.Keys.D)){
                    if(numJumps > 0){
                        move(1, 1);
                        numJumps--;
                        System.out.println("Jumps: " + numJumps);

                    }
                }
                else{
//                    Check if character's jumps <= 0
//                    If not, apply up-dash
                    if(numJumps > 0){
                        move(0, 1);
                        System.out.println("Top dash");
                        numJumps--;
                        System.out.println("Jumps: " + numJumps);

                    }
                }
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.S)){
                if (Gdx.input.isKeyPressed(Input.Keys.D)){
                    if(numJumps > 0){
                        move(1, -1);
                        numJumps--;
                        System.out.println("Jumps: " + numJumps);
                    }
                }
                else {
                    if(numJumps > 0){
                        move(0, -1);
                         numJumps--;
                         System.out.println("Jumps: " + numJumps);
                    }
                }
            }
        }

    }
    public void kill(){
        this.state = State.Dead;
        rscreen.getGame().setScreen(new MainMenu(rscreen.getGame()));
    }
    public void update(float delta){
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        if(body.getPosition().x <= 0 || body.getPosition().x >= rscreen.getWidth()
        || body.getPosition().y <= 0 || body.getPosition().y >= rscreen.getHeight()){
            kill();
        }
        handleInput(delta);
    }
    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }
    public void dispose(){

    }
}
