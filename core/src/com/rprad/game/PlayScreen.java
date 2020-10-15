package com.rprad.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class PlayScreen implements Screen {
    /*
    The main game screen. Holds all the game logic.
     */
    private float accumulator;
    private int numIterations;
    private static final float fixedTimeStep = 1f/144f;
    private static final int MAX_UPDATE_ITERATIONS = 5;

    private World world;
    private FlashDash game;
    private OrthographicCamera camera;
    private ScreenViewport viewport;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;
    private Texture background;
    private Player character;
    private BitmapFont font;
    private float screen_width;
    private float screen_height;
    private static final float x_Gravity = 0f;
    private static final float y_Gravity = 0f;
    Texture img;
    public PlayScreen(FlashDash game){
        this.game = game;
        this.world = new World(new Vector2(x_Gravity, y_Gravity), true);
        this.character = new Player(this);
        this.background = new Texture("flappy_background.png");
        camera = new OrthographicCamera();
        camera.translate(screen_width / 2, screen_height/2, 0); // Move to top left
        viewport = new ScreenViewport(camera);
        accumulator = 0f;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        update(delta);
        game.batch.begin();
        game.batch.draw(background, 0, 0);
        character.render(game.batch);
        game.batch.end();
    }

    public void update(float delta){
        if(screen_width != Gdx.graphics.getWidth() || screen_height != Gdx.graphics.getHeight()){
            screen_width = Gdx.graphics.getWidth();
            screen_height = Gdx.graphics.getHeight();
            resize((int)screen_width, (int)screen_height);
        }
        accumulator += Math.min(delta, 0.25f);
        if(accumulator >= fixedTimeStep){
            accumulator -= fixedTimeStep;
            world.step(fixedTimeStep, 6, 2);
        }
        character.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.setToOrtho(false);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {

    }
    public World getWorld(){
        return this.world;
    }
    public float getWidth(){
        return this.screen_width;
    }
    public float getHeight(){
        return this.screen_height;
    }
    public FlashDash getGame(){
        return this.game;
    }
    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
    public void setBackground(String path){
        this.background = new Texture(path);
    }
    @Override
    public void dispose() {
        game.dispose();
        world.dispose();
        background.dispose();
        font.dispose();
        character.dispose();
    }
}
