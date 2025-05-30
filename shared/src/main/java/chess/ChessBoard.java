package chess;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import chess.ChessGame.TeamColor;
import chess.ChessPiece.PieceType;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece[][] board = new ChessPiece[8][8];
    HashSet<ChessPosition> white = new HashSet<>();
    HashSet<ChessPosition> black = new HashSet<>();// this not upating
    ChessPosition whiteKing;
    ChessPosition blackKing;
    HashSet<ChessPiece> captured = new HashSet<>();

    public ChessBoard() {
        
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
    public ChessBoard(ChessPiece[][] board, HashSet<ChessPosition> white, HashSet<ChessPosition> black,
                 ChessPosition whiteKing, ChessPosition blackKing, HashSet<ChessPiece> captured){
        this.board = board;
        this.white = white;
        this.black = black;
        this.whiteKing = whiteKing;
        this.blackKing = blackKing;
        this.captured = captured;
    }

    /**copy constructor */
    public ChessBoard (ChessBoard other){
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

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (piece == null){
            board[position.getRow()-1][position.getColumn()-1] = null;
            return;
        }
        switch (piece.getPieceType()){
            case KING -> board[position.getRow()-1][position.getColumn()-1] = new King(piece.getTeamColor());
            case QUEEN -> board[position.getRow()-1][position.getColumn()-1] = new Queen(piece.getTeamColor());
            case KNIGHT -> board[position.getRow()-1][position.getColumn()-1] = new Knight(piece.getTeamColor());
            case BISHOP -> board[position.getRow()-1][position.getColumn()-1] = new Bishop(piece.getTeamColor());
            case ROOK -> board[position.getRow()-1][position.getColumn()-1] = new Rook(piece.getTeamColor());
            case PAWN -> board[position.getRow()-1][position.getColumn()-1] = new Pawn(piece.getTeamColor());
            case null, default -> board[position.getRow()-1][position.getColumn()-1]= piece;
        }
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

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if (onBoard(position)){
            return board[position.getRow()-1][position.getColumn()-1];
        }
        return null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //add kings
        addPiece(new ChessPosition(1,5), new King(TeamColor.WHITE));
        addPiece(new ChessPosition(8, 5), new King(TeamColor.BLACK));
        //add queens
        addPiece(new ChessPosition(1, 4),new Queen(TeamColor.WHITE));
        addPiece(new ChessPosition(8, 4), new Queen(TeamColor.BLACK));
        //add rooks
        addPiece(new ChessPosition(1, 1), new Rook(TeamColor.WHITE));
        addPiece(new ChessPosition(1, 8), new Rook(TeamColor.WHITE));
        addPiece(new ChessPosition(8, 1), new Rook(TeamColor.BLACK));
        addPiece(new ChessPosition(8, 8), new Rook(TeamColor.BLACK));
        //add bishops
        addPiece(new ChessPosition(1, 3), new Bishop(TeamColor.WHITE));
        addPiece(new ChessPosition(1, 6), new Bishop(TeamColor.WHITE));
        addPiece(new ChessPosition(8, 3), new Bishop(TeamColor.BLACK));
        addPiece(new ChessPosition(8, 6), new Bishop(TeamColor.BLACK));
        //add knights
        addPiece(new ChessPosition(1, 2), new Knight(TeamColor.WHITE));
        addPiece(new ChessPosition(1, 7), new Knight(TeamColor.WHITE));
        addPiece(new ChessPosition(8, 2), new Knight(TeamColor.BLACK));
        addPiece(new ChessPosition(8, 7), new Knight(TeamColor.BLACK));
        //add pawns

        for(int i = 1; i<9; i++){
            addPiece(new ChessPosition(2, i),new Pawn(TeamColor.WHITE));
            addPiece(new ChessPosition(7, i), new Pawn(TeamColor.BLACK));
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
                ChessPosition rookEnd = whiteKing.clone();
                if (move.getStartPosition().getColumn()-move.getEndPosition().getColumn()==2){
                    rookEnd.right(1);
                    movePiece(new ChessMove(new ChessPosition(1,1), rookEnd, null));
                }
                else if (move.getStartPosition().getColumn()-move.getEndPosition().getColumn()==-2){
                    rookEnd.left(1);
                    movePiece(new ChessMove(new ChessPosition(1,8), rookEnd, null));
                }
            }
            else if (piece.getPieceType()==PieceType.PAWN){
                Pawn pawn = (Pawn) piece;
                pawn.updatePosition(move.getStartPosition());
                //check if move is enPassent
                if (captive == null){
                    if (move.getStartPosition().getColumn() != move.getEndPosition().getColumn()){
                        ChessPosition captiveLocation = move.getEndPosition().clone();
                        captiveLocation.down(1);
                        captive = getPiece(captiveLocation);
                        captured.add(captive);
                        board[captiveLocation.getRow()-1][captiveLocation.getColumn()-1] = null;
                    }
                }
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
                ChessPosition rookEnd = blackKing.clone();
                if (move.getStartPosition().getColumn()-move.getEndPosition().getColumn()==2){
                    rookEnd.right(1);
                    movePiece(new ChessMove(new ChessPosition(8,1), rookEnd, null));
                }
                else if (move.getStartPosition().getColumn()-move.getEndPosition().getColumn()==-2){
                    rookEnd.left(1);
                    movePiece(new ChessMove(new ChessPosition(8,8), rookEnd, null));
                }
            }
            else if (piece.getPieceType()==PieceType.PAWN){
                Pawn pawn = (Pawn) piece;
                pawn.updatePosition(move.getStartPosition());
                if (captive == null){
                    if (move.getStartPosition().getColumn() != move.getEndPosition().getColumn()){
                        ChessPosition captiveLocation = move.getEndPosition().clone();
                        captiveLocation.up(1);
                        captive = getPiece(captiveLocation);
                        captured.add(captive);
                        board[captiveLocation.getRow()-1][captiveLocation.getColumn()-1] = null;
                    }
                }
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

    @Override
    public ChessBoard clone(){
        return new ChessBoard(this);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessBoard that=(ChessBoard) o;
        return Arrays.deepEquals(board, that.board) && Objects.equals(whiteKing, that.whiteKing) && Objects.equals(blackKing, that.blackKing);
    }

    @Override
    public int hashCode() {
        int result=Objects.hash(white, black, whiteKing, blackKing, captured);
        result=31 * result + Arrays.deepHashCode(board);
        return result;
    }
}
