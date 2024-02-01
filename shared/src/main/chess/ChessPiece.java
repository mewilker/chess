package chess;

import java.util.Collection;
import java.util.HashSet;

import chess.ChessGame.TeamColor;

/**Abstract Piece */
public class ChessPiece{
    TeamColor teamColor;
    PieceType pieceType;
    HashSet<ChessMove> moves = new HashSet<>();

    /**
     * creates piece with given color and type
     * 
     * @param color
     * @param type
     */
    public ChessPiece(ChessGame.TeamColor color, ChessPiece.PieceType type){
        teamColor = color;
        pieceType = type;
    }

    /**
     * DO NOT REMOVE
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    public TeamColor getTeamColor() {
        return teamColor;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (pieceType) {
            case BISHOP:
                return new Bishop(teamColor).pieceMoves(board, myPosition);
            case QUEEN:
                return new Queen(teamColor).pieceMoves(board, myPosition);
            case KING:
                return new King(teamColor).pieceMoves(board, myPosition);
            case KNIGHT:
                return new Knight(teamColor).pieceMoves(board, myPosition);
            case ROOK:
                return new Rook(teamColor).pieceMoves(board, myPosition);
            case PAWN:
                return new Pawn(teamColor).pieceMoves(board, myPosition);
        }
        return null;
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
        ChessBoard temp = (ChessBoard)board;
        if (board.getPiece(endpos)!= null){
            color = board.getPiece(endpos).getTeamColor();
            if(color != this.teamColor){
                moves.add(new ChessMove(myPosition, endpos.clone(), null));
                return false;
            }
        }
        else if (temp.onBoard(endpos)){
            moves.add(new ChessMove(myPosition, endpos.clone(), null));
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
        ChessPosition endPos = myPosition.clone();
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
        ChessPosition endPos = myPosition.clone();
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
        ChessPosition endPos = myPosition.clone();
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
        ChessPosition endPos = myPosition.clone();
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
        ChessPosition endPos = myPosition.clone();
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
        ChessPosition endPos = myPosition.clone();
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
        ChessPosition endPos = myPosition.clone();
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
        ChessPosition endPos = myPosition.clone();
        do {
            endPos.down(1);
            endPos.right(1);
        } while (addIfAvail(board, myPosition, endPos));
    }

    
}
