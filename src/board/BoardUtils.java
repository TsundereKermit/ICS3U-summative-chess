package board;

import java.util.HashMap;
import java.util.Map;

public class BoardUtils {
    
    //These will return true if the piece is on their respective columns
    //Similar to the slider game, these arrays prevent pieces from teleporting across the board
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);
    
    //These will return true if the piece is on their respective ranks
    public static final boolean[] EIGHTH_RANK = initRow(0);
    public static final boolean[] SEVENTH_RANK = initRow(8);
    public static final boolean[] SIXTH_RANK = initRow(16);
    public static final boolean[] FIFTH_RANK = initRow(24);
    public static final boolean[] FOURTH_RANK = initRow(32);
    public static final boolean[] THIRD_RANK = initRow(40);
    public static final boolean[] SECOND_RANK = initRow(48);
    public static final boolean[] FIRST_RANK = initRow(56);
    
    public static final String[] ALGEBRAIC_NOTATION = initAlNotation();
    public static final Map<String, Integer> POSITION_TO_COORDINATE = initPosToCoord();
    
    //Sets up the boolean arrays for the rows
    public static boolean[] initRow (int rowNumber) {
        boolean[] row = new boolean[64];
        do {
            row[rowNumber]=true;
            rowNumber++;
        } while (rowNumber%8 != 0);
        return row;
    }
    
    //Sets up the boolean arrays for the columns
    public static boolean[] initColumn(int columnNumber) {
        boolean[] column = new boolean[64];
        
        do {
            column[columnNumber] = true;
            columnNumber+=8;
        } while (columnNumber < 64);
        return column;
    }
            
    //This prevents a move from being out of bounds
    public static boolean isValidTileCoordinate (int coordinate) {
        return coordinate>=0 && coordinate < 64;
    }
    
    //Sets up the "POSITION_TO_COORDINATE" map
    private static Map<String, Integer> initPosToCoord() {
        Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = 0; i < 64; i++) {
            positionToCoordinate.put(ALGEBRAIC_NOTATION[i], i);
        }
        return positionToCoordinate;
    }
    
    //Sets up the "ALGEBRAIC_NOTATION" array
    private static String[] initAlNotation() {
        String[] array = {
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
        };
        return array;
    }
    
    //Returns the coordinate based on the Algebraic Notation
    public static int getCoordinateAtPosition(String position) {
        return POSITION_TO_COORDINATE.get(position);
    }
    
    //Returns the Algebraic Notation based on the coordinate
    public static String getPositionAtCoordinate(int coordinate) {
        return ALGEBRAIC_NOTATION[coordinate];
    }
    
}
