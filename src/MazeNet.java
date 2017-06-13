import client.Client;

public class MazeNet {
    public static void main (String args []) {
	//TODO gegebenenfalls ip und port anpassen
	Client client = new Client("localhost", 5123, "Abel");

	int id = client.login();
	System.out.println("ID = " + id);
    }
}
