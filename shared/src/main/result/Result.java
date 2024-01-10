package result;

/**generic error result, with common errors<p>
 * parent of all other results
 */
public class Result {
    
    private String message;

    /**
     * 
     * @return message of the result
     */
    public String getMessage(){
        return message;
    }

    /**
     * 
     * @param msg - will format to "Error: msg"
     */
    public void errorMessage(String msg){
        message = "Error: " + msg;
    }

    /**sets error to bad request */
    public void requestError(){
        errorMessage("bad request");
    }

    /**sets error to unauthorized */
    public void authError(){
        errorMessage("unauthorized");
    }

    /**sets error to already taken */
    public void takenError(){
        errorMessage("already taken");
    }


}
