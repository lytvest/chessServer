package ru.lytvest.chess.net;

public class Wait extends HttpItem {
    private float time = 0.5f;

    public Wait() {}
    public Wait(float time) {
        this.time = time;
    }

    @Override
    public boolean update(float delta) {
        time -= delta;
        return time <= 0f;
    }
}
