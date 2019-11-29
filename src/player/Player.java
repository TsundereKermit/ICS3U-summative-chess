package player;

import board.Board;
import board.Move;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import pieces.Alliance;
import pieces.King;
import pieces.Piece;

/**
 * Description: Parent player class, contains help and getter methods
 * 
 * Date: Jan. 9, 2019
 * Author: Tony Jiang
 */

public abstract class Player {

        //Returns a collection of moves that attack a tile
        protected static Collection<Move> calcAttack(int piecePosition, Collection<Move> moves) {
            List<Move> attackMoves = new ArrayList<>();
            for (Move move: moves) {
                if (piecePosition == move.getDestinationCoordinate()) {
                    attackMoves.add(move);
                }
            }
            return ImmutableList.copyOf(attackMoves);
        }
        
        Board board;
        King playerKing;
        Collection<Move> legalMoves;
        boolean isInCheck;
        
        Player (Board board, Collection<Move> playerLegals, Collection<Move> opponentLegals) {
            this.board = board;
            this.playerKing = establishKing();
            this.legalMoves = ImmutableList.copyOf(playerLegals);
            this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateKingCastles(playerLegals, opponentLegals)));
            this.isInCheck = !Player.calcAttack(this.playerKing.getPiecePosition(), opponentLegals).isEmpty();
        }

        //Ensures the existence of 2 kings in a given chess game
        private King establishKing() {
            for (Piece piece : getActivePieces()) {
                if (piece.getPieceType().isKing()) {
                    //Casting as king is a bit ugly but it was the best solution I found
                    return (King) piece;
                }
            }
            throw new RuntimeException("This probably should not happen :/");
        }
        
        public abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals,
                                                             Collection<Move> opponentLegals);
        public abstract Collection<Piece> getActivePieces();
        public abstract Alliance getAlliance();
        public abstract Player getOpponent();
        
        public boolean isMoveLegal(Move move) {
            return this.legalMoves.contains(move);
        }
        
        public boolean isInCheck() {
            return this.isInCheck;
        }
        
        public boolean isInCheckmate() {
            return this.isInCheck && !hasEscapeMoves();
        }
        
        public boolean isInStalemate() {
            return !this.isInCheck && !hasEscapeMoves();
        }
        
        public boolean isKingSideCastleCapable() {
            return this.playerKing.isKingSideCastleCapable();
        }
        
        public boolean isQueenSideCastleCapable() {
            return this.playerKing.isQueenSideCastleCapable();
        }
        
        public boolean isCastled() {
            return this.playerKing.isCastled();
        }
        
        //Returns the MoveTransition after a player's move
        public MoveTransition makeMove(Move move) {
            
            if(!isMoveLegal(move)) {
                return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
            } 
            
            Board transitionBoard = move.execute();
            
            Collection<Move> kingAttacks = Player.calcAttack(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
            transitionBoard.currentPlayer().getLegalMoves());
            
            if (!kingAttacks.isEmpty()) {
                return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
            }
            return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
            
        }

        //Checks for a piece's escape moves (only used with king)
        private boolean hasEscapeMoves() {
            for (Move move: legalMoves) {
                MoveTransition transition = makeMove(move);
                if(transition.getMoveStatus().isDone()) {
                    return true;
                } 
            }
            return false; 
        }

        public King getPlayerKing() {
            return this.playerKing;
        }

        public Collection<Move> getLegalMoves() {
            return this.legalMoves;
        }
}