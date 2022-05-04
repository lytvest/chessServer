package ru.lytvest.chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import lombok.val;
import ru.lytvest.chess.net.BoardRequest;
import ru.lytvest.chess.net.HttpController;
import ru.lytvest.chess.net.MoveRequest;
import ru.lytvest.chess.net.UserInfo;

public class BoardScene extends Scene {
    String idGame;
    Label label;

    public BoardScene(String idGame) {
        this.idGame = idGame;
    }

    BoardContainer boardContainer;
    Board board;

    @Override
    public void show() {
        super.show();
//        board = Board.EMPTY;
//        boardContainer = new BoardContainer(board, true, null, "", "");
//        stage.addActor(boardContainer);
//        label = new Label("find enemy...", skin);
//        label.setPosition(width() / 2 - label.getPrefWidth() / 2, height() / 2);
//        stage.addActor(label);
        sendCreate();
        startServerUpdate();

    }

    private void sendCreate() {
        val req = new BoardRequest(idGame);
        req.copyAuth(UserInfo.getInstance());
        HttpController.getBoard(req, answerBoard -> {
                    boardContainer = new BoardContainer(
                            Board.fromPen(answerBoard.getPen()),
                            answerBoard.getMeColor().equals("white"),
                            this::sendMove
                    );
                    stage.addActor(boardContainer);
                    resizeBoard();
                }, Throwable::printStackTrace
        );
    }

    private void sendMove(Move move) {
        val req = new MoveRequest(idGame, move.toString());
        req.copyAuth(UserInfo.getInstance());
        HttpController.move(req, (answer) -> {
            boardContainer.setCanUpdated(true);
            Gdx.app.log(getClass().getSimpleName(), "can update true");
        }, Throwable::printStackTrace);
    }

    private void startServerUpdate() {
        val req = new BoardRequest(idGame);
        req.copyAuth(UserInfo.getInstance());
        HttpController.getBoard(req, (response) -> {
            val board = Board.fromPen(response.getPen());
            if(boardContainer != null)
                boardContainer.updateBoard(board, Move.from(response.getMove()));
            Timers.runOneFrom(1f, this::startServerUpdate);
        }, Throwable::printStackTrace);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Gdx.app.log(getClass().getSimpleName(), "resize");
        resizeBoard();
    }

    public void resizeBoard() {
        float size = Math.min(width(), height());
        if (boardContainer != null)
            boardContainer.setBounds(0, 0, width(), height());
    }

    @Override
    public void update(float delta) {
        super.update(delta);

    }
}
