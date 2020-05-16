package com.mygdx.game.Game_screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MarkioBrothers;


public class GameOverScreen implements Screen{
    private Viewport view;
    private Stage stage;

    private Game game;


    public GameOverScreen(Game game){
        this.game = game;
        view = new FitViewport(MarkioBrothers.Virtual_W, MarkioBrothers.Virtual_H, new OrthographicCamera());
        stage = new Stage(view, ((MarkioBrothers)game).batch);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);          //takes up entire stage

        Label gOverLabel = new Label("Game Over",font);
        Label againLabel = new Label("Play Again? ",font);
        //Label livesLabel = new Label());

        table.add(gOverLabel).expandX();            //expands whole length of row
        table.row();
        table.add(againLabel).expandX().padTop(10f);

        stage.addActor(table);

       // MarkioBrothers.lives--;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()){
            game.setScreen(new GamingScreen((MarkioBrothers)game));
            dispose();
        }
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
        stage.dispose();
    }
}
