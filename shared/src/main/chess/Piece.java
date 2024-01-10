package chess;

import java.util.HashSet;

import chess.ChessGame.TeamColor;

/**Abstract Piece */
public abstract class Piece implements ChessPiece {
    TeamColor teamColor;
    PieceType pieceType;
    HashSet<ChessMove> moves = new HashSet<>();

    /**
     * creates piece with given color and type
     * 
     * @param color
     * @param type
     */
    public Piece(ChessGame.TeamColor color, ChessPiece.PieceType type){
        teamColor = color;
        pieceType = type;
    }

    @Override
    public TeamColor getTeamColor() {
        return teamColor;
    }

    @Override
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * adds move to moveset if space is open or has an opponant on it
     * 
     * @param board
     * @param myPosition
     * @param endpos
     * @return true if the piece moved into empty spot (not captured)
     */
    protected boolean addIfAvail(ChessBoard board, ChessPosition myPosition, ChessPosition endpos){
        TeamColor color= null;
        Board temp = (Board)board;
        if (board.getPiece(endpos)!= null){
            color = board.getPiece(endpos).getTeamColor();
            if(color != this.teamColor){
                moves.add(new Move(myPosition, endpos.clone(), null));
                return false;
            }
        }
        else if (temp.onBoard(endpos)){
            moves.add(new Move(myPosition, endpos.clone(), null));
            return true;
        }
        return false;
    }

    /**
     * populates moves with all valid move in the up direction
     * 
     * @param board
     * @param myPosition
     */
    protected void upMoves (ChessBoard board, ChessPosition myPosition){
        Position endPos = myPosition.clone();
        do {
            endPos.up(1);
        } while (addIfAvail(board, myPosition, endPos));
        
    }

    /**
     * populates moves with all valid move in the down direction
     * 
     * @param board
     * @param myPosition
     */
    protected void downMoves(ChessBoard board, ChessPosition myPosition){
        Position endPos = myPosition.clone();
        do {
            endPos.down(1);
        } while (addIfAvail(board, myPosition, endPos));
    }

    /**
     * populates moves with all valid move in the left direction
     * 
     * @param board
     * @param myPosition
     */
    protected void leftMoves(ChessBoard board, ChessPosition myPosition){
        Position endPos = myPosition.clone();
        do {
            endPos.left(1);
        } while (addIfAvail(board, myPosition, endPos));
    }

    /**
     * populates moves with all valid move in the right direction
     * 
     * @param board
     * @param myPosition
     */
    protected void rightMoves(ChessBoard board, ChessPosition myPosition){
        Position endPos = myPosition.clone();
        do {
            endPos.right(1);
        } while (addIfAvail(board, myPosition, endPos));
    }

    /**
     * populates moves with all valid move in the upright diagonal direction
     * 
     * @param board
     * @param myPosition
     */
    protected void upRightMoves (ChessBoard board, ChessPosition myPosition){
        Position endPos = myPosition.clone();
        do {
            endPos.up(1);
            endPos.right(1);
        } while (addIfAvail(board, myPosition, endPos));
        
    }

    /**
     * populates moves with all valid move in the downLeft diagonal direction
     * 
     * @param board
     * @param myPosition
     */
    protected void downLeftMoves (ChessBoard board, ChessPosition myPosition){
        Position endPos = myPosition.clone();
        do {
            endPos.down(1);
            endPos.left(1);
        } while (addIfAvail(board, myPosition, endPos));
    }

    /**
     * populates moves with all valid move in the upleft diagonal direction
     * 
     * @param board
     * @param myPosition
     */
    protected void upLeftMoves(ChessBoard board, ChessPosition myPosition){
        Position endPos = myPosition.clone();
        do {
            endPos.up(1);
            endPos.left(1);
        } while (addIfAvail(board, myPosition, endPos));
    }

    /**
     * populates moves with all valid move in the downright diagonal direction
     * 
     * @param board
     * @param myPosition
     */
    protected void downRightMoves(ChessBoard board, ChessPosition myPosition){
        Position endPos = myPosition.clone();
        do {
            endPos.down(1);
            endPos.right(1);
        } while (addIfAvail(board, myPosition, endPos));
    }

    
}
