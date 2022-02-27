package ru.lytvest.chess;

import java.util.ArrayList;

public class Board {

    public final String arr;
    public final Boolean isWhite;
    public final Boolean whiteOO;
    public final Boolean whiteOOO;
    public final Boolean blackOO;
    public final Boolean blackOOO;
    public final Pos oldPawn;
    public final int numberCourse;

    public Board(String arr, Boolean isWhite, Boolean whiteOO, Boolean whiteOOO, Boolean blackOO, Boolean blackOOO, Pos oldPawn, int numberCourse) {
        this.arr = arr;
        this.isWhite = isWhite;
        this.whiteOO = whiteOO;
        this.whiteOOO = whiteOOO;
        this.blackOO = blackOO;
        this.blackOOO = blackOOO;
        this.oldPawn = oldPawn;
        this.numberCourse = numberCourse;
    }

    private Board(String arr) {
        this(arr, true, true, true, true, true, null, 1);
    }

    public int getIndex(int x, int y) {
        return y * 8 + x;
    }

    public int getIndex(Pos pos) {
        return pos.y * 8 + pos.x;
    }

    public Pos indexToPos(int index) {
        return Pos.f(index % 8, index / 8);
    }


    public char get(int x, int y) {
        return arr.charAt(getIndex(x, y));
    }

    public char get(Pos pos) {
        return arr.charAt(getIndex(pos));
    }

    public Board moved(Move move) {

        char fig = get(move.start);

        String narr = movedString(arr, move);
        if (fig == 'k' && move.end.equals(Pos.f(6, 0)) && move.start.equals(Pos.f(4, 0))) {
            narr = movedString(narr, new Move(Pos.f(7, 0), Pos.f(5, 0), get(7, 0)));
        }
        if (fig == 'k' && move.end.equals(Pos.f(2, 0)) && move.start.equals(Pos.f(4, 0))) {
            narr = movedString(narr, new Move(Pos.f(0, 0), Pos.f(3, 0), get(0, 0)));
        }
        if (fig == 'K' && move.end.equals(Pos.f(6, 7)) && move.start.equals(Pos.f(4, 7))) {
            narr = movedString(narr, new Move(Pos.f(7, 7), Pos.f(5, 7), get(7, 7)));
        }
        if (fig == 'K' && move.end.equals(Pos.f(2, 7)) && move.start.equals(Pos.f(4, 7))) {
            narr = movedString(narr, new Move(Pos.f(0, 7), Pos.f(3, 7), get(0, 7)));
        }

        if (Character.toLowerCase(fig) == 'p' && move.end.equals(oldPawn)) {
            narr = setInPos(narr, Pos.f(move.end.x, move.start.y), ' ');
        }
        if (fig == 'P' && move.end.y == 0) {
            narr = setInPos(narr, move.end, Character.toUpperCase(move.figure));
        }
        if (fig == 'p' && move.end.y == 7) {
            narr = setInPos(narr, move.end, Character.toLowerCase(move.figure));
        }

        Pos oldPawn = null;
        int dy = -1;
        if (Character.isLowerCase(fig))
            dy = 1;
        if (Character.toLowerCase(fig) == 'p' && Math.abs(move.start.y - move.end.y) == 2)
            oldPawn = Pos.f(move.start.x, move.start.y + dy);
        int ncourse = numberCourse;
        if (!isWhite)
            ncourse += 1;

        return new Board(
                narr,
                !isWhite,
                whiteOO && fig != 'K' && !move.end.equals(Pos.f(7, 7)) && !move.start.equals(Pos.f(7, 7)),
                whiteOOO && fig != 'K' && !move.end.equals(Pos.f(0, 7)) && !move.start.equals(Pos.f(0, 7)),
                blackOO && fig != 'k' && !move.end.equals(Pos.f(7, 0)) && !move.start.equals(Pos.f(7, 0)),
                blackOOO && fig != 'k' && !move.end.equals(Pos.f(0, 0)) && !move.start.equals(Pos.f(0, 0)),
                oldPawn,
                ncourse
        );

    }

    String movedString(String arr, Move move) {
        char fig = get(move.start);
        String s1 = setInPos(arr, move.start, ' ');
        return setInPos(s1, move.end, fig);
    }

    String setInPos(String ss, Pos pos, char ch) {
        int index = getIndex(pos);
        return ss.substring(0, index) + ch + ss.substring(index + 1);
    }

    void pawnMoves(Pos pos, ArrayList<Pos> res) {
        if (get(pos) == 'p') {
            if (get(pos.x, pos.y + 1) == ' ')
                res.add(Pos.f(pos.x, pos.y + 1));
            if (pos.y == 1 && get(pos.x, pos.y + 1) == ' ' && get(pos.x, pos.y + 2) == ' ')
                res.add(Pos.f(pos.x, pos.y + 2));
            if (pos.x > 0 && Character.isUpperCase(get(pos.x - 1, pos.y + 1)))
                res.add(Pos.f(pos.x - 1, pos.y + 1));
            if (pos.x < 7 && Character.isUpperCase(get(pos.x + 1, pos.y + 1)))
                res.add(Pos.f(pos.x + 1, pos.y + 1));
        } else {
            if (get(pos.x, pos.y - 1) == ' ')
                res.add(Pos.f(pos.x, pos.y - 1));
            if (pos.y == 6 && get(pos.x, pos.y - 1) == ' ' && get(pos.x, pos.y - 2) == ' ')
                res.add(Pos.f(pos.x, pos.y - 2));
            if (pos.x > 0 && Character.isLowerCase(get(pos.x - 1, pos.y - 1)))
                res.add(Pos.f(pos.x - 1, pos.y - 1));
            if (pos.x < 7 && Character.isLowerCase(get(pos.x + 1, pos.y - 1)))
                res.add(Pos.f(pos.x + 1, pos.y - 1));
        }
    }

    void checkTo(int x, int y, int dx, int dy, ArrayList<Pos> res) {
        int nx = x;
        int ny = y;
        while (true) {
            nx += dx;
            ny += dy;
            if (nx >= 0 && nx < 8 && ny >= 0 && ny < 8) {
                if (get(nx, ny) == ' ')
                    res.add(new Pos(nx, ny));
                else {
                    if (Character.isUpperCase(get(nx, ny)) != Character.isUpperCase(get(x, y)))
                        res.add(new Pos(nx, ny));
                    return;
                }
            } else return;
        }
    }

    void rookMoves(Pos pos, ArrayList<Pos> res) {
        int x = pos.x;
        int y = pos.y;

        checkTo(x, y, -1, 0, res);
        checkTo(x, y, 1, 0, res);
        checkTo(x, y, 0, -1, res);
        checkTo(x, y, 0, 1, res);
    }

    void bishopMoves(Pos pos, ArrayList<Pos> res) {
        int x = pos.x;
        int y = pos.y;

        checkTo(x, y, -1, -1, res);
        checkTo(x, y, -1, 1, res);
        checkTo(x, y, 1, -1, res);
        checkTo(x, y, 1, 1, res);
    }

    void queenMoves(Pos pos, ArrayList<Pos> res) {
        rookMoves(pos, res);
        bishopMoves(pos, res);
    }


    void knightMoves(Pos pos, ArrayList<Pos> res) {
        int x = pos.x;
        int y = pos.y;
        ArrayList<Pos> list = new ArrayList<>(8);
        list.add(Pos.f(x + 1, y + 2));
        list.add(Pos.f(x + 2, y + 1));
        list.add(Pos.f(x + 2, y - 1));
        list.add(Pos.f(x + 1, y - 2));
        list.add(Pos.f(x - 1, y - 2));
        list.add(Pos.f(x - 2, y - 1));
        list.add(Pos.f(x - 2, y + 1));
        list.add(Pos.f(x - 1, y + 2));

        checkMoveInPos(pos, res, list);
    }

    private boolean checkKnightMoves(Pos p) {
        for (Pos p1 : enemyFigures()) {
            if (Character.toUpperCase(get(p1)) != 'K') {
                for (Move move : moviesNotFilterFor(p1, false)) {
                    if (move.end == p1)
                        return false;
                }
            }
        }
        return get(p) == ' ';
    }

    void kindMoves(Pos pos, ArrayList<Pos> res) {
        int x = pos.x;
        int y = pos.y;
        ArrayList<Pos> list = new ArrayList<>(8);
        list.add(Pos.f(x + 1, y - 1));
        list.add(Pos.f(x + 1, y));
        list.add(Pos.f(x + 1, y + 1));
        list.add(Pos.f(x, y - 1));
        list.add(Pos.f(x, y + 1));
        list.add(Pos.f(x - 1, y - 1));
        list.add(Pos.f(x - 1, y));
        list.add(Pos.f(x - 1, y + 1));

        checkMoveInPos(pos, res, list);

        if (get(pos) == 'K' && pos.equals(Pos.f(4, 7)) && whiteOO && checkKnightMoves(Pos.f(5, 7))
                && checkKnightMoves(Pos.f(6, 7)))
            res.add(Pos.f(6, 7));
        if (get(pos) == 'K' && pos.equals(Pos.f(4, 7)) && whiteOOO && checkKnightMoves(Pos.f(3, 7))
                && checkKnightMoves(Pos.f(2, 7)) && checkKnightMoves(Pos.f(1, 7)))
            res.add(Pos.f(2, 7));
        if (get(pos) == 'k' && pos.equals(Pos.f(4, 0)) && blackOO && checkKnightMoves(Pos.f(5, 0))
                && checkKnightMoves(Pos.f(6, 0)))
            res.add(Pos.f(6, 0));
        if (get(pos) == 'k' && pos.equals(Pos.f(4, 0)) && blackOOO && checkKnightMoves(Pos.f(3, 0))
                && checkKnightMoves(Pos.f(2, 0)) && checkKnightMoves(Pos.f(1, 0)))
            res.add(Pos.f(2, 0));

    }

    private void checkMoveInPos(Pos pos, ArrayList<Pos> res, ArrayList<Pos> list) {
        for (Pos pos1 : list) {
            if (
                    pos1.x >= 0 && pos1.x < 8 && pos1.y >= 0 && pos1.y < 8 &&
                            (Character.isUpperCase(get(pos)) != Character.isUpperCase(get(pos1)) || get(pos1) == ' ')
            ) res.add(pos1);
        }
    }

    ArrayList<Move> moviesNotFilterFor(Pos pos, boolean skipEnemy) {
        ArrayList<Pos> res = new ArrayList<>();
        char fig = get(pos);
        if (fig == ' ' || (Character.isUpperCase(fig) != isWhite && skipEnemy))
            return new ArrayList<>();

        switch (Character.toUpperCase(fig)) {
            case 'K':
                kindMoves(pos, res);
                break;
            case 'P':
                pawnMoves(pos, res);
                break;
            case 'N':
                knightMoves(pos, res);
                break;
            case 'B':
                bishopMoves(pos, res);
                break;
            case 'R':
                rookMoves(pos, res);
                break;
            case 'Q':
                queenMoves(pos, res);
                break;
        }
        ArrayList<Move> res2 = new ArrayList<Move>(res.size());
        for (Pos re : res) {
            res2.add(new Move(pos, re));
        }
        return res2;
    }

    ArrayList<Pos> enemyFigures() {
        ArrayList<Pos> res = new ArrayList<Pos>();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (get(x, y) != ' ' && Character.isUpperCase(get(x, y)) != isWhite)
                    res.add(Pos.f(x, y));
            }
        }
        return res;
    }

    ArrayList<Pos> meFigures() {
        ArrayList<Pos> res = new ArrayList<Pos>();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (get(x, y) != ' ' && Character.isUpperCase(get(x, y)) == isWhite)
                    res.add(Pos.f(x, y));
            }
        }
        return res;
    }

    Pos findPos(char fig) {
        return indexToPos(arr.indexOf(fig));
    }

    boolean isWinner() {
        char kind = 'k';
        if (!isWhite) kind = 'K';
        Pos pos = findPos(kind);
        for (Move move : allMovies()) {
            if (move.end.equals(pos))
                return true;
        }
        return false;
    }

    ArrayList<Move> allMovies() {
        ArrayList<Move> res = new ArrayList<Move>();
        for (Pos p : meFigures()) {
            res.addAll(moviesNotFilterFor(p, true));
        }
        return res;
    }

    public ArrayList<Move> filteredMovies() {
        ArrayList<Move> res = new ArrayList<>();
        for (Pos p : meFigures()) {
            for (Move move : moviesNotFilterFor(p, true)) {
                if (!moved(move).isWinner())
                    res.add(move);
            }
        }
        return res;
    }

    public ArrayList<Move> filteredMoviesFor(Pos pos) {
        ArrayList<Move> res = new ArrayList<>();

        for (Move move : moviesNotFilterFor(pos, true)) {
            if (!moved(move).isWinner())
                res.add(move);
        }

        return res;
    }


    public String toPen() {
        return toPen(arr);
    }

    String toPen(String arr) {
        StringBuilder sb = new StringBuilder();

        int countSpace = 0;
        for (int i = 0; i < arr.length(); i++) {
            char ch = arr.charAt(i);
            if ((ch != ' ' || i % 8 == 0) && countSpace > 0) {
                sb.append(countSpace);
                countSpace = 0;
            }
            if (i % 8 == 0 && i != 0)
                sb.append('/');


            if (ch != ' ')
                sb.append(ch);
            else
                countSpace++;
        }
        sb.append(' ');
        if (isWhite) sb.append('w');
        else sb.append('b');
        sb.append(' ');
        if (whiteOO) sb.append('K');
        if (whiteOOO) sb.append('Q');
        if (blackOO) sb.append('k');
        if (blackOOO) sb.append('q');
        if (!(whiteOO || whiteOOO || blackOO || blackOOO))
            sb.append('-');
        sb.append(' ');

        if (oldPawn == null)
            sb.append('-');
        else {
            sb.append(oldPawn);
        }
        sb.append(' ');
        sb.append('0').append(' ').append(numberCourse);
        return sb.toString();
    }

    public boolean canMove(Move move) {
        return moviesNotFilterFor(move.start, true).contains(move) && !moved(move).isWinner();
    }


    public static final String START_PEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public static final Board START = fromPen(START_PEN);
    public static final String EMPTY_PEN = "8/8/8/8/8/8/8/8 w KQkq - 0 1";
    public static final Board EMPTY = fromPen(EMPTY_PEN);;

    public static Board fromPen(String pen) {
        String[] ss = pen.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String line : ss[0].split("/")) {
            for (int i = 0; i < line.length(); i++) {
                if (Character.isDigit(line.charAt(i))) {
                    int count = Integer.parseInt("" + line.charAt(i));
                    for (int j = 0; j < count; j++) {
                        sb.append(" ");
                    }
                } else {
                    sb.append(line.charAt(i));
                }
            }
        }

        if (ss.length < 6) {
            System.out.println("no correct pen");
            return new Board(sb.toString());
        }
        Pos old = null;
        if (ss[3].length() == 2)
            old = Pos.from(ss[3]);

        return new Board(
                sb.toString(),
                ss[1].contains("w"),
                ss[2].contains("K"),
                ss[2].contains("Q"),
                ss[2].contains("k"),
                ss[2].contains("q"),
                old,
                Integer.parseInt(ss[5]));

    }


}


