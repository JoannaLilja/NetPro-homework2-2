package server.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class Server implements Runnable {
 	
	Selector selector;
	ServerSocketChannel serverSocket;

	public void run()
	{
		
		try
		{

			selector = Selector.open();
			
			serverSocket = ServerSocketChannel.open();
	 		serverSocket.bind(new InetSocketAddress("localhost", 8080));
			serverSocket.configureBlocking(false);
			serverSocket.register(selector, serverSocket.validOps());
			
			serve();
 

		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		} catch (ExecutionException e)
		{
			e.printStackTrace();
		}
	
	}
	
	
	private void serve() throws IOException, InterruptedException, ExecutionException
	{
		
		SelectionKey key;
		SocketChannel clientChannel;
		Iterator<SelectionKey> iter;
		
		while (true)
		{
			 
			selector.select();
			iter = selector.selectedKeys().iterator();
 
			while (iter.hasNext())
			{
				key = iter.next();
				iter.remove();
 
				if (key.isAcceptable())
				{
					clientChannel = serverSocket.accept();
 
					clientChannel.configureBlocking(false);
 					clientChannel.register(selector, SelectionKey.OP_READ, new ClientHandler(clientChannel, selector, key));
									
				} else if (key.isReadable())
					((ClientHandler) key.attachment()).read();

			}
			
		}
	}
}