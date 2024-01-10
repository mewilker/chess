package result;

import java.util.Collection;
import model.UserGame;

/**holds the list of games or the error message */
public class ListGamesResult extends Result{
    private Collection<UserGame> games;

    /**no arg constructor */
    public ListGamesResult(){
        super();
    }

    /**
     * creates a GameList result to return later
     * @param list
     */
    public ListGamesResult(Collection<UserGame> list){
        games = list;
    }

    public Collection<UserGame> getGames() {
        return games;
    }
}
