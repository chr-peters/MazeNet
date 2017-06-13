package client;

import generated.*;

import java.net.Socket;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class Client {

    // used for communication with the server
    private Socket socket;

    // the name of the intelligence
    private String name;

    // used to serialize xml
    private Marshaller marshaller;

    private UTFOutputStream outStream;
    
    public Client(String ip, int port, String name) throws IOException, JAXBException {
	this.socket = new Socket(ip, port);
	this.name = name;
	this.outStream = new UTFOutputStream(this.socket.getOutputStream());

	// create XML marshaller
	this.marshaller = JAXBContext.newInstance(MazeCom.class).createMarshaller();
	//this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    }
	
    public int login() throws IOException, JAXBException {
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

	System.out.println(message);

	// send the message to the server
	this.outStream.writeUTF8(message);
	this.outStream.flush();

	// TODO wait for reply
	
	return 0;
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
