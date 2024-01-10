package webSocketMessages.userCommands;

import model.AuthToken;

public class DrawCommand extends UserGameCommand{
    public DrawCommand(int id, AuthToken token) {
        super(CommandType.DRAW, id, token);
    }
}
