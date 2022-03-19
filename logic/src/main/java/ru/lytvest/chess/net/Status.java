package ru.lytvest.chess.net;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Status {
    private String status;
    private String message;
    private AnswerBoard game;
    private String login;

    public boolean isOk(){
        return Objects.equals(status, "ok");
    }
}
