package ru.lytvest.chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import lombok.val;
import ru.lytvest.chess.net.HttpController;

import java.util.Objects;

public class BoardScene extends Scene {
    BoardContainer boardContainer;
    Board board;

    @Override
    public void show() {
        super.show();
        board = Board.EMPTY;
        boardContainer = new BoardContainer(board, true);
        stage.addActor(boardContainer);
        final Label label = new Label("find enemy...", skin);
        label.setPosition(width() / 2 - label.getPrefWidth() / 2, height() / 2);
        stage.addActor(label);
        HttpController.getInstance().createAI(answerBoard -> {

                    boardContainer.remove();
                    board = Board.fromPen(answerBoard.getPen());
                    boardContainer = new BoardContainer(board, Objects.equals(answerBoard.getYouColor(), "white"));
                    stage.addActor(boardContainer);
                    resizeBoard();
                    label.remove();
                    boardContainer.setCanServerUpdate(true);

//                    Gdx.app.log(getClass().getSimpleName(), "get board " + answerBoard.getPen());
//                    boardContainer.updateBoard(Board.fromPen(answerBoard.getPen()),
//                            answerBoard.getMove() != null ? Move.from(answerBoard.getMove()) : null);
                }
        );

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Gdx.app.log(getClass().getSimpleName(), "resize");
        resizeBoard();
    }

    public void resizeBoard() {
        val size = Math.min(width(), height());

        boardContainer.setBounds(0, 0, width(), height());
    }

    @Override
    public void update(float delta) {
        super.update(delta);

    }
}
