package ru.lytvest.chess.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;

import java.util.Objects;
import java.util.function.Consumer;

public class GetBoardRequest extends HttpItem {

    Consumer<AnswerBoard> callback;

    public GetBoardRequest(Consumer<AnswerBoard> callback) {
        this.callback = callback;
    }

    @Override
    public void settingRequest() {
        super.settingRequest();
        builder.url(URL + "/getBoard");
        addContent();
    }

    @Override
    public void handleResponse(Net.HttpResponse httpResponse) {
        Status status = JSON.fromJson(Status.class, httpResponse.getResultAsStream());

        if (Objects.equals(status.status, "ok")){
            Gdx.app.postRunnable(() -> callback.accept(status.game));
        }

        HttpController.getBoard(callback);
    }
}
