import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import websocket.messages.*;
import websocket.commands.*;
import model.AuthToken;
import model.UserGame;
import model.Deserializer;
import serverfacade.*;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import chess.*;
import chess.ChessGame.TeamColor;
import ui.Format;
import static ui.EscapeSequences.*;

import javax.websocket.MessageHandler;
import javax.websocket.OnMessage;


public class ClientMain {
    
    private static PrintStream out = new PrintStream(System.out,true, StandardCharsets.UTF_8);
    private static Format format = new Format(out);
    private static String serverURL = "localhost:8080";
    private static final String HTTP= "http://";
    private static final String WS= "ws://";
    private static final String URI_ERROR_MSG = "Could not connect to URL. Please restart the program and try again.\n";
    

    public static void main(String[] args) throws Exception {
        if (args.length == 1){
            serverURL = args[0];
        }
        String input = "help";
        out.print(ERASE_SCREEN);
        format.title();
        prelogin(input);  
    }

    private static void prelogin(String input) throws Exception {
        while (!input.equals("quit")){
            if (input.equals("login")){
                String name = format.entryField("Username");
                String password = format.entryField("Password");
                out.print(ERASE_SCREEN);
                try{
                    ServerFacade login = new ServerFacade(HTTP + serverURL);
                    AuthToken token = login.login(name, password);
                    if (token != null){
                        postlogin(token);
                    }
                    else{
                        format.errormsg("Your username or password is incorrect.\n");
                    }
                }
                catch (URISyntaxException e){
                    format.errormsg(URI_ERROR_MSG);
                }
                catch (ConnectionException e){
                    format.errormsg(e.getMessage());
                }
                out.print(RESET_TEXT_BOLD_FAINT);
                format.title();
            }
            else if (input.equals("register")){
                String username = format.entryField("Username");
                String email = format.entryField("Email");
                String password = format.entryField("Password");
                out.print(ERASE_SCREEN);
                try{
                    ServerFacade register = new ServerFacade(HTTP +serverURL);
                    AuthToken token = register.register(username, password, email);
                    if (token != null){
                        postlogin(token);
                    }
                    else{
                        format.errormsg("That username is already taken. Please try something else.\n");
                    }
                }
                catch (URISyntaxException e){
                    format.errormsg(URI_ERROR_MSG);
                }
                catch (ConnectionException e){
                    format.errormsg(e.getMessage());
                }
                format.title();
            }
            //help
            else if (input.equals("help")){
                out.print("****OPTIONS****\n");
                out.print("Type \"help\" for options\n");
                out.print("Type \"quit\" to exit\n");
                out.print("Type \"register\" for a account\n");
                out.print("Type \"login\" to an exsisting account\n");
            }
            else{
                out.print("\n" + input + " is not recognized. Type \"help\" for options\n\n");
            }
            input = format.getInput();
        }
       return;
    }

    private static void postlogin(AuthToken token) throws Exception {
        String input = "help";
        while (!(input.equals("logout") || input.equals("quit"))){
            if (input.equals("help")){
                format.postLoginOptions();
            }
            else if (input.equals("pieces")){
                format.toggleLetters();
            }
            else if (input.equals("create")){
                String name = format.entryField("Game Name");
                try{
                    ServerFacade create = new ServerFacade(HTTP +serverURL);
                    int id = create.createGame(name, token.getAuthToken());
                    out.print("Successfully created game " + name + ".\n");
                    out.print("ID number is " + String.valueOf(id)+ "\n");
                }
                catch (URISyntaxException e){
                    format.errormsg(URI_ERROR_MSG);
                }
                catch (ConnectionException e){
                    format.errormsg(e.getMessage());
                }
            }
            else if (input.equals("list")){
                try{
                    ServerFacade list = new ServerFacade(HTTP +serverURL);
                    ArrayList<UserGame> games = (ArrayList<UserGame>) list.list(token.getAuthToken());
                    format.printGamesTable(games);
                }
                catch (URISyntaxException e){
                    format.errormsg(URI_ERROR_MSG);
                }
                catch (ConnectionException e){
                    format.errormsg(e.getMessage());
                }
            }
            else if (input.equals("join")){
                int id =format.idEntryField("Game ID");
                String team = format.entryField("Which side would you like to join? (white/black)");
                team = team.toUpperCase();
                while (!(team.equals("WHITE")||team.equals("BLACK"))){
                    format.errormsg(team + " is not a valid team. Please choose black or white.\n");
                    team = format.getInput();
                }

                try{
                    ServerFacade join = new ServerFacade(HTTP +serverURL);
                    join.join(token.getAuthToken(),id,team);
                    //WEBSOCKETS
                    WSClient websocket = new WSClient(WS +serverURL, messageHandler());
                    ConnectCommand joinSocket = new ConnectCommand(token,team,id);
                    websocket.send(new Gson().toJson(joinSocket));
                    if (team.equals("BLACK")){
                        format.setOrientation(TeamColor.BLACK);
                    }
                    gameplayLoop(websocket, id, token);
                    format.setOrientation(TeamColor.WHITE);
                }
                catch(URISyntaxException e){
                    format.errormsg(URI_ERROR_MSG);
                }
                catch(ConnectionException e){
                    format.errormsg(e.getMessage());
                }

            }
            else if (input.equals("observe")){
                int id = format.idEntryField("Game ID");
                try{
                    WSClient websocket = new WSClient(WS +serverURL, messageHandler());
                    ConnectCommand joinSocket = new ConnectCommand(token, "none", id);
                    websocket.send(new Gson().toJson(joinSocket));
                    gameplayLoop(websocket, id, token);
                }
                catch(URISyntaxException e){
                    format.errormsg(URI_ERROR_MSG);
                }
                catch(ConnectionException e){
                    format.errormsg(e.getMessage());
                }
            }
            else{
                out.print("\n" + input + " is not recognized. Type \"help\" for options\n\n");
            }
            input = format.getInput();
        }
        //logout handler
        if (input.equals("logout")){
            try{
                new ServerFacade(HTTP +serverURL).logout(token.getAuthToken());
            }
            catch (URISyntaxException e){
                format.errormsg(URI_ERROR_MSG);
            }
            catch (ConnectionException e){
                format.errormsg(e.getMessage());
            }
        }
    }

    private static void gameplayLoop(WSClient facade, int id, AuthToken token) throws IOException{
        String input = "help";
        while (!input.equals("leave")){
            if (input.equals("help")){
                out.print("****OPTIONS****\n");
                out.print("Type \"board\" to see the board\n");
                out.print("Type \"leave\" to exit\n");
                out.print("Type \"move\" to move\n");
                out.print("Type \"resign\" to forfeit win\n");
                out.print("Type \"possible\" to see possible moves for a piece\n");
                out.print("Type \"pieces\" to change the pieces to letters, or change back\n");
            }
            else if(input.equals("pieces")){
                format.toggleLetters();
            }
            else if (input.equals("board")){
            format.printGame();
            }
            else if (input.equals("possible")){
                format.possibleMoves();

            }
            else if (input.equals("resign")){
                ResignCommand cmd = new ResignCommand(id, token);
                facade.send(new Gson().toJson(cmd));
            }
            else if (input.equals("move")) {
                ChessMove move = format.move();
                if (move != null) {
                    MoveCommand cmd = new MoveCommand(id, token, move);
                    facade.send(new Gson().toJson(cmd));
                }
            }
            else{
                out.print("\n" + input + " is not recognized. Type \"help\" for options\n\n");
            }
            input = format.getInput();
        }
        LeaveCommand cmd = new LeaveCommand(id, token);
        facade.send(new Gson().toJson(cmd));
    }

    public static MessageHandler.Whole<String> messageHandler(){
        return new MessageHandler.Whole<String>() {
            @Override
            @OnMessage
            public void onMessage(String message) {
                ServerMessage command = new Gson().fromJson(message, ServerMessage.class);

                switch (command.getServerMessageType()) {
                    case LOAD_GAME:
                        LoadBoard load = new GsonBuilder().registerTypeAdapter(ChessPiece.class,
                                new Deserializer()).registerTypeAdapter(ChessPosition.class,
                                new Deserializer()).create().fromJson(message, LoadBoard.class);
                        format.updateBoard(load.getBoard());
                        format.printGame();
                        if (load.incheck()){
                            if (load.inCheckmate()){
                                format.notification("Checkmate! ");
                                if(load.getTurn() == ChessGame.TeamColor.WHITE){
                                    format.notification("Black team won!\n");
                                }
                                else{
                                    format.notification("White team won!\n");
                                }
                                break;
                            }
                            if(load.getTurn() == ChessGame.TeamColor.WHITE) {
                                format.notification("White is in check!\n");
                            }
                            else{
                                format.notification("Black is in check!\n");
                            }
                        }
                        break;
                    case ERROR :
                        ErrorCommand error = new Gson().fromJson(message,ErrorCommand.class);
                        format.errormsg(error.getmsg());
                        break;
                    case NOTIFICATION:
                        NotifyCommand notify = new Gson().fromJson(message, NotifyCommand.class);
                        format.notification(notify.getmsg());
                        break;
                    default:
                        System.out.print(message);
                }
            }
        };
    }
}
