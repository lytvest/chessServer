package ru.lytvest.chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class Scene {


    Stage stage = new Stage(new ExtendViewport(Scenes.width, Scenes.height));
    public float width() {
        return Scenes.width;
    }
    public float height() {
        return Scenes.height;
    }



    protected Skin skin;


    public void show() {
        Gdx.input.setInputProcessor(stage);
    }


    public void update(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        Gdx.app.log("Scene", "resize world width=" + this.width() + ", height=" + this.height() +
                ", display width=" + width + ", height=" + height);
        stage.getViewport().update(width, height, true);
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

}
