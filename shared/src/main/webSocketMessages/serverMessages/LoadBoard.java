package webSocketMessages.serverMessages;

import chess.Board;
import chess.ChessGame.TeamColor;

public class LoadBoard extends ServerMessage{
    Board game;
    TeamColor color;
    boolean check = false;
    boolean checkmate = false;

    public LoadBoard(Board board) {
        super(ServerMessageType.LOAD_GAME);
        game = board;
    }

    public Board getBoard (){
        return game;
    }

    public void check(){
        check = true;
    }

    public void checkmate(){
        checkmate = true;
    }

    public boolean incheck(){
        return check;
    }

    public boolean inCheckmate(){
        return checkmate;
    }

    public void setTurn(TeamColor color){
        this.color = color;
    }

    public TeamColor getTurn(){
        return color;
    }

}
