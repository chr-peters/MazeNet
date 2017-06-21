package ai;

import generated.*;

import java.util.List;
import java.util.Map;

public class AbelBoard extends BoardType{

    private List<PositionType> playerPositions;
    private Map<TreasureType, PositionType> treasurePositions;

    public AbelBoard(BoardType board) {
	super();
	// TODO create the board
    }

    public PositionType getPlayerPosition(int playerID) {
	// the IDs start with 1, so subtract 1
	return playerPositions.get(playerID - 1);
    }

    public PositionType getTreasurePosition(TreasureType treasure) {
	return treasurePositions.get(treasure);
    }
}
