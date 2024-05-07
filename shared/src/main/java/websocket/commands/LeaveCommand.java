package websocket.commands;

import model.AuthToken;

public class LeaveCommand extends UserGameCommand{

    public LeaveCommand(int id, AuthToken token) {
        super(CommandType.LEAVE, id, token);
    }

}
