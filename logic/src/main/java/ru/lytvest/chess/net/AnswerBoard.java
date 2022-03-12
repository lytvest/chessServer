package ru.lytvest.chess.net;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerBoard {

    private String youColor = "white";
    private String username = "";
    private String enemyUsername = "";
    private String pen = "";
    private long timeWhite = 0;
    private long timeBlack = 0;
    private String message = "";
    private String move;

}
