package result;

/**holds either the game id or the error message */
public class CreateGameResult extends Result{
    private int gameID;

    /**no arg constructor */
    public CreateGameResult(){
        super();
    }

    /**
     * creates a new game id
     * @param id
     */
    public CreateGameResult(int id){
        gameID = id;
    }

    public int getGameID() {
        return gameID;
    }
}
