package ru.lytvest.chess.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;


import java.util.Random;
import java.util.function.Consumer;

public class HttpController {

    public static final String URL = "http://localhost:8080";
    public final Json json = new Json();

    private ContentRequest content;
    private HttpController(){
        json.setOutputType(JsonWriter.OutputType.json);
    }


    private void send(String path, ContentRequest content, Consumer<AnswerBoard> callback){
        final HttpRequestBuilder builder = new HttpRequestBuilder();
        builder.newRequest()
                .method(Net.HttpMethods.POST)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .url(URL + path)
                .content(json.toJson(content));

        Net.HttpRequest request = builder.build();
        Gdx.app.log(getClass().getSimpleName(), "send " + request.getUrl() + " " + request.getContent());
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String res = httpResponse.getResultAsString();
                Gdx.app.log(getClass().getSimpleName(), "answer for " + request.getUrl() + " : " + res);

                Status status = json.fromJson(Status.class, res);
                if (status.isOk()) {
                    if (status.getGame() == null)
                        status.setGame(new AnswerBoard());
                    Gdx.app.postRunnable(() -> callback.accept(status.getGame()));
                }
                else {
                    Gdx.app.log(getClass().getSimpleName(), "status fail " + request.getUrl() + " " + request.getContent() + " \n    " + status);
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log(getClass().getSimpleName(), "fail "  + request.getUrl() + " " + request.getContent() + " \n    message:" + t.getMessage());
                callback.accept(null);
            }

            @Override
            public void cancelled() {
                Gdx.app.log(getClass().getSimpleName(), "cancel "  + request.getUrl() + " " + request.getContent());
                callback.accept(null);
            }
        });
    }

    private void login(Consumer<AnswerBoard> callback){
        send("/login", content, callback);
    }
    private void register(Consumer<AnswerBoard> callback){
        if(content == null) {
            Random rand = new Random();
            content = new ContentRequest("user" + rand.nextInt(1000), "p" + rand.nextInt());
        }
        send("/register", content, (ans) -> {
            if(ans == null){
                content = null;
                register(callback);
            } else {
                callback.accept(ans);
            }
        });
    }

    public void authorization(Consumer<AnswerBoard> callback) {
        if (content == null)
            register(callback);
        else
            login(callback);
    }

    public boolean isAuthorization(){
        return content != null;
    }


    public void createAI(Consumer<AnswerBoard> callback){
        if(!isAuthorization()) {
            authorization((s) -> createAI(callback));
        } else {
            send("/createAI", content, callback);
        }
    }

    public void move(String move, Consumer<AnswerBoard> callback){
        if(!isAuthorization())
            authorization((s) -> move(move, callback));
        else {
            content.setMove(move);
            send("/turn", content, callback);
            content.setMove(null);
        }
    }

    public void getBoard(Consumer<AnswerBoard> callback){
        if(!isAuthorization())
            authorization((s) -> getBoard(callback));
        else {
            send("/getBoard", content, callback);
        }
    }
    private static HttpController instance;
    public static HttpController getInstance(){
        if(instance == null)
            instance = new HttpController();
        return instance;
    }
}
