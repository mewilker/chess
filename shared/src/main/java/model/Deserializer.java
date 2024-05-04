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
        switch (typeOfT.getTypeName()) {
                    case "chess.ChessPiece":
                        GsonBuilder builder = new GsonBuilder();
                        builder.registerTypeAdapter(ChessPiece.class, pieceAdapter());
                        return builder.create().fromJson(json.getAsJsonObject().toString(), ChessPiece.class);
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
                ChessPosition lasPos = null;

                in.beginObject();

                while(in.hasNext()){
                    String name = in.nextName();
                    switch (name) {
                        case "teamColor" -> {
                            String team = in.nextString();
                            switch (team) {
                                case "BLACK" -> teamColor = TeamColor.BLACK;
                                case "WHITE" -> teamColor = TeamColor.WHITE;
                            }
                        }
                        case "pieceType" -> {

                            String type = in.nextString();
                            switch (type) {
                                case "KING" -> pieceType = PieceType.KING;
                                case "QUEEN" -> pieceType = PieceType.QUEEN;
                                case "ROOK" -> pieceType = PieceType.ROOK;
                                case "PAWN" -> pieceType = PieceType.PAWN;
                                case "BISHOP" -> pieceType = PieceType.BISHOP;
                                case "KNIGHT" -> pieceType = PieceType.KNIGHT;
                            }
                        }
                        case "moves" -> moves = new Gson().fromJson(in, new TypeToken<HashSet<ChessMove>>(){}.getType());
                        case "hasMoved" -> hasMoved = in.nextBoolean();
                        case "lastPos" -> lasPos = new Gson().fromJson(in, ChessPosition.class);
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
                            king.setMoveTrue();
                        }
                        result = king;
                        break;
                    case QUEEN:
                        result = new Queen(teamColor);
                        break;
                    case ROOK:
                        Rook rook = new Rook(teamColor);
                        if(hasMoved){
                            rook.move();
                        }
                        result = rook;
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
                            pawn.move(lasPos);
                        }
                        result = pawn;
                        break;
                }
                return result;
            }
            
        };
    }

}
