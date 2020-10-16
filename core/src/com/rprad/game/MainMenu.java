package com.rprad.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import java.io.BufferedReader;
import java.io.FileReader;

public class MainMenu implements Screen {
    /*
    Main Menu Screen, which runs on startup. Contains a "play" button,
    as well as your highest score.
     */
    private BitmapFont font;
    private float screen_width;
    private float screen_height;
    private ScreenViewport viewport;
    private OrthographicCamera camera;
    private FlashDash game;
    private Texture menuBox;
    private Texture playButton;
    private Texture background;
    private int score;
    private static final int pb_size = 50;
    public MainMenu(FlashDash game){
        this.game = game;
        font = new BitmapFont();
        this.score = readScore();
        screen_width = Gdx.graphics.getWidth();
        screen_height = Gdx.graphics.getHeight();
        background = new Texture("flappy_background.png");
        menuBox = new Texture("menu_box.png");
        playButton = new Texture("playbutton.png");
//        Icon made by Freepik on Flaticon
        camera = new OrthographicCamera();
        camera.translate(screen_width / 2, screen_height/2, 0); // Move to top left
        viewport = new ScreenViewport(camera);

    }

    @Override
    public void show() {
        /*
        List of Items:
            Background
            Floor
            Menu Box Outline
            Play Button
            Score Text (bitmap font)
         */
    }

    @Override
    public void render(float delta) {
        update(delta);
        game.batch.begin();

//        draw the background
        if(background.getWidth() > Gdx.graphics.getWidth() && background.getHeight() > Gdx.graphics.getHeight())
            game.batch.draw(background, 0, 0);
        else game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        game.batch.draw(
//                menuBox,
//                screen_width / 2 - screen_width / 4,
//                screen_height / 2 - screen_height / 4,
//                screen_width / 2,
//                screen_height / 2
//        );
        game.batch.draw(
                playButton,
                screen_width / 2 - pb_size / 2f,
                screen_height / 2 - pb_size / 2f,
                pb_size,  pb_size
        );
        game.batch.end();
    }
    public void update (float delta) {
        if(screen_width != Gdx.graphics.getWidth() || screen_height != Gdx.graphics.getHeight()){
            screen_width = Gdx.graphics.getWidth();
            screen_height = Gdx.graphics.getHeight();
            resize((int)screen_width, (int)screen_height);
        }
//        Check for changes to width and height
//        Check if the user presses the "Enter" key or clicks the play button
//        to start the game.

        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            startPlaying();
        }
        if(Gdx.input.isTouched()){
//            Check x-coordinate is in the range [screen_width / 2 - 25, screen_width / 2 + 25] and y is in the range
//            [screen_height / 2 - 25, screen_height / 2 + 25]
            if(Gdx.input.getX() >= screen_width / 2 - pb_size / 2f
                && Gdx.input.getX() <= screen_width / 2 + pb_size / 2f
                && Gdx.input.getY() >= screen_height / 2 - pb_size / 2f
            && Gdx.input.getY() <= screen_height / 2 + pb_size / 2f){
                startPlaying();
            }
        }

    }
    public void startPlaying(){
        game.setScreen(new PlayScreen(this.game));
        dispose();
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

    @Override
    public void resume() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        font.dispose();
        background.dispose();
        menuBox.dispose();
        playButton.dispose();
        game.dispose();
//        floor.dispose();
    }
    private int readScore () {
        try {
            BufferedReader br = new BufferedReader(new FileReader("score.txt"));
            score = Integer.parseInt(br.readLine());
            return score;
        } catch (java.io.IOException e){
            e.printStackTrace();
        }
        return 0;
    }
}