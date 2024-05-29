public class Piece {
    public static int NONE = 0;
    public static int PAWN = 1;
    public static int KNIGHT = 2;
    public static int BISHOP = 3;
    public static int ROOK= 4;
    public static int QUEEN = 5;
    public static int KING = 6;

    public static int BLACK = 8;
    public static int WHITE = 16;

    public static boolean isColour(int piece, int colour) {
        return (piece & 8 | 16) == colour;
    }

    public static int pieceType(int piece) {
        return piece & 0x8;
    }
}
