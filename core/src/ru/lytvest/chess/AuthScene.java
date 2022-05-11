package ru.lytvest.chess;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import lombok.val;
import ru.lytvest.chess.net.AuthRequest;
import ru.lytvest.chess.net.HttpController;
import ru.lytvest.chess.net.UserInfo;

public class AuthScene extends Scene{

    Label label;
    TextButton errorButtonRepeat;
    Image bg;

    @Override
    public void show() {
        super.show();
        bg = new Image(Scenes.getSkin(), "round-rectangle");
        val gray = skin.getColor("bg-grey");
        label = new Label("", skin);
        bg.setColor(gray);
        bg.setSize(700, 400);
        bg.setPosition(width() / 2 - bg.getWidth() / 2, height() / 2 - bg.getHeight() / 2);
        stage.addActor(bg);
        val user = UserInfo.getInstance();
        label.setText("login: " + user.getLogin() + "\npassword: " + user.getPass() + "\nПодождите идет Авторизация...");
        label.setPosition(bg.getX() + 10, bg.getY() + bg.getHeight() - label.getPrefHeight() - 10);
        stage.addActor(label);

        errorButtonRepeat = new TextButton("Повторить еще раз", getSkin());

        errorButtonRepeat.setBounds(bg.getX() + 10, bg.getY() + 10, errorButtonRepeat.getPrefWidth() + 20, errorButtonRepeat.getPrefHeight() + 20);
        errorButtonRepeat.setVisible(false);
        errorButtonRepeat.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sendRegisterRequest();
                errorButtonRepeat.setVisible(false);
            }
        });
        stage.addActor(errorButtonRepeat);
        sendRegisterRequest();
    }

    private void sendRegisterRequest() {
        UserInfo.randomInfo();
        val user = UserInfo.getInstance();
        val request = new AuthRequest();
        request.copyAuth(user);
        HttpController.register(request, (res) -> {
            label.setText(label.getText() + "\nУспешно!");
            label.setPosition(bg.getX() + 10, bg.getY() + bg.getHeight() - label.getPrefHeight() - 10);
            Timers.runOneFrom(1, () -> {
                Scenes.push(new ChooseGameScene());
            });
        }, (e) -> {
            label.setText("login: " + user.getLogin() + "\npassword: " + user.getPass() + "\nПодождите идет Авторизация..."
                    + "\nОшибка!\n" + e.getMessage());
            label.setPosition(bg.getX() + 10, bg.getY() + bg.getHeight() - label.getPrefHeight() - 10);
            errorButtonRepeat.setVisible(true);
        });
    }
}
