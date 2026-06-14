package com.perf32.javabird;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Game extends ApplicationAdapter implements IRestart{
    private SpriteBatch batch;
    private FitViewport viewport;
    private OrthographicCamera uiCamera;

    private Bird bird;
    private ArrayList<PipePair> pipes = new ArrayList<PipePair>();

    private Rectangle2D.Float floor;
    private Rectangle2D.Float ceiling;

    private Label scoreLabel;
    private float score;
    private FreeTypeFontGenerator generator;
    private BitmapFont font;

    private GameOverScreen gameOverScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(8, 8 * (9f / 16.0f));
        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        bird = new Bird();
        bird.create(this, 1.0f, 0.5f);

        floor = new Rectangle2D.Float(0,-1, Float.MAX_VALUE,1);
        ceiling = new Rectangle2D.Float(0,viewport.getWorldHeight(), Float.MAX_VALUE,1);
        createPipePairs();

        generator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Regular.ttf"));
        FreeTypeFontParameter parameters = new FreeTypeFontParameter();
        parameters.size = 24;
        font = generator.generateFont(parameters);

        Label.LabelStyle scoreStyle = new Label.LabelStyle();
        scoreStyle.font = font;
        scoreStyle.fontColor = Color.WHITE;
        scoreLabel = new Label("Score: " + score, scoreStyle);

        updateFont();
    }

    public void increaseScore(){
        score++;
        scoreLabel.setText("Score: " + score);
    }

    public void onDeath(){
        gameOverScreen = new GameOverScreen(this);
        gameOverScreen.onClicked = this;
    }
    public void restart(){
        if(gameOverScreen != null)gameOverScreen = null;
        disposePipePairs();
        createPipePairs();
        bird.reset();
    }
    public void createPipePairs(){
        for(float i = 0; i < 15; i+=1.5f){
            PipePair pipe = new PipePair();
            pipe.create(this, i + viewport.getWorldWidth() / 2.0f, 1.5f);
            pipes.add(pipe);
        }
    }
    @Override
    public void resize(int width, int height){
        viewport.update(width, height, true);
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        updateFont();
    }
    private void updateFont(){
    }
    @Override
    public void render() {
        logic();
        draw();
    }

    private void logic(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)){
            restart();
        }
        bird.update();
        if(bird.isAlive())
            movePipePairs();
    }
    private void movePipePairs(){
        for(int i = 0; i < pipes.size(); i++){
            PipePair pipe = pipes.get(i);
            pipe.update();
            Rectangle2D.Float collider = pipe.getCollider();
            if(collider.x + collider.getWidth() < 0){
                pipes.remove(i);
                i--;
            }
        }
    }
    private void draw(){
        ScreenUtils.clear(Color.BLUE);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        for(PipePair pipe : pipes){
            pipe.draw(batch);
        }
        bird.draw(batch);
        if(gameOverScreen != null)gameOverScreen.draw(batch);
        batch.end();

        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();
        scoreLabel.draw(batch, 1);
        batch.end();
    }
    @Override
    public void dispose() {
        bird.dispose();
        batch.dispose();
        generator.dispose();
        if(gameOverScreen != null)gameOverScreen = null;
        disposePipePairs();
    }
    private void disposePipePairs(){
        for(PipePair pipe : pipes)
            pipe.dispose();
        pipes.clear();
    }
    public FitViewport getViewport(){return viewport;}
    public Rectangle2D.Float getFloor(){return floor;}
    public Rectangle2D.Float getCeiling(){return ceiling;}
    public ArrayList<PipePair> getPipePairs(){return pipes;}
    public float getScore(){return score;}
}
