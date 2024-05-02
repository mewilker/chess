package chess;

import java.util.Collection;
import chess.ChessGame.TeamColor;

/** Rook piece
 * can move any number sideways only (no diagonals)
 *
 * <p>Castling conditions:<ol>
 * <li> King and Rook's first move
 * <li> No pieces between them
 * <li> King is not in Check
 * <li> both pieces are safe afterwards</ol>
 * The rook will jump the king
 */
public class Rook extends ChessPiece{
  //TODO implement castling
  boolean hasMoved = false;

  /**
   * constructs a Rook given color and Piece type
   * (not recommended)
   *
   * @param color
   * @param type
   */
  public Rook(TeamColor color, PieceType type) {
    super(color, type);
  }

  /**
   * constructs a Rook given color
   * @param color
   */
  public Rook(TeamColor color){
    super(color, ChessPiece.PieceType.ROOK);
  }

  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
    moves.clear();
    upMoves(board, myPosition);
    downMoves(board, myPosition);
    leftMoves(board, myPosition);
    rightMoves(board, myPosition);
    return moves;
  }

  /**
   *
   * @param board
   * @param myPosition
   * @return true if this has moved
   */
  public boolean hasMoved(ChessBoard board, ChessPosition myPosition){
    if (this.teamColor== TeamColor.WHITE){
      if (myPosition.getRow() !=1){
        hasMoved = true;
      }
    }
    else{
      if (myPosition.getRow() != 8){
        hasMoved = true;
      }
    }
    return hasMoved;
  }

  public void move(){
    hasMoved = true;
  }

  @Override
  public String toString() {
    return "R";
  }
}
