package board;  

import pieces.Piece;

import java.util.Map;
import java.util.HashMap;

/**
 * Description: Mostly a helper class for the behavior of the game
 * 
 * Date: Jan. 9, 2019
 * Author: Tony Jiang
 */

public abstract class Tile {
    
        protected final int tileCoordinates;
        
        private static final Map<Integer, EmptyTile> EMPTY_TILES = createAllPossibleEmptyTiles();
        
        //Sets up a map of empty tiles for future use
        private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
            
            final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
            
            for (int i = 0; i < 64; i++) {
                emptyTileMap.put(i, new EmptyTile(i));
            }
            
            return emptyTileMap;
            
        }
        
        //Creates the tiles on a chess board
        public static Tile createTile (final int tileCoordinates, final Piece piece) {
            
            if (piece != null)
                return new OccupiedTile(tileCoordinates, piece);
            else
                return EMPTY_TILES.get(tileCoordinates);
        
        }
        
        private Tile (int tileCoordinates) {
            this.tileCoordinates = tileCoordinates;
        }
        
        public abstract boolean isTileOccupied();
        
        public abstract Piece getPiece();
        
        public int getTileCoordinate() {
            return this.tileCoordinates;
        }
        
        //Instantiated if no pieces are on the tile
        public static final class EmptyTile extends Tile {
            
            @Override
            public String toString() {
                return "-";
            }
        
            EmptyTile(int coordinates) {
                super(coordinates);
            }
            
            @Override   
            public boolean isTileOccupied() {
                return false;
            }
            
            @Override
            public Piece getPiece() {
                return null;
            }
        
        }
        
        //Instantiated if there is a piece on the tile
        public static final class OccupiedTile extends Tile {
            
            private final Piece pieceOnTile;
            
            @Override
            public String toString() {
                return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() : getPiece().toString();
            }
        
            OccupiedTile(int tileCoordinate, Piece pieceOnTile) {
                super(tileCoordinate);
                this.pieceOnTile = pieceOnTile;
            }
            
            @Override 
            public boolean isTileOccupied() {
                return true;
            }
            
            @Override
            public Piece getPiece() {
                return this.pieceOnTile;
            }
        }
        
        
}
