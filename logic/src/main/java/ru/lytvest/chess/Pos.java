package ru.lytvest.chess;

import java.util.Objects;

public class Pos {
    final int x, y;

    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Pos f(int x, int y) {
        return new Pos(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pos pos = (Pos) o;
        return x == pos.x && y == pos.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "" + (char)('a' + x) + (8 - y);
    }

    public static Pos from(String s){
        return new Pos(s.charAt(0) - 'a', 8 - Integer.parseInt("" + s.charAt(1)));
    }
}
