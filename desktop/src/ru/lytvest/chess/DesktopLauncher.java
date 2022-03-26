package ru.lytvest.chess;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import ru.lytvest.chess.Starter;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		TexturePacker.Settings settings = new TexturePacker.Settings();
		settings.maxWidth = 2048;
		settings.maxHeight = 2048;

		//TexturePacker.process(settings, "../source",".","skin.atlas");
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowSizeLimits(16 * 40, 9 * 40, 16 * 170, 9 * 170);
		config.setWindowedMode(13 * 90, 9 * 90);
		new Lwjgl3Application(new Starter(), config);
	}
}
