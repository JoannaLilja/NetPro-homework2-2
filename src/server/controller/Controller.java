package server.controller;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import server.model.Game;
import server.net.ClientHandler;
import shared.GameData;

public class Controller
{
	
	Game game;
	ClientHandler client;
	
	public Controller(Game game, ClientHandler client)
	{
		this.game = game;
		this.client = client;
	}

	public void receiveGuess(String string) throws IOException, InterruptedException, ExecutionException
	{
		game.receiveGuess(string);
	}

	public GameData getGameData()
	{
		return game.getGameData();
	}

	public void respond() throws IOException
	{
		client.sendResponse(game.getGameData());
	}

}
