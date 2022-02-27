package ru.lytvest.chess.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.function.Consumer;

public class HttpItem implements Net.HttpResponseListener {

    public static final String URL = "http://localhost:8080";
    public final HttpRequestBuilder builder = new HttpRequestBuilder();
    public static final Json JSON = new Json();
    static {
        JSON.setOutputType(JsonWriter.OutputType.json);
    }

    boolean send = false;
    boolean isEnd = false;
    public boolean update(float delta){
        if(!send){
            send = true;
            builder.newRequest();
            settingRequest();
            Gdx.net.sendHttpRequest(getRequest(), this);
        }
        return isEnd;
    }

    public Net.HttpRequest getRequest(){
        return builder.build();
    }
    public void settingRequest(){
        baseSetting();
    }
    public void baseSetting() {
        builder.method(Net.HttpMethods.POST)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
    }

    public void handleResponse(Net.HttpResponse httpResponse) {}
    @Override
    public void handleHttpResponse(Net.HttpResponse httpResponse) {
        isEnd = true;
        handleResponse(httpResponse);
    }
    public void addContent(){
        builder.content(JSON.toJson(HttpController.content));
    }

    @Override
    public void failed(Throwable t) {
        isEnd = true;
        Gdx.app.log(getClass().getSimpleName(), "http fail " + t.getMessage());
    }

    @Override
    public void cancelled() {
        isEnd = true;
        Gdx.app.log(getClass().getSimpleName(), "http cancelled");
    }


}
