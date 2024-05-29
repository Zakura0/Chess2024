public class FEN {
    public static String currentFEN(Board board, boolean enPassant) {
        StringBuilder fen = new StringBuilder();
        
        // Board squares
        for (int rank = 7; rank >= 0; rank--) {
            int numEmptyFiles = 0;
            for (int file = 0; file < 8; file++) {
                int i = rank * 8 + file;
                int piece = board.square[i];
                if (piece != 0) {
                    if (numEmptyFiles != 0) {
                        fen.append(numEmptyFiles);
                        numEmptyFiles = 0;
                    }
                    boolean isBlack = Piece.isColour(piece, Piece.BLACK);
                    int pieceType = Piece.pieceType(piece);
                    char pieceChar = ' ';
                    switch (pieceType) {
                        case Piece.ROOK:
                            pieceChar = 'R';
                            break;
                        case Piece.KNIGHT:
                            pieceChar = 'N';
                            break;
                        case Piece.BISHOP:
                            pieceChar = 'B';
                            break;
                        case Piece.QUEEN:
                            pieceChar = 'Q';
                            break;
                        case Piece.KING:
                            pieceChar = 'K';
                            break;
                        case Piece.PAWN:
                            pieceChar = 'P';
                            break;
                    }
                    fen.append(isBlack ? Character.toLowerCase(pieceChar) : pieceChar);
                } else {
                    numEmptyFiles++;
                }
            }
            if (numEmptyFiles != 0) {
                fen.append(numEmptyFiles);
            }
            if (rank != 0) {
                fen.append('/');
            }
        }

        fen.append(' ');
        fen.append(board.isWhiteToMove ? 'w' : 'b');

        fen.append(' ');
        fen.append((board.plyCount / 2) + 1);

        return fen.toString();
    }
}