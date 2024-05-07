package webSocketMessages.userCommands;

import model.AuthToken;

public class ResignCommand extends UserGameCommand{

    public ResignCommand(int id, AuthToken token) {
        super(CommandType.RESIGN, id, token);
    }

}
