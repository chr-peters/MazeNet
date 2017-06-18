package ai;

import generated.*;

public class RandomAI implements AI {

    // the id of the player
    int id;

    public RandomAI(int id) {
	this.id = id;
    }

    /**
     * Not yet a random AI, just move the card where the
     * last opponent just moved it
     */
    public MoveMessageType move(AwaitMoveMessageType gameState) {
	PositionType forbidden = gameState.getBoard().getForbidden();
	// test if it is the first move, if it is, just move position (1, 0)
	if (forbidden == null ) {
	    forbidden = new PositionType();
	    forbidden.setRow(0);
	    forbidden.setCol(1);
	}

	PositionType pinPosition = new PositionType();

	// find out the current pin position
	for (int row = 0; row < 7; row++) {
	    for (int col = 0; col < 7; col++) {
		if (gameState.getBoard().getRow().get(row).getCol().get(col).getPin().getPlayerID().contains(this.id)) {
		    pinPosition.setRow(row);
		    pinPosition.setCol(col);
		    break;
		}
	    }
	}

	// create the move
	MoveMessageType move = new MoveMessageType();
	move.setShiftPosition(forbidden);
	move.setNewPinPos(pinPosition);
	move.setShiftCard(gameState.getBoard().getShiftCard());

	return move;
    }

}
