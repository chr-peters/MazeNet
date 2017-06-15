package client;

public class MazeComException extends Exception {
    public MazeComException(String msg) {
	super(msg);
    }
    public MazeComException() {
	super("An unnamed MazeComException!");
    }
}
