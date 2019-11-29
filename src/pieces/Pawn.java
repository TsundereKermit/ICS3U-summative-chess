package pieces;

import board.Board;
import board.Move;
import board.BoardUtils;
import board.Move.*;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Description: Pawn, able to jump, en passant and promote to a queen,
 * for more information: https://en.wikipedia.org/wiki/Pawn_(chess)
 * 
 * Date: Jan. 9, 2019
 * Author: Tony Jiang
 */

public class Pawn extends Piece {

    //Contains all possible offsets of a move by the pawn
    public static int[] CANDIDATE_MOVE_COORDINATE = {7, 8, 9, 16};
            
    public Pawn(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.PAWN, true);
    }
    
    public Pawn(int piecePosition, Alliance pieceAlliance, boolean isFirstMove) {
        super(piecePosition, pieceAlliance, PieceType.PAWN, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        
        List<Move> legalMoves = new ArrayList<>();
        
        for (int offset : CANDIDATE_MOVE_COORDINATE) {
            
            int destination = this.piecePosition + (this.getPieceAlliance().getDirection()*offset);
            
            //Checks if the destination is out of bounds
            if(!BoardUtils.isValidTileCoordinate(destination)) {
                continue;
            }
            
            //An offset of 8 means a normal "pawn push", only possible when the destination is not occupied
            if (offset == 8 && !board.getTile(destination).isTileOccupied()) {
                
                //The pawn gets promoted to a queen if it touches the farthest rank
                if (this.pieceAlliance.isPawnPromotionSquare(destination)) {
                    legalMoves.add(new PawnPromotion(new PawnMove(board, this, destination)));
                } else {
                    legalMoves.add(new PawnMove(board, this, destination));
                }
            } 
            
            //An offset of 16 is a "pawn jump", only possible for the first move and an empty route
            else if (offset == 16 && this.isFirstMove() && 
                      ((BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getPieceAlliance().isBlack()) || 
                      (BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceAlliance().isWhite()))) {
                int behindDest = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                
                //This checks for the tile right ahead and the tile 2 tiles ahead to be empty
                //Impossible for the pawn to jump to the last rank on its first move so no check for pawn promotion
                if (!board.getTile(behindDest).isTileOccupied() && 
                    !board.getTile(destination).isTileOccupied()) {
                    legalMoves.add(new PawnJump(board, this, destination));
                }
                
            //An offset of 7 means an attack is made by the pawn, only possible if the attacked tile is occupied
            } else if (offset == 7 && 
                      !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                      (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {
                
                //Checks for the occupancy of the tile being attacked
                if (board.getTile(destination).isTileOccupied()) {
                    Piece pieceOnDest = board.getTile(destination).getPiece();
                    
                    //Checks for the alliances of the attacked piece (must be different from pawn)
                    if (this.pieceAlliance != pieceOnDest.getPieceAlliance()) {
                        
                        //The pawn gets promoted to a queen if it touches the farthest rank
                        if (this.pieceAlliance.isPawnPromotionSquare(destination)) {
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, destination, pieceOnDest)));
                        } else {
                            legalMoves.add(new PawnAttackMove(board, this, destination, pieceOnDest));
                        }
                    }
                } 
                
                //The pawn can also attack diagonally via en passant
                else if (board.getEnPassantPawn() != null) {
                    if (board.getEnPassantPawn().getPiecePosition() == this.piecePosition + (this.pieceAlliance.getOppositeDirection())) {
                        Piece pieceOnDest = board.getEnPassantPawn();
                        
                        //Checks for the alliances of the attacked piece (must be different from pawn)
                        if (this.getPieceAlliance() != pieceOnDest.getPieceAlliance()) {
                            legalMoves.add(new PawnEnPassantMove(board, this, destination, pieceOnDest));
                        }
                    }
                }
            } 
            
            //An offset of 9 means an attack is made by the pawn, only possible if the attacked tile is occupied
            else if (offset == 9 &&
                      !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()) ||
                      (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()))) {
                
                //Checks for the occupancy of the tile being attacked
                if (board.getTile(destination).isTileOccupied()) {
                      Piece pieceOnDest = board.getTile(destination).getPiece();
                      
                      //Checks for the alliances of the attacked piece (must be different from pawn)
                      if (this.pieceAlliance != pieceOnDest.getPieceAlliance()) {
                        if (this.pieceAlliance.isPawnPromotionSquare(destination)) {
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, destination, pieceOnDest)));
                        } else {
                            legalMoves.add(new PawnAttackMove(board, this, destination, pieceOnDest));
                        }
                    }
                } 
                
                //The pawn can also attack diagonally via en passant
                else if (board.getEnPassantPawn() != null) {
                    if (board.getEnPassantPawn().getPiecePosition() == this.piecePosition - (this.pieceAlliance.getOppositeDirection())) {
                        Piece pieceOnDest = board.getEnPassantPawn();
                        
                        //Checks for the alliances of the attacked piece (must be different from pawn)
                        if (this.getPieceAlliance() != pieceOnDest.getPieceAlliance()) {
                            legalMoves.add(new PawnEnPassantMove(board, this, destination, pieceOnDest));
                        }
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }
    
    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }
    
    @Override
    public Pawn movePiece(Move move) {
        return new Pawn (move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }
    
    public Piece getPromotionPiece() {
        return new Queen(this.piecePosition, this.pieceAlliance, false);
    }
    
}
