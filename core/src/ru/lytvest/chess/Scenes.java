package ru.lytvest.chess;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class Scenes {

    public static float width;
    public static float height;
    private static Skin skin = null;

    private static Array<Scene> scenes = new Array<>(false, 7);


    public static void resize(int nw, int nh) {
        width = 16 * 90;
        height = ((float) nh) / nw * width;

        if(!scenes.isEmpty()){
            scenes.get(scenes.size - 1).resize(nw, nh);
        }
    }

    public static void push(Scene scene) {
        scenes.add(scene);
        scene.setSkin(skin);
        scene.show();
        scene.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public static void push(Class<? extends Scene> cls) {
        try {
            Scene scene = ClassReflection.newInstance(cls);
            push(scene);
        } catch (ReflectionException e) {
            e.printStackTrace();
        }
    }

    public static void backAndPush(Scene scene) {
        back();
        push(scene);
    }

    public static void back() {
        if (!scenes.isEmpty()) {
            scenes.removeIndex(scenes.size - 1);
        }
    }

    public static void update(float delta) {

        if (!scenes.isEmpty()) {
            scenes.get(scenes.size - 1).update(delta);
        }
        Timers.update(delta);
    }

    public static Skin getSkin() {
        return skin;
    }

    public static void setSkin(Skin skin) {
        Scenes.skin = skin;
    }


    public static void backAndPush(Class<? extends Scene> scene) {
        back();
        push(scene);
    }
}
