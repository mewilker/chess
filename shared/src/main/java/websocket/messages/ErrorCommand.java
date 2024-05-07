package websocket.messages;

import model.AuthToken;

public class ErrorCommand extends ServerMessage{
    String errorMessage;
    public ErrorCommand(int id, AuthToken token) {
        super(ServerMessageType.ERROR);
        errorMessage = "ERROR\n";
    }

    /**
     * formats to "ERROR: "+ msg
     * 
     * @param msg
     */
    public void changemsg(String msg){
        errorMessage = "ERROR: " + msg + "\n";
    }

    public void httpError(int gameID){
        errorMessage = "ERROR: could not find id:" + String.valueOf(gameID) + " or user by provided token\n";
    }

    public void moveError(){
        errorMessage = "ERROR: move is not valid\n";
    }

    public String getmsg(){
        return errorMessage;
    }

}
