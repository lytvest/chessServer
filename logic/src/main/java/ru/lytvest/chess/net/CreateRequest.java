package ru.lytvest.chess.net;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CreateRequest extends AuthRequest{
    int time;

    public CreateRequest(int time) {
        super();
        this.time = time;
    }
}
