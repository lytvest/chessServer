package ru.lytvest.chess.net;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    String login;
    String pass;

    public void copyAuth(AuthRequest auth){
        login = auth.login;
        pass = auth.pass;
    }
}
