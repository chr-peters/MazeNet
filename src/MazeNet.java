import client.Client;

import generated.*;
import ai.*;

import java.util.Scanner;

public class MazeNet {
    private static String host = "localhost";
    private static String chosen_ai = "manhattan";

    private static void parseCommandLine(String[] args) {
	if(args.length==1) {
	    if(args[0].toLowerCase().equals("localhost") || args[0].contains(".")) {
		host = args[0].toLowerCase();
	    } else {
		chosen_ai = args[0].toLowerCase();
	    }
	} else if(args.length==2) {
	    host = args[0].toLowerCase();
	    chosen_ai = args[1].toLowerCase();
	}
    }

    public static void main (String args []) {
	try {
	    parseCommandLine(args);

	    Client client = new Client(host, 5124, "Abel");

	    int id = client.login();
	    System.out.println("Started the game with ID =  " + id);

	    // choose the correct AI
	    AI ai;
	    switch(chosen_ai) {
	    case "manhattan": ai = new AlphaMazeLevel2(id, new ManhattanEvaluator(), new ManhattanEvaluator(), 10, 300,  0.1); break;
	    case "random": ai = new AlphaMazeLevel2(id, new RandomEvaluator(), new RandomEvaluator(), 10, 300,  0.1); break;
	    case "wall": ai = new AlphaMazeLevel2(id, new WallEvaluator(), new WallEvaluator(), 10, 300,  0.1); break;
	    default: ai = new AlphaMazeLevel2(id, new ManhattanEvaluator(), new ManhattanEvaluator(), 10, 300,  0.1); break;
	    }

	    // beginning of the game loop
	    while (!client.gameOver()) {
		
		AwaitMoveMessageType gameState = client.awaitMove();

		double t0 = System.currentTimeMillis();
		MoveMessageType move = ai.move(gameState);
		double dt = System.currentTimeMillis() - t0;

		System.out.println("Took me "+dt/1000.+" seconds.");

		client.sendMove(move);

	    }
	    // close the connection
	    client.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
