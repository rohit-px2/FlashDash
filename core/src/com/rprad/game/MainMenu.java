package com.rprad.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    private Viewport viewport;
    private OrthographicCamera camera;
    private FlashDash game;
    private Texture menuBox;
    private Texture playButton;
    private Texture background;
    private int score;
    public MainMenu(FlashDash game){
        font = new BitmapFont();
        this.game = game;
        this.score = readScore();
        screen_width = Gdx.graphics.getWidth();
        screen_height = Gdx.graphics.getHeight();
        background = new Texture("flappy_background.png");
        menuBox = new Texture("menu_box.png");
        playButton = new Texture("playbutton.png");
//        Icon made by Freepik on Flaticon
        camera = new OrthographicCamera();
        camera.translate(screen_width / 2, screen_height/2, 0);
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
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.setProjectionMatrix(camera.combined);
//        draw the background
        game.batch.draw(background, 0, 0);
        game.batch.draw(
                menuBox,
                screen_width / 2 - screen_width / 4,
                screen_height / 2 - screen_height / 4,
                screen_width / 2,
                screen_height / 2
        );
        game.batch.draw(
                playButton,
                screen_width / 2 - 25,
                screen_height / 2 - 25,
                50,  50
        );
        font.draw(game.batch, "FLASH DASH", screen_width / 3f, screen_height * 3f / 4.15f);
        game.batch.end();
    }
    public void update (float delta) {
//        Check for changes to width and height
//        Check if the user presses the "Enter" key or clicks the play button
//        to start the game.
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            game.setScreen(new PlayScreen(this.game));
            dispose();
        }
        if(Gdx.input.isTouched()){
//            Check x-coordinate is in the range [screen_width / 2 - 25, screen_width / 2 + 25] and y is in the range
//            [screen_height / 2 - 25, screen_height / 2 + 25]
            if(Gdx.input.getX() >= screen_width / 2 - 25
                && Gdx.input.getX() <= screen_width / 2 + 25
                && Gdx.input.getY() >= screen_height / 2 - 25
            && Gdx.input.getY() <= screen_height / 2 + 25){
                game.setScreen(new PlayScreen(this.game));
                dispose();
            }

        }
    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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