package com.perf32.javabird;

import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PipePair{
    private Texture pipeEnd;
    private Texture pipe;

    private Sprite topPipeEndSprite;
    private Sprite topPipeSprite;

    private Sprite bottomPipeEndSprite;
    private Sprite bottomPipeSprite;

    private float pipeEndSpriteHeight;

    private float height;
    private boolean isPassed = false;

    private Rectangle2D.Float topCollider;
    private Rectangle2D.Float bottomCollider;

    public void create(Game game, float x, float pipeHeight){
        pipeEnd = new Texture("pipe_end.png");
        pipe = new Texture("pipe.png");

        topPipeSprite = new Sprite(pipe);
        topPipeEndSprite = new Sprite(pipeEnd);

        bottomPipeSprite = new Sprite(pipe);
        bottomPipeEndSprite = new Sprite(pipeEnd);

        height = pipeHeight;

        pipeEndSpriteHeight = 1 / ((float)pipeEnd.getWidth() / pipeEnd.getHeight());

        float pipeWidth = 0.65f;
        topPipeEndSprite.setSize(pipeWidth, -pipeEndSpriteHeight);
        topPipeSprite.setSize(pipeWidth, pipeHeight);

        bottomPipeEndSprite.setSize(pipeWidth, pipeEndSpriteHeight);
        bottomPipeSprite.setSize(pipeWidth, pipeHeight);

        FitViewport viewport = game.getViewport();
        topCollider = new Rectangle2D.Float(x, viewport.getWorldHeight() - height, pipeWidth, height);
        bottomCollider = new Rectangle2D.Float(x, 0, pipeWidth, height);

    }
    public void update(){
        float moveAmount = 1.0f * Gdx.graphics.getDeltaTime();
        topCollider.x -= moveAmount;
        bottomCollider.x -= moveAmount;

        topPipeSprite.setPosition(topCollider.x, topCollider.y);
        topPipeEndSprite.setPosition(topCollider.x, topCollider.y);

        bottomPipeSprite.setPosition(bottomCollider.x, bottomCollider.y);
        bottomPipeEndSprite.setPosition(bottomCollider.x, bottomCollider.y + (height - pipeEndSpriteHeight));
    }
    public void draw(Batch batch){
        topPipeSprite.draw(batch);
        topPipeEndSprite.draw(batch);

        bottomPipeSprite.draw(batch);
        bottomPipeEndSprite.draw(batch);
    }
    public boolean intersects(Rectangle2D.Float collider){
        return bottomCollider.intersects(collider) || topCollider.intersects(collider);
    }
    public void setPassed(){isPassed = true;}
    public void dispose(){
        pipeEnd.dispose();
        pipe.dispose();
    }
    public Rectangle2D.Float getCollider(){return topCollider;}
    public float getX(){return topCollider.x;}
    public float getWidth(){return 1.0f;}
    public boolean isPassed(){return isPassed;}
}
