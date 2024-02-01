package webSocketMessages.serverMessages;

import chess.ChessBoard;
import chess.ChessGame.TeamColor;

public class LoadBoard extends ServerMessage{
    ChessBoard game;
    TeamColor color;
    boolean check = false;
    boolean checkmate = false;

    public LoadBoard(ChessBoard board) {
        super(ServerMessageType.LOAD_GAME);
        game = board;
    }

    public ChessBoard getBoard (){
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
