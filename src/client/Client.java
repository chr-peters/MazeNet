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
    
    public Client(String ip, int port, String name) throws IOException, JAXBException {
	this.socket = new Socket(ip, port);
	this.name = name;
	this.outStream = new UTFOutputStream(this.socket.getOutputStream());

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

	// serialize the login message
	StringWriter writer = new StringWriter();
	this.marshaller.marshal(mazeCom, writer);
	String message = writer.toString();

	// send the message to the server
	this.outStream.writeUTF8(message);
	this.outStream.flush();

	// get the reply message
	MazeCom reply = messageQueue.take();

	// TODO check if the message type is correct
	
	return reply.getLoginReplyMessage().getNewID();
    }
	
    public AwaitMoveMessageType awaitMove() {
	return null;
    }
	
    public void sendMove(MoveMessageType move) {
		
    }
	
    public boolean gameOver() {
	return true;
    }
}
