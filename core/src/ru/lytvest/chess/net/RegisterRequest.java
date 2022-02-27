package ru.lytvest.chess.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;

import java.util.Random;
import java.util.function.Consumer;

public class RegisterRequest extends HttpItem{
    Random rand = new Random();
    Consumer<String> callback;

    public RegisterRequest(Consumer<String> callback) {
        this.callback = callback;
    }

    @Override
    public void settingRequest() {
        super.settingRequest();
        if(HttpController.content == null)
            HttpController.content = new ContentRequest("user" + rand.nextInt(1000), "p" + rand.nextInt());
        builder.url(URL + "/register")
                .content(JSON.toJson(HttpController.content));
    }

    @Override
    public void handleResponse(Net.HttpResponse httpResponse) {
        Status status = JSON.fromJson(Status.class, httpResponse.getResultAsStream());
        if(status.status.equals("ok")){
            Gdx.app.postRunnable(() -> {
                callback.accept(HttpController.content.user);
            });
        }
    }
}
