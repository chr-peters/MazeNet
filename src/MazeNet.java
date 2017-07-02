import client.Client;

import generated.*;
import ai.*;

import java.util.Scanner;

public class MazeNet {
    public static void main (String args []) {
	try {
	    //TODO gegebenenfalls ip und port anpassen
	    Client client = new Client("localhost", 5123, "Alpha Maze");

	    int id = client.login();
	    System.out.println("Started the game with ID =  " + id);

	    // 0.1 is the optional noiseFactor
	    AI ai = new AlphaMazeLevel2(id, new ManhattanEvaluator(), new ManhattanEvaluator(), 10, 300,  0.1);

	    // beginning of the game loop
	    while (!client.gameOver()) {
		
		AwaitMoveMessageType gameState = client.awaitMove();

		double t0 = System.currentTimeMillis();
		MoveMessageType move = ai.move(gameState);
		double dt = System.currentTimeMillis()-t0;
		System.out.println("Took me "+dt/1000+" seconds.");

		client.sendMove(move);

	    }
	    // close the connection
	    client.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
