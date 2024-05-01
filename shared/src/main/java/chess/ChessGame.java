package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor teamTurn = TeamColor.WHITE;
    ChessBoard playBoard;
    TeamColor winner = null;
    public ChessGame() {
        playBoard = new ChessBoard();
        playBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = (ChessPiece) playBoard.getPiece(startPosition);
        Collection<ChessMove> moves = piece.pieceMoves(playBoard, startPosition.clone());
        HashSet<ChessMove> possmoves = new HashSet<>(moves);//makes a flippin deep clone of the piece moves
        HashSet<ChessMove> validmoves = new HashSet<>();
        for (ChessMove move : possmoves){
            ChessBoard board = playBoard.clone();
            try{
                playBoard.movePiece(move.clone());
                if(!isInCheck(piece.getTeamColor())){
                    validmoves.add(move.clone());
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

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
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

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        if (playBoard.kingPos(teamColor) == null){
            return false;
        }
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

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)){
            return false;
        }
        if (hasNoMovesToMake(teamColor)){
            setwinner(teamColor);
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
      return !isInCheckmate(teamColor) && hasNoMovesToMake(teamColor);
    }

    /** helper method determining if there are moves left on the board*/
    public boolean hasNoMovesToMake(TeamColor teamColor) {
        ChessBoard board = playBoard.clone();
        Collection<ChessPosition> places = board.piecesOnBoard(teamColor);
        for (ChessPosition place : places){
            Collection<ChessMove> moves = validMoves(place.clone());
            if(moves.size()>0){
                return false;
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        playBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
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
