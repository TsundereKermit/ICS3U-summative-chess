package pieces;

import board.Move;
import board.Board;
import java.util.Collection;

/**
 * Description: The parent class of all the pieces, contains getter methods and
 * piece behavior
 * 
 * Date: Jan. 9, 2019
 * Author: Tony Jiang
 */

public abstract class Piece {

        protected final PieceType pieceType;
        protected final int piecePosition;
        protected final Alliance pieceAlliance;
        protected final boolean isFirstMove;
        private final int hashCode;
        
        Piece (final int piecePosition, final Alliance pieceAlliance, final PieceType pieceType, final boolean isFirstMove) {
            this.pieceAlliance = pieceAlliance;
            this.piecePosition = piecePosition;
            this.isFirstMove = isFirstMove;
            this.pieceType = pieceType;
            this.hashCode = computeHashCode();
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Piece)) {
                return false;
            }
            Piece otherPiece = (Piece) o;
            return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() && 
                    pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove();
        }
        
        @Override
        public int hashCode() {
            return hashCode;
        }
        
        public int getPiecePosition() {
            return piecePosition;
        }
        public Alliance getPieceAlliance() {
            return this.pieceAlliance;
        }
        
        public int getPieceValue() {
            return this.pieceType.getPieceValue();
        }
        
        public boolean isFirstMove() {
            return this.isFirstMove;
        }
        
        public PieceType getPieceType() {
            return this.pieceType;
        }
        
        public abstract Collection<Move> calculateLegalMoves(final Board board);
        public abstract Piece movePiece(Move move);

        private int computeHashCode() {
            int result = pieceType.hashCode();
            result = 31 + result + pieceAlliance.hashCode();
            result = 31 * result + piecePosition;
            result = 31 * result + (isFirstMove ? 1 : 0);
            return result;
        }
        
        public enum PieceType {
            
            PAWN("P") {
                @Override
                public boolean isKing() {
                    return false;
                }

                @Override
                public boolean isRook() {
                    return false;
                }

                @Override
                public int getPieceValue() {
                    return 100;
                }
            },
            KNIGHT("N") {
                @Override
                public boolean isKing() {
                    return false;
                }

                @Override
                public boolean isRook() {
                    return false;
                }

                @Override
                public int getPieceValue() {
                    return 300;
                }
            },
            BISHOP("B") {
                @Override
                public boolean isKing() {
                    return false;
                }

                @Override
                public boolean isRook() {
                    return false;
                }

                @Override
                public int getPieceValue() {
                    return 300;
                }
            },
            ROOK("R") {
                @Override
                public boolean isKing() {
                    return false;
                }

                @Override
                public boolean isRook() {
                    return true;
                }

                @Override
                public int getPieceValue() {
                    return 500;
                }
            },
            QUEEN("Q") {
                @Override
                public boolean isKing() {
                    return false;
                }

                @Override
                public boolean isRook() {
                    return false;
                }

                @Override
                public int getPieceValue() {
                    return 1000;
                }
            },
            KING("K") {
                @Override
                public boolean isKing() {
                    return true;
                }

                @Override
                public boolean isRook() {
                    return false;
                }

                @Override
                public int getPieceValue() {
                    return 10000;
                }
            };
            
            private final String pieceName;
            
            PieceType (final String pieceName) {
                this.pieceName = pieceName;
            }
            
            @Override
            public String toString() {
                return this.pieceName;
            }

            public abstract boolean isKing();
            public abstract boolean isRook();
            public abstract int getPieceValue();
        }
}