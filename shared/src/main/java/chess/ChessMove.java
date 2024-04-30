package chess;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    ChessPosition startPos;
    ChessPosition endPos;
    ChessPiece.PieceType promotion;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        startPos = startPosition;
        endPos = endPosition;
        promotion = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPos;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPos;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotion;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = startPos.getRow()*31+ endPos.getRow();
        result *= startPos.getColumn() + endPos.getColumn();
        return result;
    }

    @Override
    /**
     * @return true if startpos, endpos and promotion are the same */
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (obj == null){
            return false;
        }
        if (getClass() != obj.getClass()){
            return false;
        }
        ChessMove other = (ChessMove) obj;
        if (startPos == null) {
            if (other.startPos != null)
                return false;
        }
        else if (!startPos.equals(other.startPos)){
            return false;
        }
        if (endPos == null) {
            if (other.endPos != null)
                return false;
        }
        else if (!endPos.equals(other.endPos)){
            return false;
        }
        if (promotion != other.promotion)
            return false;
        return true;
    }

    @Override
    public ChessMove clone(){
        return new ChessMove(this.startPos, this.endPos, this.promotion);
    }

    @Override
    public String toString(){
        StringBuilder out = new StringBuilder("StartPos:");
        out.append(startPos.toString()+'\n');
        out.append("EndPos:");
        out.append(endPos.toString()+'\n');
        return out.toString();
    }
}
