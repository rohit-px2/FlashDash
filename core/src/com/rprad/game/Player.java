package com.rprad.game;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class Player extends Sprite {
    public static final float JUMP_DURATION = 0.1f;
    enum State{Idle, DownDashing, Dashing, Dead}
    private State state;
    private static final float JUMP_SPEED = 20f;
    private boolean isMoving;
    private float airTime;
    private int numJumps;
    private Body body;
    private World world;
    private float screen_width;
    private float screen_height;
    Sprite sprite;
    private Texture spriteSheet;
    private PlayScreen rScreen;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> defaultDashAnimation;
    private Animation<TextureRegion> downDashAnimation;
    private Animation<TextureRegion> deathAnimation;
    private Sprite baseSprite;
    private TextureAtlas atlas;
    private float frameTimer;
    public Player(PlayScreen screen){
        frameTimer = 0f;
//        Creating Player Animations
        spriteSheet = new Texture("characterPack.png");
        Array<TextureRegion> frames = new Array<>();

        for(int i = 2; i < 6; i++){
            frames.add(new TextureRegion(spriteSheet, i * 50, 0, 50, 37));
        }
        idleAnimation = new Animation<>(1f/6f, frames, Animation.PlayMode.LOOP);
        frames.clear();

        for(int i = 0; i < 2; i++){
            frames.add(new TextureRegion(spriteSheet, i * 50, 0, 50, 37));
        }
        downDashAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
        frames.clear();

        frames.add(new TextureRegion(spriteSheet, 6 * 50, 0, 50, 37));
        defaultDashAnimation = new Animation<>(0.2f, frames, Animation.PlayMode.LOOP);
        frames.clear();

        isMoving = false;
        this.state = State.Idle;
        numJumps = 2;
        this.rScreen = screen;
        sprite = new Sprite(new Texture("sprite-idle.png"));
        sprite.setPosition((Gdx.graphics.getWidth() - sprite.getWidth()) / 2f, (Gdx.graphics.getHeight() - sprite.getHeight()) / 2f);
        // Body for box2d physics
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(sprite.getX() / FlashDash.PPM, sprite.getY() / FlashDash.PPM);
        body = rScreen.getWorld().createBody(bdef);
        body.setUserData("Player");
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / FlashDash.PPM / 2, sprite.getHeight() / FlashDash.PPM / 2);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.density = 1f;
        Fixture fixture = body.createFixture(fdef);
        shape.dispose();
    }
    private void move(float x, float y){
        stopMoving();
        if(y < 0) this.state = State.DownDashing;
        else this.state = State.Dashing;
        isMoving = true;
        body.setLinearVelocity(JUMP_SPEED * x, JUMP_SPEED * y);
        System.out.println(body.getLinearVelocity().toString());
//        Move the character vertically, but not horizontally
    }
    private TextureRegion getFrame (float delta) {
//        returns the animation frame the sprite should be at.
        switch(this.state){
            case DownDashing:
                return downDashAnimation.getKeyFrame(delta);
            case Dashing:
                return defaultDashAnimation.getKeyFrame(delta);
            case Idle:
            default:
                return idleAnimation.getKeyFrame(delta);
        }
    }
    private void handleInput(float dt){
//        Keyboard input
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
            else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                if (numJumps > 0) {
                   move(1, 0);
                   numJumps--;
                }
            }
        }
//       TODO: Android/Swipe Input Processing

    }
    private void stopMoving () {
        body.setLinearVelocity(0f, 0f);
        isMoving = false;
        airTime = 0f;
        this.state = State.Idle;
    }
    private void kill(){
        this.state = State.Dead;
        rScreen.getGame().setScreen(new MainMenu(rScreen.getGame()));
        dispose();
    }
    public void update(float delta){
        frameTimer += delta;
        if(isMoving){
            airTime += delta;
            if(airTime > JUMP_DURATION){
                stopMoving();
            }
        }
        sprite.setRegion(getFrame(frameTimer));
        sprite.setPosition(body.getPosition().x * FlashDash.PPM, body.getPosition().y * FlashDash.PPM);
        if(body.getPosition().x <= 0 || body.getPosition().x >= rScreen.getWidth() / FlashDash.PPM
        || body.getPosition().y <= 0 || body.getPosition().y >= rScreen.getHeight() / FlashDash.PPM){
            kill();
        }
        handleInput(delta);
    }

    public void render(SpriteBatch batch){
        sprite.draw(batch);
    }
    public void dispose(){
        world.dispose();

    }
}
