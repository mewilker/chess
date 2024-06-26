package chess;

import java.util.Collection;
import chess.ChessGame.TeamColor;

/**King piece:<ul>
 * <li>can only be one per team
 * <li>can move one space in any direction
 * <li>determines the win or loss of a game</ul>
 *
 * <p>Castling conditions:<ol>
 * <li> King and Rook's first move
 * <li> No pieces between them
 * <li> King is not in Check
 * <li> both pieces are safe afterwards</ol>
 * The King will move two spaces towards the rook
 */
public class King extends ChessPiece{
  boolean hasMoved = false;

  /**
   * constructs a King given color and Piece type
   * (not recommended)
   *
   * @param color
   * @param type
   */
  public King(TeamColor color, PieceType type) {
    super(color, type);
  }

  /**
   * constructs a King of given color
   *
   * @param color
   */
  public King(TeamColor color){
    super(color, ChessPiece.PieceType.KING);
  }


  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
    moves.clear();
    ChessPosition endpos = myPosition.clone();
    //up
    endpos.up(1);
    addIfAvail(board, myPosition, endpos);
    //upright
    endpos.right(1);
    addIfAvail(board, myPosition, endpos);
    //right
    endpos.down(1);
    addIfAvail(board, myPosition, endpos);
    //downright
    endpos.down(1);
    addIfAvail(board, myPosition, endpos);
    //down
    endpos.left(1);
    addIfAvail(board, myPosition, endpos);
    //downleft
    endpos.left(1);
    addIfAvail(board, myPosition, endpos);
    //left
    endpos.up(1);
    addIfAvail(board, myPosition, endpos);
    //upleft
    endpos.up(1);
    addIfAvail(board, myPosition, endpos);
    return moves;
  }

  /**
   *
   * @param myPosition
   * @return true if this has moved
   */
  public boolean hasMoved(ChessPosition myPosition){
    if (this.teamColor== TeamColor.WHITE){
      if (myPosition.getRow() !=1 || myPosition.getColumn() != 5){
        hasMoved = true;
      }
    }
    else{
      if (myPosition.getRow() != 8 || myPosition.getColumn() != 5){
        hasMoved = true;
      }
    }
    return hasMoved;
  }

  public void setMoveTrue(){
    hasMoved = true;
  }

  @Override
  public String toString() {
    return "K";
  }
}
