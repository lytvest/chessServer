package ru.lytvest.chess;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SizeToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ru.lytvest.chess.net.HttpController;

import java.util.HashMap;
import java.util.Map;

public class BoardContainer extends Group {

    private final Color whiteColor = Scenes.getSkin().getColor("white-cell");
    private final Color whiteColorGreen = Scenes.getSkin().getColor("white-cell-green");
    private final Color blackColor = Scenes.getSkin().getColor("black-cell");
    private final Color blackColorGreen = Scenes.getSkin().getColor("black-cell-green");

    private Board board;
    private final boolean isWhite;
    private final HashMap<Pos, Image> figures = new HashMap<>();
    private final HashMap<Pos, Image> cells = new HashMap<>();
    private final HashMap<Character, Label> labels = new HashMap<>();
    private float size;
    private float startX = 0f;
    private float startY = 0f;
    private boolean canUpdated = true;

    public BoardContainer(Board board, boolean isWhite) {
        this.board = board;
        this.isWhite = isWhite;
        addListener(new MoveListener());
        createCells();
        updateBoard(board, null);
    }

    private void createFigure(char ch, Pos pos) {
        Gdx.app.log(getClass().getSimpleName(), "createFigure " + ch + " in pos " + pos);
        String name =  "chess/w" + ch;
        if(!Character.isUpperCase(ch)) {
            name = "chess/b" + Character.toUpperCase(ch);
        }
        TextureRegion reg = Scenes.getSkin().getRegion(name);
        reg.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image img = new Image(reg);

        img.setSize(size / 10f, size / 10f);
        img.setName("" + ch);
        figures.put(pos, img);
        addActor(img);
    }
    private Pos toDisplayPos(Pos pos) {
        if (!isWhite)
            return new Pos(7 - pos.x, pos.y);
        return new Pos(pos.x, 7 - pos.y);
    }
    private void moveFigure(Pos old, Pos newPos){
        if (!figures.containsKey(old))
            return;
        Image img = figures.get(old);
        figures.remove(old);
        removeFigure(newPos);
        figures.put(newPos, img);
        Pos pos = toDisplayPos(newPos);
        MoveToAction action = new MoveToAction();
        action.setDuration(0.3f);
        action.setPosition(startX + pos.x * size, startY + pos.y * size);
        img.addAction(action);
        Gdx.app.log(getClass().getSimpleName(), "" + img + " count actions: " + img.getActions().size);
    }

    private void removeFigure(Pos pos){
        if(figures.containsKey(pos)){
            Image img = figures.get(pos);
            figures.remove(pos);
            removeActor(img);
        }
    }

    public void updateBoard(Board board, Move move){
        if(! canUpdated) {
            Gdx.app.log(getClass().getSimpleName(), "update cancel move:"+ move + " " + board.toPen());
            return;
        }
        this.board = board;
        if (move != null){
            moveFigure(move.start, move.end);
        }
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Pos pos = new Pos(x, y);
                if(board.get(pos) == ' ' && figures.containsKey(pos)){
                    removeFigure(pos);
                }
                if(board.get(pos) != ' ' && !figures.containsKey(pos)){
                    createFigure(board.get(pos), pos);
                }
            }
        }
    }
    private void createCells(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Image img = new Image(Scenes.getSkin(), "white");
                cells.put(new Pos(i, j), img);
                addActor(img);
            }
        }
        clearCellsColor();

        String letters = "abcdefgh";
        String nums = "12345678";

        for (int i = 0; i < 8; i++) {
            Label label = new Label("" + letters.charAt(i), Scenes.getSkin(), "board");
            labels.put(letters.charAt(i), label);
            if (i % 2 != 0) {
                label.setColor(whiteColor);
            } else
                label.setColor(blackColor);
            addActor(label);

            Label num = new Label("" + nums.charAt(i), Scenes.getSkin(), "board");
            labels.put(nums.charAt(i), num);
            if (i % 2 == 0) {
                num.setColor(whiteColor);
            } else
                num.setColor(blackColor);
            addActor(num);
        }
    }
    private void clearCellsColor(){
        for(Map.Entry<Pos, Image> entry : cells.entrySet()){
            if ((entry.getKey().x + entry.getKey().y) % 2 == 0)
                entry.getValue().setColor(blackColor);
            else
                entry.getValue().setColor(whiteColor);
        }
    }
    private void updateCellSize(){
        for(Map.Entry<Pos, Image> entry : cells.entrySet()){
            Pos pos = toDisplayPos(entry.getKey());
            entry.getValue().setBounds(startX + pos.x * size, startY + pos.y * size, size, size);
        }
        String letters = "abcdefgh";
        String nums = "12345678";
        if (!isWhite){
            letters = "hgfedcba";
            nums = "87654321";
        }

        for (int i = 0; i < 8; i++) {
            Label label = labels.get(letters.charAt(i));
            label.setPosition(startX + size * i + 6f, startY);

            Label num = labels.get(nums.charAt(i));
            num.setPosition(getWidth() - startX - 3f - num.getPrefWidth(), startY + size * (1 + i) - num.getPrefHeight());
        }
    }

    private void updateFigurePositionAndSize(){
        for(Map.Entry<Pos, Image> entry : figures.entrySet()) {
            SizeToAction action = new SizeToAction();
            action.setSize(size, size);
            action.setDuration(0.3f);
            entry.getValue().addAction(action);
            Pos ip = toDisplayPos(entry.getKey());
            entry.getValue().setPosition(startX + ip.x * size, startY + ip.y * size);
        }
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();

        size = Math.min(getWidth(), getHeight()) / 8f;

        if (getWidth() > getHeight())
            startX = getWidth() / 2 - getHeight() / 2;
        else
            startY = getHeight() / 2 - getWidth() / 2;

        updateCellSize();
        updateFigurePositionAndSize();
    }

    private void setGreenColor(Pos pos){
        if (cells.containsKey(pos)){
            if ((pos.x + pos.y) % 2 == 0)
                cells.get(pos).setColor(blackColorGreen);
            else
                cells.get(pos).setColor(whiteColorGreen);
        }
    }


    class MoveListener extends ClickListener{


        Pos old = null;
        Actor oldActor = null;
        float sx, sy;
        float tdx, tdy;


        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            boolean res = super.touchDown(event, x, y, pointer, button);
            tdx = x;
            tdy = y;

            Pos clickPos = getClickPos(x, y);
            if (old == null) {
                if (board.get(clickPos) != ' ') {
                    setGreenColor(clickPos);
                    for (Move moved : board.filteredMoviesFor(clickPos)) {
                        setGreenColor(moved.end);
                    }
                    old = clickPos;
                    oldActor = figures.get(old);
                    sx = oldActor.getX() - x;
                    sy = oldActor.getY() - y;

                }
            }

            return res;
        }
        public static final float TOUCH_MOVE = 10f;

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            super.touchDragged(event, x, y, pointer);
            if(old != null && (Math.abs(x - tdx) > TOUCH_MOVE || Math.abs(y - tdy) > TOUCH_MOVE)  )
                oldActor.setPosition(sx + x, sy + y);
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            if (old != null  ) {
                Pos clickPos = getClickPos(x, y);
                if (!board.canMove(new Move(old, clickPos)) || board.isWhite != isWhite) {
                    MoveToAction action = new MoveToAction();
                    Pos pos = toDisplayPos(old);
                    action.setPosition(startX + pos.x * size, startY + pos.y * size);
                    action.setDuration(0.3f);
                    oldActor.addAction(action);
                }
                if (old.equals(clickPos)) {
                    old = null;
                }
            }

            super.touchUp(event, x, y, pointer, button);
        }

        private Pos getClickPos(float x, float y) {
            return toDisplayPos(new Pos((int) ((x - startX) / size), (int) ((y - startY) / size)));
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
            Pos clickPos = getClickPos(x, y);
            Gdx.app.log(getClass().getSimpleName(), "click in pos " + clickPos + " old pos=" + old);
            if (old == null) {
                if (board.get(clickPos) != ' ') {
                    setGreenColor(clickPos);
                    for (Move moved : board.filteredMoviesFor(clickPos)) {
                        setGreenColor(moved.end);
                    }
                    old = clickPos;
                }
            } else {

                clearCellsColor();
                Move move = new Move(old, clickPos);
                old = null;
                if(!board.canMove(move) && board.isWhite != isWhite)
                    return;

                board = board.moved(move);
                Gdx.app.log(getClass().getSimpleName(), "move " + move + " win:" + board.isWinner());

                updateBoard(board, move);
                canUpdated = false;
                HttpController.move(move.toString(), (answer) -> {
                    Gdx.app.log(getClass().getSimpleName(), "move to " + move + " pen:" + answer.pen);
                    canUpdated = true;
                    updateBoard(Board.fromPen(answer.pen), null);
                });
            }

        }
    }
}
