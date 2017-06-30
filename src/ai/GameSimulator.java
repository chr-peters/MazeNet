package ai;

import generated.*;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.Stack;

public class GameSimulator {

    public static void main(String args[]) {
	// create the ais
	Map<Integer, AI> players = new HashMap<>();
	players.put(1, new AlphaMazeLevel1(1, new ManhattanEvaluator(), 0.1));
	players.put(2, new AlphaMazeLevel1(2, new WallEvaluator(), 0.1));

	// simulate a game and print the winner
	GameSimulator simulator = new GameSimulator();
	int winner = simulator.simulate(players);
	System.out.println("The winner is "+winner+"!");
    }

    /**
     * This method simulates a game on a random board.
     *
     * @param players Each AI mapped to its playerID
     *
     * @return The ID of the winning player
     */
    public int simulate(Map<Integer, AI> players) {
	// first, give each player a random treasure to look for
	Map<Integer, TreasureType> currentTreasures = new HashMap<>();
	// a list of all the treasures
	List<TreasureType> allTreasures = GameSimulator.getAllTreasures();
	Collections.shuffle(allTreasures);
	for (int id: players.keySet()) {
	    // get the first (ramdom due to shuffle) treasure type and add it to the map
	    currentTreasures.put(id, allTreasures.get(0));
	    // remove the treasure from the available list
	    allTreasures.remove(0);
	}
	// each player still has to find the same amount of treasures
	Map<Integer, Integer> treasuresToGo = new HashMap<>();
	for (int id: players.keySet()) {
	    treasuresToGo.put(id, 24/players.size() + 1);
	}
	// simulate the game
	return simulate(players, new Board(), currentTreasures, Collections.emptyList(), treasuresToGo, 1);
    }

    /**
     * This method simulates a game with specified parameters.
     *
     * @param players          Each AI mapped to its playerID
     * @param board            The starting board for the simulation
     * @param currentTreasures The current treasure for each playerID
     * @param foundTreasures   The treasures that have already been found
     * @param nextMove         The ID of the player who's turn is next
     * @param treasuresToGo    How many treasures does each player still have to find?
     *
     * @return The ID of the winning player
     */
    public int simulate(Map<Integer, AI> players, Board board, Map<Integer, 
			TreasureType> currentTreasures, List<TreasureType> foundTreasures, 
			Map<Integer, Integer> treasuresToGo, int nextMove) {
	// first get all available treasures
	List<TreasureType> availableTreasures = getAllTreasures();
	availableTreasures.removeAll(foundTreasures);
	availableTreasures.removeAll(currentTreasures.values());

	// now create a stack for each player representing the treasures he still has to find
	Map<Integer, Stack<TreasureType>> playerStacks = new HashMap<>();
	
	// now fill the stack for each player with random treasures
	Collections.shuffle(availableTreasures);
	for(int id: players.keySet()) {
	    playerStacks.put(id, new Stack<>());
	    // at the bottom of the stack, place the starting position
	    playerStacks.get(id).push(TreasureType.fromValue("Start0"+id));
	    // now fill the stack
	    for (int i = 0; i<treasuresToGo.get(id)-2; i++) {
		playerStacks.get(id).push(availableTreasures.get(0));
		availableTreasures.remove(0);
	    }
	    // add the current treasure at the top
	    playerStacks.get(id).push(currentTreasures.get(id));
	}

	// the game loop
	while (true) {
	    // let each player make a move
	    for (int i=0; i<players.size(); i++) {
		int currentID = Math.max((nextMove + i) % (players.size()+1), 1);
		
		// create the AwaitMoveMessageType, use copies for safety reasons
		AwaitMoveMessageType msg = new AwaitMoveMessageType();
		msg.setBoard(new Board(board));
		List<TreasuresToGoType> tmpTGT = new ArrayList<>();
		for (int id: players.keySet()) {
		    TreasuresToGoType tmp = new TreasuresToGoType();
		    tmp.setPlayer(id);
		    tmp.setTreasures(playerStacks.get(id).size());
		    tmpTGT.add(tmp);
		}
		// msg.setTreasuresToGo(tmpTGT);
		// TODO extend AwaitMoveMessageType to add treasuresToGo
	    }
	}
    }

    public static List<TreasureType> getAllTreasures() {
	List<TreasureType> allTreasures = new ArrayList<TreasureType>();
	allTreasures.add(TreasureType.SYM_01);
        allTreasures.add(TreasureType.SYM_02);
        allTreasures.add(TreasureType.SYM_03);
        allTreasures.add(TreasureType.SYM_04);
        allTreasures.add(TreasureType.SYM_05);
        allTreasures.add(TreasureType.SYM_06);
        allTreasures.add(TreasureType.SYM_07);
        allTreasures.add(TreasureType.SYM_08);
        allTreasures.add(TreasureType.SYM_09);
        allTreasures.add(TreasureType.SYM_10);
        allTreasures.add(TreasureType.SYM_11);
        allTreasures.add(TreasureType.SYM_12);
        allTreasures.add(TreasureType.SYM_13);
        allTreasures.add(TreasureType.SYM_14);
        allTreasures.add(TreasureType.SYM_15);
        allTreasures.add(TreasureType.SYM_16);
        allTreasures.add(TreasureType.SYM_17);
        allTreasures.add(TreasureType.SYM_18);
        allTreasures.add(TreasureType.SYM_19);
        allTreasures.add(TreasureType.SYM_20);
        allTreasures.add(TreasureType.SYM_21);
        allTreasures.add(TreasureType.SYM_22);
        allTreasures.add(TreasureType.SYM_23);
        allTreasures.add(TreasureType.SYM_24);
	return allTreasures;
    }
    
}
