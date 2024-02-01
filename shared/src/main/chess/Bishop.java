package chess;

import java.util.Collection;

import chess.ChessGame.TeamColor;

/**Bishop Piece
 * can move any number diagonally (not sideways)
 */
public class Bishop extends ChessPiece{

    /**
     * constructs a Bishop given color and Piece type
     * (not recommended)
     * 
     * @param color
     * @param type
     */
    public Bishop(TeamColor color, PieceType type) {
        super(color, type);
    }

    /**
     * constructs a Bishop of given color
     * 
     * @param color
     */
    public Bishop(TeamColor color){
        super(color, ChessPiece.PieceType.BISHOP);
    }

    
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        moves.clear();
        upLeftMoves(board, myPosition);
        upRightMoves(board, myPosition);
        downLeftMoves(board, myPosition);
        downRightMoves(board, myPosition);
        return moves;
    }


    @Override
    public String toString() {
        return "B";
    }

    
}
