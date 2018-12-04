package shared;

import java.io.Serializable;
import java.util.LinkedList;

public class GameData implements Serializable
{

	private int triesLeft;
	private int points;
	private String displayWord;
	private char winLoss;
	private LinkedList<String> guessHistory;
	private String errorMessage;
	
	/**
	 *   Creates a new DTO GameData
	 *   
	 * @param triesLeft
	 * @param points
	 * @param displayWord
	 * @param winLoss
	 * @param guessHistory
	 * @param errorMessage
	 */
	public GameData(int triesLeft, int points, String displayWord, char winLoss, LinkedList<String> guessHistory, String errorMessage)
	{
		this.triesLeft = triesLeft;
		this.points = points;
		this.displayWord = displayWord;
		this.winLoss = winLoss;
		this.guessHistory = guessHistory;
		this.errorMessage = errorMessage;
	}
	
	public int getTriesLeft()
	{
		return triesLeft;
	}
	
	public int getPoints()
	{
		return points;
	}
	
	public String getDisplayWords()
	{
		return displayWord;
	}
	
	public char getWinLoss()
	{
		return winLoss;
	}
	
	public LinkedList<String> getGuessHistory()
	{
		return guessHistory;
	}
	public String getError()
	{
		return errorMessage;
	}
	
	
}
