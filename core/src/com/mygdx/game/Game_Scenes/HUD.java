package com.mygdx.game.Game_Scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MarkioBrothers;



public class HUD implements Disposable{
    public Stage HUD_Stage;         //like a box
    private Viewport HUD_vport;     //new viewport, so HUD doesn't shift around on screen

    private Integer Game_timer;
    private float time_counter;
    private static Integer points;

    private Label countdown_Lab;
    private static Label points_Lab;
    private Label time_Lab;
    private Label level_Lab;
    private Label world_Lab;
    private Label markio_lab;

    public HUD (SpriteBatch spr_batch){
        Game_timer = 300;
        time_counter = 0;
        points = 0;

        HUD_vport = new FitViewport(MarkioBrothers.Virtual_W,MarkioBrothers.Virtual_H,new OrthographicCamera());
        HUD_Stage = new Stage(HUD_vport,spr_batch);

        Table Hud_table = new Table();          //organizes labels in our stage
        Hud_table.top();                        //shifts from default position, to top pf stage
        Hud_table.setFillParent(true);          // table is now size of stage

        time_Lab = new Label("TIME",new Label.LabelStyle(new BitmapFont(),Color.WHITE));
        level_Lab = new Label("1-1",new Label.LabelStyle(new BitmapFont(),Color.WHITE));
        world_Lab = new Label("WORLD",new Label.LabelStyle(new BitmapFont(),Color.WHITE));
        markio_lab = new Label("MARKIO",new Label.LabelStyle(new BitmapFont(),Color.WHITE));
        points_Lab = new Label(String.format("%06d",points),new Label.LabelStyle(new BitmapFont(),Color.WHITE));
        countdown_Lab = new Label(String.format("%03d",Game_timer),new Label.LabelStyle(new BitmapFont(),Color.WHITE));

        Hud_table.add(markio_lab).expandX().padTop(10);        //expand label across screen, share equal space on screen
        Hud_table.add(world_Lab).expandX().padTop(10);
        Hud_table.add(time_Lab).expandX().padTop(10);
        Hud_table.row();                                        //creates new row
        Hud_table.add(points_Lab).expandX();
        Hud_table.add(level_Lab).expandX();
        Hud_table.add(countdown_Lab).expandX();

        HUD_Stage.addActor(Hud_table);
    }

    public void update(float deltaTime){
        time_counter += deltaTime;
        if (time_counter >= 1){
            Game_timer--;
            countdown_Lab.setText(String.format("%03d", Game_timer));
            time_counter = 0;
        }
    }

    public static void addScore(int intValue){
        points += intValue;
        points_Lab.setText(String.format("%06d",points));
    }

    @Override
    public void dispose() {
        HUD_Stage.dispose();
    }
}
