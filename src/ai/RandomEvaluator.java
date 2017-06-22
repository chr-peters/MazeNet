package ai;

import generated.*;

public class RandomEvaluator implements BoardEvaluator {

    /**
     * Assigns each board a random score
     */
    public double evaluate(Board board, int playerID, TreasureType treasure) {
	return Math.random();
    }

}
