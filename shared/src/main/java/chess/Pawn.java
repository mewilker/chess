package chess;

import java.util.Collection;
import chess.ChessGame.TeamColor;

/**Pawn piece<ul>
 * <li>can move 2 on first move
 * <li>after must move 1
 * <li>captures diagonally</ul>
 *
 * <p>En Passent - if the pawn on the first x2 would have been caputured in a 1x move,
 * they are captured as if they made a 1x move
 *
 */
public class Pawn extends ChessPiece{
  boolean hasMoved = false;
  ChessPosition lastPos = null;

  /**
   * constructs a Pawn given color and Piece type
   * (not recommended)
   *
   * @param color
   * @param type
   */
  public Pawn(TeamColor color, PieceType type) {
    super(color, type);
  }

  /**
   * constructs a Pawn given color
   * @param color
   */
  public Pawn(TeamColor color){
    super(color, ChessPiece.PieceType.PAWN);
  }

  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
    moves.clear();
    twoSpace(board, myPosition);
    oneSpace(board, myPosition);
    capture(board, myPosition);
    enPassant(board,myPosition);
    return moves;
  }

  /**
   * adds two space forward move if it's still their first move
   *
   * @param board
   * @param myPosition
   */
  private void twoSpace(ChessBoard board, ChessPosition myPosition){
    if(!hasMoved(board, myPosition)){
      ChessPosition endpos = myPosition.clone();
      if (this.teamColor == TeamColor.WHITE){
        endpos.up(1);
        if (board.getPiece(endpos)== null){//first space empty
          endpos.up(1); // second space empty
          if (board.getPiece(endpos)== null){
            moves.add(new ChessMove(myPosition, endpos, null));
          }
        }
        return;
      }
      else{ // TeamColor.BLACK
        endpos.down(1);
        if (board.getPiece(endpos)== null){//first space empty
          endpos.down(1); // second space empty
          if (board.getPiece(endpos)== null){
            moves.add(new ChessMove(myPosition, endpos, null));
          }
        }
      }

    }
  }

  /**
   * adds a move forward if the space is available
   *
   * @param board
   * @param myPosition
   */
  private void oneSpace(ChessBoard board, ChessPosition myPosition){
    ChessPosition endpos = myPosition.clone();
    if (this.teamColor == TeamColor.WHITE){
      endpos.up(1);
      if (board.getPiece(endpos)== null){//first space empty
        checkPromote(board, myPosition, endpos);
      }
      return;
    }
    else{ // TeamColor.BLACK
      endpos.down(1);
      if (board.getPiece(endpos)== null){//first space empty
        checkPromote(board, myPosition, endpos);
      }
    }
  }

  /**
   * adds diagonal moves if capture is available
   *
   * @param board
   * @param myPosition
   */
  private void capture(ChessBoard board, ChessPosition myPosition){
    ChessPosition endpos = myPosition.clone();
    if (this.teamColor == TeamColor.WHITE){
      endpos.up(1);
      endpos.right(1);
      if (board.getPiece(endpos)!= null){//diagonal not empty
        if(board.getPiece(endpos).getTeamColor()!=this.teamColor){//enemy
          checkPromote(board, myPosition, endpos.clone());
        }
      }
      endpos.left(2);
      if (board.getPiece(endpos)!= null){//diagonal not empty
        if(board.getPiece(endpos).getTeamColor()!=this.teamColor){//enemy
          checkPromote(board, myPosition, endpos.clone());
        }
      }
      return;
    }
    else{ // TeamColor.BLACK
      endpos.down(1);
      endpos.right(1);
      if (board.getPiece(endpos)!= null){//diagonal not empty
        if(board.getPiece(endpos).getTeamColor()!=this.teamColor){//enemy
          checkPromote(board, myPosition, endpos.clone());
        }
      }
      endpos.left(2);
      if (board.getPiece(endpos)!= null){//diagonal not empty
        if(board.getPiece(endpos).getTeamColor()!=this.teamColor){//enemy
          checkPromote(board, myPosition, endpos.clone());
        }
      }
    }
  }

  /**
   * checks if the pawn needs to be promoted and adds the associated moves
   *
   * @param board
   * @param myPosition
   * @param endpos
   */
  private void checkPromote(ChessBoard board, ChessPosition myPosition, ChessPosition endpos){
    if (this.teamColor == TeamColor.WHITE){
      if (endpos.getRow()==8){
        moves.add(new ChessMove(myPosition, endpos, PieceType.BISHOP));
        moves.add(new ChessMove(myPosition, endpos, PieceType.QUEEN));
        moves.add(new ChessMove(myPosition, endpos, PieceType.ROOK));
        moves.add(new ChessMove(myPosition, endpos, PieceType.KNIGHT));
      }
      else{
        moves.add(new ChessMove(myPosition, endpos, null));
      }
      return;
    }
    else{ // TeamColor.BLACK
      if (endpos.getRow()==1){
        moves.add(new ChessMove(myPosition, endpos, PieceType.BISHOP));
        moves.add(new ChessMove(myPosition, endpos, PieceType.QUEEN));
        moves.add(new ChessMove(myPosition, endpos, PieceType.ROOK));
        moves.add(new ChessMove(myPosition, endpos, PieceType.KNIGHT));
      }
      else {
        moves.add(new ChessMove(myPosition, endpos, null));
      }
    }
  }

  /**
   * <p>Opponent double moves pawn</p>
   * <p>It ends next to yours, skipping capture</p>
   * <p>The immediate following turn, you can capture as if they had only moved one</p>*/
  private void enPassant(ChessBoard board, ChessPosition myPos){
    ChessPosition newLeft = myPos.clone();
    ChessPosition newRight = myPos.clone();
    newLeft.left(1);
    newRight.right(1);
    if (checkPass(newLeft, myPos, board)){
      return;
    }
    checkPass(newRight, myPos, board);
  }

  private boolean checkPass(ChessPosition side, ChessPosition myPos, ChessBoard board){
    if(board.onBoard(side)) {
      ChessPiece opponent=board.getPiece(side);
      if (opponent != null && opponent.getTeamColor() != getTeamColor() && opponent.pieceType == PieceType.PAWN) {
        Pawn pawn=(Pawn) opponent;
        if (pawn.getLastPosition()!= null){
        if (pawn.getLastPosition().getRow() == 2 && pawn.getTeamColor() == TeamColor.WHITE
                || pawn.getLastPosition().getRow() == 7 && pawn.getTeamColor() == TeamColor.BLACK) {
          ChessPosition endPos = pawn.getLastPosition().clone();
          if (pawn.getLastPosition().getRow() == 2) {
            endPos.up(1);
          } else {
            endPos.down(1);
          }
          moves.add(new ChessMove(myPos, endPos, null));
          return true;
        }
        }
      }
    }
    return false;
  }

  /**
   *
   * @param board
   * @param myPosition
   * @return true if has Moved
   */
  public boolean hasMoved(ChessBoard board, ChessPosition myPosition){
    if (this.teamColor== TeamColor.WHITE){
      if (myPosition.getRow() !=2){
        hasMoved = true;
      }
    }
    else{
      if (myPosition.getRow() != 7){
        hasMoved = true;
      }
    }
    return hasMoved;
  }

  public ChessPosition getLastPosition(){
    return lastPos;
  }

  @Override
  public String toString() {
    return "p";
  }

  public void move(ChessPosition myPosition){
    hasMoved = true;
    lastPos = myPosition;
  }

  public void updatePosition(ChessPosition myPos){
    lastPos = myPos;
  }
}
