package client;

import generated.*;

import java.net.Socket;

import java.io.IOException;
import java.io.StringReader;

import java.util.concurrent.LinkedBlockingDeque;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class MessageListener implements Runnable {

    private Socket socket;

    private LinkedBlockingDeque<MazeCom> messageQueue;

    private boolean running;

    private UTFInputStream inStream;

    private Unmarshaller unmarshaller;

    public MessageListener(Socket socket, LinkedBlockingDeque<MazeCom> messageQueue) throws IOException, JAXBException {
	this.socket = socket;
	this.messageQueue = messageQueue;
	this.inStream = new UTFInputStream(this.socket.getInputStream());

	this.unmarshaller = JAXBContext.newInstance(MazeCom.class).createUnmarshaller();
	this.running = false;
    }
    
    @Override
    public void run() {
	this.running = true;
	while( this.running ) {
	    try {
		// read message from the server
		String message = inStream.readUTF8();

		// create the MazeCom object from the message
		StringReader stringReader = new StringReader(message);
		MazeCom mazeCom = (MazeCom) this.unmarshaller.unmarshal(stringReader);
		// insert the received message into the queue
		messageQueue.offerLast(mazeCom);
	    } catch (JAXBException e) {
		e.printStackTrace();
	    } catch (IOException e) {
		this.stop();
	    }
	}
    }

    public void stop() {
	this.running = false;
    }
}
