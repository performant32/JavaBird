package com.perf32.javabird;

import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Bird{
    private Game game;
    private Texture birdTexture;
    private Sprite birdSprite;
    private Sound flapSound;

    private float velocity = 0.0f;
    private float jumpHeight = 0.76f;
    private float gravity = -4.0f;
    private float score = 0;

    private Rectangle2D.Float collider;
    private boolean isAlive = true;

    public void create(Game game, float startingX, float size){
        this.game = game;
        birdTexture = new Texture("bird.png");
        birdSprite = new Sprite(birdTexture);
        birdSprite.setSize(size, size);
        collider = new Rectangle2D.Float(startingX, getStartingAltitude(), size, size);
        birdSprite.setPosition(collider.x, collider.y);
        flapSound = Gdx.audio.newSound(Gdx.files.internal("sfx_wing.mp3"));
    }
    public float getStartingAltitude(){
        return (game.getViewport().getWorldHeight() + 1) / 2.0f;
    }
    public void reset(){
        collider.y = getStartingAltitude();
        velocity = 0;
        score = 0;
        isAlive = true;
    }
    public void update(){
        float delta = Gdx.graphics.getDeltaTime();

        if(isAlive)handleInput();
        velocity += gravity * delta;

        collider.y += velocity * delta;

        if(isAlive)
            checkDeath();

        birdSprite.setPosition(collider.x, collider.y);
    }
    private void handleInput(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            velocity = (float)Math.sqrt(-2f * gravity * jumpHeight);
        }
    }
    private void checkDeath(){
        Rectangle2D.Float floor = game.getFloor();
        if(collider.intersects(floor)){
            collider.y = 0;
            die();
            return;
        }
        for (PipePair pipe : game.getPipePairs()) {
            if(pipe.intersects(collider)){
                die();
                return;
            }
            if(collider.x > pipe.getX() + pipe.getWidth() && !pipe.isPassed()){
                game.increaseScore();
                pipe.setPassed();
            }
        }
    }
    public void die(){
        isAlive = false;
        game.onDeath();
    }
    public void draw(SpriteBatch batch){
        birdSprite.draw(batch);
    }
    public void dispose(){
        birdTexture.dispose();
        flapSound.dispose();
    }
    public float getScore(){return score;}
    public boolean isAlive(){return isAlive;}
}
