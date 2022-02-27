package ru.lytvest.chess.net;

import com.badlogic.gdx.Gdx;

import java.util.ArrayDeque;
import java.util.function.Consumer;

public class HttpController {


    private static ArrayDeque<HttpItem> queue = new ArrayDeque<>();

    public static ContentRequest content;


    public static void update(float delta) {
        if (!queue.isEmpty()){
            if(queue.peekFirst().update(delta)){
                HttpItem item = queue.pollFirst();
                Gdx.app.log("HttpController", "queue remove " + (item != null ? item.getClass().getSimpleName() : "null"));
            }
        }
    }
    public static void addLast(HttpItem item){
        queue.addLast(item);
    }

    public static void authorization(Consumer<String> callback){

        if(content == null){
            Gdx.app.log("HttpController", "send register request");
            RegisterRequest register = new RegisterRequest(callback);
            addLast(register);
        } else {
            Gdx.app.log("HttpController", "send Login request");
            addLast(new LoginRequest(callback));
        }
    }

    public static void move(String move, Consumer<AnswerBoard> callback){
        if(content == null)
            authorization((s) -> {});

        Gdx.app.log("HttpController", "send move [" + move + "] request");
        addLast(new MoveRequest(move, callback));
    }

    public static void getBoard(Consumer<AnswerBoard> callback){
        if(content == null)
            authorization((s) -> {});

        Gdx.app.log("HttpController", "send getBoard request");
        addLast(new Wait(0.7f));
        addLast(new GetBoardRequest(callback));
    }

}
