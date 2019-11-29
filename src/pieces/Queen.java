package pieces;

import board.Board;
import board.Move;
import board.Move.MajorMove;
import board.Tile;
import board.BoardUtils;
import board.Move.MajorAttackMove;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;

/**
 * Description: Queen, major piece, moves both diagonally, vertically and horizontally
 * for more information: https://en.wikipedia.org/wiki/Queen_(chess)
 * 
 * Date: Jan. 9, 2019
 * Author: Tony Jiang
 */

public class Queen extends Piece {

    //Contains all possible offsets of a move by the queen
    //*the offsets will be looped so I do not have to list all the possible offsets
    public static int[] CANDIDATE_MOVE_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};
    
    public Queen(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.QUEEN, true);
    }
    
    public Queen(int piecePosition, Alliance pieceAlliance, boolean isFirstMove) {
        super(piecePosition, pieceAlliance, PieceType.QUEEN, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        
        List<Move> legalMoves = new ArrayList<>();
        
        for (int offset : CANDIDATE_MOVE_COORDINATES) {
            
            int destination = this.piecePosition;
            
            while (BoardUtils.isValidTileCoordinate(destination)) {
                
                //This prevents the queen from jumping across the board
                if (isFirstColumnExclusion(destination, offset) ||
                    isEighthColumnExclusion(destination, offset)) {
                    break;
                }
                
                destination+=offset;
                
                //Checks if the destination is out of bounds
                if (BoardUtils.isValidTileCoordinate(destination)) {
                    Tile destTile = board.getTile(destination);
                    
                    //MajorMove if nothing on the tile (not attacking)
                    if (!destTile.isTileOccupied()) {
                        legalMoves.add(new MajorMove(board, this, destination));
                    } else {
                        Piece destPiece = destTile.getPiece();
                        Alliance alliance = destPiece.getPieceAlliance();
                        
                            //MajorAttackMove if there is a piece on the destination
                            //and their alliances are not the same
                            if (this.pieceAlliance != alliance) {
                                legalMoves.add(new MajorAttackMove(board, this, destination, destPiece));
                            }
                            break;
                    }
                
                }
                
            }
        }
        
        return legalMoves;
    }
    
    public static boolean isFirstColumnExclusion (int currentPosition, int offset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (offset == -9 || offset == 7 || offset == -1);
    }
    
    public static boolean isEighthColumnExclusion (int currentPosition, int offset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (offset == -7 || offset == 9 || offset == 1);
    }
    
    @Override
    public String toString() {
        return PieceType.QUEEN.toString();
    }
    
    @Override
    public Queen movePiece(Move move) {
        return new Queen (move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }
}