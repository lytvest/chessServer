package ru.lytvest.chess;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "of")
public class Position {
    public int x, y;

    @Override
    public String toString() {
        return "" + (char)('a' + x) + (8 - y);
    }

    public static Position from(String s){
        return new Position(s.charAt(0) - 'a', 8 - Integer.parseInt("" + s.charAt(1)));
    }

    public boolean isCorrect(){
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }
}
