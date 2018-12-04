package client.controller;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import client.net.Client;
import client.view.Window;
import shared.GameData;

public class Controller
{
	
	Client client;
	Window view;

	
	public Controller(Client client, int id)
	{
		this.client = client;
		this.view = new Window(this, id);
	}


	public void guess(String guess) throws IOException
	{
		CompletableFuture.runAsync(() -> {
				try
				{
					client.addRequest(guess);
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
			});
	}
	
	public void updateInfo(GameData gameData) 
	{
		view.updateInfo(gameData);
	}


	public void endGame() throws IOException
	{
		client.stop();
	}
	

}
