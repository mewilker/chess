package websocket.commands;

import model.AuthToken;

public class ObserverCommand extends UserGameCommand{

    public ObserverCommand(int id, AuthToken token) {
        super(CommandType.JOIN_OBSERVER, id, token);
    }

    

}
