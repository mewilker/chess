package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;

import com.google.gson.Gson;

import chess.ChessBoard;
import chess.ChessGame;
import chess.InvalidMoveException;
import chess.ChessGame.TeamColor;
import websocket.messages.*;
import websocket.commands.*;
import websocket.commands.UserGameCommand.CommandType;
import dataaccess.*;
import model.UserGame;
import model.AuthToken;

@WebSocket
public class WebSocketServer {
    private static GameDAO gdao;
    private static AuthDAO adao;
    private Map<AuthToken, Session> individuals = new HashMap<>();
    private Map<Integer,List<AuthToken>> runningGames = new HashMap<>();

    public WebSocketServer() throws DataAccessException{
        gdao = new GameDAO();
        adao = new AuthDAO();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException{
        //System.out.print(message);
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        //potential authtoken checking
        switch (command.getCommandType()) {
            case CONNECT-> joinPlayer(new Gson().fromJson(message, ConnectCommand.class), session);
            //case LEAVE -> leave(new Gson().fromJson(message, LeaveCommand.class), session);
            case MAKE_MOVE-> move(new Gson().fromJson(message, MoveCommand.class), session);
            case RESIGN -> resign(new Gson().fromJson(message, ResignCommand.class), session);
            default -> System.out.print(message);
        }
    }

    private void resign(ResignCommand command, Session session) throws IOException {
        AuthToken token = null;
        int id = command.getid();
        UserGame game = null;
        TeamColor winner = null;
        try{
            game = gdao.find(id);
            token = validate(id, game, command);
            if (game.getBlackUsername() != null && token.getUserName().equals(game.getBlackUsername())){
                winner = game.resign(TeamColor.BLACK);
            }
            else if (game.getWhiteUsername() != null && token.getUserName().equals(game.getWhiteUsername())){
                winner = game.resign(TeamColor.WHITE);
            }
            else{
                ErrorCommand error = new ErrorCommand(id, token);
                error.changemsg("Observers can't resign! Try leave instead");
                session.getRemote().sendString(new Gson().toJson(error));
                return;
            }

            gdao.updateGame(game);
        }
        catch (DataAccessException e){
            authError(session, e, id, token);
            return;
        }
        catch(InvalidMoveException e){
            moveError(session, e, id, token);
            return;
        }

        List<AuthToken> list = runningGames.get(id);
        NotifyCommand notify = new NotifyCommand(id, token);
        if (winner.equals(TeamColor.BLACK)){
            notify.winner(game.getBlackUsername());    
        }
        else{
            notify.winner(game.getWhiteUsername());
        }
        notifyOthers(list, token, notify);
        notify.setmsg("You have lost! Good game!");
        session.getRemote().sendString(new Gson().toJson(notify));
    }

    private static void authError(Session session, DataAccessException e, int id, AuthToken token) throws IOException {
        ErrorCommand error = new ErrorCommand(id, token);
        if (e.getMessage().equals("not found")){
            error.httpError(id);
        }
        else{
            error.changemsg(e.getMessage());
        }
        session.getRemote().sendString(new Gson().toJson(error));
    }

    private static void moveError(Session session, InvalidMoveException e, int id, AuthToken token) throws IOException {
        ErrorCommand error = new ErrorCommand(id, token);
        error.moveError();
        if (e.getMessage() != null){
            error.changemsg(e.getMessage());
        }
        session.getRemote().sendString(new Gson().toJson(error));
    }


    private void move(MoveCommand command, Session session) throws IOException{
        AuthToken token = null;
        int id = command.getid();
        UserGame game = null;
        boolean check = false;
        boolean checkMate = false;
        ChessGame g;
        String opponent;
        try{
            game = gdao.find(id);
            token = validate(id, game, command);

            if (game.getBlackUsername() != null && token.getUserName().equals(game.getBlackUsername())){
                game.makeMove(command.getMove(), TeamColor.BLACK);
                g = game.getGame();
                check = g.isInCheck(TeamColor.WHITE);
                checkMate = g.isInCheckmate(TeamColor.WHITE);
                opponent = game.getWhiteUsername();
                if (opponent == null){
                    opponent = "White";
                }
            }
            else if (game.getWhiteUsername() != null && token.getUserName().equals(game.getWhiteUsername())){
                game.makeMove(command.getMove(), TeamColor.WHITE);
                g =game.getGame();
                check = g.isInCheck(TeamColor.BLACK);
                checkMate = g.isInCheckmate(TeamColor.BLACK);
                opponent = game.getBlackUsername();
                opponent = "Black";
            }
            else{
                ErrorCommand error = new ErrorCommand(id, token);
                error.changemsg("Observers can't move pieces!");
                session.getRemote().sendString(new Gson().toJson(error));
                return;
            }

            gdao.updateGame(game);

        }
        catch (DataAccessException e){
            authError(session, e, id, token);
            return;
        }
        catch(InvalidMoveException e){
            moveError(session, e, id, token);
            return;
        }


        List<AuthToken> list = runningGames.get(id);
        NotifyCommand notify = new NotifyCommand(id, token);
        notify.move(token.getUserName());
        LoadBoard load = new LoadBoard((ChessBoard) game.getGame().getBoard());
        if (check){
            load.check();
            notify.check(token.getUserName(), opponent);
            if (checkMate){
                load.checkmate();
                notify.checkMate(token.getUserName());
            }
        }
        notifyOthers(list, token, notify);
        load.setTurn(g.getTeamTurn());
        notifyAll(list, load);
        
    }


    private void leave(LeaveCommand command, Session session) throws IOException{
        AuthToken token = null;
        int id = command.getid();
        UserGame game = null;
        try{
            game = gdao.find(id);
            token = validate(id, game, command);
        }
        catch (DataAccessException e) {
            authError(session, e, id, token);
            return;
        }

        List<AuthToken> list = runningGames.get(id);

        if (list==null){
            //do nothing -- this probably shouldn't be an issue.
        }
        else{
            //notify other players or observers
            list.remove(token);
            runningGames.put(id, list);
            NotifyCommand notification = new NotifyCommand(id, token);
            notification.leave(token.getUserName());
            notifyOthers(list, token, notification);
        }
        individuals.remove(token);
        
    }


    private void joinPlayer(ConnectCommand command, Session session) throws IOException{
        //save session to game id if valid
        AuthToken token = null;
        int id = command.getid();
        UserGame game = null;
        ChessGame chess = null;
        try{
            game = gdao.find(id);
            token = validate(id, game, command);
            chess = game.getGame();
            //if no team turn set, set teamturn to white
            if (chess.getTeamTurn()==null){
                chess.setTeamTurn(TeamColor.WHITE);
                game.updateGame(chess);
            }
            gdao.updateGame(game);
        }
        catch (DataAccessException e) {
            authError(session, e, id, token);
            return;
        }
        individuals.put(token, session);
        List<AuthToken> list = runningGames.get(id);

        if (list==null){
            list = new ArrayList<>();
            list.add(token);
            runningGames.put(id, list); 
        }
        else{
            //notify other players or observers
            list.add(token);
            runningGames.put(id, list);
            NotifyCommand notification = new NotifyCommand(id, token);
            if (command.getPlayerColor() == null){
                notification.observe(token.getUserName());
            } else {
                notification.join(token.getUserName(), command.getPlayerColor());
            }
            notifyOthers(list, token, notification);
        }
        //send board
        ChessGame g = game.getGame();
        boolean check = g.isInCheck(g.getTeamTurn());
        boolean checkmate = g.isInCheckmate(g.getTeamTurn());
        LoadBoard load = new LoadBoard((ChessBoard) chess.getBoard());
        if (check){load.check();}
        if (checkmate){load.checkmate();}
        load.setTurn(g.getTeamTurn());
        session.getRemote().sendString(new Gson().toJson(load)); 
    }

    private AuthToken validate(int id, UserGame game, UserGameCommand command) throws DataAccessException{
        AuthToken token;
        token = adao.findToken(command.getToken());
        if (command.getCommandType().equals(CommandType.CONNECT)){
            ConnectCommand join = (ConnectCommand) command;
            if (join.getPlayerColor() != null) {
                switch (join.getPlayerColor()) {
                    case TeamColor.WHITE:
                        if (game.getWhiteUsername() == null || !game.getWhiteUsername().equals(token.getUserName())) {
                            throw new DataAccessException("Something went wrong joining the game");
                        }
                        break;
                    case TeamColor.BLACK:
                        if (game.getBlackUsername() == null || !game.getBlackUsername().equals(token.getUserName())) {
                            throw new DataAccessException("Something went wrong joining the game");
                        }
                        break;
                }
            }
        }
        else if (command.getCommandType().equals(CommandType.LEAVE)){
            if (game.getWhiteUsername() != null && game.getWhiteUsername().equals(token.getUserName())){
                game.setWhiteUsername(null);
                gdao.updateGame(game);
            }
            else if (game.getBlackUsername() != null && game.getBlackUsername().equals(token.getUserName())){
                game.setBlackUsername(null);
                gdao.updateGame(game);
            }
        }
        return token;
    }

    private void notifyAll(List<AuthToken> list, LoadBoard load) throws IOException{
        for (AuthToken t : list){
            Session s = individuals.get(t);
            s.getRemote().sendString(new Gson().toJson(load));
        }
    }

    private void notifyOthers(List<AuthToken> list, AuthToken token, NotifyCommand notification) throws IOException{
        for (AuthToken t : list){
            if (!t.equals(token)){
                Session s = individuals.get(t);
                s.getRemote().sendString(new Gson().toJson(notification));
            }
        }
    }

}


