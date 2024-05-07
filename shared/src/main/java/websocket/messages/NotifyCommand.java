package websocket.messages;

import chess.ChessGame.TeamColor;
import model.AuthToken;

public class NotifyCommand extends ServerMessage{

    String message;

    public NotifyCommand(int id, AuthToken token) {
        super(ServerMessageType.NOTIFICATION);
    }

    public void join(String username, TeamColor color){
        String team = "";
        switch (color) {
            case WHITE-> team = "white";
            case BLACK-> team = "black";
        }
        message = username + " joined as the " + team + " team!\n";
    }

    public void observe(String username){
        message = username + " is now watching!\n";
    }

    public void leave(String username){
        message = username + " has left!\n";
    }

    public void move (String username){
        message = username + " has made a move!\n";
    }

    public void check (String mover, String username){
        message = mover + " has made a move! " + username + " is in check!\n";
    }

    public void checkMate (String username){
        message = "Checkmate! " + username + " has won!\n";
    }

    public void winner(String username){
        message = username + " has won!\n";
    }

    public void setmsg(String msg){
        message = msg;
    }

    public String getmsg(){
        return message;
    }

}
