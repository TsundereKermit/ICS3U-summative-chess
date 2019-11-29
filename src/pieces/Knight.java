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
 * Description: Knight, minor piece in chess, must move 2 spaces away horizontally
 * and 1 space away vertically
 * for more information: https://en.wikipedia.org/wiki/Knight_(chess)
 * 
 * Date: Jan. 9, 2019
 * Author: Tony Jiang
 */

public class Knight extends Piece {

    //Contains all possible offsets of a move by the knight
    public static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};
    
    public Knight(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.KNIGHT, true);
    }
    
    public Knight(int piecePosition, Alliance pieceAlliance, boolean isFirstMove) {
        super(piecePosition, pieceAlliance, PieceType.KNIGHT, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        
        List<Move> legalMoves = new ArrayList<>();
        
        for (int offset : CANDIDATE_MOVE_COORDINATES) {
            
            int destination;
            
            destination = this.piecePosition + offset;
            
            //Checks if the destination is out of bounds
            if (BoardUtils.isValidTileCoordinate(destination)) {
                
                //This prevents the knight from jumping across the board
                if (isFirstColumnExclusion(this.piecePosition, offset) ||
                    isSecondColumnExclusion(this.piecePosition, offset) ||
                    isSeventhColumnExclusion(this.piecePosition, offset) ||
                    isEighthColumnExclusion(this.piecePosition, offset)) {
                    continue;
                }
                        
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
                }
            }
        }
        
        return legalMoves;
    
        }
    
    private static boolean isFirstColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -17 || candidateOffset == -10 ||
                candidateOffset == 6 || candidateOffset == 15);
    }
    
    private static boolean isSecondColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == -10 || candidateOffset == 6);
    }
    
    private static boolean isSeventhColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == -6 || candidateOffset == 10);
    }
    
    private static boolean isEighthColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -15 || candidateOffset == -6 ||
                candidateOffset == 10 || candidateOffset == 17);
    }
    
    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

    @Override
    public Knight movePiece(Move move) {
        return new Knight (move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }
    
}
