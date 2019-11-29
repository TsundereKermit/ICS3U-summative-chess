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
 * Description: Bishop, minor piece in chess, moves only in a diagonal fashion,
 * for more information: https://en.wikipedia.org/wiki/Bishop_(chess)
 * 
 * Date: Jan. 9, 2019
 * Author: Tony Jiang
 */

public class Bishop extends Piece {
    
    //Contains all possible offsets of a move by the bishop
    public final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -7, 7, 9};
    
    public Bishop(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.BISHOP, true);
    }
    
    public Bishop(int piecePosition, Alliance pieceAlliance, boolean isFirstMove) {
        super(piecePosition, pieceAlliance, PieceType.BISHOP, isFirstMove);
    }
    
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        
        List<Move> legalMoves = new ArrayList<>();
        
        for (int offset : CANDIDATE_MOVE_COORDINATES) {
            
            int destination = this.piecePosition;
            
            while (BoardUtils.isValidTileCoordinate(destination)) {
                
                //This prevents the bishop from jumping across the board
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
        return BoardUtils.FIRST_COLUMN[currentPosition] && (offset == -9 || offset == 7);
    }
    
    public static boolean isEighthColumnExclusion (int currentPosition, int offset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (offset == -7 || offset == 9);
    }
    
    @Override
    public String toString() {
        return PieceType.BISHOP.toString();
    }

    @Override
    public Bishop movePiece(Move move) {
        return new Bishop (move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }
}
