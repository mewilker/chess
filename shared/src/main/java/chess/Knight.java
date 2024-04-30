package chess;

import java.util.Collection;
import chess.ChessGame.TeamColor;

/**Knight piece<ul>
 * <li>can jump pieces
 * <li>can move in a 1x2 L shape</ul>
 */
public class Knight extends ChessPiece{
  /**
   * constructs a Knight given color and Piece type
   * (not recommended)
   *
   * @param color
   * @param type
   */
  public Knight(TeamColor color, PieceType type) {
    super(color, type);
  }

  /**
   * constructs a Knight of given color
   * @param color
   */
  public Knight(TeamColor color){
    super(color, ChessPiece.PieceType.KNIGHT);
  }

  @Override
  public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
    moves.clear();
    ChessPosition endpos = myPosition.clone();
    endpos.up(2);
    endpos.right(1);
    addIfAvail(board, myPosition, endpos);
    endpos.right(1);
    endpos.down(1);
    addIfAvail(board, myPosition, endpos);
    endpos.down(2);
    addIfAvail(board, myPosition, endpos);
    endpos.down(1);
    endpos.left(1);
    addIfAvail(board, myPosition, endpos);
    endpos.left(2);
    addIfAvail(board, myPosition, endpos);
    endpos.up(1);
    endpos.left(1);
    addIfAvail(board, myPosition, endpos);
    endpos.up(2);
    addIfAvail(board, myPosition, endpos);
    endpos.up(1);
    endpos.right(1);
    addIfAvail(board, myPosition, endpos);
    return moves;
  }

  @Override
  public String toString() {
    return "N";
  }
}
