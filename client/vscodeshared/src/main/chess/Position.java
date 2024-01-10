package chess;

/** implementation the ChessPosition class from starter code. 
 * 
 * Constructor(row, column) and default, equals, get column and row
 * <p>
 * Valid Row and Column represented as 1-8
*/
public class Position implements ChessPosition {
    int row;
    int column;

    public Position(){
        super();
    }
    
    /**
     * Creates a new Position based on parameters
     * Valid rows and columns are 1-8
     * 
     * @param row
     * @param column
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**copy constructor */
    public Position(Position other){
        row = other.row;
        column = other.column;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = row*result+column;
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
        Position other = (Position) obj;
        if (row != other.row)
            return false;
        if (column != other.column)
            return false;
        return true;
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
    public Position clone(){
        return new Position(this);
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder(row + ',' + column);
        return str.toString();
    }
    
}
