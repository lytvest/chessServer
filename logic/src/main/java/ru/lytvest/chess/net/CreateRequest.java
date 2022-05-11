package ru.lytvest.chess.net;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CreateRequest extends AuthRequest{
    @Getter
    int time;

    public CreateRequest(int time) {
        super();
        this.time = time;
    }
}
