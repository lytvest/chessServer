package ru.lytvest.chess;

//import lombok.AllArgsConstructor;
//import lombok.Value;

//@Value
//@AllArgsConstructor(staticName = "of")
public class Position {
    public int x, y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Position of(int x, int y){
        return new Position(x, y);
    }

    @Override
    public String toString() {
        return "" + (char)('a' + x) + (8 - y);
    }

    public static Position from(String s){
        return new Position(s.charAt(0) - 'a', 8 - Integer.parseInt("" + s.charAt(1)));
    }
}
