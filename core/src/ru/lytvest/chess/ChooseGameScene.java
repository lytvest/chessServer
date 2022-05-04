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
        table.setBounds(50, 50, 600, 600);
        table.add(labelInfo).width(400).height(80).row();
        table.add(fiveMinutesButton).width(400).height(80).row();
        table.add(tenMinutesButton).width(400).height(80).row();
        table.add(botButton).width(400).height(80).row();

        runTimers();

        fiveMinutesButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                val request = new CreateRequest(5 * 60 * 1000);
                request.copyAuth(UserInfo.getInstance());
                HttpController.createAI(request, res -> {
                    startTimerIsSearch(res.getIdGame());
                }, Throwable::printStackTrace);
            }
        });

    }

    private void openBoardScene(String id){
        Timers.stop("stats");
        Scenes.push(new BoardScene(id));
    }

    private void startTimerIsSearch(String id){
        val req = new SearchRequest(id);
        req.copyAuth(UserInfo.getInstance());
        HttpController.search(req, res -> {
            if (res.isStart()){
                openBoardScene(res.getIdGame());
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
