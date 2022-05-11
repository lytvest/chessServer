package ru.lytvest.chess;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import lombok.val;
import ru.lytvest.chess.net.CreateRequest;
import ru.lytvest.chess.net.HttpController;
import ru.lytvest.chess.net.SearchRequest;
import ru.lytvest.chess.net.UserInfo;

public class ChooseGameScene extends Scene{

    TextButton fiveMinutesButton;
    TextButton tenMinutesButton;
    TextButton botButton;
    Label labelInfo;


    @Override
    public void show() {
        super.show();
        Table table = new Table();
        stage.addActor(table);
        labelInfo = new Label("--", getSkin());
        fiveMinutesButton = new TextButton("5 минут", getSkin());
        tenMinutesButton = new TextButton("10 минут", getSkin());
        botButton = new TextButton("Сыграть с компьютером", getSkin());
        table.add(labelInfo).width(400).height(80).row();
        table.add(fiveMinutesButton).width(400).height(80).row();
        table.add(tenMinutesButton).width(400).height(80).row();
        table.add(botButton).width(400).height(80).row();
        table.setBounds(width() / 2 - 300, height() / 2 - table.getPrefHeight() / 2, 600, table.getPrefHeight());


        runTimers();

        fiveMinutesButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                val request = new CreateRequest(5 * 60 * 1000);
                request.copyAuth(UserInfo.getInstance());
                HttpController.create(request, res -> {
                    startTimerIsSearch(res.getIdGame());
                }, Throwable::printStackTrace);
            }
        });
        tenMinutesButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                val request = new CreateRequest(10 * 60 * 1000);
                request.copyAuth(UserInfo.getInstance());
                HttpController.create(request, res -> {
                    startTimerIsSearch(res.getIdGame());
                }, Throwable::printStackTrace);
            }
        });
        botButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                val request = new CreateRequest(20 * 60 * 1000);
                request.copyAuth(UserInfo.getInstance());
                HttpController.createAI(request, res -> {
                    startTimerIsSearch(res.getIdGame());
                }, Throwable::printStackTrace);
            }
        });

    }

    private void openBoardScene(String id, int maxTime){
        Timers.stop("stats");
        Scenes.push(new BoardScene(id, maxTime));
    }

    private void startTimerIsSearch(String id){
        val req = new SearchRequest(id);
        req.copyAuth(UserInfo.getInstance());
        HttpController.search(req, res -> {
            if (res.isStart()){
                openBoardScene(res.getIdGame(), (int) res.getMaxTime());
            } else {
                Timers.runOneFrom(1.3f, () -> startTimerIsSearch(id));
            }
        }, Throwable::printStackTrace);
    }

    private void runTimers(){

        Timers.repeat("stats", 2f, () -> {
            HttpController.getStatistic(stats -> {
                labelInfo.setText(stats.getCountGames() + " Партий \n" + stats.getCountPlayers() + " Игроков");
            }, Throwable::printStackTrace);
        });
    }
}
