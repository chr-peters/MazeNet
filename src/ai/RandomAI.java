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
	PositionType shiftPosition = gameState.getBoard().getForbidden();
	// test if it is the first move, if it is, just move position (1, 0)
	if (shiftPosition == null ) {
	    shiftPosition = new PositionType();
	    shiftPosition.setRow(0);
	    shiftPosition.setCol(1);
	} else {
	    if (shiftPosition.getRow() == 0) {
		shiftPosition.setRow(6);
	    } else if (shiftPosition.getRow() == 6) {
		shiftPosition.setRow(0);
	    } else if (shiftPosition.getCol() == 0) {
		shiftPosition.setCol(6);
	    } else {
		shiftPosition.setCol(0);
	    }
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
	move.setShiftPosition(shiftPosition);
	move.setNewPinPos(pinPosition);
	move.setShiftCard(gameState.getBoard().getShiftCard());

	return move;
    }

}
