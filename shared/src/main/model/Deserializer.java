package model;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import chess.*;
import chess.ChessGame.TeamColor;
import chess.ChessPiece.PieceType;

public class Deserializer implements JsonDeserializer{

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        // TODO Auto-generated method stub
        switch (typeOfT.getTypeName()) {
                    case "chess.ChessPosition":
                        return context.deserialize(json, Position.class);
                    case "chess.ChessMove":
                        return context.deserialize(json, Move.class);
                    case "chess.ChessBoard":
                        return context.deserialize(json, Board.class);
                    case "chess.ChessPiece":
                        GsonBuilder builder = new GsonBuilder();
                        builder.registerTypeAdapter(ChessPiece.class, pieceAdapter());
                        return builder.create().fromJson(json.getAsJsonObject().toString(), ChessPiece.class);
                    case "chess.ChessGame":
                        return context.deserialize(json, GameImpl.class);
                    case "chess.GameImpl":
                    TeamColor teamColor = null;    
                    JsonObject jsonObject = json.getAsJsonObject();
                        JsonObject playBoard = jsonObject.getAsJsonObject("playBoard");
                        if(jsonObject.has("teamTurn")){
                            String team = jsonObject.get("teamTurn").getAsString();
                            switch (team) {
                                case "BLACK" -> teamColor = TeamColor.BLACK;
                                case "WHITE" -> teamColor = TeamColor.WHITE;
                            }
                        }
                        GameImpl result = new GameImpl();
                        result.setTeamTurn(teamColor);
                        result.setBoard((Board)deserialize(playBoard, Board.class, context));
                        return result;
                    case "chess.Board":
                        ChessPiece[][] board = new Piece[8][8];
                        HashSet<ChessPosition> white = new HashSet<>();
                        HashSet<ChessPosition> black = new HashSet<>();// this not upating
                        ChessPosition whiteKing;
                        ChessPosition blackKing;
                        HashSet<ChessPiece> captured = new HashSet<>();
                        jsonObject = json.getAsJsonObject();
                        builder = new GsonBuilder();
                        builder.registerTypeAdapter(ChessPiece.class, pieceAdapter());
                        board = builder.create().fromJson(jsonObject.get("board"), ChessPiece[][].class);
                        white = (HashSet<ChessPosition>) deserialize(jsonObject.get("white"), HashSet.class, context);
                        black = (HashSet<ChessPosition>) deserialize(jsonObject.get("black"), HashSet.class, context);
                        whiteKing = (ChessPosition) deserialize(jsonObject.get("whiteKing"), ChessPosition.class, context);
                        blackKing = (ChessPosition) deserialize(jsonObject.get("blackKing"), ChessPosition.class, context);
                        captured = (HashSet<ChessPiece>) deserialize(jsonObject.get("captured"), HashSet.class, context);
                        return new Board(board, white, black, whiteKing, blackKing, captured);
                    default:
                        break;
                }
            //entering this makes an infinate loop
            return context.deserialize(json, typeOfT);
    }

    public static TypeAdapter<ChessPiece> pieceAdapter(){
        return new TypeAdapter<ChessPiece>() {

            @Override
            public void write(JsonWriter out, ChessPiece value) throws IOException {
                Gson gson = new Gson();

                switch (value.getPieceType()) {
                    case KING -> gson.getAdapter(King.class).write(out, (King) value);
                    case QUEEN -> gson.getAdapter(Queen.class).write(out, (Queen) value);
                    case ROOK -> gson.getAdapter(Rook.class).write(out, (Rook) value);
                    case KNIGHT ->gson.getAdapter(Knight.class).write(out, (Knight) value);
                    case BISHOP -> gson.getAdapter(Bishop.class).write(out, (Bishop) value);
                    case PAWN -> gson.getAdapter(Pawn.class).write(out, (Pawn) value);
                }
            }

            @Override
            public ChessPiece read(JsonReader in) throws IOException {
                TeamColor teamColor = null;
                PieceType pieceType = null;
                HashSet<ChessMove> moves = null;
                boolean hasMoved = false;

                in.beginObject();

                while(in.hasNext()){
                    String name = in.nextName();
                    switch (name) {
                        case "teamColor":
                            String team = in.nextString();
                            switch (team) {
                                case "BLACK" -> teamColor = TeamColor.BLACK;
                                case "WHITE" -> teamColor = TeamColor.WHITE;
                            }
                            break;
                        case "pieceType":
                            String type = in.nextString();
                            switch (type) {
                                case "KING" -> pieceType = PieceType.KING;
                                case "QUEEN" -> pieceType = PieceType.QUEEN;
                                case "ROOK" -> pieceType = PieceType.ROOK;
                                case "PAWN" -> pieceType = PieceType.PAWN;
                                case "BISHOP" -> pieceType = PieceType.BISHOP;
                                case "KNIGHT" -> pieceType = PieceType.KNIGHT;
                            }
                            break;
                        case "moves":
                            GsonBuilder builder = new GsonBuilder();
                            builder.registerTypeAdapter(ChessMove.class, new Deserializer());
                            builder.registerTypeAdapter(ChessPosition.class, new Deserializer());
                            moves = builder.create().fromJson(in, new TypeToken<HashSet<ChessMove>>(){}.getType());
                            break;
                        case "hasMoved":
                            hasMoved = in.nextBoolean();
                            break;
                    }
                }

                in.endObject();

                if(teamColor == null && pieceType == null && moves ==null){
                    return null;
                }
                ChessPiece result = null;
                switch (pieceType) {
                    case KING:
                        King king = new King(teamColor);
                        if(hasMoved){
                            //TODO castling thing not impled
                        }
                        result = (ChessPiece) king;
                        break;
                    case QUEEN:
                        result = new Queen(teamColor);
                        break;
                    case ROOK:
                        Rook rook = new Rook(teamColor);
                        if(hasMoved){
                            //TODO castling thing not impled
                        }
                        result = (ChessPiece) rook;
                        break;
                    case BISHOP:
                        result = new Bishop(teamColor);
                        break;
                    case KNIGHT:
                        result = new Knight(teamColor);
                        break;
                    case PAWN:
                        Pawn pawn = new Pawn(teamColor);
                        if (hasMoved){
                            pawn.move();
                        }
                        result = (ChessPiece) pawn;
                        break;
                }
                return result;
            }
            
        };
    }

}
