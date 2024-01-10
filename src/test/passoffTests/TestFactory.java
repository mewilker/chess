package passoffTests;

import chess.*;

/**
 * Used for testing your code
 * Add in code using your classes for each method for each FIXME
 */
public class TestFactory {

    //Chess Functions
    //------------------------------------------------------------------------------------------------------------------
    public static ChessBoard getNewBoard(){
        Board board = new Board();
		return board;
    }

    public static ChessGame getNewGame(){
        GameImpl game = new GameImpl();
		return game;
    }

    public static ChessPiece getNewPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type){
        Piece piece;
        switch (type) {
            case KING:
                piece = new King(pieceColor);
                break;
            case QUEEN:
                piece = new Queen(pieceColor);
                break;
            case ROOK:
                piece = new Rook(pieceColor);
                break;
            case BISHOP:
                piece = new Bishop(pieceColor);
                break;
            case KNIGHT:
                piece = new Knight(pieceColor);
                break;
            case PAWN:
                piece = new Pawn(pieceColor);
                break;
            default:
                piece = null;
                break;
        }
		return piece;
    }

    public static ChessPosition getNewPosition(Integer row, Integer col){
        Position pos = new Position(row, col);
		return pos;
    }

    public static ChessMove getNewMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece){
        Move move = new Move(startPosition, endPosition, promotionPiece);

		return move;
    }
    //------------------------------------------------------------------------------------------------------------------


    //Server API's
    //------------------------------------------------------------------------------------------------------------------
    public static String getServerPort(){
        return "8080";
    }
    //------------------------------------------------------------------------------------------------------------------


    //Websocket Tests
    //------------------------------------------------------------------------------------------------------------------
    public static Long getMessageTime(){
        /*
        Changing this will change how long tests will wait for the server to send messages.
        3000 Milliseconds (3 seconds) will be enough for most computers. Feel free to change as you see fit,
        just know increasing it can make tests take longer to run.
        (On the flip side, if you've got a good computer feel free to decrease it)
         */
        return 3000L;
    }
    //------------------------------------------------------------------------------------------------------------------
}
