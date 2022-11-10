package ru.lytvest.chess;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;

import java.util.*;

public class Board {

    public final char[][] arr;
    public final Boolean isWhite;
    public final Boolean whiteOO;
    public final Boolean whiteOOO;
    public final Boolean blackOO;
    public final Boolean blackOOO;
    public final Position oldPawn;
    public final int numberCourse;

    public Board(char[][] arr, Boolean isWhite, Boolean whiteOO, Boolean whiteOOO, Boolean blackOO, Boolean blackOOO, Position oldPawn, int numberCourse) {
        this.arr = arr;
        this.isWhite = isWhite;
        this.whiteOO = whiteOO;
        this.whiteOOO = whiteOOO;
        this.blackOO = blackOO;
        this.blackOOO = blackOOO;
        this.oldPawn = oldPawn;
        this.numberCourse = numberCourse;
    }

    private Board(char[][] arr) {
        this(arr, true, true, true, true, true, null, 1);
    }


    public char get(int x, int y) {
        return arr[y][x];
    }

    public char get(Position position) {
        return arr[position.y][position.x];
    }

    public Board moved(Move move) {
        char[][] next = copy();

        char fig = get(move.start);

        movedNotControl(next, move);
        if (fig == 'k' && move.end.equals(Position.of(6, 0)) && move.start.equals(Position.of(4, 0))) {
            movedNotControl(next, new Move(Position.of(7, 0), Position.of(5, 0), get(7, 0)));
        }
        if (fig == 'k' && move.end.equals(Position.of(2, 0)) && move.start.equals(Position.of(4, 0))) {
            movedNotControl(next, new Move(Position.of(0, 0), Position.of(3, 0), get(0, 0)));
        }
        if (fig == 'K' && move.end.equals(Position.of(6, 7)) && move.start.equals(Position.of(4, 7))) {
            movedNotControl(next, new Move(Position.of(7, 7), Position.of(5, 7), get(7, 7)));
        }
        if (fig == 'K' && move.end.equals(Position.of(2, 7)) && move.start.equals(Position.of(4, 7))) {
            movedNotControl(next, new Move(Position.of(0, 7), Position.of(3, 7), get(0, 7)));
        }

        if (Character.toLowerCase(fig) == 'p' && move.end.equals(oldPawn)) {
            set(next, Position.of(move.end.x, move.start.y), ' ');
        }
        if (fig == 'P' && move.end.y == 0) {
            set(next, move.end, Character.toUpperCase(move.figure));
        }
        if (fig == 'p' && move.end.y == 7) {
            set(next, move.end, Character.toLowerCase(move.figure));
        }

        Position oldPawn = null;
        int dy = -1;
        if (Character.isLowerCase(fig))
            dy = 1;
        if (Character.toLowerCase(fig) == 'p' && Math.abs(move.start.y - move.end.y) == 2)
            oldPawn = Position.of(move.start.x, move.start.y + dy);
        int ncourse = numberCourse;
        if (!isWhite)
            ncourse += 1;

        return new Board(
                next,
                !isWhite,
                whiteOO && fig != 'K' && !move.end.equals(Position.of(7, 7)) && !move.start.equals(Position.of(7, 7)),
                whiteOOO && fig != 'K' && !move.end.equals(Position.of(0, 7)) && !move.start.equals(Position.of(0, 7)),
                blackOO && fig != 'k' && !move.end.equals(Position.of(7, 0)) && !move.start.equals(Position.of(7, 0)),
                blackOOO && fig != 'k' && !move.end.equals(Position.of(0, 0)) && !move.start.equals(Position.of(0, 0)),
                oldPawn,
                ncourse
        );
    }

    char[][] copy() {
        char[][] narr = Arrays.copyOf(arr, arr.length);
        for (int i = 0; i < narr.length; i++) {
            narr[i] = Arrays.copyOf(arr[i], arr[i].length);
        }
        return narr;
    }

    void movedNotControl(char[][] next, Move move) {
        char fig = get(move.start);
        set(next, move.start, ' ');
        set(next, move.end, fig);
    }

    void set(char[][] next, Position position, char ch) {
        next[position.y][position.x] = ch;
    }

    public void pawnMoves(Position position, ArrayList<Position> res) {
        pawnMoves(position, res, false);
    }
    void pawnMoves(Position position, ArrayList<Position> res, boolean ignoreFigures) {
        if (get(position) == 'p') {
            if (get(position.x, position.y + 1) == ' '|| ignoreFigures)
                res.add(Position.of(position.x, position.y + 1));
            if (position.y == 1 && (get(position.x, position.y + 1) == ' ' && get(position.x, position.y + 2) == ' ' || ignoreFigures))
                res.add(Position.of(position.x, position.y + 2));
            if (position.x > 0 && Character.isUpperCase(get(position.x - 1, position.y + 1)))
                res.add(Position.of(position.x - 1, position.y + 1));
            if (position.x < 7 && Character.isUpperCase(get(position.x + 1, position.y + 1)))
                res.add(Position.of(position.x + 1, position.y + 1));
        } else {
            if (get(position.x, position.y - 1) == ' '|| ignoreFigures)
                res.add(Position.of(position.x, position.y - 1));
            if (position.y == 6 && (get(position.x, position.y - 1) == ' ' && get(position.x, position.y - 2) == ' '|| ignoreFigures))
                res.add(Position.of(position.x, position.y - 2));
            if (position.x > 0 && Character.isLowerCase(get(position.x - 1, position.y - 1)))
                res.add(Position.of(position.x - 1, position.y - 1));
            if (position.x < 7 && Character.isLowerCase(get(position.x + 1, position.y - 1)))
                res.add(Position.of(position.x + 1, position.y - 1));
        }
    }

    void checkTo(int x, int y, int dx, int dy, ArrayList<Position> res) {
        int nx = x;
        int ny = y;
        while (true) {
            nx += dx;
            ny += dy;
            if (nx >= 0 && nx < 8 && ny >= 0 && ny < 8) {
                if (get(nx, ny) == ' ')
                    res.add(Position.of(nx, ny));
                else {
                    if (isMe(get(nx, ny)) != isMe(get(x, y)))
                        res.add(Position.of(nx, ny));
                    return;
                }
            } else return;
        }
    }

    void rookMoves(Position position, ArrayList<Position> res) {
        int x = position.x;
        int y = position.y;

        checkTo(x, y, -1, 0, res);
        checkTo(x, y, 1, 0, res);
        checkTo(x, y, 0, -1, res);
        checkTo(x, y, 0, 1, res);
    }

    void bishopMoves(Position position, ArrayList<Position> res) {
        int x = position.x;
        int y = position.y;

        checkTo(x, y, -1, -1, res);
        checkTo(x, y, -1, 1, res);
        checkTo(x, y, 1, -1, res);
        checkTo(x, y, 1, 1, res);
    }

    void queenMoves(Position position, ArrayList<Position> res) {
        rookMoves(position, res);
        bishopMoves(position, res);
    }


    void knightMoves(Position position, ArrayList<Position> res) {
        int x = position.x;
        int y = position.y;
        ArrayList<Position> list = new ArrayList<>(8);
        list.add(Position.of(x + 1, y + 2));
        list.add(Position.of(x + 2, y + 1));
        list.add(Position.of(x + 2, y - 1));
        list.add(Position.of(x + 1, y - 2));
        list.add(Position.of(x - 1, y - 2));
        list.add(Position.of(x - 2, y - 1));
        list.add(Position.of(x - 2, y + 1));
        list.add(Position.of(x - 1, y + 2));

        checkMoveInPos(position, res, list);
    }

    private boolean checkKnightMoves(Position p) {
        for (Position p1 : enemyFigures()) {
            if (Character.toUpperCase(get(p1)) != 'K') {
                for (Move move : moviesNotFilterFor(p1, false)) {
                    if (move.end == p1)
                        return false;
                }
            }
        }
        return get(p) == ' ';
    }

    void kindMoves(Position position, ArrayList<Position> res) {
        int x = position.x;
        int y = position.y;
        ArrayList<Position> list = new ArrayList<>(8);
        list.add(Position.of(x + 1, y - 1));
        list.add(Position.of(x + 1, y));
        list.add(Position.of(x + 1, y + 1));
        list.add(Position.of(x, y - 1));
        list.add(Position.of(x, y + 1));
        list.add(Position.of(x - 1, y - 1));
        list.add(Position.of(x - 1, y));
        list.add(Position.of(x - 1, y + 1));

        checkMoveInPos(position, res, list);

        if (get(position) == 'K' && position.equals(Position.of(4, 7)) && whiteOO && checkKnightMoves(Position.of(5, 7))
                && checkKnightMoves(Position.of(6, 7)))
            res.add(Position.of(6, 7));
        if (get(position) == 'K' && position.equals(Position.of(4, 7)) && whiteOOO && checkKnightMoves(Position.of(3, 7))
                && checkKnightMoves(Position.of(2, 7)) && checkKnightMoves(Position.of(1, 7)))
            res.add(Position.of(2, 7));
        if (get(position) == 'k' && position.equals(Position.of(4, 0)) && blackOO && checkKnightMoves(Position.of(5, 0))
                && checkKnightMoves(Position.of(6, 0)))
            res.add(Position.of(6, 0));
        if (get(position) == 'k' && position.equals(Position.of(4, 0)) && blackOOO && checkKnightMoves(Position.of(3, 0))
                && checkKnightMoves(Position.of(2, 0)) && checkKnightMoves(Position.of(1, 0)))
            res.add(Position.of(2, 0));

    }

    private void checkMoveInPos(Position position, ArrayList<Position> res, ArrayList<Position> list) {
        for (Position position1 : list) {
            if (
                    position1.x >= 0 && position1.x < 8 && position1.y >= 0 && position1.y < 8 &&
                            (isMe(get(position)) != isMe(get(position1)) || get(position1) == ' ')
            ) res.add(position1);
        }
    }

    public ArrayList<Move> moviesNotFilterFor(Position position, boolean skipEnemy) {
        ArrayList<Position> res = new ArrayList<>();
        char fig = get(position);
        if (fig == ' ' || (isEnemy(fig) && skipEnemy))
            return new ArrayList<>();

        switch (Character.toUpperCase(fig)) {
            case 'K':
                kindMoves(position, res);
                break;
            case 'P':
                pawnMoves(position, res);
                break;
            case 'N':
                knightMoves(position, res);
                break;
            case 'B':
                bishopMoves(position, res);
                break;
            case 'R':
                rookMoves(position, res);
                break;
            case 'Q':
                queenMoves(position, res);
                break;
        }
        ArrayList<Move> res2 = new ArrayList<>(res.size());
        for (Position re : res) {
            res2.add(new Move(position, re));
        }
        return res2;
    }

    public ArrayList<Position> enemyFigures() {
        ArrayList<Position> res = new ArrayList<>();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (get(x, y) != ' ' && isEnemy(get(x, y)))
                    res.add(Position.of(x, y));
            }
        }
        return res;
    }

    public ArrayList<Position> meFigures() {
        ArrayList<Position> res = new ArrayList<Position>();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (get(x, y) != ' ' && isMe(get(x, y)))
                    res.add(Position.of(x, y));
            }
        }
        return res;
    }
    public boolean isMe(char ch){
        return Character.isUpperCase(ch) == isWhite;
    }
    public boolean isEnemy(char ch){
        return Character.isUpperCase(ch) != isWhite;
    }
    public boolean isMe(char ch, boolean isWhite){
        return Character.isUpperCase(ch) == isWhite;
    }
    public boolean isEnemy(char ch, boolean isWhite){
        return Character.isUpperCase(ch) != isWhite;
    }

    public Set<Position> visibleFigures(boolean isWhite){
        ArrayList<Position> meFigures = isWhite == this.isWhite ? meFigures() : enemyFigures();
        ArrayList<Position> enemyFigures = isWhite != this.isWhite ? meFigures() : enemyFigures();
        Set<Position> res = new HashSet<>();
        for(Position pos : meFigures){
            for(Move move : moviesNotFilterFor(pos, false)){
                if (get(move.end) != ' ' && isEnemy(get(move.end), isWhite)){
                    res.add(move.end);
                }
            }
            val list = new ArrayList<Position>();
            if (get(pos) == 'p' || get(pos) == 'P'){
                pawnMoves(pos, list, true);
            }
            for(val pos1 : list){
                if (get(pos1) != ' ')
                    res.add(pos1);
            }
        }
        for(Position pos : enemyFigures){
            for(Move move : moviesNotFilterFor(pos, false)){
                if (get(move.end) != ' ' && isMe(get(move.end), isWhite)){
                    res.add(move.start);
                    break;
                }
            }
        }
        System.out.println(getClass().getSimpleName() + "  --> " + res);
        res.addAll(meFigures);
        return res;
    }

    private Position findPos(char fig) {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (arr[y][x] == fig)
                    return Position.of(x, y);
            }
        }
        return null;
    }

    public boolean notCanEatKind() {
        char kind = 'k';
        if (!isWhite) kind = 'K';
        Position position = findPos(kind);
        for (Move move : allMovies()) {
            if (move.end.equals(position))
                return false;
        }
        return true;
    }

    public boolean isEndGame(){
        return filteredMovies().size() == 0;
    }

    public ArrayList<Move> allMovies() {
        ArrayList<Move> res = new ArrayList<Move>();
        for (Position p : meFigures()) {
            res.addAll(moviesNotFilterFor(p, true));
        }
        return res;
    }

    public ArrayList<Move> filteredMovies() {
        ArrayList<Move> res = new ArrayList<>();
        for (Position p : meFigures()) {
            for (Move move : moviesNotFilterFor(p, true)) {
                if (moved(move).notCanEatKind())
                    res.add(move);
            }
        }
        return res;
    }

    public ArrayList<Move> filteredMoviesFor(Position position) {
        ArrayList<Move> res = new ArrayList<>();

        for (Move move : moviesNotFilterFor(position, true)) {
            if (moved(move).notCanEatKind())
                res.add(move);
        }

        return res;
    }


    public String toPen() {
        StringBuilder sb = new StringBuilder();

        int countSpace = 0;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                char ch = get(x, y);
                if ((ch != ' ' || x == 0) && countSpace > 0) {
                    sb.append(countSpace);
                    countSpace = 0;
                }
                if (x == 0 && y != 0)
                    sb.append('/');


                if (ch != ' ')
                    sb.append(ch);
                else
                    countSpace++;
            }
        }
        if (countSpace > 0)
            sb.append(countSpace);
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
        return moviesNotFilterFor(move.start, true).contains(move) && moved(move).notCanEatKind();
    }


    public static final String START_PEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public static final Board START = fromPen(START_PEN);
    public static final String EMPTY_PEN = "8/8/8/8/8/8/8/8 w KQkq - 0 1";
    public static final Board EMPTY = fromPen(EMPTY_PEN);
    ;

    public static Board fromPen(String pen) {
        String[] ss = pen.split(" ");
        char[][] arr = new char[8][];
        String[] lines = ss[0].split("/");
        for (int i = 0; i < 8; i++) {
            arr[i] = new char[8];
            int j = 0;
            for (int k = 0; k < lines[i].length(); k++) {
                char ch = lines[i].charAt(k);
                if (Character.isDigit(ch)) {
                    int count = Integer.parseInt("" + ch);
                    for (int p = 0; p < count; p++) {
                        arr[i][j++] = ' ';
                    }
                } else {
                    arr[i][j++] = ch;
                }
            }
        }


        if (ss.length < 6) {
            System.out.println("no correct pen");
            return new Board(arr);
        }
        Position old = null;
        if (ss[3].length() == 2)
            old = Position.from(ss[3]);

        return new Board(
                arr,
                ss[1].contains("w"),
                ss[2].contains("K"),
                ss[2].contains("Q"),
                ss[2].contains("k"),
                ss[2].contains("q"),
                old,
                Integer.parseInt(ss[5]));

    }

    private float score() {
        float sum = 0;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                switch (arr[y][x]) {
                    case 'P':
                        sum += 10 + (isWhite ? pawnEvalWhite[y][x] : pawnEvalBlack[y][x]);
                        break;
                    case 'R':
                        sum += 50 + (isWhite ? rookEvalWhite[y][x] : rookEvalBlack[y][x]);
                        break;
                    case 'N':
                        sum += 30 + (knightEval[y][x]);
                        break;
                    case 'B':
                        sum += 30 + (isWhite ? bishopEvalWhite[y][x] : bishopEvalBlack[y][x]);
                        break;
                    case 'Q':
                        sum += 90 + evalQueen[y][x];
                        break;
                    case 'K':
                        sum += 900 + (isWhite ? kingEvalWhite[y][x] : kingEvalBlack[y][x]);
                        break;
                    case 'p':
                        sum -= 10 - (isWhite ? pawnEvalWhite[y][x] : pawnEvalBlack[y][x]);
                        break;
                    case 'r':
                        sum -= 50 - (isWhite ? rookEvalWhite[y][x] : rookEvalBlack[y][x]);
                        break;
                    case 'n':
                        sum -= 30 - (knightEval[y][x]);
                        break;
                    case 'b':
                        sum -= 30 - (isWhite ? bishopEvalWhite[y][x] : bishopEvalBlack[y][x]);
                        break;
                    case 'q':
                        sum -= 90 - evalQueen[y][x];
                        break;
                    case 'k':
                        sum -= 900 - (isWhite ? kingEvalWhite[y][x] : kingEvalBlack[y][x]);
                        break;
                }
            }
        }
        if (isWhite)
            return sum;
        return -sum;
    }

    private Random rand = new Random();

    public Move bestTurn(int depth) {

        ArrayList<Move> list = filteredMovies();
        if (list.isEmpty())
            return null;
        ArrayList<Move> bests = new ArrayList<>();
        float max = Float.MIN_VALUE;
        for (Move move : list) {

            float sc = bestScoreFor(moved(move), depth, false);
            //System.out.println("sc " + sc + " -:" + move);
            if (max < sc || bests.isEmpty()) {
                max = sc;
                bests.clear();
            }
            if (max == sc) {
                bests.add(move);
            }
        }
        return bests.get(rand.nextInt(bests.size()));
    }

    @Data
    @AllArgsConstructor
    static class F {
        Move move;
        float sc;
    }

    private static float bestScoreFor(Board board, int depth, boolean isMax) {
        if (depth <= 0)
            return board.score();

        ArrayList<F> list = new ArrayList<>();


        for (Move move : board.filteredMovies()) {
            float sc = board.moved(move).score();
            list.add(new F(move, sc));
        }
        if (list.isEmpty())
            return 0;

        float best;
        if (isMax) {
            list.sort((o1, o2) -> Float.compare(o2.sc, o1.sc));
            best = list.get(0).sc;
            F s = list.get(list.size() / 2);

            for (F f : list) {
                if (f.sc < s.sc)
                    break;
                float ns = bestScoreFor(board.moved(f.move), depth - 1, !isMax);
                if (ns > best)
                    best = ns;
            }

        } else {
            list.sort((o1, o2) -> Float.compare(o1.sc, o2.sc));
            best = list.get(0).sc;
            F s = list.get(list.size() / 2);
            for (F f : list) {
                if (f.sc > s.sc)
                    break;
                float ns = bestScoreFor(board.moved(f.move), depth - 1, !isMax);
                if (ns < best)
                    best = ns;
            }
        }
        return best;
    }


    private static double[][] pawnEvalWhite = new double[][]{
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0},
            {1.0, 1.0, 2.0, 3.0, 3.0, 2.0, 1.0, 1.0},
            {0.5, 0.5, 1.0, 2.5, 2.5, 1.0, 0.5, 0.5},
            {0.0, 0.0, 0.0, 2.0, 2.0, 0.0, 0.0, 0.0},
            {0.5, -0.5, -1.0, 0.0, 0.0, -1.0, -0.5, 0.5},
            {0.5, 1.0, 1.0, -2.0, -2.0, 1.0, 1.0, 0.5},
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
    };

    private static double[][] pawnEvalBlack = new double[][]{
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {0.5, 1.0, 1.0, -2.0, -2.0, 1.0, 1.0, 0.5},
            {0.5, -0.5, -1.0, 0.0, 0.0, -1.0, -0.5, 0.5},
            {0.0, 0.0, 0.0, 2.0, 2.0, 0.0, 0.0, 0.0},
            {0.5, 0.5, 1.0, 2.5, 2.5, 1.0, 0.5, 0.5},
            {1.0, 1.0, 2.0, 3.0, 3.0, 2.0, 1.0, 1.0},
            {5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0},
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
    };

    private static double[][] knightEval = new double[][]
            {
                    {-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0},
                    {-4.0, -2.0, 0.0, 0.0, 0.0, 0.0, -2.0, -4.0},
                    {-3.0, 0.0, 1.0, 1.5, 1.5, 1.0, 0.0, -3.0},
                    {-3.0, 0.5, 1.5, 2.0, 2.0, 1.5, 0.5, -3.0},
                    {-3.0, 0.0, 1.5, 2.0, 2.0, 1.5, 0.0, -3.0},
                    {-3.0, 0.5, 1.0, 1.5, 1.5, 1.0, 0.5, -3.0},
                    {-4.0, -2.0, 0.0, 0.5, 0.5, 0.0, -2.0, -4.0},
                    {-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0}
            };

   private static double[][] bishopEvalWhite = new double[][]{
            {-2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0},
            {-1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0},
            {-1.0, 0.0, 0.5, 1.0, 1.0, 0.5, 0.0, -1.0},
            {-1.0, 0.5, 0.5, 1.0, 1.0, 0.5, 0.5, -1.0},
            {-1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, -1.0},
            {-1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -1.0},
            {-1.0, 0.5, 0.0, 0.0, 0.0, 0.0, 0.5, -1.0},
            {-2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0}
    };

    private static double[][] bishopEvalBlack = new double[][]{

            {-2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0},
            {-1.0, 0.5, 0.0, 0.0, 0.0, 0.0, 0.5, -1.0},
            {-1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -1.0},
            {-1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, -1.0},
            {-1.0, 0.5, 0.5, 1.0, 1.0, 0.5, 0.5, -1.0},
            {-1.0, 0.0, 0.5, 1.0, 1.0, 0.5, 0.0, -1.0},
            {-1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0},
            {-2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0},
    };

    private static double[][] rookEvalWhite = new double[][]{
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
            {0.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {0.0, 0.0, 0.0, 0.5, 0.5, 0.0, 0.0, 0.0}
    };

    private static double[][] rookEvalBlack = new double[][]{

            {0.0, 0.0, 0.0, 0.5, 0.5, 0.0, 0.0, 0.0},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
            {0.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.5},
            {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
    };

    private static double[][] evalQueen = new double[][]{
            {-2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0},
            {-1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0},
            {-1.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -1.0},
            {-0.5, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -0.5},
            {0.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -0.5},
            {-1.0, 0.5, 0.5, 0.5, 0.5, 0.5, 0.0, -1.0},
            {-1.0, 0.0, 0.5, 0.0, 0.0, 0.0, 0.0, -1.0},
            {-2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0}
    };

    private static double[][] kingEvalWhite = new double[][]{

            {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            {-2.0, -3.0, -3.0, -4.0, -4.0, -3.0, -3.0, -2.0},
            {-1.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -1.0},
            {2.0, 2.0, 0.0, 0.0, 0.0, 0.0, 2.0, 2.0},
            {2.0, 3.0, 1.0, 0.0, 0.0, 1.0, 3.0, 2.0}
    };

    private static double[][] kingEvalBlack = new double[][]{
            {2.0, 3.0, 1.0, 0.0, 0.0, 1.0, 3.0, 2.0},
            {2.0, 2.0, 0.0, 0.0, 0.0, 0.0, 2.0, 2.0},
            {-1.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -1.0},
            {-2.0, -3.0, -3.0, -4.0, -4.0, -3.0, -3.0, -2.0},
            {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
            {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        if (numberCourse != board.numberCourse) return false;
        if (!Arrays.deepEquals(arr, board.arr)) return false;
        if (isWhite != null ? !isWhite.equals(board.isWhite) : board.isWhite != null) return false;
        if (whiteOO != null ? !whiteOO.equals(board.whiteOO) : board.whiteOO != null) return false;
        if (whiteOOO != null ? !whiteOOO.equals(board.whiteOOO) : board.whiteOOO != null) return false;
        if (blackOO != null ? !blackOO.equals(board.blackOO) : board.blackOO != null) return false;
        if (blackOOO != null ? !blackOOO.equals(board.blackOOO) : board.blackOOO != null) return false;
        return oldPawn != null ? oldPawn.equals(board.oldPawn) : board.oldPawn == null;
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(arr);
        result = 31 * result + (isWhite != null ? isWhite.hashCode() : 0);
        result = 31 * result + (whiteOO != null ? whiteOO.hashCode() : 0);
        result = 31 * result + (whiteOOO != null ? whiteOOO.hashCode() : 0);
        result = 31 * result + (blackOO != null ? blackOO.hashCode() : 0);
        result = 31 * result + (blackOOO != null ? blackOOO.hashCode() : 0);
        result = 31 * result + (oldPawn != null ? oldPawn.hashCode() : 0);
        result = 31 * result + numberCourse;
        return result;
    }

}


