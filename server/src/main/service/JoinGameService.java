package service;

import chess.ChessGame.TeamColor;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.AuthToken;
import model.UserGame;
import request.JoinGameRequest;
import result.Result;

/**Connects users to a game */
public class JoinGameService {
    
    AuthDAO adao = new AuthDAO();
    GameDAO gdao = new GameDAO();
    
    /**
     * Verifies that the specified game exists, and, if a color is specified, 
     * adds the caller as the requested color to the game. If no color is specified the user 
     * is joined as an observer. This request is idempotent.
     * 
     * @param request - auth token, game id and color
     * @return <ul><li>null if success <li>bad request error 
     * <li>unauthorized error <li>already taken error <li>general error </ul>
     */
    public Result joinGame(JoinGameRequest request){
        Result result = new Result();
        try{
            //bad request check
            if (request == null || !request.valid()){
                result.requestError();
                return result;
            }
            
            //auth token check
            AuthToken token = adao.findToken(request.getAuthToken());

            UserGame game = gdao.find(request.getGameID());
            
            //already taken error
            if (request.getPlayerColor() == TeamColor.WHITE){
                if (game.getWhiteUsername()!= null){
                    if(!token.getUserName().equals(game.getWhiteUsername())){
                        result.takenError();
                        return result;
                    }
                }
                game.setWhiteUsername(token.getUserName());
            }
            else if (request.getPlayerColor() == TeamColor.BLACK){
                if (game.getBlackUsername()!= null){
                    if(!token.getUserName().equals(game.getBlackUsername())){
                        result.takenError();
                        return result;
                    }
                }
                game.setBlackUsername(token.getUserName());
            }
            else{
                //observer logic
            }
            gdao.updateGame(game);

        }
        catch(DataAccessException e){
            if (e.getMessage().equals("not found")){
                result.authError();
            }
            //general error
            else {
                result.errorMessage(e.getMessage());
            }
        }

        return result;
    }
}
