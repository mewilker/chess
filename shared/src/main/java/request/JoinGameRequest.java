package request;

import chess.ChessGame;
import chess.ChessGame.TeamColor;

/**join game requests with Authorization header, teamColor, and gameID */
public class JoinGameRequest {
    private AuthorizedRequest authToken;
    private ChessGame.TeamColor playerColor;
    private int gameID;

    public JoinGameRequest(){
        super();
    }

    public JoinGameRequest(String token, int id, String playerColor){
        authToken = new AuthorizedRequest(token);
        gameID = id;
        if (playerColor.equals("BLACK")){
            this.playerColor = TeamColor.BLACK;
        }
        else if (playerColor.equals("WHITE")){
            this.playerColor = TeamColor.WHITE;
        }
    }
    
    public String getAuthToken() {
        return authToken.getAuthToken();
    }

    public void setAuthToken (String token){
        authToken = new AuthorizedRequest(token);
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public int getGameID() {
        return gameID;
    }

    /**
     * 
     * @return true if request is valid
     */
    public boolean valid(){
        if(gameID == 0){
            return false;
        }
        return true;
    }
}
