package ru.lytvest.chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class LoadingScene extends Scene{



    VisLabel label = new VisLabel("Loading...", Color.WHITE);
    AssetManager manager = new AssetManager();
    Class<? extends Scene> nextScene;

    LoadingScene(Class<? extends Scene> nextScene){
        this.nextScene = nextScene ;
        label.setPosition(width() / 2 - label.getPrefWidth() / 2, height() / 2);
        stage.addActor(label);

        VisTextButton btn = new VisTextButton("started", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                label.setText("started");
            }
        });
        stage.addActor(btn);
    }

    @Override
    public void show() {
        super.show();
        Gdx.app.log(getClass().getSimpleName(), "loading...");
        manager.load("skin.json", Skin.class);

    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(manager.update()){
            Gdx.app.log(getClass().getSimpleName(), "end loading...");
            Skin skin = manager.get("skin.json", Skin.class);
            Scenes.setSkin(skin);
            Scenes.backAndPush(nextScene);

        } else {
            label.setText("loading... " + manager.getProgress() * 100 + "%");
        }
    }
}
