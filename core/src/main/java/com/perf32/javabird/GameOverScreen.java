package com.perf32.javabird;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.viewport.FitViewport;

interface IRestart{
    public void restart();
}
public class GameOverScreen{
    private Button button;
    public IRestart onClicked;

    public GameOverScreen(Game game){
        Button.ButtonStyle style = new Button.ButtonStyle();
        button = new Button(style);
        FitViewport viewport = game.getViewport();
        button.setScale(0.5f, 0.5f);
        button.setPosition((viewport.getWorldWidth() - button.getWidth()) / 2, (viewport.getWorldHeight() - button.getHeight()) / 2);
        System.out.println("game over");
    }
    public void update(){
        if(button.isPressed()){
            onClicked.restart();
        }
    }
    public void draw(Batch batch){
        button.draw(batch, 0.9f);
    }
    public void dispose(){
    }
}
