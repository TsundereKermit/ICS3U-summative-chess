package board;

import board.Board.Builder;
import pieces.Pawn;
import pieces.Piece;
import pieces.Rook;

/**
 * Description: All the moves possible in chess (except castling) and some getters
 * 
 * Date: Jan. 9, 2019
 * Author: Tony Jiang
 */

public abstract class Move {

    
        final Board board;
        final Piece movedPiece;
        int destination;
        boolean isFirstMove;
        Piece attackedPiece;
        static Move nullMove = new InvalidMove();
        
        public int getDestinationCoordinate() {
            return this.destination;
        }
        
        public int getCurrentCoordinate() {
            return this.getMovedPiece().getPiecePosition();
        }
        
        //Executes the move that calls this method
        public Board execute() {
                
                Builder builder = new Builder();
                
                //Places the pieces of the current player
                for (Piece piece: this.board.currentPlayer().getActivePieces()) {
                    if (!this.movedPiece.equals(piece)) {
                        builder.setPiece(piece);
                    }
                }
                //Places the pieces of the opponent
                for (Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()) {
                    builder.setPiece(piece);
                }
                
                builder.setPiece(this.movedPiece.movePiece(this));
                //Sets the movemaker as the next player
                builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
                
                return builder.build();
                
        }
        
        //Primary constructor for most moves
        private Move(Board board, Piece movePiece, int destination) {
            
            this.board = board;
            this.movedPiece = movePiece;
            this.destination = destination;
            this.isFirstMove = movePiece.isFirstMove();
            
        }
        
        //Overloaded constructor for the invalid move
        private Move(Board board, int destCoord) {
            
            this.board = board;
            this.destination = destCoord;
            this.movedPiece = null;
            this.isFirstMove = false;
            
        }
        
        @Override
        public int hashCode() {
            int prime = 31;
            int result = 1;
            result = prime * result + this.movedPiece.getPiecePosition();
            result = prime * result + this.movedPiece.hashCode();
            result = prime * result + this.movedPiece.getPiecePosition();
            return result;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (! (o instanceof Move)) {
                return false;
            }
            Move otherMove = (Move) o;
            return this.getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
                    this.getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                    this.getMovedPiece() == otherMove.getMovedPiece();
        }
        
        /*
         * Takes the board, piece that calls this move (movePiece) and its destination
         * Note that this applies to all pieces except for the pawns (the king counts
         * as well) and this move is not used when attacking
        */
        public static class MajorMove extends Move {

            public MajorMove(Board board, Piece movePiece, int destination) {
                super(board, movePiece,destination);
            }
            
            @Override
            public boolean equals(Object o) {
                return this == o || o instanceof MajorMove && super.equals(o);
            }
            
            //Prints out the move in standard convention
            @Override
            public String toString() {
                return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destination);
            }
            
        }
        
        /*
         * Takes the board, piece that calls this move (movePiece) and its destination
         * Note that this applies to all pieces except for the pawns (the king counts
         * as well) and this move is only used when attacking
        */
        public static class MajorAttackMove extends AttackMove {
            
            public MajorAttackMove(Board board, Piece movePiece, int destination, Piece attackedPiece) {
                super(board, movePiece, destination, attackedPiece);
                this.attackedPiece = attackedPiece;
            }
            
            @Override
            public boolean equals(Object o) {
                return this == o || o instanceof MajorAttackMove && super.equals(o);
            } 
            
            /*
             * Prints out the move in standard convention
             * This is slightly different than MajorMove as it incorporates the tile 
             * being attacked and its coordinate
            */
            @Override
            public String toString() {
                return movedPiece.getPieceType() + 
                       "x" + 
                       BoardUtils.getPositionAtCoordinate(destination);
            }
            
        }
        
        //Placeholder class as this does not get used
        public static class AttackMove extends Move {
            
            //Needs the attacked piece for its coordinate
            Piece attackedPiece;

            public AttackMove(Board board, Piece movePiece, int destination, Piece attackedPiece) {
                super(board, movePiece,destination);
            }
            
            @Override
            public boolean isAttacked() {
                return true;
            }
            
            @Override
            public Piece getAttackedPiece() {
                return this.attackedPiece;
            }
            
            @Override
            public int hashCode() {
                return this.attackedPiece.hashCode() + super.hashCode();
            }
            
            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof Move)) {
                    return false;
                }
                AttackMove otherAttackMove = (AttackMove) o;
                return super.equals(otherAttackMove) &&
                       getAttackedPiece() == otherAttackMove.getAttackedPiece();
            }
        }
        
        /*
         * Takes the board, piece that calls this move (movePiece) and its destination
         * Only used for pawns that move one tile forward
        */
        public static class PawnMove extends Move {
            
            //public PawnMove (Board board, Pawn movePawn, int destination) {
            public PawnMove(Board board, Piece movePiece, int destination) {
                super(board, movePiece,destination);
            }
            
            ////Prints out the move in standard convention
            @Override
            public String toString() {
                return BoardUtils.getPositionAtCoordinate(destination); 
            }
            
            @Override
            public boolean equals(Object o) {
                return this == o || o instanceof PawnMove && super.equals(o);
            }
            
        }
        
        /*
         * This is not an actual "move", it only decorates the actual move as the pawn promotion
        */
        public static class PawnPromotion extends Move {
            
            Move decoratedMove;
            Pawn promotedPawn;
            
            public PawnPromotion(Move decoratedMove) {
                super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinate());
                this.decoratedMove = decoratedMove;
                //Casting as a pawn is a bit ugly but it was the best choice for this case
                this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
            }
            
            //Overrides the actual move's execute as the pawn promotion
            @Override 
            public Board execute() {
                
                    Board pawnMovedBoard = this.decoratedMove.execute();
                    Builder builder = new Builder();
                    
                    //Places all the pieces of the current player
                    for (Piece piece :  pawnMovedBoard.currentPlayer().getActivePieces()) {
                        if (!(promotedPawn == piece)) {
                            builder.setPiece(piece);
                        }
                    }
                    //Places all the pieces of the opponent
                    for (Piece piece : pawnMovedBoard.currentPlayer().getOpponent().getActivePieces()) {
                        builder.setPiece(piece);
                    }
                    
                    builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
                    //Switches move maker to the next player
                    builder.setMoveMaker(pawnMovedBoard.currentPlayer().getAlliance());
                    
                    return builder.build();
                    
            }
            
            @Override
            public boolean isAttacked() {
                return decoratedMove.isAttacked();
            }
            
            @Override 
            public Piece getAttackedPiece() {
                return decoratedMove.getAttackedPiece();
            }
            
            @Override
            public int hashCode() {
                return decoratedMove.hashCode() + (31 + promotedPawn.hashCode());
            }
            
            @Override
            public boolean equals(Object o) {
                return this == o || o instanceof PawnPromotion && super.equals(o);
            }
            
            //Prints out the move in standard convention
            //Note that the promotion is automatically chosen as queen (not uniform with the rules)
            @Override
            public String toString() {
                return decoratedMove.toString() + "=Q";
            }
            
        }
        
        /*
         * Takes the board, piece that calls this move (movePiece) and its destination
         * Only used for pawns that attack other pieces
        */
        public static class PawnAttackMove extends AttackMove {

            public PawnAttackMove(Board board, Piece movePiece, int destination, Piece attackedPiece) {
                super(board, movePiece, destination, attackedPiece);
                this.attackedPiece = attackedPiece;
            }  
            
            @Override
            public boolean equals(Object o) {
                return this == o || o instanceof PawnAttackMove && super.equals(o);
            }
            
            /*
             * //Prints out the move in standard convention
             * This is slightly different than PawnMove as it incorporates the tile 
             * being attacked and its coordinate
            */
            @Override
            public String toString() {
                return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0,1) 
                       + "x" + 
                       BoardUtils.getPositionAtCoordinate(this.destination);
            }
        }
        
        /*
         * Takes the board, piece that calls this move (movePiece) and its destination
         * Only used for the pawn's special attack, En Passant
         * More information here: https://en.wikipedia.org/wiki/En_passant
        */
        public static class PawnEnPassantMove extends PawnAttackMove {

            public PawnEnPassantMove(Board board, Piece movePiece, int destination, Piece attackedPiece) {
                super(board, movePiece, destination, attackedPiece);
            }  
            
            @Override
            public boolean equals(Object o) {
                return this == o || o instanceof PawnEnPassantMove && super.equals(o);
            }
            
            @Override
            public Board execute() {
                
                Builder builder = new Builder();
                
                //Places the player's pieces
                for (Piece piece : this.board.currentPlayer().getActivePieces()) {
                    if (!this.movedPiece.equals(piece)) {
                        builder.setPiece(piece);
                    }
                }
                //Places the opponent's pieces
                for (Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                    if (!piece.equals(this.getAttackedPiece())) {
                        builder.setPiece(piece);
                    }
                }
                
                builder.setPiece(this.movedPiece.movePiece(this));
                //Sets the movemaker as the next player
                builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
                
                return builder.build();
                
            }
        }
        
        /*
         * Takes the board, piece that calls this move (movePiece) and its destination
         * The pawn can move 2 spaces forward during its first move
         * More information here: https://en.wikipedia.org/wiki/Pawn_(chess)
        */
        public static class PawnJump extends Move {

            public PawnJump(Board board, Piece movePiece, int destination) {
                super(board, movePiece, destination);
            }  
            
            @Override
            public Board execute() {
                
                Builder builder = new Builder();
                
                //Places the player's pieces
                for (Piece piece: this.board.currentPlayer().getActivePieces()) {
                    if (!this.movedPiece.equals(piece)) {
                        builder.setPiece(piece);
                    }
                }
                //Places the opponent's pieces
                for (Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()) {
                    builder.setPiece(piece);
                }
                
                Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
                builder.setPiece(movedPawn);
                builder.setEnPassantPawn(movedPawn);
                //Sets the movemaker as the next player
                builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
                 
                return builder.build();
                
            }
            
            //Prints out the move in standard convention
            @Override
            public String toString() {
                return BoardUtils.getPositionAtCoordinate(destination); 
            }
        }
        
        //Experimental
        //TODO Finish Castling
        public static abstract class CastleMove extends Move {

            Rook castleRook;
            int rookCoord;
            int rookDest;
            
            public CastleMove(Board board, Piece movePiece, int destination, Rook castleRook, int rookCoord, int rookDest) {
                super(board, movePiece,destination);
                this.castleRook = castleRook;
                this.rookCoord = rookCoord;
                this.rookDest = rookDest;
            }
            
            public Rook getCastleRook() {
                return this.castleRook;
            }
            
            @Override
            public boolean isCastlingMove() {
                return true;
            }
            
            @Override
            public Board execute() {
                
                Builder builder = new Builder();
                for (Piece piece: this.board.currentPlayer().getActivePieces()) {
                    if (!this.movedPiece.equals(piece) && !(this.castleRook.equals(piece)));
                        builder.setPiece(piece);
                }
                for (Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()) {
                    builder.setPiece(piece);
                }
                builder.setPiece(this.movedPiece.movePiece(this));
                //todo look into firstmove on normal pieces
                builder.setPiece(new Rook(this.rookCoord, this.castleRook.getPieceAlliance()));
                builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
                return builder.build();
            }
            
            @Override
            public int hashCode() {
                int prime = 31;
                int result = super.hashCode();
                result = prime * result + castleRook.hashCode();
                result = prime * result + rookDest;
                return result;
            }
            
            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (!(o instanceof CastleMove)) {
                    return false;
                }
                CastleMove oCastleMove = (CastleMove) o;
                return super.equals(oCastleMove) && castleRook.equals(oCastleMove.getCastleRook());
            }
        }
        
        //TODO Finish castling
        public static class KingSideCastle extends CastleMove {

            public KingSideCastle(Board board, Piece movePiece, int destination, Rook castleRook, int rookCoord, int rookDest) {
                super(board, movePiece,destination, castleRook, rookCoord, rookDest);
            }
            
            @Override
            public boolean equals(Object o) {
                return this == o || o instanceof KingSideCastle && super.equals(o);
            }
            
            //Prints out the move in standard convention
            @Override
            public String toString() {
                return "0-0";
            }
        }
        
        //TODO Finish Castling
        public static class QueenSideCastle extends CastleMove {

            public QueenSideCastle(Board board, Piece movePiece, int destination, Rook castleRook, int rookCoord, int rookDest) {
                super(board, movePiece,destination, castleRook, rookCoord, rookDest);
            }
            
            @Override
            public boolean equals(Object o) {
                return this == o || o instanceof QueenSideCastle && super.equals(o);
            }
            
            //Prints out the move in standard convention
            @Override
            public String toString() {
                return "0-0-0";
            }
        }
        
        //Used when the move is invalid
        public static class InvalidMove extends Move {

            public InvalidMove() {
                super(null, 65);
            }
            
            @Override 
            public Board execute() {
                throw new RuntimeException("Cannot execute");
            }
            
            @Override
            public int getCurrentCoordinate() {
                return -1;
            }
        }
        
        //Experimental
        //TODO Finish Castling
        public boolean isAttacked() {
            return false;
        }
        
        //TODO Finish Castling
        public boolean isCastlingMove() {
            return false;
        }
        
        public Piece getAttackedPiece() {
            return attackedPiece;
        }
        
        public Board getBoard() {
            return board;
        }
        
        public static class MoveFactory { 
                
                //This should not be initiated
                private MoveFactory() {
                    throw new RuntimeException("Probably should not get here");
                }
                
                //Returns the move that player played
                public static Move createMove(Board board, int coordinate, int destination) {
                    for (Move move : board.getAllLegalMoves()) {
                        if (move.getCurrentCoordinate() == coordinate &&
                            move.getDestinationCoordinate() == destination) {
                            return move;
                        }
                    }
                    //This only happens if the move is illegal, hench the nullMove
                    return nullMove;
                }
        }
        
        public Piece getMovedPiece() {
            return this.movedPiece;
        }
    
}
