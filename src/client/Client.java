package client;

import generated.*;

import java.net.Socket;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import java.util.concurrent.LinkedBlockingDeque;

public class Client {

    // used for communication with the server
    private Socket socket;

    // the name of the intelligence
    private String name;

    // used to serialize xml
    private Marshaller marshaller;

    private UTFOutputStream outStream;

    private LinkedBlockingDeque<MazeCom> messageQueue;

    private MessageListener messageListener;

    // flag to determine if the game is over
    private boolean isGameOver;
    
    public Client(String ip, int port, String name) throws IOException, JAXBException {
	this.socket = new Socket(ip, port);
	this.name = name;
	this.outStream = new UTFOutputStream(this.socket.getOutputStream());
	this.isGameOver = false;

	// create XML marshaller
	this.marshaller = JAXBContext.newInstance(MazeCom.class).createMarshaller();
	//this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	// create the message queue
	this.messageQueue = new LinkedBlockingDeque<>();

	// create the messageListener
	this.messageListener = new MessageListener(this.socket, this.messageQueue);
	new Thread(this.messageListener).start();
    }
	
    public int login() throws IOException, JAXBException, InterruptedException {
	MazeCom mazeCom = new MazeCom();
	mazeCom.setMcType(MazeComType.LOGIN);
	
	// create the login message
	LoginMessageType loginMessage = new LoginMessageType();
	loginMessage.setName(this.name);
	mazeCom.setLoginMessage(loginMessage);

	this.sendMazeCom(mazeCom);

	// get the reply message
	MazeCom reply = messageQueue.take();

	// TODO check if the message type is correct
	
	return reply.getLoginReplyMessage().getNewID();
    }
	
    public AwaitMoveMessageType awaitMove() throws InterruptedException {
	// take the first element of the messageQueue
	MazeCom message = messageQueue.take();

	// TODO check if the type is correct

	return message.getAwaitMoveMessage();
    }
	
    public void sendMove(MoveMessageType move) throws InterruptedException, IOException, JAXBException {
	MazeCom mazeCom = new MazeCom();
	mazeCom.setMcType(MazeComType.AWAITMOVE);
	mazeCom.setMoveMessage(move);

	// send the message to the server
	this.sendMazeCom(mazeCom);

	// get the reply message
	MazeCom reply = messageQueue.take();

	// TODO check if the move was correct

	// get the next message and see if it is a win message
	reply = messageQueue.take();
	if( reply.getMcType() == MazeComType.WIN ){
	    this.isGameOver = true;
	} else {
	    // put the message back into the queue
	    messageQueue.offerFirst(reply);
	}
	
    }
	
    public boolean gameOver() {
	return this.isGameOver;
    }

    /**
     * close the socket and stop the messageListener
     */
    public void close() throws IOException {
	this.messageListener.stop();
	this.socket.close();
    }

    /**
     * serializes and sends a MazeCom to the server
     */
    private void sendMazeCom(MazeCom mazeCom) throws IOException, JAXBException {
	StringWriter writer = new StringWriter();
	this.marshaller.marshal(mazeCom, writer);
	String message = writer.toString();
	
	// send the message to the server
	this.outStream.writeUTF8(message);
	this.outStream.flush();
    }
}
