package ru.lytvest.chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import lombok.val;
import ru.lytvest.chess.net.BoardRequest;
import ru.lytvest.chess.net.HttpController;
import ru.lytvest.chess.net.MoveRequest;
import ru.lytvest.chess.net.UserInfo;

public class BoardScene extends Scene {
    String idGame;
    Label label;
    int maxTime;

    public BoardScene(String idGame, int maxTime) {
        this.idGame = idGame;
        this.maxTime = maxTime;
    }

    BoardContainer boardContainer;
    PlayerInfoContainer playerInfoContainer;

    @Override
    public void show() {
        super.show();
//        board = Board.EMPTY;
//        boardContainer = new BoardContainer(board, true, null, "", "");
//        stage.addActor(boardContainer);
        label = new Label("Игра окончена!", skin);
        label.setColor(Color.BLACK);
        label.setPosition(width() / 2 - label.getPrefWidth() / 2, height() / 2);
        label.setVisible(false);
        stage.addActor(label);

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
                    playerInfoContainer = new PlayerInfoContainer(answerBoard.getUsername(), answerBoard.getEnemyUsername(), maxTime);
                    stage.addActor(boardContainer);
                    stage.addActor(playerInfoContainer);
                    resizeElements();
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
        playerInfoContainer.enemyActive();
    }

    private void startServerUpdate() {
        val req = new BoardRequest(idGame);
        req.copyAuth(UserInfo.getInstance());
        HttpController.getBoard(req, (response) -> {
            val nBoard = Board.fromPen(response.getPen());
            if (boardContainer != null) {
                if (boardContainer.isCanUpdated() && !boardContainer.getBoard().equals(nBoard)){
                    boardContainer.updateBoard(nBoard, Move.from(response.getMove()));
                    if (nBoard.numberCourse > 1)
                        playerInfoContainer.meActive();
                    playerInfoContainer.updateTime((int) response.getMeTime(), (int) response.getEnemyTime());
                }
            }
            if (!response.isEnd())
                Timers.runOneFrom(1f, this::startServerUpdate);
            else {
                label.setVisible(true);
                playerInfoContainer.stop();

            }
        }, Throwable::printStackTrace);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        Gdx.app.log(getClass().getSimpleName(), "resize");
        resizeElements();
    }

    public void resizeElements() {
        if (boardContainer != null)
            boardContainer.setBounds(0, 0, width() * 0.7f, height());
        if (playerInfoContainer != null) {
            playerInfoContainer.setBounds(width() * 0.7f, height() * 0.3f, width() * 0.3f, height() * 0.38f);
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);

    }
}
