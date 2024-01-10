package webSocketMessages.userCommands;

import java.util.Objects;

import model.AuthToken;

/**
 * Represents a command a user can send the server over a websocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    private AuthToken token;
    protected int gameID;
    protected String message;

    public UserGameCommand(String authToken) {
        this.authToken = authToken;
    }

    protected UserGameCommand(CommandType type, int id, AuthToken token){
        this.commandType = type;
        this.gameID = id;
        this. token = token;
        authToken = token.getAuthToken();
    }

    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN,
        DRAW,
        POSSIBLE
    }

    protected CommandType commandType;

    private final String authToken;

    public String getAuthString() {
        return authToken;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }

    public String getMessage(){
        return message;
    }

    public int getid(){
        return gameID;
    }

    public String getUsername(){
        return token.getUserName();
    }

    public String getToken(){
        return authToken;
    }
}
