package chess;

import chess.ChessPiece.PieceType;

/**implementation of starter code ChessMove 
 * may need to change return type of methods from interfaces
 * 
*/
public class Move implements ChessMove {
    ChessPosition startPos;
    ChessPosition endPos;
    ChessPiece.PieceType promotion;


    public Move(){
        super();
    }

    public Move(ChessPosition startpos, ChessPosition endpos, ChessPiece.PieceType type){
        startPos = startpos;
        endPos = endpos;
        promotion = type;
    }
    
    @Override
    public ChessPosition getStartPosition() {
        return startPos;
    }

    @Override
    public ChessPosition getEndPosition() {
        return endPos;
    }

    @Override
    public PieceType getPromotionPiece() {
        //pieces can only be promoted at the ends of the boards
        //pieces can only be promoted if they are pawns
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
        Move other = (Move) obj;
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

    public Move clone(){
        return new Move(this.startPos, this.endPos, this.promotion);
    }

    public String toString(){
        StringBuilder out = new StringBuilder("StartPos:");
        out.append(startPos.toString()+'\n');
        out.append("EndPos:");
        out.append(endPos.toString()+'\n');
        return out.toString();
    }
    
}
