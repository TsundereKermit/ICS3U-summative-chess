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
 * Description: King, the most important piece in chess, can move 1 square in any direction
 * for more information: https://en.wikipedia.org/wiki/King_(chess)
 * 
 * Date: Jan. 9, 2019
 * Author: Tony Jiang
 */

public class King extends Piece {

    //Contains all possible offsets of a move by the king
    private static int[] CANDIDATE_MOVE_COORDINATE = {-9, -8, -7, -1, 1, 7, 8, 9};
    private final Boolean kingSideCastleCapable;
    private final Boolean queenSideCastleCapable;
    private final boolean isCastled;
    
    public King(int piecePosition, Alliance pieceAlliance, boolean kingSideCastleCapable, boolean queenSideCastleCapable) {
        super(piecePosition, pieceAlliance, PieceType.KING, true);
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
        this.isCastled = false;
    }
    
    public King(int piecePosition, Alliance pieceAlliance, boolean isFirstMove, boolean isCastled, boolean kingSideCastleCapable, boolean queenSideCastleCapable) {
        super(piecePosition, pieceAlliance, PieceType.KING, isFirstMove);
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
        this.isCastled = isCastled;
    }
    
    //Experimental
    //TODO finish castling
    public boolean isCastled() {
        return isCastled;
    }
    
    public boolean isKingSideCastleCapable() {
        return kingSideCastleCapable;
    }
    
    public boolean isQueenSideCastleCapable() {
        return queenSideCastleCapable;
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        
        List<Move> legalMoves = new ArrayList<>();
        
        
        for (int offset: CANDIDATE_MOVE_COORDINATE) {
            
            int destination;
            destination = this.piecePosition + offset;
            
            //This prevents the king from jumping across the board
            if (isFirstColumnExclusion(this.piecePosition, offset) || 
                isEighthColumnExclusion(this.piecePosition, offset)) {
                continue;
            }
            
            //Checks if the destination is out of bounds
            if (BoardUtils.isValidTileCoordinate(destination)) {
                Tile destTile = board.getTile(destination);
                
                //MajorMove if nothing on the tile (not attacking)
                if (!destTile.isTileOccupied()) {
                    //Unfortunately I don't know how to make the castling logic work. I have all the pieces of the puzzle but I don't know how to put them together
                    /*
                    if (offset == 2 || offset == -2) {
                        Collection<Move> temp = board.currentPlayer().calculateKingCastles(legalMoves, board.currentPlayer().getOpponent().getLegalMoves());
                        if (!(temp.isEmpty())) {
                            legalMoves.addAll(temp);
                        }
                    } else {
                        legalMoves.add(new MajorMove(board, this, destination));
                    }
                    */
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
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 || candidateOffset == 7);
    }
    
    private static boolean isEighthColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1 || candidateOffset == 9);
    }
    
    @Override
    public String toString() {
        return PieceType.KING.toString();
    }
    
    @Override
    public King movePiece(Move move) {
        return new King (move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), false, move.isCastlingMove(), false, false);
    }
    
}
