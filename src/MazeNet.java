import client.Client;

import java.util.Scanner;

public class MazeNet {
    public static void main (String args []) {
	try {
	    //TODO gegebenenfalls ip und port anpassen
	    Client client = new Client("localhost", 5123, "Abel");

	    int id = client.login();
	    System.out.println("ID = " + id);

	    // wait to make it possible for the server to respond
	    Scanner sc = new Scanner(System.in);
	    sc.nextLine();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
