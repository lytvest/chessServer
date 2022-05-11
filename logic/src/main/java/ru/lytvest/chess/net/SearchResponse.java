package ru.lytvest.chess.net;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchResponse {
    String idGame;
    String start;
    long maxTime;
    public SearchResponse(String idGame, boolean start, long maxTime){
        this.idGame = idGame;
        this.start = start ? "t" : "f";
        this.maxTime = maxTime;
    }

    public boolean isStart(){
        return start.equals("t");
    }

    public void setStart(boolean start){
        this.start = start ? "t" : "f";
    }
}
