package ai;

import generated.*;

public interface BoardEvaluator {
    public double evaluate(AbelBoard board, int playerID, TreasureType treasure);
}
