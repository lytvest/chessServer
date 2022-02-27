package ru.lytvest.chess;

import java.util.Objects;

public class Move {
    public final Pos start;
    public final Pos end;
    public final char figure;

    public Move(Pos start, Pos end, char figure) {
        this.start = start;
        this.end = end;
        this.figure = figure;
    }

    public Move(Pos start, Pos end) {
        this(start, end, '-');
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Move move = (Move) o;

        if (figure != move.figure) return false;
        if (start != null ? !start.equals(move.start) : move.start != null) return false;
        return end != null ? end.equals(move.end) : move.end == null;
    }

    @Override
    public int hashCode() {
        int result = start != null ? start.hashCode() : 0;
        result = 31 * result + (end != null ? end.hashCode() : 0);
        result = 31 * result + (int) figure;
        return result;
    }

    @Override
    public String toString() {
        if(figure == '-')
            return start.toString() + end.toString();

        return start.toString() + end.toString() + figure;
    }

    public static Move from(String s){
        char fig = '-';
        if(s.length() == 5)
            fig = s.charAt(4);
        return new Move(Pos.from(s.substring(0,2)), Pos.from(s.substring(2,4)), fig);
    }
}
