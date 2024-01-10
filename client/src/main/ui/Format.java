package ui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;

import chess.*;
import chess.ChessGame.TeamColor;

import java.io.PrintStream;

import static ui.EscapeSequences.*;

public class Format {
    private PrintStream out;
    boolean toggleLetters = false;
    Board board;
    TeamColor orientation = TeamColor.WHITE;
    
    public Format(PrintStream out) {
        this.out = out;
    }

    public String getInput(){
        out.print(SET_TEXT_COLOR_WHITE);
        String line;
        Scanner s = new Scanner(System.in);
        line = s.nextLine();
        out.print(SET_TEXT_COLOR_BLUE);
        //s.close();
        return line;

    }

    public String entryfield(String prompt){
        out.print(SET_TEXT_BOLD);
        out.print(prompt + ":");
        out.print(RESET_TEXT_BOLD_FAINT);
        String result = getInput();
        out.print(SET_TEXT_COLOR_BLUE);
        return result;
    }

    public void title(){
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_BLINKING);
        out.print(SET_TEXT_COLOR_GREEN);
        out.print(SET_TEXT_BOLD);
        out.print(WHITE_QUEEN);
        out.print(" Welcome to Makenna's Chess Server!");
        out.print(WHITE_QUEEN);
        out.print("\n\n\n");
        out.print(RESET_TEXT_BOLD_FAINT);
        out.print(RESET_TEXT_BLINKING);
        out.print(SET_TEXT_COLOR_BLUE);
    }

    public void toggleLetters(){
        if (toggleLetters){
            toggleLetters = false;
            out.print("Changed to chess pieces.\n");
        }
        else{
            toggleLetters = true;
            out.print("Changed to letters\n");
        }
    }

    public void errormsg(String message){
        out.print(SET_TEXT_BLINKING);
        out.print(SET_TEXT_COLOR_RED);
        out.print(SET_TEXT_BOLD);
        out.print(message);
        out.print(SET_TEXT_COLOR_BLUE);
        out.print(RESET_TEXT_BLINKING);
        out.print(RESET_TEXT_BOLD_FAINT);
    }
    
    public void printGame(){
        if(orientation == TeamColor.BLACK){ 
            printBlackGame(board);
        }
        else{
            printWhiteGame(board);
        }
    }

    public void printBlackGame(Board game){
        String header;
        if (toggleLetters){
            header = " a  b  c  d  e  f  g  h \n";
        }
        else{
            header = EMPTY+"a"+EMPTY+"b"+EMPTY+"c"+EMPTY+"d"+EMPTY+"e"+EMPTY+"f"+EMPTY+"g"+EMPTY+"h\n";
        }
        out.print(header);
        for(int i = 1; i< 9; i++){
            out.print(String.valueOf(i));
            for (int j = 1; j < 9; j++){
                if ((i%2==0 && j%2==0) || (i%2==1 && j%2==1)){
                    out.print(SET_BG_COLOR_DARK_GREEN);
                }
                else{
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                }
                if (toggleLetters){
                    out.print(" ");
                }
                Piece piece = (Piece) game.getPiece(new Position(i, j));
                printPiece(piece);
            }
            out.print(SET_BG_COLOR_BLACK);
            out.print(String.valueOf(i) + "\n");
        }
        out.print(header);
    }

    public void printWhiteGame(Board game){
        String header;
        if (toggleLetters){
            header = " h  g  f  e  d  c  b  a \n";
        }
        else{
            header = EMPTY+"h"+EMPTY+"g"+EMPTY+"f"+EMPTY+"e"+EMPTY+"d"+EMPTY+"c"+EMPTY+"b"+EMPTY+"a\n";
        }
        out.print(header);
        for(int i = 8; i> 0; i--){
            out.print(String.valueOf(i));
            for (int j = 8; j>0; j--){
                if ((i%2==0 && j%2==0) || (i%2==1 && j%2==1)){
                    out.print(SET_BG_COLOR_DARK_GREEN);
                }
                else{
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                }
                if (toggleLetters){
                    out.print(" ");
                }
                Piece piece = (Piece) game.getPiece(new Position(i, j));
                printPiece(piece);
            }
            out.print(SET_BG_COLOR_BLACK);
            out.print(String.valueOf(i) + "\n");
        }
        out.print(header);
    }
    
    public void printBlackGame(Board game, HashSet<Position> moves, Position start){
        String header;
        if (toggleLetters){
            header = " a  b  c  d  e  f  g  h \n";
        }
        else{
            header = EMPTY+"a"+EMPTY+"b"+EMPTY+"c"+EMPTY+"d"+EMPTY+"e"+EMPTY+"f"+EMPTY+"g"+EMPTY+"h\n";
        }
        out.print(header);
        for(int i = 1; i< 9; i++){
            out.print(String.valueOf(i));
            for (int j = 1; j < 9; j++){
                Position pos = new Position(i, j);
                if(moves.contains(pos)){
                    if ((i%2==0 && j%2==0) || (i%2==1 && j%2==1)){
                        out.print(SET_BG_COLOR_DARK_BLUE);
                    }
                    else{
                        out.print(SET_BG_COLOR_CYAN);
                    }
                }
                else if (pos.equals(start)){
                    out.print(SET_BG_COLOR_YELLOW);
                }
                else{
                    if ((i%2==0 && j%2==0) || (i%2==1 && j%2==1)){
                        out.print(SET_BG_COLOR_DARK_GREEN);
                    }
                    else{
                        out.print(SET_BG_COLOR_LIGHT_GREY);
                    }
                }
                if (toggleLetters){
                    out.print(" ");
                }
                Piece piece = (Piece) game.getPiece(new Position(i, j));
                printPiece(piece);
            }
            out.print(SET_BG_COLOR_BLACK);
            out.print(String.valueOf(i) + "\n");
        }
        out.print(header);
    }

    public void printWhiteGame(Board game, HashSet<Position> moves, Position start){
        String header;
        if (toggleLetters){
            header = " h  g  f  e  d  c  b  a \n";
        }
        else{
            header = EMPTY+"h"+EMPTY+"g"+EMPTY+"f"+EMPTY+"e"+EMPTY+"d"+EMPTY+"c"+EMPTY+"b"+EMPTY+"a\n";
        }
        out.print(header);
        for(int i = 8; i> 0; i--){
            out.print(String.valueOf(i));
            for (int j = 8; j>0; j--){
                Position pos = new Position(i, j);
                if(moves.contains(pos)){
                    if ((i%2==0 && j%2==0) || (i%2==1 && j%2==1)){
                        out.print(SET_BG_COLOR_DARK_BLUE);
                    }
                    else{
                        out.print(SET_BG_COLOR_CYAN);
                    }
                }
                else if(pos.equals(start)){
                    out.print(SET_BG_COLOR_YELLOW);
                }
                else{
                    if ((i%2==0 && j%2==0) || (i%2==1 && j%2==1)){
                        out.print(SET_BG_COLOR_DARK_GREEN);
                    }
                    else{
                        out.print(SET_BG_COLOR_LIGHT_GREY);
                    }
                }
                if (toggleLetters){
                    out.print(" ");
                }
                Piece piece = (Piece) game.getPiece(new Position(i, j));
                printPiece(piece);
            }
            out.print(SET_BG_COLOR_BLACK);
            out.print(String.valueOf(i) + "\n");
        }
        out.print(header);
    }
    
    public void printPiece(Piece piece){
        if (piece == null){
            if (toggleLetters){
                out.print(" ");
            }
            else{
                out.print(EMPTY);
            }
        }
        else if (piece.getTeamColor() == TeamColor.BLACK){
            out.print(SET_TEXT_COLOR_BLACK);
            if (toggleLetters){
                out.print(piece.toString());
            }
            else{
                switch (piece.getPieceType()) {
                    case PAWN:
                    out.print(BLACK_PAWN);
                    break;
                    case BISHOP:
                    out.print(BLACK_BISHOP);
                    break;
                    case KNIGHT:
                        out.print(BLACK_KNIGHT);
                        break;
                    case ROOK:
                        out.print(BLACK_ROOK);
                        break;
                    case KING:
                        out.print(BLACK_KING);
                        break;
                    case QUEEN:
                    out.print(BLACK_QUEEN);
                    break;
                    default:
                    break;
                }
            }
        }
        else{
            out.print(SET_TEXT_COLOR_WHITE);
            if (toggleLetters){
                out.print(piece.toString());
            }
            else{
                switch (piece.getPieceType()) {
                    case PAWN:
                        out.print(WHITE_PAWN);
                        break;
                    case BISHOP:
                        out.print(WHITE_BISHOP);
                        break;
                    case KNIGHT:
                    out.print(WHITE_KNIGHT);
                    break;
                    case ROOK:
                    out.print(WHITE_ROOK);
                    break;
                    case KING:
                    out.print(WHITE_KING);
                    break;
                    case QUEEN:
                    out.print(WHITE_QUEEN);
                    break;
                    default:
                    break;
                }
            }
        }
        out.print(" ");
        out.print(SET_TEXT_COLOR_BLUE);
    }
    
    public Move move(){
        String position = entryfield("Start Position (number letter)");
        Position start = parseString(position);
        if (start != null) {
            ChessPiece piece = board.getPiece(start);
            if (piece != null) {
                position = entryfield("End position (number letter)");
                Position end = parseString(position);
                Move move = null;
                if (end != null) {
                    if (piece.getPieceType() == ChessPiece.PieceType.PAWN &&
                            (piece.getTeamColor() == ChessGame.TeamColor.BLACK && end.getRow() == 1
                                    || piece.getTeamColor() == ChessGame.TeamColor.WHITE && end.getRow() == 8)) {
                        do {
                            position = entryfield("Promotion (queen, knight, rook, bishop)");
                            position = position.toUpperCase();
                            switch (position) {
                                case "QUEEN" -> move = new Move(start, end, ChessPiece.PieceType.QUEEN);
                                case "KNIGHT" -> move = new Move(start, end, ChessPiece.PieceType.KNIGHT);
                                case "ROOK" -> move = new Move(start, end, ChessPiece.PieceType.ROOK);
                                case "BISHOP" -> move = new Move(start, end, ChessPiece.PieceType.BISHOP);
                                default -> errormsg(position + "is not a valid promotion.");
                            }
                        } while (move == null);
                    }
                    else {
                        move = new Move(start, end, null);
                    }
                    return move;
                }
                else {
                    errormsg("Not a valid end position!\n");
                }
            }
            else {
                errormsg("There is no piece there!\n");
            }
        }
        else {
            errormsg("Not a valid start position!\n");
        }
        return null;
    }

    public void possibleMoves() {
        String position = entryfield("Piece position (number letter)");
        Position pos = parseString(position);
        if (pos != null) {
            ChessPiece piece = board.getPiece(pos);
            if (piece == null){
                errormsg("No piece at that position");
                printGame();
            }
            else{
                GameImpl game = new GameImpl();
                game.setBoard(new Board(board));
                Collection<ChessMove> moves = game.validMoves(pos);
                HashSet<Position> set = new HashSet<>();
                for (ChessMove move : moves){
                    set.add((Position)move.getEndPosition());
                }
                if(set.isEmpty()){
                    errormsg("No valid moves!\n");
                }
                else{
                    if (orientation == TeamColor.BLACK){
                        printBlackGame(board, set, pos);
                    }
                    else{
                        printWhiteGame(board, set, pos);
                    }
                }
            }
        }
        else{
            errormsg("Not a valid position!");
        }
    }

    public static Position parseString(String input){
        int row = 0;
        int column = 0;
        for (int i =0; i<input.length(); i++){
            String s = input.substring(i, i+1);
            try{
                row = Integer.parseInt(s);
            }
            catch(NumberFormatException e){
                char c = input.charAt(i);
                if (Character.isLetter(c)){
                    c = Character.toLowerCase(c);
                    switch (c) {
                        case 'a' -> column = 1;
                        case 'b' -> column = 2;
                        case 'c' -> column = 3;
                        case 'd' -> column = 4;
                        case 'e' -> column = 5;
                        case 'f' -> column = 6;
                        case 'g' -> column = 7;
                        case 'h' -> column = 8;
                    }
                }
            }
        }
        if (row < 1 || row > 8 && column < 1){
            return null;
        }
        return new Position(row, column);
    }

    public void notification(String msg){
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print(msg);
        out.print(SET_TEXT_COLOR_BLUE);
    }

    public void setOrientation(TeamColor color){
        orientation = color;
    }
    
    public void updateBoard(Board board){
        this.board = board;
    }
}
