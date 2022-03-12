package ru.lytvest.chessserver;

import ru.lytvest.chess.net.AnswerBoard;
import ru.lytvest.chessserver.entities.User;

public interface GameInf {

    AnswerBoard getAnswer(User user) ;
    AnswerBoard move(User user, String turn) ;

}
