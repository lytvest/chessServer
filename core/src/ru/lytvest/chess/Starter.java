package ru.lytvest.chess;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.VisUI;
import ru.lytvest.chess.net.HttpController;

public class Starter extends ApplicationAdapter {

	
	@Override
	public void create () {
		VisUI.load(VisUI.SkinScale.X2);
		Scenes.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Scenes.push(new LoadingScene(BoardScene.class));
	}

	@Override
	public void render () {
		ScreenUtils.clear(0.08627451f, 0.08235294f, 0.07058824f, 1);
		Scenes.update(Gdx.graphics.getDeltaTime());
		HttpController.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize(int width, int height) {
		Scenes.resize(width, height);
	}

	@Override
	public void dispose () {
		VisUI.dispose();
	}
}
