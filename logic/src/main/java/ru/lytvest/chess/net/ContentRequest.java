package ru.lytvest.chess.net;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ContentRequest {
    public String user;
    public String pass;
    public String move;

    public ContentRequest(){}

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
            hashed.update(pass.getBytes(Charset.forName("UTF-8")));
            result = new String(hashed.digest());
            //System.out.println("hashed " + hashed);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println("pass=" + pass + " hash" + result);
        return result;
    }

    @Override
    public String toString() {
        return "ContentRequest{" +
                "user='" + user + '\'' +
                ", pass='" + pass + '\'' +
                ", move='" + move + '\'' +
                '}';
    }
}
