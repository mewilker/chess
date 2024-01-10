package chess;

/**
 * Represents moving a chess piece on a chessboard
 * 
 * Note: You can add to this interface, but you should not alter the existing
 * methods.
 */
public interface ChessMove {
    /**
     * DO NOT REMOVE
     * @return ChessPosition of starting location
     */
    ChessPosition getStartPosition();

    /**
     * DO NOT REMOVE
     * @return ChessPosition of ending location
     */
    ChessPosition getEndPosition();

    /**
     * DO NOT REMOVE
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     * 
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    ChessPiece.PieceType getPromotionPiece();
}
