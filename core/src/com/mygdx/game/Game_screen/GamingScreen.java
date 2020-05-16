package com.mygdx.game.Game_screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Game_Scenes.HUD;
import com.mygdx.game.Game_Sprites.Enemy;
import com.mygdx.game.Game_Sprites.Goodies.Goody;

import com.mygdx.game.Game_Sprites.Goodies.GoodyDefinition;
import com.mygdx.game.Game_Sprites.Goodies.Mush;
import com.mygdx.game.Game_Sprites.Markio;
import com.mygdx.game.MarkioBrothers;
import com.mygdx.game.Tools.B2worldCreate;
import com.mygdx.game.Tools.WorldContactListener;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;



public class GamingScreen implements Screen{
    private TextureAtlas textureAtlas;

    private MarkioBrothers markio_game;
    private OrthographicCamera gaming_camera;       //POV of player of game
    private Viewport game_viewport;
    private HUD mar_HUD;

    //variables for Tiled
    private TmxMapLoader mapload;
    private TiledMap Markio_map;
    private OrthogonalTiledMapRenderer mapRenderer;

    //variables for Box2d
    private World gameWorld;
    private Box2DDebugRenderer debugRenderer;
    private B2worldCreate create;

    //sprites
    private Markio Markio_player;

    private Array<Goody> goodies;
    private LinkedBlockingQueue<GoodyDefinition> goodies2Spawn;

    public GamingScreen(MarkioBrothers game){       //constructor takes in the game
        textureAtlas = new TextureAtlas("Markio_andEnemies.pack");

        markio_game = game;
        gaming_camera = new OrthographicCamera();
        game_viewport = new FitViewport(MarkioBrothers.Virtual_W/MarkioBrothers.pixelpm,MarkioBrothers.Virtual_H/MarkioBrothers.pixelpm,gaming_camera);
        mar_HUD = new HUD(game.batch);

        mapload = new TmxMapLoader();
        Markio_map = mapload.load("makio1_1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(Markio_map,1/MarkioBrothers.pixelpm);

        //sets game camera to be centered correctly at beginning of game
        gaming_camera.position.set(game_viewport.getWorldWidth()/2,game_viewport.getWorldHeight()/2,0);

        gameWorld = new World(new Vector2(0,-10),true);
        debugRenderer = new Box2DDebugRenderer();

        create = new B2worldCreate(this);

        Markio_player = new Markio(this);      //create a markio in world

        gameWorld.setContactListener(new WorldContactListener());

        goodies = new Array<Goody>();
        goodies2Spawn = new LinkedBlockingQueue<GoodyDefinition>();

    }

    public void spawnGoody (GoodyDefinition gDef){
        goodies2Spawn.add(gDef);
    }

    public void handleSpawningGoodies(){
        if (!goodies2Spawn.isEmpty()){
            GoodyDefinition gDef = goodies2Spawn.poll();
            if (gDef.classType == Mush.class){
                goodies.add(new Mush(this, gDef.pos.x, gDef.pos.y));
            }
        }
    }

    public TextureAtlas getTextureAtlas(){
        return textureAtlas;
    }

    public void handleInput(float delta_time){
        if (Markio_player.currState != Markio.State.DEAD){
            if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
                Markio_player.body_box2d.applyLinearImpulse(new Vector2(0, 4f), Markio_player.body_box2d.getWorldCenter(), true);
                try {
                    TimeUnit.MILLISECONDS.sleep(250L);      //manually jump, timer before
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                Markio_player.body_box2d.applyLinearImpulse(new Vector2(0, -1f), Markio_player.body_box2d.getWorldCenter(), true);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Markio_player.body_box2d.getLinearVelocity().x <= 2 )
                Markio_player.body_box2d.applyLinearImpulse(new Vector2(0.1f,0),Markio_player.body_box2d.getWorldCenter(),true);

            if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && Markio_player.body_box2d.getLinearVelocity().x >= -2 )
                Markio_player.body_box2d.applyLinearImpulse(new Vector2(-0.1f,0),Markio_player.body_box2d.getWorldCenter(),true);

        }
    }

    public void update(float delata_time){
        handleInput(delata_time);           //handle user input first
        handleSpawningGoodies();

        gameWorld.step(1/60f,6,2);

        Markio_player.update(delata_time);
        for (Enemy enemy: create.getEnemies()){
            enemy.update(delata_time);
            if (enemy.getX() < Markio_player.getX() + 224/MarkioBrothers.pixelpm)
                enemy.BodyB2.setActive(true);
        }

        for (Goody goody: goodies)
            goody.update(delata_time);

        mar_HUD.update(delata_time);

        if (Markio_player.currState != Markio.State.DEAD)
            gaming_camera.position.x = Markio_player.body_box2d.getPosition().x;

        gaming_camera.update();
        mapRenderer.setView(gaming_camera);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        //clears screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.render();       //render our map

        debugRenderer.render(gameWorld,gaming_camera.combined); //renders box2ddebug

        markio_game.batch.setProjectionMatrix(gaming_camera.combined);
        markio_game.batch.begin();
        Markio_player.draw(markio_game.batch);
        for (Enemy enemy: create.getEnemies())
            enemy.draw(markio_game.batch);

        for (Goody goody: goodies)
            goody.draw(markio_game.batch);

        markio_game.batch.end();

        //sets batch to draw what the HUD camera sees
        markio_game.batch.setProjectionMatrix(mar_HUD.HUD_Stage.getCamera().combined);
        mar_HUD.HUD_Stage.draw();

        if (gameOver()) {
            markio_game.setScreen(new GameOverScreen(markio_game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {             //adjusts screen size
        game_viewport.update(width,height);
    }

    public boolean gameOver(){
        if (Markio_player.currState == Markio.State.DEAD && Markio_player.getStateTimer() > 2)
            return true;
        return false;
    }

    public World getGameWorld(){
        return gameWorld;
    }

    public TiledMap getMap (){
        return Markio_map;
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
        Markio_map.dispose();
        mapRenderer.dispose();
        gameWorld.dispose();
        debugRenderer.dispose();
        mar_HUD.dispose();
    }
}
