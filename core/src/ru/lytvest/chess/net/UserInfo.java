package ru.lytvest.chess.net;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Random;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class UserInfo extends AuthRequest{

    public UserInfo(String login, String pass){
        super(login, pass);
    }
    private static UserInfo info;

    public static UserInfo getInstance(){
        if(info == null) {
            Random rand = new Random();
            info = new UserInfo("user-" + rand.nextInt(100), "pass" + rand.nextInt(10000));
        }
        return info;
    }
}
