package chess;

import java.util.Collection;

import chess.ChessGame.TeamColor;

/**Queen piece
 * can move any number any direction
 */
public class Queen extends ChessPiece{

    /**
     * constructs a Queen given color and Piece type
     * (not recommended)
     * 
     * @param color
     * @param type
     */
    public Queen(TeamColor color, PieceType type) {
        super(color, type);
    }

    /**
     * constructs a Queen given color
     * @param color
     */
    public Queen(TeamColor color){
        super(color, ChessPiece.PieceType.QUEEN);
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        moves.clear();
        upMoves(board, myPosition);
        upRightMoves(board, myPosition);
        rightMoves(board, myPosition);
        downRightMoves(board, myPosition);
        downMoves(board, myPosition);
        downLeftMoves(board, myPosition);
        leftMoves(board, myPosition);
        upLeftMoves(board, myPosition);
        return moves;
    }

    

    @Override
    public String toString() {
        return "Q";
    }
    
}
