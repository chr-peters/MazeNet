package client;

import generated.*;

import java.net.Socket;

import java.io.IOException;
import java.io.StringWriter;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import java.util.concurrent.LinkedBlockingDeque;

public class Client {

    // used for communication with the server
    private SSLSocket socket;

    // the name of the intelligence
    private String name;

    // used to serialize xml
    private Marshaller marshaller;

    private UTFOutputStream outStream;

    private LinkedBlockingDeque<MazeCom> messageQueue;

    private MessageListener messageListener;

    // flag to determine if the game is over
    private boolean isGameOver;

    // id of the client, necessary to send messages to the server
    private int id;
    
    public Client(String ip, int port, String name) throws IOException, JAXBException {
    // setup truststore to verify server-certificate
    System.setProperty("javax.net.ssl.trustStore", "./data/ssl/truststore.jks");
    System.setProperty("javax.net.ssl.trustStorePassword", "transformers");
	
    // create sslSocket
    System.out.println("Baue Verbindung auf mit " + ip + " Port: " + port);
    this.socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(ip, port);
    
    //this.socket = new Socket(ip, port);
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
	
    public int login() throws IOException, JAXBException, InterruptedException, MazeComException {
	MazeCom mazeCom = new MazeCom();
	mazeCom.setMcType(MazeComType.LOGIN);
	
	// create the login message
	LoginMessageType loginMessage = new LoginMessageType();
	loginMessage.setName(this.name);
	mazeCom.setLoginMessage(loginMessage);

	this.sendMazeCom(mazeCom);

	// get the reply message
	MazeCom reply = messageQueue.take();

	if ( reply.getMcType() == MazeComType.LOGINREPLY ) {
	    // everything went well
	    this.id = reply.getLoginReplyMessage().getNewID();
	    return this.id;
	} else if ( reply.getMcType() == MazeComType.ACCEPT ) {
	    // something went wrong, throw an exception containing information about the error
	    throw new MazeComException("The login was not accepted: ErrorCode = " +
				       reply.getAcceptMessage().getErrorCode().value());
	} else if ( reply.getMcType() == MazeComType.DISCONNECT) {
	    // throw an exception containing information about the disconnect

	    // TODO will the player be able to reconnect? If not, set isGameOver = false

	    throw new MazeComException("Received DISCONNECT message: Name = "+
				       reply.getDisconnectMessage().getName()+", ErrorCode = " + 
				       reply.getDisconnectMessage().getErrorCode().value());
	} else {
	    // received unexpected message type
	    // just put the message back into the queue and throw an exception
	    this.messageQueue.offerFirst(reply);
	    throw new MazeComException("Unexpected reply type! Expected LOGINREPLY, ACCEPT or DISCONNECT, got "+
				       reply.getMcType().value());
	}
    }
	
    public AwaitMoveMessageType awaitMove() throws InterruptedException, MazeComException {
	// take the first element of the messageQueue
	MazeCom message = messageQueue.take();

	// check if it is an await move message
	if ( message.getMcType() != MazeComType.AWAITMOVE ) {
	    // unexpected reply, just put the message back into the queue ant throw an exception
	    this.messageQueue.offerFirst(message);
	    throw new MazeComException("Unexpected reply type! Expected AWAITMOVE, got "+
				       message.getMcType().value());
	}

	return message.getAwaitMoveMessage();
    }
	
    public void sendMove(MoveMessageType move) throws InterruptedException, IOException, 
						      JAXBException, MazeComException {
	MazeCom mazeCom = new MazeCom();
	mazeCom.setId(this.id);
	mazeCom.setMcType(MazeComType.MOVE);
	mazeCom.setMoveMessage(move);

	// send the message to the server
	this.sendMazeCom(mazeCom);

	// get the reply message
	MazeCom reply = messageQueue.take();

	// error handling
	if ( reply.getMcType() == MazeComType.ACCEPT && !reply.getAcceptMessage().isAccept() ) {
	    // the move was not accepted
	    // throw an exception containing information about the error
	    throw new MazeComException("The move was not accepted: ErrorCode = " +
				       reply.getAcceptMessage().getErrorCode().value());
	} else if (reply.getMcType() == MazeComType.DISCONNECT ) {
	    // throw an exception containing the information about the disconnect

	    // TODO will the player be able to reconnect? If not, set isGameOver = false

	    throw new MazeComException("Received DISCONNECT message: Name = "+
				       reply.getDisconnectMessage().getName()+", ErrorCode = " + 
				       reply.getDisconnectMessage().getErrorCode().value());
	} else if ( reply.getMcType() != MazeComType.ACCEPT && reply.getMcType() != MazeComType.DISCONNECT ){
	    // received unexpected message type
	    // just put the message back into the queue and throw an exception
	    this.messageQueue.offerFirst(reply);
	    throw new MazeComException("Unexpected reply type! Expected ACCEPT or DISCONNECT, got "+
				       reply.getMcType().value());
	}

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
