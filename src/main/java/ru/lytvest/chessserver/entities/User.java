package ru.lytvest.chessserver.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;


    public String name;

    public String pass;

    private static MessageDigest hashed = null;

    public static User from(String name, String pass){
        var user = new User();
        user.name = name;
        user.pass = sha(pass);
        return user;
    }

    public static String sha(String pass) {

        String result = "no password";
        try {
            if (hashed == null) {
                hashed = MessageDigest.getInstance("SHA-256");
            }
            hashed.update(pass.getBytes(StandardCharsets.UTF_8));
            result = new String(hashed.digest());
            hashed.reset();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        System.out.println("pass=" + result);
        return result;
    }
}
