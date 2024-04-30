package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    int row;
    int column;

    /**
     * Creates a new Position based on parameters
     * Valid rows and columns are 1-8
     *
     * @param row
     * @param col
     */
    public ChessPosition(int row, int col) {
        this.row = row;
        column = col;
    }

    /**
     * Copy Constructor
     */

    public ChessPosition(ChessPosition other){
        row = other.row;
        column = other.column;
    }


    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return column;
    }

    /**
     * row + n
     * @param n umber of spaces up
     */
    public void up(int n){
        row +=n;
    }

    /**
     * row - n
     * @param n umber of spaces down
     */
    public void down(int n){
        row-=n;
    }

    /**
     * column + n
     * @param n umber of spaces right
     */
    public void right(int n){
        column+=n;
    }

    /**
     * column - n
     * @param n umber of spaces left
     */
    public void left(int n){
        column-=n;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + row;
        result = prime * result + column;
        return result;
    }

    @Override
    /**@return true if row and column are the same */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChessPosition other = (ChessPosition) obj;
        if (row != other.row)
            return false;
      return column == other.column;
    }

    @Override
    public ChessPosition clone(){
        return new ChessPosition(this);
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder(row + ',' + column);
        return str.toString();
    }
}
