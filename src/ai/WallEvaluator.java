package ai;

import generated.*;

import java.util.ArrayList;
import java.util.List;

public class WallEvaluator implements BoardEvaluator {
    private int minWalls;

    /**
     * count walls between player and treasure
     */
    private void number_obstacles(Board board, Position player, PositionType treasure, List<Position> path, int walls) {
	// if treasure is reached, check number of walls
	if(player.equals(treasure)) {
	    if(walls<this.minWalls) {
		this.minWalls = walls;
	    }
	    return;
	}
	path.add(new Position(player));
	// test top way
	Position next_pos = new Position(player.getRow()-1>=0 ? player.getRow()-1 : 6, player.getCol());
	if(!path.contains(next_pos)) {
	    int tmp = 0;
	    if(!board.getCard(player.getRow(), player.getCol()).getOpenings().isTop()) {
		tmp++;
	    }
	    if(!board.getCard(next_pos.getRow(), next_pos.getCol()).getOpenings().isBottom()) {
		tmp++;
	    }
	    if(walls+tmp<this.minWalls) {
		number_obstacles(board, next_pos, treasure, path, walls+tmp);
	    }
	}
	// test right way
	next_pos = new Position(player.getRow(), player.getCol()+1<=6 ? player.getCol()+1 : 0);
	if(!path.contains(next_pos)) {
	    int tmp = 0;
	    if(!board.getCard(player.getRow(), player.getCol()).getOpenings().isRight()) {
		tmp++;
	    }
	    if(!board.getCard(next_pos.getRow(), next_pos.getCol()).getOpenings().isLeft()) {
		tmp++;
	    }
	    if(walls+tmp<this.minWalls) {
		number_obstacles(board, next_pos, treasure, path, walls+tmp);
	    }
	}
	// test bottom way
	next_pos = new Position(player.getRow()+1<=6 ? player.getRow()+1 : 0, player.getCol());
	if(!path.contains(next_pos)) {
	    int tmp = 0;
	    if(!board.getCard(player.getRow(), player.getCol()).getOpenings().isBottom()) {
		tmp++;
	    }
	    if(!board.getCard(next_pos.getRow(), next_pos.getCol()).getOpenings().isTop()) {
		tmp++;
	    }
	    if(walls+tmp<this.minWalls) {
		number_obstacles(board, next_pos, treasure, path, walls+tmp);
	    }
	}
	// test left way
	next_pos = new Position(player.getRow(), player.getCol()-1>=0 ? player.getCol()-1 : 6);
	if(!path.contains(next_pos)) {
	    int tmp = 0;
	    if(!board.getCard(player.getRow(), player.getCol()).getOpenings().isLeft()) {
		tmp++;
	    }
	    if(!board.getCard(next_pos.getRow(), next_pos.getCol()).getOpenings().isRight()) {
		tmp++;
	    }
	    if(walls+tmp<this.minWalls) {
		number_obstacles(board, next_pos, treasure, path, walls+tmp);
	    }
	}
	path.remove(player);
    }

    /**
     * Assigns each board a score based on walls between player and treasure and distance between them
     */
    public double evaluate(Board board, int playerID, TreasureType treasure) {
	this.minWalls = Integer.MAX_VALUE;
	Position player_pos = board.findPlayer(playerID);
	PositionType treasure_pos = board.findTreasure(treasure);
	if(player_pos.equals(treasure_pos)) {
	    return 14;
	} else if(treasure_pos==null) {
	    return 0;
	} else {
	    number_obstacles(board, player_pos, treasure_pos, new ArrayList<>(), 0);
	    return 13-0.5*this.minWalls;
	}
    }

}
