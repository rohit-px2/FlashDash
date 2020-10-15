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
    private World world;
    private FlashDash game;
    private OrthographicCamera camera;
    private ScreenViewport viewport;

    private Player character;
    private BitmapFont font;
    private float screen_width;
    private float screen_height;
    private static final float x_Gravity = -300f;
    private static final float y_Gravity = -98f;
    Texture img;
    public PlayScreen(FlashDash game){
        this.game = game;
        this.world = new World(new Vector2(x_Gravity, 0), true);
        this.character = new Player(this);
        camera = new OrthographicCamera();
        camera.translate(screen_width / 2, screen_height/2, 0); // Move to top left
        viewport = new ScreenViewport(camera);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        update(delta);
        game.batch.begin();
        game.batch.draw(new Texture("badlogic.jpg"), 0, 0);
        character.render(game.batch);
        game.batch.end();
    }

    public void update(float delta){
        if(screen_width != Gdx.graphics.getWidth() || screen_height != Gdx.graphics.getHeight()){
            screen_width = Gdx.graphics.getWidth();
            screen_height = Gdx.graphics.getHeight();
            resize((int)screen_width, (int)screen_height);
        }
        character.handleInput(delta);
        world.step(Gdx.graphics.getDeltaTime(), 12, 4);
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
    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
