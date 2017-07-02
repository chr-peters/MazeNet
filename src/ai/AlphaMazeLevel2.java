package ai;

import generated.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class AlphaMazeLevel2 implements AI {

    // the ID of the player
    private int playerID;

    // the evaluation function
    private BoardEvaluator evaluator;

    // the AI used in the simulations
    private BoardEvaluator simulationEvaluator;

    // how many nodes are expanded?
    private int nodeCount;

    // how many simulations are executed per node
    private int simulationsPerNode;

    // how much noise is added to each evaluation of the heuristic
    private double noiseFactor;

    // the instance of the simulator
    private GameSimulator simulator;

    /**
     * @param playerID            The id of the player
     * @param evaluator           This is the heuristic that determines the nodeCount best boards to 
     *                            run the simulation on
     * @param simulationEvaluator This is the Evaluator that is used in the simulations
     * @param nodeCount           The amount of boards that are used as a basis for a simulation
     * @param simulationsPerNode  How many games are simulated on each node considered
     * @param noiseFactor         How much noise is added to each evaluation of the evaluators
     */
    public AlphaMazeLevel2(int playerID, BoardEvaluator evaluator, BoardEvaluator simulationEvaluator, 
			   int nodeCount, int simulationsPerNode, double noiseFactor) {
	this.playerID = playerID;
	this.evaluator = evaluator;
	this.simulationEvaluator = simulationEvaluator;
	this.nodeCount = nodeCount;
	this.simulationsPerNode = simulationsPerNode;
	this.noiseFactor = noiseFactor;
	this.simulator = new GameSimulator();
    }

    /**
     * This AI first determines the nodeCount best nodes of the next level of the search tree according
     * to the evaluator heuristic. In the next step, simulationsPerNode simulations are executed on each
     * of this nodes. The move that leads to the most promising outcome is returned.
     */
    @Override
    public MoveMessageType move(AwaitMoveMessageType gameState) {
	// the nodeCount best moves will be stored here
	List<Node> bestMoves = new ArrayList<>();
	// a node can enter the bestMoves list, if its value is above the lowerThreshold
	double lowerThreshold = Double.MIN_VALUE;

	// now iterate over all possible shifting positions
	List<Position> shiftPositions = Position.getPossiblePositionsForShiftcard();
	// remove the forbidden position
	PositionType forbidden = gameState.getBoard().getForbidden();
	if (forbidden!=null) {
	    // new Position due to the fact that PositionType does not override equals
	    shiftPositions.remove(new Position(forbidden));
	}
	for(Position shiftPosition: shiftPositions) {
	    // now iterate over all possible rotations of the shift card
	    for (Card shiftCard: new Card(gameState.getBoard().getShiftCard()).getPossibleRotations()) {
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

		// iterate over all reachable positions and evaluate the corresponding board
		for (Position curPosition: reachablePositions) {
		    curBoard.movePlayer(oldPos, curPosition, this.playerID);

		    // now evaluate the board
		    double curScore = this.evaluator.evaluate(curBoard, this.playerID, 
							      gameState.getTreasure()) + 
			noiseFactor * Math.random();

		    // now move the player back to where he was
		    curBoard.movePlayer(curPosition, oldPos, this.playerID);

		    // now test if the move is a candidate for the best moves
		    if (bestMoves.size() < nodeCount || curScore > lowerThreshold) {
			// insert the move in order
			int insertPos = 0;
			for (; insertPos < bestMoves.size(); insertPos++) {
			    if (curScore > bestMoves.get(insertPos).value) {
				break;
			    }
			}
			// create a copy of curMove to store it
			MoveMessageType moveToStore = new MoveMessageType();
			moveToStore.setShiftCard(new Card(shiftCard));
			moveToStore.setShiftPosition(new Position(shiftPosition));
			moveToStore.setNewPinPos(curPosition);
			// now store it
			bestMoves.add(insertPos, new Node(moveToStore, curScore));
			// if there are too many nodes in the list, remove the last one
			if (bestMoves.size() > nodeCount) {
			    bestMoves.remove(bestMoves.size()-1);
			}
			// update the lowerThreshold
			lowerThreshold = bestMoves.get(bestMoves.size()-1).value;
		    }
		}
	    }
	}
	
	// now carry out the simulations for each element of bestMoves
	// first, set the parameters
	int numberOfPlayers = gameState.getTreasuresToGo().size();
	// the id of the player whos turn is next
	int nextPlayer = Math.max((this.playerID + 1)%(numberOfPlayers+1), 1);
	// create a map with the players and the corresponding ais
	Map<Integer, AI> players = new HashMap<>();
	for(int id = 1; id<=numberOfPlayers; id++) {
	    players.put(id, new AlphaMazeLevel1(id, this.simulationEvaluator, this.noiseFactor));
	}
	for (Node curNode: bestMoves) {
	    // get the found treasures
	    List<TreasureType> foundTreasures = new ArrayList<>();
	    foundTreasures.addAll(gameState.getFoundTreasures());

	    // get the treasures to go for each player
	    Map<Integer, Integer> treasuresToGo = new HashMap<>();
	    for (TreasuresToGoType t: gameState.getTreasuresToGo()) {
		treasuresToGo.put(t.getPlayer(), t.getTreasures());
	    }

	    // get the current treasures for each player
	    Map<Integer, TreasureType> currentTreasures = new HashMap<>();
	    List<TreasureType> availableTreasures = GameSimulator.getAllTreasures();
	    availableTreasures.removeAll(foundTreasures);
	    Collections.shuffle(availableTreasures);
	    for(int id = 1; id<=numberOfPlayers; id++) {
		if (id == this.playerID) {
		    currentTreasures.put(id, gameState.getTreasure());
		} else if (treasuresToGo.get(id) == 1) {
		    // the player has to go back to his starting position
		    currentTreasures.put(id, TreasureType.fromValue("Start0"+id));
		} else {
		    // assign the player a random treasure to look for
		    currentTreasures.put(id, availableTreasures.get(0));
		    availableTreasures.remove(0);
		}
	    }

	    // TODO do the move to get the starting board of the simulation
	    // test if a player is on a treasure to update the parameters accordingly
	    
	}
	return bestMoves.get(0).move;
    }

    private static class Node {
	public MoveMessageType move;
	public double value;

	public Node (MoveMessageType move, double value) {
	    this.move = move;
	    this.value = value;
	}
    }

}
