package server.integration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

public class FileHandler
{


	public String getWord() throws IOException
	{
		String line;
		int randomLine;
		Random r = new Random();

		randomLine = r.nextInt(51528) + 1;	
		
		Stream<String> lines = Files.lines(Paths.get("data/words.txt"));
		    line = lines.skip(randomLine).findFirst().get();

		return line;
	}
	
}
