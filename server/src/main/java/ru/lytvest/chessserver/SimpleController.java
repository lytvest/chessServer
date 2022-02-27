package ru.lytvest.chessserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.lytvest.chessserver.entities.User;
import ru.lytvest.chess.net.ContentRequest;
import ru.lytvest.chessserver.repos.UserRepository;


@Controller
public class SimpleController {

    @Autowired
    private UserRepository users;

    private GameManager gameManager = new GameManager();

    @GetMapping("/")
    public String index(Model model){
        //model.addAttribute(new Answer("no name " + Math.random()));
        model.addAttribute("status", "ok");
        return "jsonTemplate";
    }

    @PostMapping("register")
    public String register(Model model, @RequestBody ContentRequest contentRequest){

        if(users.findByName(contentRequest.user) != null){
            model.addAttribute("status", "fail");
            model.addAttribute("message", "login already in use");
        } else {
            users.save(new User(contentRequest));
            model.addAttribute("status", "ok");
            model.addAttribute("login", contentRequest.user);
        }
        return "jsonTemplate";
    }

    @PostMapping("login")
    public String login(Model model, @RequestBody ContentRequest contentRequest){
        User user = findUser(model, contentRequest);
        if (user == null){
            return "jsonTemplate";
        }

        model.addAttribute("status", "ok");
        model.addAttribute("login", user.name);


        return "jsonTemplate";
    }

    private User findUser(Model model, ContentRequest contentRequest) {
        var user = new User(contentRequest);
        var userFound = users.findByNameAndPass(user.name, user.pass);
        //System.out.println("controller user find " + userFound);
        if (userFound == null) {
            model.addAttribute("status", "fail");
            model.addAttribute("message", "no correct password or login");
        }
        return userFound;
    }

    @PostMapping("getBoard")
    public String findGame(Model model, @RequestBody ContentRequest contentRequest){
        User user = findUser(model, contentRequest);
        System.out.println("get board for " + user);
        if (user == null){
            return "jsonTemplate";
        }

        var answer = gameManager.findGame(user);
        if (answer != null) {
            model.addAttribute("game", answer);
            model.addAttribute("status", "ok");
        } else {
            model.addAttribute("status", "fail");
            model.addAttribute("message", "find enemy");
        }

        return "jsonTemplate";
    }

    @PostMapping("turn")
    public String turn(Model model, @RequestBody ContentRequest contentRequest){
        User user = findUser(model, contentRequest);

        System.out.println("turn for " + user);
        if (user == null){
            return "jsonTemplate";
        }
        if (contentRequest.move == null){
            model.addAttribute("status", "fail");
            model.addAttribute("message", "no find turn");
        }

        var answer = gameManager.turn(user, contentRequest.move);
        if (answer != null) {
            model.addAttribute("game", answer);
            model.addAttribute("status", "ok");
        } else {
            model.addAttribute("status", "fail");
            model.addAttribute("message", "find enemy");
        }

        return "jsonTemplate";
    }

    @PostMapping("endGame")
    public String endGame(Model model, @RequestBody ContentRequest contentRequest){
        User user = findUser(model, contentRequest);
        if (user == null){
            return "jsonTemplate";
        }

        var answer = gameManager.endGame(user);
        if (answer != null) {
            model.addAttribute("game", answer);
            model.addAttribute("status", "ok");
        } else {
            model.addAttribute("status", "fail");
            model.addAttribute("message", "ops end game failed");
        }

        return "jsonTemplate";
    }
}
