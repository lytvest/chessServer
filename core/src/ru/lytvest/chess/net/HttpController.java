package ru.lytvest.chess.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonWriter;
import lombok.val;


import java.util.function.Consumer;

public class HttpController {

    public static final String URL = "http://localhost:8080";
    public final Json json = new Json();
    private final JsonReader reader = new JsonReader();

    private HttpController(){
        json.setOutputType(JsonWriter.OutputType.json);
    }



    private <SEND, GET> void send(String path, Class<GET> getClass, SEND send, Consumer<GET> callSuccess, Consumer<Throwable> callException){
        final HttpRequestBuilder builder = new HttpRequestBuilder();
        builder.newRequest()
                .method(Net.HttpMethods.POST)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .url(URL + path);

        if (send != null){
            builder.content(json.toJson(send));
        }

        final Net.HttpRequest request = builder.build();
        //  Gdx.app.log(getClass().getSimpleName(), "send " + request.getUrl() + " " + request.getContent());
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String res = httpResponse.getResultAsString();
                Gdx.app.log(getClass().getSimpleName(), "response " + res);
                val value = reader.parse(res);
                val status = value.get("status").asString();
                if (status.equals("ok")){
                    val response = value.get("response");
                    val obj = response.toJson(JsonWriter.OutputType.json);
                    Gdx.app.log(getClass().getSimpleName(), "answer OK for " + request.getUrl() + " : " + obj);
                    val get = json.fromJson(getClass, obj);
                    Gdx.app.postRunnable(() -> callSuccess.accept(get));
                } else {
                    Gdx.app.log(getClass().getSimpleName(), "answer FAIL for " + request.getUrl() );
                    Gdx.app.postRunnable(() -> callException.accept(new Exception(value.get("message").asString())));
                }
            }

            @Override
            public void failed(Throwable t) {
                //      Gdx.app.log(getClass().getSimpleName(), "fail "  + request.getUrl() + " " + request.getContent() + " \n    message:" + t.getMessage());
                t.printStackTrace();
                callException.accept(t);
            }

            @Override
            public void cancelled() {
                Gdx.app.log(getClass().getSimpleName(), "cancel "  + request.getUrl() + " " + request.getContent());

            }
        });
    }

    public static void login(AuthRequest request, Consumer<AuthResponse> callSuccess, Consumer<Throwable> callException){
        instance().send("/login", AuthResponse.class, request, callSuccess, callException);
    }
    public static void register(AuthRequest request, Consumer<AuthResponse> callbackSuccess, Consumer<Throwable> callException){
        instance().send("/register", AuthResponse.class, request, callbackSuccess, callException);
    }

    public static void createAI(CreateRequest request, Consumer<CreateResponse> response, Consumer<Throwable> exc){
        instance().send("/createAI", CreateResponse.class, request, response, exc);
    }
    public static void create(CreateRequest request, Consumer<CreateResponse> response, Consumer<Throwable> exc){
        instance().send("/create", CreateResponse.class, request, response, exc);
    }

    public static void move(MoveRequest request, Consumer<MoveResponse> response, Consumer<Throwable> exc){
        instance().send("/turn", MoveResponse.class, request, response, exc);
    }

    public static void getBoard(BoardRequest request, Consumer<BoardResponse> response, Consumer<Throwable> exc){
        instance().send("/getBoard", BoardResponse.class, request, response, exc);
    }

    public static void getStatistic(Consumer<Statistic> response, Consumer<Throwable> exc){
        instance().send("/statistic", Statistic.class, null, response, exc);
    }

    public static void search(SearchRequest request, Consumer<SearchResponse> response, Consumer<Throwable> exc){
        instance().send("/search", SearchResponse.class, request, response, exc);
    }

    private static HttpController instance;
    public static HttpController instance(){
        if(instance == null)
            instance = new HttpController();
        return instance;
    }
}
