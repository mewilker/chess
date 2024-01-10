package chess;

import java.util.Collection;
import java.util.HashSet;

import chess.ChessGame.TeamColor;
import chess.ChessPiece.PieceType;

/**implements starter code Chess Board */
public class Board implements ChessBoard {

    //a board has 8*8 positions
    ChessPiece[][] board = new Piece[8][8];
    HashSet<ChessPosition> white = new HashSet<>();
    HashSet<ChessPosition> black = new HashSet<>();// this not upating
    ChessPosition whiteKing;
    ChessPosition blackKing;
    HashSet<ChessPiece> captured = new HashSet<>();

    /**no arg constructor */
    public Board(){
        super();
    }

    /**
     * json adapter constructor
     * 
     * @param board
     * @param white
     * @param black
     * @param whiteKing
     * @param blackKing
     * @param captured
     */
    public Board(ChessPiece[][] board, HashSet<ChessPosition> white, HashSet<ChessPosition> black,
        ChessPosition whiteKing, ChessPosition blackKing, HashSet<ChessPiece> captured){
        this.board = board;
        this.white = white;
        this.black = black;
        this.whiteKing = whiteKing;
        this.blackKing = blackKing;
        this.captured = captured;
    }
    
    /**copy constructor */
    public Board (Board other){
        for(int i=0; i<8; i++){
            for(int j=0; j<8;j++){
                board[i][j] = other.board[i][j];//shallow copy of pieces
            }
        }
        for (ChessPosition pos : other.white){
            white.add(pos);
        }
        for (ChessPosition pos : other.black){
            black.add(pos);
        }
        if(other.whiteKing!= null){
            whiteKing = other.whiteKing.clone();
        }
        if(other.blackKing!= null){
            blackKing = other.blackKing.clone();
        }
        captured = other.captured; //TODO fix shallow copy of captured pieces
    }
    
    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()-1][position.getColumn()-1]= piece;
        if(piece.getTeamColor()==TeamColor.WHITE){
            white.add(position);
            if(piece.getPieceType()==PieceType.KING){
                whiteKing = position;
            }
        }
        else {
            black.add(position);
            if(piece.getPieceType()==PieceType.KING){
                blackKing = position;
            }
        }
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        if (onBoard(position)){
            return board[position.getRow()-1][position.getColumn()-1];
        }
        return null;
    }

    @Override
    public void resetBoard() {
        //add kings
        addPiece(new Position(1,5), new King(TeamColor.WHITE));
        addPiece(new Position(8, 5), new King(TeamColor.BLACK));
        //add queens
        addPiece(new Position(1, 4),new Queen(TeamColor.WHITE));
        addPiece(new Position(8, 4), new Queen(TeamColor.BLACK));
        //add rooks
        addPiece(new Position(1, 1), new Rook(TeamColor.WHITE));
        addPiece(new Position(1, 8), new Rook(TeamColor.WHITE));
        addPiece(new Position(8, 1), new Rook(TeamColor.BLACK));
        addPiece(new Position(8, 8), new Rook(TeamColor.BLACK));
        //add bishops
        addPiece(new Position(1, 3), new Bishop(TeamColor.WHITE));
        addPiece(new Position(1, 6), new Bishop(TeamColor.WHITE));
        addPiece(new Position(8, 3), new Bishop(TeamColor.BLACK));
        addPiece(new Position(8, 6), new Bishop(TeamColor.BLACK));
        //add knights
        addPiece(new Position(1, 2), new Knight(TeamColor.WHITE));
        addPiece(new Position(1, 7), new Knight(TeamColor.WHITE));
        addPiece(new Position(8, 2), new Knight(TeamColor.BLACK));
        addPiece(new Position(8, 7), new Knight(TeamColor.BLACK));
        //add pawns

        for(int i = 1; i<9; i++){
            addPiece(new Position(2, i),new Pawn(TeamColor.WHITE));
            addPiece(new Position(7, i), new Pawn(TeamColor.BLACK));
        }

    }

    /**
     * moves a piece on the board from start to end
     * 
     * @param move
     * @throws InvalidMoveException if the start has no piece
     */
    public void movePiece (ChessMove move) throws InvalidMoveException {
        ChessPiece piece = getPiece(move.getStartPosition());
        if (piece == null){
            throw new InvalidMoveException();
        }
        ChessPiece captive = getPiece(move.getEndPosition());
        if(captive!= null){
            captured.add(captive);
        }
        board[move.getStartPosition().getRow()-1][move.getStartPosition().getColumn()-1]=null;
        if(move.getPromotionPiece()==null){
            board[move.getEndPosition().getRow()-1][move.getEndPosition().getColumn()-1]=piece;
        }
        else{//promotions
            switch (move.getPromotionPiece()) {
                case QUEEN:    
                    addPiece(move.getEndPosition(), new Queen(piece.getTeamColor()));
                    break;
                case BISHOP:             
                    addPiece(move.getEndPosition(), new Bishop(piece.getTeamColor()));
                    break;
                case ROOK:
                    addPiece(move.getEndPosition(), new Rook(piece.getTeamColor()));
                    break;
                case KNIGHT:
                    addPiece(move.getEndPosition(), new Knight(piece.getTeamColor()));
                    break;
                default:
                    throw new InvalidMoveException();
            }
        }

        if(piece.getTeamColor()==TeamColor.WHITE){
            white.remove(move.getStartPosition());
            white.add(move.getEndPosition());
            if(piece.getPieceType()==PieceType.KING){
                whiteKing = move.getEndPosition();
            }
            if(captive!= null){
                black.remove(move.getEndPosition());
            }
        }
        else{
            black.remove(move.getStartPosition());
            black.add(move.getEndPosition());
            if(piece.getPieceType()==PieceType.KING){
                blackKing = move.getEndPosition();
            }
            if(captive!= null){
                white.remove(move.getEndPosition());
            }
        }

    }

    /**
     * checks if the position given is in the 1-8 range
     * 
     * @param pos
     * @return true if on board, false if not
     */
    public boolean onBoard (ChessPosition pos){
        if (pos.getColumn()>8||pos.getColumn()<1){
            return false;
        }
        if (pos.getRow()>8||pos.getRow()<1){
            return false;
        }

        return true;
    }

    /**
     * 
     * @param color
     * @return that teams collection of positions where pieces are
     */
    public Collection<ChessPosition> piecesOnBoard(TeamColor color){
        if (color == TeamColor.WHITE){
            return white;
        }
        return black;
    }

    /**
     * 
     * @param color
     * @return position of that team's king
     */
    public ChessPosition kingPos (TeamColor color){
        if (color == TeamColor.WHITE){
            return whiteKing;
        }
        return blackKing;
    }

    public Board clone(){
        return new Board(this);
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (int i=7; i>-1; i--){
            out.append("|");
            for (int j=0; j<8; j++){
                if(board[i][j]==null){
                    out.append("  ");
                }
                else{
                    out.append(board[i][j].toString());
                }
                out.append("|");
            }
            out.append("\n");
        }
        return out.toString();
    }
    
}
