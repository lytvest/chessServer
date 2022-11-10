package ru.lytvest.mmo;

import org.junit.jupiter.api.Test;
import ru.lytvest.chess.Board;
import ru.lytvest.chess.Position;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBoard {

    @Test
    public void testStartMovePawn(){
        Board board = Board.START;
        ArrayList<Position> arr1 = new ArrayList<>();
        board.pawnMoves(Position.from("e2"), arr1);

        assertEquals(arr1, List.of(Position.from("e3"), Position.from("e4")));

        ArrayList<Position> arr2 = new ArrayList<>();
        board.pawnMoves(Position.from("e7"), arr2);

        assertEquals(arr2, List.of(Position.from("e6"), Position.from("e5")));
    }
}
