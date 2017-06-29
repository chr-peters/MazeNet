package ai;

import generated.*;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

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
	// simulate the game
	return simulate(players, new Board(), currentTreasures);
    }

    /**
     * This method simulates a game with specified parameters.
     *
     * @param players          Each AI mapped to its playerID
     * @param board            The starting board for the simulation
     * @param currentTreasures The current treasure for each playerID
     *
     * @return The ID of the winning player
     */
    public int simulate(Map<Integer, AI> players, Board board, Map<Integer, TreasureType> currentTreasures) {
	return 0;
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
