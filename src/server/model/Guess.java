package server.model;

class Guess
{

	String guess;
	boolean isWordGuess;
	
	Guess(String guess)
	{
		this.guess = guess;
	}
	
	String getGuess()
	{
		return guess;
	}
	
	boolean getIsWordGuess()
	{
		return isWordGuess;
	}
	
	boolean isValid()
	{
		if(guess.length() == 1 && Character.isLetter(guess.charAt(0)))
		{
			isWordGuess = false;
			return true;
		}
		if(this.isWord())
		{
			isWordGuess = true;
			return true;
		}
		
		System.err.println("invalid guess: input must be a letter or a word");
		return false;
				
	}

	
	private boolean isWord()
	{
		if(guess.isEmpty())
			return false;
		for(int i = 0; i<guess.length(); i++)
		{
			if(!Character.isLetter(guess.charAt(i)))
				return false;
		}
		
		return true;
	}
	
}
