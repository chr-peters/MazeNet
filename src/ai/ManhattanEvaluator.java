package ai;

import generated.*;

public class ManhattanEvaluator implements BoardEvaluator {
  /**
  * Calculates the manhattan distance between the player and the desired treasure
  */
  private int distance(Position player, PositionType treasure) {
	   return Math.abs(player.getRow()-treasure.getRow())+Math.abs(player.getCol()-treasure.getCol());
  }

  /**
  * Assigns each board a score based the manhattan distance between the player
  * and the treasure (1-13)
  */
    public double evaluate(Board board, int playerID, TreasureType treasure) {
      Position player_pos = board.findPlayer(playerID);
	    PositionType treasure_pos = board.findTreasure(treasure);
	    if(treasure_pos==null) {
	       return 0;
	    } else {
	       return 13-distance(player_pos, treasure_pos);
	    }
    }
}
