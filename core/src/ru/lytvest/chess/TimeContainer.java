package ru.lytvest.chess;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;


public class TimeContainer extends Group {

    Image bgTime = new Image(Scenes.getSkin(), "round-rectangle");
    Image bgName = new Image(Scenes.getSkin(), "round-rectangle");
    Image line = new Image(Scenes.getSkin(), "white");
    Color green = Scenes.getSkin().getColor("bg-green");
    Color gray = Scenes.getSkin().getColor("bg-grey");
    Color lineGreen = Scenes.getSkin().getColor("line-green");
    Color fontLight = Scenes.getSkin().getColor("font-light");
    Color fontGray = Scenes.getSkin().getColor("font-gray");
    float maxTime = 5 * 60;
    Label timeLabel = new Label(timeToString(), Scenes.getSkin());
    Label nameLabel;

    float lineProgress = 1f;

    float time = 0;

    boolean active = false;
    boolean isRotate = false;


    public TimeContainer(String name, boolean isRotate) {
        this.isRotate = isRotate;

        line.setColor(lineGreen);
        bgTime.setColor(gray);
        bgName.setColor(gray);

        nameLabel = new Label(name, Scenes.getSkin(), "board");
        timeLabel.setColor(fontGray);
        nameLabel.setColor(fontGray);

        addActor(bgTime);
        addActor(bgName);
        addActor(line);
        addActor(nameLabel);
        addActor(timeLabel);
        updateColors();
    }

    public void updateTime(int time){
        this.time = time / 1000f;
    }

    public void setActive(boolean active) {
        this.active = active;
        updateColors();
    }

    private void updateColors() {
        if (active) {
            bgTime.setColor(green);
            timeLabel.setColor(fontLight);
        } else {
            bgTime.setColor(gray);
            timeLabel.setColor(fontGray);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (active) {
            time += delta;
            timeLabel.setText(timeToString());
            lineProgress = 1 - (time / maxTime);
            lineUpdateSize();
            if (time > maxTime)
                time = maxTime;
        }
    }

    private String timeToString() {
        int t1 = Math.round((maxTime - time));
        int t2 = t1 % 60;
        int t3 = t1 / 60 % 60;
        return addZero("" + t3) + ":" + addZero("" + t2);
    }

    private String addZero(String s) {

        return s.length() < 2 ? "0" + s : s;
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();

        bgName.setBounds(0, getHeight() - nameLabel.getPrefHeight() - 20, getWidth(), nameLabel.getPrefHeight() + 20);
        bgTime.setSize(timeLabel.getPrefWidth() + 50, getHeight() - bgName.getHeight());
        if (isRotate) {
            bgName.setY(0);
            bgTime.setY(bgName.getHeight());
        }
        nameLabel.setPosition(40, bgName.getY() + 10);
        timeLabel.setPosition(bgTime.getWidth() / 2 - timeLabel.getPrefWidth() / 2, bgTime.getY() + bgTime.getHeight() / 2 - timeLabel.getPrefHeight() / 2);

        lineUpdateSize();
    }

    private void swapY(Actor a1, Actor a2){
        float pos = a1.getY();
        a1.setY(a2.getY());
        a2.setY(pos);
    }

    private void lineUpdateSize() {
        line.setBounds(0, bgTime.getHeight() - 3, getWidth() * lineProgress, 6);
        if (isRotate) {
            line.setY(bgName.getHeight() - 3);
        }
    }
}
