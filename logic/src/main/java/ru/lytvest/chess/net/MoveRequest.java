package ru.lytvest.chess.net;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class MoveRequest extends BoardRequest{
    String move;

    public MoveRequest(String idGame, String move) {
        this.idGame = idGame;
        this.move = move;
    }
}
