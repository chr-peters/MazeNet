package ai;

import generated.*;

public class RandomAI implements AI {

    /**
     * Not yet a random AI, just move the card where the
     * last opponent just moved it
     */
    public MoveMessageType move(AwaitMoveMessageType gameState) {
	PositionType forbidden = gameState.getBoard().getForbidden();
	// test if it is the first move, if it is, just move position (1, 1)
	if (forbidden == null ) {
	    forbidden.setRow(1);
	    forbidden.setCol(1);
	}
	return null;
    }

}
