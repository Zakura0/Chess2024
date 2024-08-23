package Core.Pieces;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import Core.Board;
import Core.Game;
import Core.Move;

/**
 * Die Klasse Piece repräsentiert eine Schachfigur.
 * @author Gruppe 02
 */
public abstract class Piece {

    private int _row;
    private int _col;
    private boolean _color;
    private List<Move> _possibleMoves;

    public Piece(int row, int col, boolean color) {
        this._row = row;
        this._col = col;
        this._color = color;
        this._possibleMoves = new ArrayList<>();
    }

    /**
     * Bewegt die Figur auf das angegebene Feld.
     */
    public void move(int row, int col) {
        Board.board[this._row][this._col] = null;
        Board.board[row][col] = this;
        this._row = row;
        this._col = col;
    }

    public static Piece getPiece(int row, int col) {
        return Board.board[row][col];
    }

    public int getCol() {
        return this._col;
    }

    public int getRow() {
        return this._row;
    }

    public boolean getColor() {
        return this._color;
    }

    public List<Move> getPossibleMoves() {
        return this._possibleMoves;
    }

    public void setPossibleMoves(List<Move> moves) {
        this._possibleMoves = moves;
    }

    /**
     * Prüft, ob der Zug gültig ist.
     * 
     * @param move Der Zug, der überprüft werden soll.
     */
    protected boolean checkMoveValid(Move move) {
        int startRow = move.getCurrRow();
        int startCol = move.getCurrCol();
        Piece destPiece = Board.board[move.getDestRow()][move.getDestCol()];
        this.move(move.getDestRow(), move.getDestCol());
        if (Game.checkForCheck(this.getColor())) {
            this.move(startRow, startCol);
            Board.board[move.getDestRow()][move.getDestCol()] = destPiece;
            return false;
        } else {
            this.move(startRow, startCol);
            Board.board[move.getDestRow()][move.getDestCol()] = destPiece;
            return true;
        }
    }

    /**
     * Berechnet die möglichen Züge der Figur.
     */
    public abstract void calculatePossibleMoves();

    public abstract String getName();

    protected boolean isBlocked(int row, int col) {
        if (Board.board[row][col] != null) {
            return true;
        }
        return false;
    }

    protected boolean isOpponent(int row, int col) {
        if (Board.board[row][col] != null) {
            if (Board.board[row][col]._color != this._color) {
                return true;
            }
        }
        return false;
    }

    protected boolean isFriendlyRook(int row, int col) {
        if (Board.board[row][col] instanceof Rook && !isOpponent(row, col)) {
            return true;
        } else {
            return false;
        }
    }

    public Image getImage() {
        String imagePath = "";
        if (getName() != null) {
            switch (getName()) {
                case "king_w":
                    imagePath = "/IMG/white-king.png";
                    break;
                case "queen_w":
                    imagePath = "/IMG/white-queen.png";
                    break;
                case "rook_w":
                    imagePath = "/IMG/white-rook.png";
                    break;
                case "bishop_w":
                    imagePath = "/IMG/white-bishop.png";
                    break;
                case "knight_w":
                    imagePath = "/IMG/white-knight.png";
                    break;
                case "pawn_w":
                    imagePath = "/IMG/white-pawn.png";
                    break;
                case "king_b":
                    imagePath = "/IMG/black-king.png";
                    break;
                case "queen_b":
                    imagePath = "/IMG/black-queen.png";
                    break;
                case "rook_b":
                    imagePath = "/IMG/black-rook.png";
                    break;
                case "bishop_b":
                    imagePath = "/IMG/black-bishop.png";
                    break;
                case "knight_b":
                    imagePath = "/IMG/black-knight.png";
                    break;
                case "pawn_b":
                    imagePath = "/IMG/black-pawn.png";
                    break;
            }
        }
        InputStream imageStream = getClass().getResourceAsStream(imagePath);
        if (imageStream != null) {
            try {
                return new ImageIcon(ImageIO.read(imageStream)).getImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public abstract String getAlgebraicNotation();
}
