package ai;

import generated.MoveMessageType;
import generated.AwaitMoveMessageType;

public interface AI {
	public MoveMessageType move(AwaitMoveMessageType gameState);
}
