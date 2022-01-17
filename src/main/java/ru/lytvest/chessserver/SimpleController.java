package ru.lytvest.chessserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.lytvest.chessserver.entities.User;
import ru.lytvest.chessserver.repos.UserRepository;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


@Controller
public class SimpleController {

    @Autowired
    private UserRepository users;
    @GetMapping("/")
    public String index(Model model){
        model.addAttribute(new Answer("no name " + Math.random()));
        return "jsonTemplate";
    }

    @GetMapping("add/{user}/{pass}")
    public String addNewUser(Model model, @PathVariable("pass") String pass, @PathVariable String user){

        users.save(User.from(user, pass));
        model.addAttribute("users", users.findAll());

        return "jsonTemplate";
    }
}
