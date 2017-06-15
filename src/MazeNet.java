import client.Client;

import generated.*;

import java.util.Scanner;

public class MazeNet {
    public static void main (String args []) {
	try {
	    //TODO gegebenenfalls ip und port anpassen
	    Client client = new Client("localhost", 5123, "Abel");

	    int id = client.login();
	    System.out.println("ID = " + id);

	    // TODO create the AI

	    // beginning of the game loop
	    while (!client.gameOver()) {
		
		AwaitMoveMessageType gameState = client.awaitMove();

		// TODO feed the gameState into the AI
		// TODO send move to the client
		// client.sendMove(ai.move(gameState));

	    }
	    // close the connection
	    client.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
