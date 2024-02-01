package chess;

import java.util.Collection;
import java.util.HashSet;

/**implementation of ChessGame starter code */
public class ChessGame {

    TeamColor teamTurn;
    ChessBoard playBoard;
    TeamColor winner = null;

    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * DO NOT REMOVE
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = (ChessPiece) playBoard.getPiece(startPosition);
        Collection<ChessMove> moves = piece.pieceMoves(playBoard, startPosition.clone());
        HashSet<ChessMove> possmoves = new HashSet<>(moves);//makes a flippin deep clone of the piece moves
        HashSet<ChessMove> validmoves = new HashSet<>();
        for (ChessMove move : possmoves){
            ChessMove temp = (ChessMove)move;
            ChessBoard board = playBoard.clone();
            try{
                playBoard.movePiece(temp.clone());
                if(!isInCheck(piece.getTeamColor())){
                    validmoves.add(temp.clone());
                }
            }
            catch(InvalidMoveException e){
                System.out.println("program attempted to move a piece not there");
                //validmoves.remove(move);
            }
            finally{
                setBoard(board);
            }
        }
        return validmoves;
    }

    public void makeMove(ChessMove move) throws InvalidMoveException {
        boolean invalidMove = true;
        ChessPosition start = move.getStartPosition().clone();
        if(playBoard.getPiece(start) != null && playBoard.getPiece(start).getTeamColor()==teamTurn){
            Collection<ChessMove> moves = validMoves(start.clone());
            for (ChessMove moveitr : moves){
                if (moveitr.equals(move)){
                    invalidMove = false;
                    playBoard.movePiece(move);
                    advanceTurn();
                    return;
                }
            }
        }
        if (invalidMove){
            throw new InvalidMoveException();
        }
    }

    public boolean isInCheck(TeamColor teamColor) {
        TeamColor opponent = opponentColor(teamColor);
        Collection<ChessMove> moves = new HashSet<>();
        Collection<ChessPosition> places = playBoard.piecesOnBoard(opponent);
        for (ChessPosition place : places){
            ChessPiece piece = playBoard.getPiece(place);
            moves= piece.pieceMoves(playBoard, place.clone());
            for (ChessMove move : moves){
                if(move.getEndPosition().equals(playBoard.kingPos(teamColor))){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)){
            return false;
        }
        if (isInStalemate(teamColor)){
            setwinner(teamColor);
            return true;
        }
        return false;
    }

    public boolean isInStalemate(TeamColor teamColor) {
        ChessBoard board = (ChessBoard) playBoard.clone();
        Collection<ChessPosition> places = board.piecesOnBoard(teamColor);
        for (ChessPosition place : places){
            Collection<ChessMove> moves = validMoves(place.clone());
            if(moves.size()>0){
                return false;
            }
        }
        return true;
    }

    public void setBoard(ChessBoard board) {
        playBoard = (ChessBoard) board;
        //playBoard.resetBoard();
    }

    public ChessBoard getBoard() {
        return playBoard;
    }

    /**
     * 
     * @param teamColor
     * @return opposing teams color
     */
    private TeamColor opponentColor(TeamColor teamColor){
        TeamColor opponent;
        if (teamColor == TeamColor.WHITE){
            opponent = TeamColor.BLACK;
        }
        else{
            opponent = TeamColor.WHITE;
        }
        return opponent;
    }

    /**changes the turn to the other team */
    private void advanceTurn(){
        if (teamTurn == TeamColor.WHITE){
            teamTurn = TeamColor.BLACK;
            return;
        }
        teamTurn = TeamColor.WHITE;
    }

    public TeamColor winner(){
        return winner;
    }

    public void setwinner(TeamColor color){
        winner = opponentColor(color);
    }
    
}
