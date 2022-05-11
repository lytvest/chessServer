package ru.lytvest.chess.net;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse {

    String meColor;
    String username;
    String enemyUsername;
    String pen;
    long meTime;
    long enemyTime;
    String message;
    String move;
    int end = FALSE;
    public boolean isEnd(){
        return end == TRUE;
    }
    public void setEnd(boolean end){
        this.end = end ? TRUE : FALSE;
    }

    public static final int TRUE = 1;
    public static final int FALSE = 0;
}
