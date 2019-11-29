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
 * Description: Rook, major piece in chess, can move horizontally and vertically 
 * for more information: https://en.wikipedia.org/wiki/Rook_(chess)
 * 
 * Date: Jan. 9, 2019
 * Author: Tony Jiang
 */

public class Rook extends Piece {
    
    //Contains all possible offsets of a move by the rook
    //*the offsets will be looped so I do not have to list all the possible offsets
    public static int[] CANDIDATE_MOVE_COORDINATES = {-8, -1, 1, 8};
    
    public Rook(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.ROOK, true);
    }
    
    public Rook(int piecePosition, Alliance pieceAlliance, boolean isFirstMove) {
        super(piecePosition, pieceAlliance, PieceType.ROOK, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        
        List<Move> legalMoves = new ArrayList<>();
        
        for (int offset : CANDIDATE_MOVE_COORDINATES) {
            
            int destination = this.piecePosition;
            
            //Checks if the destination is out of bounds
            while (BoardUtils.isValidTileCoordinate(destination)) {
                
                //This prevents the rook from jumping across the board
                if (isFirstColumnExclusion(destination, offset) ||
                    isEighthColumnExclusion(destination, offset)) {
                    break;
                }
                
                destination+=offset;
                
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
        return BoardUtils.FIRST_COLUMN[currentPosition] && offset == -1;
    }
    
    public static boolean isEighthColumnExclusion (int currentPosition, int offset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && offset == 1;
    }
    
    @Override
    public String toString() {
        return PieceType.ROOK.toString();
    }
    
    @Override
    public Rook movePiece(Move move) {
        return new Rook (move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }
}
