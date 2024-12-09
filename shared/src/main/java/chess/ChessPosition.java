package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.ArrayList;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
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
        return col;
    }

    @Override
    public String toString() {
        //return "(" + row + "," + col + ")";
        String colLetter = "Z";
        switch(col) {
            case 1 -> colLetter = "A";
            case 2 -> colLetter = "B";
            case 3 -> colLetter = "C";
            case 4 -> colLetter = "D";
            case 5 -> colLetter = "E";
            case 6 -> colLetter = "F";
            case 7 -> colLetter = "G";
            case 8 -> colLetter = "H";
        }
        return String.format("%s%d",colLetter,row);
    }

    //equals and hash functions
    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
