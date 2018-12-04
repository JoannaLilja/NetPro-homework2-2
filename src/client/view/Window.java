package client.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.*;

import client.controller.Controller;
import shared.GameData;


public class Window extends JFrame implements ActionListener
{
	
	
	Controller contr;
	
	
	JLabel guessesLeftLabel = new JLabel("",SwingConstants.CENTER);
	JLabel pointsLabel = new JLabel("",SwingConstants.CENTER);
	JLabel displayWordLabel = new JLabel("",SwingConstants.CENTER);
	
	JTextArea history = new JTextArea(1, 1);
	JScrollPane textPane = new JScrollPane (history);
	
	JTextField guessField = new JTextField("");
	JButton guessBtn = new JButton ("GUESS");

	JPanel topPanel = new JPanel(new GridLayout(2,1));
	JPanel infoPanel = new JPanel(new GridLayout(1,2));
	JPanel inputPanel = new JPanel(new GridLayout(1,1));

	
	/**
	 * Constructor for View. Takes the Controller for communication with the network layer. The id is simply used too make the windows look nice. 
	 * @param contr
	 * @param id 
	 */
	public Window(Controller contr, int id)
	{
		super("Hangman player " + (id+1)); 
		
		this.contr = contr;
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		
		Container contentPane = getContentPane();
		
		setSize(450,350); 
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		if(id == 0){setLocation(5,(int)height/2-this.getHeight()/2-50);}
		if(id == 1){setLocation((int)width/2-this.getWidth()/2,(int)height/2-this.getHeight()/2-50);}
		if(id == 2){setLocation((int)width-this.getWidth()-5,(int)height/2-this.getHeight()/2-50);}

		inputPanel.setOpaque(false);
		setVisible (true);
		

		history.setEditable(false);
		guessBtn.addActionListener(this);
		
		
		displayWordLabel.setFont(new Font("serif",Font.BOLD,30));


		contentPane.add("North", topPanel);
		contentPane.add("Center", textPane);
		contentPane.add("South", inputPanel);
	    
				
		topPanel.add(infoPanel);
		infoPanel.add(guessesLeftLabel);
		infoPanel.add(pointsLabel);
		
		topPanel.add(displayWordLabel);

		inputPanel.add(guessField);
		inputPanel.add(guessBtn);
				
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	try
		    	{
					contr.endGame();
					
				} catch (IOException e)
		    	{
					e.printStackTrace();
				}
		    }
		});

	}

	/**
	 * Called when an action is performed. Takes an ActionEvent e that identifies the action.
	 */
	public void actionPerformed(ActionEvent e)
	{

		String guess = guessField.getText();

		history.append("Player guessed: " + guess + "\n");
		try 
		{
			contr.guess(guess);
		} 
		catch (IOException e1)
		{
			e1.printStackTrace();
		}

	}
	
	private void setHistory(LinkedList<String> entries)
	{
		for(String entry : entries)
		{
			history.append("Player guessed: " + entry + '\n');
		}
	}
	
	public void updateInfo(GameData gameData)
	{
		
		try
		{
		guessField.setText("");
		history.setText("");
		guessesLeftLabel.setText(gameData.getTriesLeft() + " guesses left");
		pointsLabel.setText(gameData.getPoints() + " points");
		setDisplayWordLabel(gameData.getDisplayWords());
		
		
		if(gameData.getWinLoss() == 'w')
			history.append("CORRECT! +1 point");
		else if(gameData.getWinLoss() == 'l')
			history.append("OUT OF GUESSES! -1 point");

		setHistory(gameData.getGuessHistory());
				
		if (!gameData.getError().isEmpty())
			history.append(gameData.getError() + "\n");
		}
		catch (NullPointerException e)
		{
			
		}
		
	}

	private void setDisplayWordLabel(String word)
	{
		StringBuilder displayWord = new StringBuilder();
		
		for(int i = 0; i < word.length()-1; i++)
		{
			displayWord.append(word.charAt(i)+" ");
		}
		displayWord.append(word.charAt(word.length()-1));
		
		displayWordLabel.setText(displayWord.toString());
	}


	
}
