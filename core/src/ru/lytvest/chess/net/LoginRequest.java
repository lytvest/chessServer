package ru.lytvest.chess.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;

import java.util.function.Consumer;

public class LoginRequest extends HttpItem{
    Consumer<String> callback;
    public LoginRequest(Consumer<String> callback) {
        this.callback = callback;
    }

    @Override
    public void settingRequest() {
        super.settingRequest();
        addContent();
        builder.url(URL + "/login");
    }

    @Override
    public void handleResponse(Net.HttpResponse httpResponse) {
        Status status = JSON.fromJson(Status.class, httpResponse.getResultAsStream());
        if(status.getStatus().equals("ok")){
            Gdx.app.postRunnable(() -> {
                callback.accept(HttpController.content.user);
            });
        }
    }



}
