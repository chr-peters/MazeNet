import client.Client;

import generated.*;
import ai.*;

import java.util.Scanner;

public class MazeNet {
    public static void main (String args []) {
	try {
	    //TODO gegebenenfalls ip und port anpassen
	    Client client = new Client("localhost", 5123, "Abel");

	    int id = client.login();
	    System.out.println("ID = " + id);

	    AI ai = new RandomAI(id);

	    // beginning of the game loop
	    while (!client.gameOver()) {
		
		AwaitMoveMessageType gameState = client.awaitMove();

		MoveMessageType move = ai.move(gameState);

		client.sendMove(move);

	    }
	    // close the connection
	    client.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
