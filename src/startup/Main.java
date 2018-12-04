package startup;

import client.net.Client;
import server.net.Server;

public class Main
{
	public static void main(String[] args)
	{
		new Thread(new Server()).start();
		for(int i = 0; i < 3; i++)
			new Thread(new Client(i)).start();
	}
}
