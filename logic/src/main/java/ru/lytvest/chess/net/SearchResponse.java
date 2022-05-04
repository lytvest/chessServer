package ru.lytvest.chess.net;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchResponse {
    String idGame;
    String start;
    public SearchResponse(String idGame, boolean start){
        this.idGame = idGame;
        this.start = start ? "t" : "f";
    }

    public boolean isStart(){
        return start.equals("t");
    }

    public void setStart(boolean start){
        this.start = start ? "t" : "f";
    }
}
