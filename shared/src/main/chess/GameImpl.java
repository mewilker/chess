package chess;

import java.util.Collection;
import java.util.HashSet;

/**implementation of ChessGame starter code */
public class GameImpl implements ChessGame {

    TeamColor teamTurn;
    Board playBoard;
    TeamColor winner = null;

    @Override
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    @Override
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Piece piece = (Piece) playBoard.getPiece(startPosition);
        Collection<ChessMove> moves = piece.pieceMoves(playBoard, startPosition.clone());
        HashSet<ChessMove> possmoves = new HashSet<>(moves);//makes a flippin deep clone of the piece moves
        HashSet<ChessMove> validmoves = new HashSet<>();
        for (ChessMove move : possmoves){
            Move temp = (Move)move;
            Board board = playBoard.clone();
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

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        boolean invalidMove = true;
        Position start = move.getStartPosition().clone();
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

    @Override
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

    @Override
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

    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        Board board = (Board) playBoard.clone();
        Collection<ChessPosition> places = board.piecesOnBoard(teamColor);
        for (ChessPosition place : places){
            Collection<ChessMove> moves = validMoves(place.clone());
            if(moves.size()>0){
                return false;
            }
        }
        return true;
    }

    @Override
    public void setBoard(ChessBoard board) {
        playBoard = (Board) board;
        //playBoard.resetBoard();
    }

    @Override
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
