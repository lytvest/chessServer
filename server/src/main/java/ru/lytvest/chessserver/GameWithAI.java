package ru.lytvest.chessserver;

import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lytvest.chess.Board;
import ru.lytvest.chess.Move;
import ru.lytvest.chess.net.AnswerBoard;
import ru.lytvest.chessserver.entities.User;

import java.util.Objects;
import java.util.Random;

public class GameWithAI implements GameInf {

    Logger log = LoggerFactory.getLogger(getClass());

    Board board = Board.START;
    long timeWhite = 0;
    long timeBlack = 0;
    long oldTurnTime = 0;
    Move oldTurn = null;
    User user;
    Random random = new Random();
    String color = random.nextBoolean() ? "white": "black";
    boolean isCalculated = false;

    GameWithAI(User user){
        this.user = user;
    }

    boolean firstAnswer = true;
    @Override
    public AnswerBoard getAnswer(User user) {
        if(!firstAnswer)
            enemyMove();
        firstAnswer = false;

        val old = oldTurn == null ? null : oldTurn.toString();
        //oldTurn = null;
        return AnswerBoard.builder()
                .username(user.getName())
                .enemyUsername("ai-bot")
                .pen(board.toPen())
                .timeBlack(timeBlack)
                .timeWhite(timeWhite)
                .youColor(color)
                .move(old)
        .build();
    }

    @Override
    public AnswerBoard move(User user, String turn) {
        Move move = Move.from(turn);
        firstAnswer = true;
        if (Objects.equals(color, "white") != board.isWhite || !board.canMove(move)) {
            return getAnswer(user);
        }

        updateTime(board.isWhite);

        moved(move);
        val answer = getAnswer(user);
        answer.setMove(move.toString());

        return answer;
    }

    void enemyMove(){
        if (board.isWhite != Objects.equals(color, "white") && !isCalculated) {
            isCalculated = true;
            val move = board.bestTurn(2);
            if(move != null) {
                moved(move);
                updateTime(board.isWhite);
            }
            isCalculated = false;
        }
    }

    private void moved(Move move) {
        board = board.moved(move);
        oldTurnTime = System.currentTimeMillis();
        oldTurn = move;
        log.info("game for (" + user.getName() + ") move:" + move);
    }

    private void updateTime(boolean isWhite) {
        if (board.numberCourse != 1 && isWhite) {
            timeWhite += System.currentTimeMillis() - oldTurnTime;
        }
        if (board.numberCourse != 1 && !isWhite) {
            timeBlack = System.currentTimeMillis() - oldTurnTime;
        }
    }
}
