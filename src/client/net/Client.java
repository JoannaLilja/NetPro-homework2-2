package client.net;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import client.controller.Controller;
import shared.GameData;
 
public class Client implements Runnable
{
	
	private SocketChannel socket;
	private Selector selector;
	private SelectionKey sKey;
	private Controller contr;
	private final Queue<ByteBuffer> messageQueue = new ArrayDeque<>();

	private int id;
	
	public Client(int id)
	{
		this.id = id;
	}
 
	public void run()
	{
		
		contr = new Controller(this, id);
		
		 try {
	            socket = SocketChannel.open();
	            socket.configureBlocking(false);
	            socket.connect(new InetSocketAddress("localhost", 8080));

	            selector = Selector.open();
	            sKey = socket.register(selector, SelectionKey.OP_CONNECT);

	            while (socket != null) {
	                selector.select();

	                for (SelectionKey key : selector.selectedKeys())
	                {
	                    selector.selectedKeys().remove(key);
	                    if (key.isConnectable()) {
	                        socket.finishConnect();
	                        key.interestOps(SelectionKey.OP_READ);
	                    } else if (key.isReadable()) {
	                        readResponse();
	                    } else if (key.isWritable()) {
	                        sendRequests();
	                    }
	                }
	            }
	        } catch (IOException e) 
		 	{
	        	e.printStackTrace();
	        }
		
		
	}
	
	public void addRequest(String request) throws IOException
	{
		byte[] message = request.getBytes();
		ByteBuffer buffer = ByteBuffer.wrap(message);
		messageQueue.add(buffer);
		
		sKey.interestOps(SelectionKey.OP_WRITE);
        selector.wakeup();
		
	}
	
	
	private void readResponse() throws IOException
	{
		GameData gameData = null;
		ByteBuffer buffer = ByteBuffer.allocate(256*2);
		socket.read(buffer);
		byte[] byteArr = buffer.array();
		
		ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(byteArr));
		
		try { 
			gameData = (GameData) objIn.readObject();	
		} 
		catch (ClassNotFoundException e){ 
			e.printStackTrace(); 
		}
		
		objIn.close();		
		
		if(gameData != null)contr.updateInfo(gameData);
	}

	
	private void sendRequests() throws IOException
	{
		ByteBuffer msg;
        synchronized (this) {
            while ((msg = messageQueue.peek()) != null) {
                socket.write(msg);
                if (msg.hasRemaining()) {
                    return;
                }

                messageQueue.remove();
            }

            sKey.interestOps(SelectionKey.OP_READ);
        }

	}

	public void stop() {

			try {
				socket.close();
				socket.keyFor(selector).cancel();
				socket = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}