package model;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessGame;
import chess.InvalidMoveException;
import chess.ChessGame.TeamColor;

/**runs a chess.game, as well as IDs, names and assigns users and observers */
public class UserGame {
    private int gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private ChessGame game;

    
    /**
     * Creates a new game with an id
     * 
     * @param name - Game name
     */
    public UserGame(String name){
        gameName = name;
        gameID = 0;
        game = new ChessGame();//TODO: make sure the board is set up
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        game.setBoard(board);
    }

    public int getGameID() {
        return gameID;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public void updateGame(ChessGame game){
        this.game = game;
    }

    public void changeID(int ID){
        gameID = ID;
    }

    public void makeMove(ChessMove move, TeamColor color) throws InvalidMoveException{
        if (game.winner() == null){
            if (game.getTeamTurn().equals(color)){
                game.makeMove(move);
            }
            else {
                throw new InvalidMoveException("It's not your turn!\n");
            }
        }
        else {
            throw new InvalidMoveException("The game has already been won!\n");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + gameID;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserGame other = (UserGame) obj;
        if (gameID != other.gameID)
            return false;
        return true;
    }

    public TeamColor resign(TeamColor color) throws InvalidMoveException{
        if (game.winner() == null){
            game.setwinner(color);
            return game.winner();
        }
        else {
            throw new InvalidMoveException ("The game has already been won!");
        }
    }
}
