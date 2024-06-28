public class Board {
    
    public static Piece[][] board;

    public static void initializeBoard() {
        board = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(1, i, false, false);
            Game.blackPieces.add(board[1][i]);
            board[6][i] = new Pawn(6, i, true, false);
            Game.whitePieces.add(board[6][i]);
        }
        board[0][0] = new Rook(0, 0, false, false);
        Game.blackPieces.add(board[0][0]);
        board[0][1] = new Knight(0, 1, false, false);
        Game.blackPieces.add(board[0][1]);
        board[0][2] = new Bishop(0, 2, false, false);
        Game.blackPieces.add(board[0][2]);
        board[0][3] = new Queen(0, 3, false, false);
        Game.blackPieces.add(board[0][3]);
        board[0][4] = new King(0, 4, false, false);
        Game.blackPieces.addFirst(board[0][4]);
        board[0][5] = new Bishop(0, 5, false, false);
        Game.blackPieces.add(board[0][5]);
        board[0][6] = new Knight(0, 6, false, false);
        Game.blackPieces.add(board[0][6]);
        board[0][7] = new Rook(0, 7, false, false);
        Game.blackPieces.add(board[0][7]);
        board[7][0] = new Rook(7, 0, true, false);
        Game.whitePieces.add(board[7][0]);
        board[7][1] = new Knight(7, 1, true, false);
        Game.whitePieces.add(board[7][1]);
        board[7][2] = new Bishop(7, 2, true, false);
        Game.whitePieces.add(board[7][2]);
        board[7][3] = new Queen(7, 3, true, false);
        Game.whitePieces.add(board[7][3]);
        board[7][4] = new King(7, 4, true, false);
        Game.whitePieces.addFirst(board[7][4]);
        board[7][5] = new Bishop(7, 5, true, false);
        Game.whitePieces.add(board[7][5]);                
        board[7][6] = new Knight(7, 6, true, false);
        Game.whitePieces.add(board[7][6]);
        board[7][7] = new Rook(7, 7, true, false);
        Game.whitePieces.add(board[7][7]);
    }

    

    


}
