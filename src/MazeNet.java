import client.Client;

import generated.*;
import ai.*;

import java.util.Scanner;
import java.util.Arrays;

import java.util.concurrent.atomic.AtomicBoolean;

public class MazeNet {
    private static String host = "localhost";
    private static String chosen_ai = "combined";

    // used when the real AI has not enough time left
    private static AI fallbackAI;

    // true, if the real AI has a move available
    private static AtomicBoolean moveAvailable = new AtomicBoolean(false);

    // the calculated move of the main AI
    private static MoveMessageType calculatedMove;

    // the number of simulations for each core for each board to evaluate
    private static int simulationsPerCore = 70;

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

	    Client client = new Client(host, 5124, "Das_Abelsche_Softwarehaus");

	    int id = client.login();
	    System.out.println("Started the game with ID =  " + id);

	    // create the fallback AI
	    fallbackAI = new AlphaMazeLevel1(id, new CombinedEvaluator(Arrays.asList(new Double [] {0.8, 0.2}), Arrays.asList(new BoardEvaluator[] {
			    new ManhattanEvaluator(), new WallEvaluator()})), 0.1);

	    // get the number of cores
	    int numberOfCores = Runtime.getRuntime().availableProcessors();

	    // choose the correct AI
	    AI ai;
	    switch(chosen_ai) {
	    case "manhattan": ai = new AlphaMazeLevel2(id, new ManhattanEvaluator(), new ManhattanEvaluator(), 20, numberOfCores * simulationsPerCore, 15,  0.1); break;
	    case "random": ai = new AlphaMazeLevel2(id, new RandomEvaluator(), new RandomEvaluator(), 20, numberOfCores * simulationsPerCore, 15,  0.1); break;
	    case "wall": ai = new AlphaMazeLevel2(id, new WallEvaluator(), new WallEvaluator(), 20, numberOfCores * simulationsPerCore, 15, 0.1); break;
	    case "combined": ai = new AlphaMazeLevel2(id, new CombinedEvaluator(Arrays.asList(new Double [] {0.8, 0.2}), Arrays.asList(new BoardEvaluator[] {new ManhattanEvaluator(), new WallEvaluator()})), new ManhattanEvaluator(), 20, numberOfCores * simulationsPerCore, 15, 0.1); break;
	    default: ai = new AlphaMazeLevel2(id, new CombinedEvaluator(Arrays.asList(new Double [] {0.8, 0.2}), Arrays.asList(new BoardEvaluator[] {new ManhattanEvaluator(), new WallEvaluator()})), new ManhattanEvaluator(), 20, numberOfCores * simulationsPerCore, 15, 0.1); break;
	    }

	    // beginning of the game loop
	    while (!client.gameOver()) {

		MazeNet.moveAvailable.set(false);
		
		AwaitMoveMessageType gameState = client.awaitMove();

		Thread aiThread = new Thread(new Runnable() {
			@Override
			public void run() {
			    MoveMessageType move = ai.move(gameState);
			    MazeNet.calculatedMove = move;
			    MazeNet.moveAvailable.set(true);
			}
		    });

		// used for time measurement
		double t0 = System.currentTimeMillis();

		// now start computing the move and wait a maximum of 18 seconds
		aiThread.start();
		aiThread.join(18000);

		// contains the move that is to be made
		MoveMessageType curMove;
		
		// if the AI calculated a move, use it, otherwise use the fallback ai
		if (MazeNet.moveAvailable.get()) {
		    System.out.println("Made move using the main AI.");
		    curMove = MazeNet.calculatedMove;
		} else {
		    System.out.println("Made move using the fallback AI.");
		    curMove = fallbackAI.move(gameState);
		}

		double dt = System.currentTimeMillis() - t0;

		System.out.println("Took me "+dt/1000.+" seconds.");

		client.sendMove(curMove);

		// now end the aiThread
		try {
		    if (aiThread.isAlive()) {
			aiThread.stop(); // yolo
		    }
		} catch (Exception e) {
		    // well, something went wrong... but who cares
		}

	    }
	    // close the connection
	    client.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
