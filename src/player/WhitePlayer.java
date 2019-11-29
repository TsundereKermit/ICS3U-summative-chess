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
 * Description: Children of the player class for the white player
 * 
 * Date: Jan. 9, 2019
 * Author: Tony Jiang
 */

public class WhitePlayer extends Player {

    public WhitePlayer(Board board, Collection<Move> whiteLegalMoves, Collection<Move> blackLegalMoves) {
        super(board, whiteLegalMoves, blackLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer(); //FOR NOW!!!
    }

    //Experimental
    //TODO finish castling
    @Override
    public Collection<Move> calculateKingCastles(Collection<Move> legalPlayer, Collection<Move> legalOppo) {
        List<Move> temp = new ArrayList<>();
        if (this.playerKing.isFirstMove() && (!this.isInCheck())) {
            //White kingside castle 
            if (!this.board.getTile(61).isTileOccupied() && 
                !this.board.getTile(62).isTileOccupied()) {
                Tile rookTile = this.board.getTile(63);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calcAttack(62, legalOppo).isEmpty() && 
                        Player.calcAttack(61, legalOppo).isEmpty() && 
                        rookTile.getPiece().getPieceType().isRook()) {
                        temp.add(new KingSideCastle(this.board, 
                                                          this.playerKing, 
                                                          62, 
                                                          (Rook)rookTile.getPiece(), 
                                                          rookTile.getTileCoordinate(), 
                                                          61));
                    }
                }
            }
            //White queenside castle
            if (!this.board.getTile(59).isTileOccupied() && 
                !this.board.getTile(58).isTileOccupied() && 
                !this.board.getTile(57).isTileOccupied()) {
                Tile rookTile = this.board.getTile(56);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calcAttack(59, legalOppo).isEmpty() && 
                        Player.calcAttack(58, legalOppo).isEmpty() && 
                        Player.calcAttack(57, legalOppo).isEmpty() && 
                        rookTile.getPiece().getPieceType().isRook()) {
                        temp.add(new QueenSideCastle(this.board, 
                                                          this.playerKing, 
                                                          58, 
                                                          (Rook)rookTile.getPiece(), 
                                                          rookTile.getTileCoordinate(), 
                                                          59));
                    }
                }
            }
        }
        return ImmutableList.copyOf(temp);
    }
    
}
