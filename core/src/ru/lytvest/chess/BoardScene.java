package ru.lytvest.chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import ru.lytvest.chess.net.BoardResponse;
import ru.lytvest.chess.net.HttpController;

import java.util.Objects;

public class BoardScene extends Scene {
    BoardContainer boardContainer;
    Board board;

    @Override
    public void show() {
        super.show();
        board = Board.EMPTY;
        boardContainer = new BoardContainer(board, true, null, "", "");
        stage.addActor(boardContainer);
        final Label label = new Label("find enemy...", skin);
        label.setPosition(width() / 2 - label.getPrefWidth() / 2, height() / 2);
        stage.addActor(label);
       // sendCreate(label);

    }

//    private void sendCreate(Label label) {
//        HttpController.create(answerBoard -> {
//                    if (answerBoard != null) {
//                        createNewBoard(label, answerBoard);
//                    } else {
//
//                    }
//                }
//        );
//    }
//
//    private void sendGetBoard(Label label) {
//        HttpController.getBoard();
//    }
//
//    private void createNewBoard(Label label, BoardResponse boardResponse) {
//        boardContainer.remove();
//        board = Board.fromPen(boardResponse.getPen());
//        boardContainer = new BoardContainer(board, Objects.equals(boardResponse.getMeColor(), "white"), boardResponse.getIdGame(), boardResponse.getUsername(), boardResponse.getEnemyUsername());
//        stage.addActor(boardContainer);
//        resizeBoard();
//        label.remove();
//        boardContainer.setCanServerUpdate(true);
//    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Gdx.app.log(getClass().getSimpleName(), "resize");
        resizeBoard();
    }

    public void resizeBoard() {
        float size = Math.min(width(), height());
        boardContainer.setBounds(0, 0, width(), height());
    }

    @Override
    public void update(float delta) {
        super.update(delta);

    }
}
