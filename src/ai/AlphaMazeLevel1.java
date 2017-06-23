package ai;

import generated.*;

import java.util.List;

public class AlphaMazeLevel1 implements AI {

    // the evaluation function used in this ai
    private BoardEvaluator evaluator;

    // the ID of the player
    private int playerID;

    public AlphaMazeLevel1(int playerID, BoardEvaluator evaluator) {
	this.evaluator = evaluator;
	this.playerID = playerID;
    }

    /**
     * This AI generates the search tree down to the first level.
     * This means that all possible moves of the current turn are
     * generated and evaluated. The best move is returned.
     */
    @Override
    public MoveMessageType move(AwaitMoveMessageType gameState) {
	// initialize with an empty move
	MoveMessageType currentBestMove = new MoveMessageType();
	double currentBestScore = Double.MIN_VALUE;
	// iterate over all possible shifting positions
	for(int row = 0; row < 7; row++) {
	    for (int col = 0; col < 7; col++) {
		// test if it is a valid shifting position
		if ((row == 0 || row == 6) && col%2 == 1 || (col == 0 || col == 6) && row%2 == 1) {
		    // test if the move is allowed
		    PositionType forbidden = gameState.getBoard().getForbidden();
		    if (forbidden != null && forbidden.getRow() == row && forbidden.getCol() == col) {
			// if the move is not allowed, continue with the next one
			continue;
		    }

		    // create PositionType for the shift
		    PositionType shiftPosition = new PositionType();
		    shiftPosition.setRow(row);
		    shiftPosition.setCol(col);

		    // iterate over all possible orientations of the shift card
		    for(Card shiftCard: new Card(gameState.getBoard().getShiftCard()).getPossibleRotations()){

			// create a temporary moveMessage
			MoveMessageType curMove = new MoveMessageType();
			curMove.setShiftCard(shiftCard);
			curMove.setShiftPosition(shiftPosition);

			// create a temporary board
			Board curBoard = new Board(gameState.getBoard());

			// to determine all reachable positions, first apply the shift only
			curBoard.proceedShift(curMove);

			// get the old position of the player for later calculations
			PositionType oldPos = curBoard.findPlayer(this.playerID);

			// now get all the reachable positions
			List<Position> reachablePositions = curBoard.getAllReachablePositions(oldPos);
		    
			// iterate over each reachable position and evaluate the corresponding board
			for (Position curPosition: reachablePositions) {
			    curBoard.movePlayer(oldPos, curPosition, this.playerID);
			
			    // now evaluate the board
			    double curScore = this.evaluator.evaluate(curBoard, this.playerID, gameState.getTreasure());
			
			    // now move the player back to where he was
			    curBoard.movePlayer(curPosition, oldPos, this.playerID);
			
			    // if a new highscore was found, update the current best move
			    if (curScore > currentBestScore) {
				curMove.setNewPinPos(curPosition);
				currentBestMove = curMove;
				currentBestScore = curScore;
			    }
			}
		    }
		}
	    }
	}
	
	// return the best move
	return currentBestMove;
    }

}
