package ru.lytvest.chess.net;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentRequest {
    public String user;
    public String pass;
    public String move;

    public ContentRequest(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

    private static MessageDigest hashed = null;
    public static String sha(String pass) {

        String result = "no password";
        try {
            if (hashed == null) {
                hashed = MessageDigest.getInstance("SHA-256");
            }
            hashed.reset();
            hashed.update(pass.getBytes(StandardCharsets.UTF_8));
            result = new String(hashed.digest());
            //System.out.println("hashed " + hashed);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println("pass=" + pass + " hash" + result);
        return result;
    }
}
