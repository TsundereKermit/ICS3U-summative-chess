package player;

/**
 * Description: Helper class for determining how a move ends
 * 
 * Date: Jan. 9, 2019
 * Author: Tony Jiang
 */

public enum MoveStatus {
    DONE {
        @Override
        public boolean isDone() {
            return true;
        }
    },
    ILLEGAL_MOVE {
        @Override
        public boolean isDone() {
            return false;
        }
    },
    LEAVES_PLAYER_IN_CHECK {
        @Override
        public boolean isDone() {
            return false;
        } 
    };
    public abstract boolean isDone();
}

