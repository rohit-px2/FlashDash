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
    public static final float JUMP_DURATION = 0.5f;
    enum State{Idle, Dashing, Dead}
    private State state;
    private static final float JUMP_DISTANCE = 100f;
    private boolean isMoving;
    private float airTime;
    private int numJumps;
    private Body body;
    private World world;
    private float screen_width;
    private float screen_height;
    Sprite sprite;
    private final PlayScreen rScreen;
    private Animation<TextureRegion> dashAnimation;
    private Animation<TextureRegion> deathAnimation;
    private Sprite baseSprite;
    public Player(PlayScreen screen){
        isMoving = false;
        this.state = State.Idle;
        this.rScreen = screen;
        numJumps = 2;
        sprite = new Sprite(new Texture("sprite-idle.png"));
        sprite.setPosition((Gdx.graphics.getWidth() - sprite.getWidth()) / 2f, (Gdx.graphics.getHeight() - sprite.getHeight()) / 2f);
        // Body for box2d physics
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(sprite.getX(), sprite.getY());
        body = rScreen.getWorld().createBody(bdef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2, sprite.getHeight() / 2);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 1f;
        Fixture fixture = body.createFixture(fdef);
        shape.dispose();
    }
    public void move(float x, float y){
        isMoving = true;
        body.setLinearVelocity(10000 * x, 10000 * y);
        System.out.println(body.getLinearVelocity().toString());
//        Move the character vertically, but not horizontally
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
    public void stopMoving () {
        body.setLinearVelocity(0f, 0f);
        System.out.println(body.getLinearVelocity().toString());
        isMoving = false;
        airTime = 0f;
    }
    public void kill(){
        this.state = State.Dead;
        rScreen.getGame().setScreen(new MainMenu(rScreen.getGame()));
    }
    public void update(float delta){
        if(isMoving){
            airTime += delta;
            if(airTime > JUMP_DURATION){
                stopMoving();
            }
        }
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        if(body.getPosition().x <= 0 || body.getPosition().x >= rScreen.getWidth()
        || body.getPosition().y <= 0 || body.getPosition().y >= rScreen.getHeight()){
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
