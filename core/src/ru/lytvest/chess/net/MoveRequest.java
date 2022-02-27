package ru.lytvest.chess.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;

import java.util.Objects;
import java.util.function.Consumer;

public class MoveRequest extends HttpItem{
    String move;
    Consumer<AnswerBoard> callback;

    public MoveRequest(String move, Consumer<AnswerBoard> callback) {
        this.move = move;
        this.callback = callback;
    }

    @Override
    public void settingRequest() {
        super.settingRequest();
        HttpController.content.move = move;
        builder.url(URL + "/turn");
        addContent();
    }

    @Override
    public void handleResponse(Net.HttpResponse httpResponse) {
        super.handleResponse(httpResponse);
        Status status = JSON.fromJson(Status.class, httpResponse.getResultAsStream());

        if (Objects.equals(status.status, "ok")){
            Gdx.app.postRunnable(() -> callback.accept(status.game));
        } else {
            HttpController.move(HttpController.content.move, callback);
        }
    }
}
