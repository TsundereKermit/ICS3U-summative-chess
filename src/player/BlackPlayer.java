package player;

import board.Board;
import board.Move;
import board.Move.KingSideCastle;
import board.Move.QueenSideCastle;
import board.Tile;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import pieces.Alliance;
import pieces.Piece;
import pieces.Rook;

/**
 * Description: Children of the player class for the black player
 * 
 * Date: Jan. 9, 2019
 * Author: Tony Jiang
 */

public class BlackPlayer extends Player {

    public BlackPlayer(Board board, Collection<Move> whiteLegalMoves, Collection<Move> blackLegalMoves) {
        super(board, blackLegalMoves, whiteLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }
    
    @Override
    public Player getOpponent() {
        return this.board.whitePlayer(); //FOR NOW!!
    }

    //Experimental
    //TODO finish castling
    @Override
    public Collection<Move> calculateKingCastles(Collection<Move> legalPlayer, Collection<Move> legalOppo) {
        List<Move> temp = new ArrayList<>();
        if (this.playerKing.isFirstMove() && (!this.isInCheck())) {
            //Black kingside castle 
            if (!this.board.getTile(5).isTileOccupied() && 
                !this.board.getTile(6).isTileOccupied()) {
                Tile rookTile = this.board.getTile(7);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calcAttack(5, legalOppo).isEmpty() && 
                        Player.calcAttack(6, legalOppo).isEmpty() && 
                        rookTile.getPiece().getPieceType().isRook()) {
                        temp.add(new KingSideCastle(this.board, 
                                                          this.playerKing, 
                                                          6, 
                                                          (Rook)rookTile.getPiece(), 
                                                          rookTile.getTileCoordinate(), 
                                                          5));
                    }
                }
            }
            //Black queenside castle
            if (!this.board.getTile(1).isTileOccupied() && 
                !this.board.getTile(2).isTileOccupied() && 
                !this.board.getTile(3).isTileOccupied()) {
                Tile rookTile = this.board.getTile(0);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calcAttack(1, legalOppo).isEmpty() && 
                        Player.calcAttack(2, legalOppo).isEmpty() && 
                        Player.calcAttack(3, legalOppo).isEmpty() && 
                        rookTile.getPiece().getPieceType().isRook()) {
                        temp.add(new QueenSideCastle(this.board, 
                                                          this.playerKing, 
                                                          2, 
                                                          (Rook)rookTile.getPiece(), 
                                                          rookTile.getTileCoordinate(), 
                                                          3));
                    }
                }
            }
        }
        return ImmutableList.copyOf(temp);
    }
    
}
