package ai;

import generated.*;

public class WallEvaluator implements BoardEvaluator {

    /**
     * count walls between player and treasure
     */
    private int number_obstacles(Board board, Position player, PositionType treasure) {
	if(treasure==null) {
	    return 100;
	}
	if(player.equals(treasure)) {
	    return -100;
	}
	int wall1 = 0;
	int[] cur_pos = {player.getRow(), player.getCol()};
	while(cur_pos[0]!=treasure.getRow()) {
	    if(cur_pos[0]-treasure.getRow()>0) {
		if(!board.getCard(cur_pos[0]-1, cur_pos[1]).getOpenings().isTop()) {
		    wall1++;
		}
		cur_pos[0]--;
	    } else {
		if(!board.getCard(cur_pos[0]+1, cur_pos[1]).getOpenings().isBottom()) {
		    wall1++;
		}
		cur_pos[0]++;
	    }
	}
	while(cur_pos[1]!=treasure.getCol()) {
	    if(cur_pos[1]-treasure.getCol()>0) {
		if(!board.getCard(cur_pos[0], cur_pos[1]-1).getOpenings().isLeft()) {
		    wall1++;
		}
		cur_pos[1]--;
	    } else {
		if(!board.getCard(cur_pos[0], cur_pos[1]+1).getOpenings().isRight()) {
		    wall1++;
		}
		cur_pos[1]++;
	    }
	}
	int wall2 = 0;
	cur_pos[0] = player.getRow();
	cur_pos[1] = player.getCol();
	while(cur_pos[1]!=treasure.getCol()) {
	    if(cur_pos[1]-treasure.getCol()>0) {
		if(!board.getCard(cur_pos[0], cur_pos[1]-1).getOpenings().isLeft()) {
		    wall2++;
		}
		cur_pos[1]--;
	    } else {
		if(!board.getCard(cur_pos[0], cur_pos[1]+1).getOpenings().isRight()) {
		    wall2++;
		}
		cur_pos[1]++;
	    }
	}
	while(cur_pos[0]!=treasure.getRow()) {
	    if(cur_pos[0]-treasure.getRow()>0) {
		if(!board.getCard(cur_pos[0]-1, cur_pos[1]).getOpenings().isTop()) {
		    wall2++;
		}
		cur_pos[0]--;
	    } else {
		if(!board.getCard(cur_pos[0]+1, cur_pos[1]).getOpenings().isBottom()) {
		    wall2++;
		}
		cur_pos[0]++;
	    }
	}
	return Math.min(wall1, wall2);
    }

    /**
     * Assigns each board a score based on walls between player and treasure
     */
    public double evaluate(Board board, int playerID, TreasureType treasure) {
	return 100-number_obstacles(board, board.findPlayer(playerID), board.findTreasure(treasure));
    }

}
