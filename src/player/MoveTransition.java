package player;

import board.Board;
import board.Move;

/**
 * Description: Helper class for finishing a move
 * 
 * Date: Jan. 9, 2019
 * Author: Tony Jiang
 */

public class MoveTransition {
    
        private final Board transitionBoard;
        private final Move move;
        private final MoveStatus moveStatus;
        
        public MoveTransition (Board transitionBoard, Move move, MoveStatus moveStatus) {
            this.transitionBoard = transitionBoard;
            this.move = move;
            this.moveStatus = moveStatus;
        }

        public MoveStatus getMoveStatus() {
            return this.moveStatus;
        }
        
        public Board getTransitionBoard() {
            return transitionBoard;
        }
}