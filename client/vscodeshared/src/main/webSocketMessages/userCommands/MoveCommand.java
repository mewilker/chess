package webSocketMessages.userCommands;

import chess.ChessMove;
import model.AuthToken;

public class MoveCommand extends UserGameCommand{

    ChessMove move;

    public MoveCommand(int id, AuthToken token, ChessMove move) {
        super(CommandType.MAKE_MOVE, id, token);
        this.move = move;
    }

    public ChessMove getMove() {
        return move;
    }

}
