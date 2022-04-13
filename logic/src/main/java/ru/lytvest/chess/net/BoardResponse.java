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
}
