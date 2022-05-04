package ru.lytvest.chessserver;

import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.lytvest.chess.net.*;
import ru.lytvest.chessserver.entities.User;
import ru.lytvest.chessserver.repos.UserRepository;


@Controller
public class SimpleController {

    @Autowired
    private UserRepository users;

    Logger log = LoggerFactory.getLogger(SimpleController.class);

    @Autowired
    private GameManager gameManager;

    @GetMapping("/")
    public String index(Model model){
        //model.addAttribute(new Answer("no name " + Math.random()));
        model.addAttribute("status", "ok");
        return "jsonTemplate";
    }

    @PostMapping("register")
    public String register(Model model, @RequestBody AuthRequest request){

        if(users.findByName(request.getLogin()) != null){
            model.addAttribute("status", "fail");
            model.addAttribute("message", "login already in use");
        } else {
            users.save(new User(request.getLogin(), request.getPass()));
            model.addAttribute("status", "ok");
            model.addAttribute("response", new AuthResponse(request.getLogin()));
            log.info("register user " + request);
        }
        return "jsonTemplate";
    }

    @PostMapping("login")
    public String login(Model model, @RequestBody AuthRequest request){
        User user = findUser(model, request);
        if (user == null){
            return "jsonTemplate";
        }

        model.addAttribute("status", "ok");
        model.addAttribute("response", new AuthResponse(request.getLogin()));


        return "jsonTemplate";
    }

    private User findUser(Model model, AuthRequest request) {
        var user = new User(request.getLogin(), request.getPass());
        var userFound = users.findByNameAndPass(user.getName(), user.getPass());
        //System.out.println("controller user find " + userFound);
        if (userFound == null) {
            model.addAttribute("status", "fail");
            model.addAttribute("message", "no correct password or login");
        }
        return userFound;
    }

    @PostMapping("getBoard")
    public String findGame(Model model, @RequestBody BoardRequest request){
        User user = findUser(model, request);

        if (user == null){
            return "jsonTemplate";
        }
        if (request.getIdGame() == null){
            model.addAttribute("status", "fail");
            model.addAttribute("message", "idGame null " + request);
            return "jsonTemplate";
        }

        var answer = gameManager.findGame(request.getIdGame(), request.getLogin());

        log.info("get board for " + user + " " + request.getIdGame() + " " + answer);
        if (answer != null) {
            model.addAttribute("response", answer);
            model.addAttribute("status", "ok");
        } else {
            model.addAttribute("status", "fail");
            model.addAttribute("message", "find enemy");
        }

        return "jsonTemplate";
    }

    @PostMapping("createAI")
    public String createAI(Model model, @RequestBody CreateRequest request){
        User user = findUser(model, request);

        log.info("create ai for " + user);
        if (user == null){
            return "jsonTemplate";
        }

        val answer = gameManager.createAI(user.getName());
        if (answer != null) {
            log.info("answer create ai response " + answer.getIdGame());
            model.addAttribute("response", answer);
            model.addAttribute("status", "ok");
        } else {
            model.addAttribute("status", "fail");
            model.addAttribute("message", "find enemy");
        }

        return "jsonTemplate";
    }

    @PostMapping("create")
    public String create(Model model, @RequestBody CreateRequest request){
        User user = findUser(model, request);

        log.info("create for " + user);
        if (user == null){
            return "jsonTemplate";
        }

        val answer = gameManager.create(user.getName());
        if (answer != null) {
            model.addAttribute("response", answer);
            model.addAttribute("status", "ok");
        } else {
            model.addAttribute("status", "fail");
            model.addAttribute("message", "find enemy");
        }

        return "jsonTemplate";
    }

    @PostMapping("search")
    public String search(Model model, @RequestBody SearchRequest request){
        User user = findUser(model, request);

        log.info("create for " + user);
        if (user == null){
            return "jsonTemplate";
        }

        val answer = gameManager.search(request.getId());
        if (answer != null) {
            model.addAttribute("response", answer);
            model.addAttribute("status", "ok");
        } else {
            model.addAttribute("status", "fail");
            model.addAttribute("message", "find enemy");
        }

        return "jsonTemplate";
    }


    @PostMapping("turn")
    public String turn(Model model, @RequestBody MoveRequest request){
        User user = findUser(model, request);

        log.info("turn for " + user + " " + request);
        if (user == null){
            return "jsonTemplate";
        }
        if (request.getMove() == null || request.getIdGame() == null){
            model.addAttribute("status", "fail");
            model.addAttribute("message", "no find turn");
            return "jsonTemplate";
        }

        var answer = gameManager.turn(request.getIdGame(), request.getLogin(), request.getMove());
        if (answer != null) {
            model.addAttribute("response", answer);
            model.addAttribute("status", "ok");
        } else {
            model.addAttribute("status", "fail");
            model.addAttribute("message", "find enemy");
        }

        return "jsonTemplate";
    }

    @PostMapping("statistic")
    public String turn(Model model){


        var answer = gameManager.getStatistic();
        if (answer != null) {
            model.addAttribute("response", answer);
            model.addAttribute("status", "ok");
        } else {
            model.addAttribute("status", "fail");
            model.addAttribute("message", "find enemy");
        }

        return "jsonTemplate";
    }


}
