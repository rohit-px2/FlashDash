package com.rprad.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglGraphics;
import com.rprad.game.FlashDash;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Flash Dash";
		config.width = FlashDash.WIDTH;
		config.height = FlashDash.HEIGHT;
		new LwjglApplication(new FlashDash(), config);
	}
}
