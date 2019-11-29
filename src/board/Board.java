package board;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pieces.*;
import player.*;

/**
 * Description: Sets up the standard starting board and some getter methods
 * 
 * Date: Jan. 9, 2019
 * Author: Tony Jiang
 */

public class Board {

    
        private final WhitePlayer whitePlayer;
        private final BlackPlayer blackPlayer;
        private final Player currentPlayer;
        private final List<Tile> gameBoard;
        private final Collection<Piece> whitePieces;
        private final Collection<Piece> blackPieces;
        private final Pawn enPassantPawn; //only stays for 1 turn
        
        private Board (Builder builder) {
            
            this.gameBoard = createGameBoard(builder);
            this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
            this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);
            
            this.enPassantPawn = builder.enPassantPawn;
            
            Collection<Move> whiteLegalMoves = calculateLegalMoves(this.whitePieces);
            Collection<Move> blackLegalMoves = calculateLegalMoves(this.blackPieces);
            
            this.whitePlayer = new WhitePlayer(this, whiteLegalMoves, blackLegalMoves);
            this.blackPlayer = new BlackPlayer(this, whiteLegalMoves, blackLegalMoves);
            this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
            
        }
        
        //Prints out a text representation of the board, now replaced with gui
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 64; i++) {
                String tileText = this.gameBoard.get(i).toString();
                builder.append(String.format("%3s", tileText));
                if ((i + 1) % 8 == 0) {
                    builder.append("\n");
                }
            }
            return builder.toString();
        }
        
        public Player whitePlayer() {
            return this.whitePlayer;
        }

        public Player blackPlayer() {
            return this.blackPlayer;
        }
        
        public Collection<Piece> getBlackPieces() {
            return this.blackPieces;
        }
        
        public Collection<Piece> getWhitePieces() {
            return this.whitePieces;
        }
        
        public Tile getTile(int tileCoordinate) {
            return gameBoard.get(tileCoordinate);
        }
        
        //Returns all the possible moves on the board
        public Iterable<Move> getAllLegalMoves() {
            return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(), 
                                                                   this.blackPlayer.getLegalMoves()));
        }
        
        public Player currentPlayer() {
            return currentPlayer;
        }
        
        public Pawn getEnPassantPawn() {
            return enPassantPawn;
        }
        
        //Returns a collection of all the possible moves available to a collection of pieces
        private Collection<Move> calculateLegalMoves (Collection<Piece> pieces) {
            List<Move> legalMoves = new ArrayList<>();
            for (Piece piece : pieces) {
                legalMoves.addAll(piece.calculateLegalMoves(this));
            }
            return legalMoves;
        }
        
        //Returns a collection of all the pieces in play
        private static Collection<Piece> calculateActivePieces(List<Tile> gameBoard, Alliance alliance) {
            List<Piece> activePieces = new ArrayList<>();
            
            for (Tile tile : gameBoard) {
                if (tile.isTileOccupied()) {
                    Piece piece = tile.getPiece();
                    if (piece.getPieceAlliance() == alliance) {
                        activePieces.add(piece);
                    }
                }
            }
            return activePieces;
        }
        
        //Returns a list of the tiles in their respective places
        private static List<Tile> createGameBoard(Builder builder) {
            //64 tiles on a standard chess board
            Tile[] tiles = new Tile[64];
            for (int i = 0; i < 64; i++) {
                tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));  
            }
            return ImmutableList.copyOf(tiles);
        }
        
        //Returns a "board" with all the pieces in their starting tiles
        public static Board createStandardBoard() {
            Builder builder = new Builder();
            
            //Black pieces
            builder.setPiece(new Rook(0, Alliance.BLACK));
            builder.setPiece(new Knight(1, Alliance.BLACK));
            builder.setPiece(new Bishop(2, Alliance.BLACK));
            builder.setPiece(new Queen(3, Alliance.BLACK));
            builder.setPiece(new King(4, Alliance.BLACK, true, true));
            builder.setPiece(new Bishop(5, Alliance.BLACK));
            builder.setPiece(new Knight(6, Alliance.BLACK));
            builder.setPiece(new Rook(7, Alliance.BLACK));
            //Black pawns
            builder.setPiece(new Pawn(8, Alliance.BLACK));
            builder.setPiece(new Pawn(9, Alliance.BLACK));
            builder.setPiece(new Pawn(10, Alliance.BLACK));
            builder.setPiece(new Pawn(11, Alliance.BLACK));
            builder.setPiece(new Pawn(12, Alliance.BLACK));
            builder.setPiece(new Pawn(13, Alliance.BLACK));
            builder.setPiece(new Pawn(14, Alliance.BLACK));
            builder.setPiece(new Pawn(15, Alliance.BLACK));
            
            //White pieces
            builder.setPiece(new Rook(56, Alliance.WHITE));
            builder.setPiece(new Knight(57, Alliance.WHITE));
            builder.setPiece(new Bishop(58, Alliance.WHITE));
            builder.setPiece(new Queen(59, Alliance.WHITE));
            builder.setPiece(new King(60, Alliance.WHITE, true, true));
            builder.setPiece(new Bishop(61, Alliance.WHITE));
            builder.setPiece(new Knight(62, Alliance.WHITE));
            builder.setPiece(new Rook(63, Alliance.WHITE));
            //White pawns
            builder.setPiece(new Pawn(48, Alliance.WHITE));
            builder.setPiece(new Pawn(49, Alliance.WHITE));
            builder.setPiece(new Pawn(50, Alliance.WHITE));
            builder.setPiece(new Pawn(51, Alliance.WHITE));
            builder.setPiece(new Pawn(52, Alliance.WHITE));
            builder.setPiece(new Pawn(53, Alliance.WHITE));
            builder.setPiece(new Pawn(54, Alliance.WHITE));
            builder.setPiece(new Pawn(55, Alliance.WHITE));
            
            //White move first
            builder.setMoveMaker(Alliance.WHITE);
            
            return builder.build();
        }

        
        public static class Builder {
            
            Map<Integer, Piece> boardConfig;
            Alliance nextMoveMaker;
            Pawn enPassantPawn;
                
            public Builder() {
                this.boardConfig = new HashMap<>(); //this is used to store both the pieces and their positions
            }
            
            //Used to put pieces in their respective places
            public Builder setPiece(Piece piece) {
                this.boardConfig.put(piece.getPiecePosition(), piece);
                return this;
            }
            
            public Builder setMoveMaker (Alliance alliance) {
                this.nextMoveMaker = alliance;
                return this;
            }
            
            //Returns finished board
            public Board build() {
                return new Board(this);
            }

            public void setEnPassantPawn(Pawn enPassantPawn) {
                this.enPassantPawn = enPassantPawn;
            }
        }
}
