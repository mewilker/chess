package chess;

import java.util.Objects;

import chess.ChessPiece.PieceType;

/**implementation of starter code ChessMove 
 * may need to change return type of methods from interfaces
 * 
*/
public class ChessMove {
    ChessPosition startPos;
    ChessPosition endPos;
    ChessPiece.PieceType promotion;


    public ChessMove(){
        super();
    }

    public ChessMove(ChessPosition startpos, ChessPosition endpos, ChessPiece.PieceType type){
        startPos = startpos;
        endPos = endpos;
        promotion = type;
    }
    
    public ChessPosition getStartPosition() {
        return startPos;
    }

    public ChessPosition getEndPosition() {
        return endPos;
    }

    public PieceType getPromotionPiece() {
        //pieces can only be promoted at the ends of the boards
        //pieces can only be promoted if they are pawns
        return promotion;
    }


    @Override
    public int hashCode() {
        int result = startPos.hashCode();
        result = 31 *result + endPos.hashCode();
        if (promotion != null){
            result += promotion.hashCode();
        }
        return result;
    }

    @Override
    /**@return true of startpos, endpos and promotion are the same */
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
