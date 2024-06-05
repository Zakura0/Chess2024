import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    // Piece Types
    public final int NONE = 0;
    public final int PAWN = 1;
    public final int KNIGHT = 2;
    public final int BISHOP = 3;
    public final int ROOK = 4;
    public final int QUEEN = 5;
    public final int KING = 6;

    // Pieurs
    public static final int WHITE = 0;
    public final int BLACK = 8;

    // Pieces
    public final int WHITE_PAWN = PAWN | WHITE; // 1
    public final int WHITE_KNIGHT = KNIGHT | WHITE; // 2
    public final int WHITE_BISHOP = BISHOP | WHITE; // 3
    public final int WHITE_ROOK = ROOK | WHITE; // 4
    public final int WHITE_QUEEN = QUEEN | WHITE; // 5
    public final int WHITE_KING = KING | WHITE; // 6

    public final int BLACK_PAWN = PAWN | BLACK; // 9
    public final int BLACK_KNIGHT = KNIGHT | BLACK; // 10
    public final int BLACK_BISHOP = BISHOP | BLACK; // 11
    public final int BLACK_ROOK = ROOK | BLACK; // 12
    public final int BLACK_QUEEN = QUEEN | BLACK; // 13
    public final int BLACK_KING = KING | BLACK; // 14

    public int _row;
    public int _col;
    public boolean _color;
    public List<Move> _possibleMoves;

    // Bit Masks
    private static final int TYPE_MASK = 0b0111;
    private static final int COLOUR_MASK = 0b1000;

    public Piece(int row, int col, boolean color){ 
        this._row = row;
        this._col = col;
        this._color = color;
        this._possibleMoves = new ArrayList<>();
    }

    // Returns true if given piece matches the given colour. If piece is of type 'none', result will always be false.
    public static boolean isColour(int piece, int colour) {
        return (piece & COLOUR_MASK) == colour && piece != 0;
    }

    public static boolean isWhite(int piece) {
        return isColour(piece, WHITE);
    }

    public static int pieceColour(int piece) {
        return piece & COLOUR_MASK;
    }

    public static int pieceType(int piece) {
        return piece & TYPE_MASK;
    }

    public  char getSymbol(int piece) {
        int pieceType = pieceType(piece);
        char symbol = switch (pieceType) {
            case ROOK -> 'R';
            case KNIGHT -> 'N';
            case BISHOP -> 'B';
            case QUEEN -> 'Q';
            case KING -> 'K';
            case PAWN -> 'P';
            default -> ' ';
        };
        return isWhite(piece) ? symbol : Character.toLowerCase(symbol);
    }

    public  int getPieceTypeFromSymbol(char symbol) {
        symbol = Character.toUpperCase(symbol);
        return switch (symbol) {
            case 'R' -> ROOK;
            case 'N' -> KNIGHT;
            case 'B' -> BISHOP;
            case 'Q' -> QUEEN;
            case 'K' -> KING;
            case 'P' -> PAWN;
            default -> NONE;
        };
    }
}

