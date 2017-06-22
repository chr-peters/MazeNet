package ai;

import generated.*;

public interface BoardEvaluator {
    public double evaluate(Board board, int playerID, TreasureType treasure);
}
