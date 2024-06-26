package websocket.commands;

import chess.ChessGame.TeamColor;
import model.AuthToken;

public class ConnectCommand extends UserGameCommand{
    private TeamColor playerColor;
    
    public ConnectCommand(AuthToken token, TeamColor team, int id){
        super(CommandType.CONNECT, id, token);
        this.playerColor = team;
    }

    public ConnectCommand(AuthToken token, String team, int id){
        super(CommandType.CONNECT, id, token);
        switch (team) {
            case "WHITE" -> this.playerColor = TeamColor.WHITE;
            case "BLACK" -> this.playerColor = TeamColor.BLACK;
            default -> this.playerColor = null;
        }
    }

    public TeamColor getPlayerColor(){
        return playerColor;
    }
}
