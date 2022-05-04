package ru.lytvest.chess;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Move {
    public Position start;
    public Position end;
    public char figure;


    public Move(Position start, Position end) {
        this(start, end, '-');
    }

    public static Move from(String s) {
        if (s == null)
            return null;

        char fig = '-';
        if (s.length() == 5)
            fig = s.charAt(4);
        return new Move(Position.from(s.substring(0, 2)), Position.from(s.substring(2, 4)), fig);
    }
    @Override
    public String toString(){
        if (figure == '-')
            return "" + start + end;
        else
            return "" + start + end + figure;
    }

}
