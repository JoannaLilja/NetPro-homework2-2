package server.model;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import server.controller.Controller;
import server.integration.FileHandler;
import shared.GameData;


/**
 * Stores game data and holds functionality related to the game
 */
public class Game
{
		
	private Controller contr;
	private FileHandler fileHandler = new FileHandler();

	private int triesLeft;
	private int points;
	private String currentWord;
	private StringBuilder displayWord = new StringBuilder();
	private char winLoss = '-';
	private LinkedList<String> guessHistory = new LinkedList<String>();
	private String errorMessage;

	
	public Game() throws IOException, InterruptedException, ExecutionException
	{		
		pickNewWord();
	}
	
	public void setController(Controller contr)
	{
		this.contr = contr;
	}
	
	public GameData getGameData()
	{
        return new GameData(triesLeft, points, displayWord.toString(), winLoss, guessHistory, errorMessage);
	}
	
	char getWinLoss()
	{
		return winLoss;
	}
	
	int getTriesLeft()
	{
		return triesLeft;
	}
	
	int getPoints()
	{
		return points;
	}
	
	String getError()
	{
		return errorMessage;
	}
	
	String getDisplayWord()
	{
		return displayWord.toString();
	}
	
	private void pickNewWord() throws IOException, InterruptedException, ExecutionException
	{
		
		CompletableFuture.runAsync(() -> {
			
			try { 
				currentWord = fileHandler.getWord();
				
				System.out.println(currentWord);

				triesLeft = currentWord.length();
				
				for(int i = 0; i < currentWord.length(); i++)
					displayWord.append('_');
											
				contr.respond();

			} 
			catch (IOException e) { 
				e.printStackTrace();
			}

		});
		
		if (displayWord.length()>0) 
			displayWord = new StringBuilder();

	}
	
	public void receiveGuess(String guess) throws IOException, InterruptedException, ExecutionException
	{
		
		errorMessage = "";
		if (!guess.equals(".start"))
		{
			Guess g = new Guess(guess);
			
			if (g.isValid())
			{
				guessHistory.add(guess);
				if (g.isWordGuess)
					receiveWordGuess(guess);
				else
					receiveLetterGuess(guess.charAt(0));
			}
		}
	}
	
	private void receiveWordGuess(String guess) throws IOException, InterruptedException, ExecutionException
	{
		if (guess.equalsIgnoreCase(currentWord))
		{
			points++;
			winLoss = 'w';
			guessHistory.clear();
			pickNewWord();
			
		}
		else if(triesLeft <= 0)
		{
			points--;
			winLoss = 'l';
			guessHistory.clear();
			pickNewWord();
		}
		else
		{
			triesLeft--;
			contr.respond();
		}

	}
	
	private void receiveLetterGuess(char guess) throws IOException, InterruptedException, ExecutionException
	{
		
		boolean correct = false;
		
		winLoss = '-';

		
		for (int i = 0; i < currentWord.length(); i++)
			if (currentWord.charAt(i) == guess)
			{
				displayWord.setCharAt(i, guess);
				correct = true;
			}
		if(correct == false)
		{
			triesLeft--;
			if(triesLeft <= 0)
			{
				points--;
				winLoss = 'l';
				guessHistory.clear();
				pickNewWord();
			}
			else
			{
				contr.respond();
			}
		}
		else
			contr.respond();

		
	}
	

}
