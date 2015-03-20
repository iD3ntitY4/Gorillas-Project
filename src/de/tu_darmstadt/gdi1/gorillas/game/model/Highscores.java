package de.tu_darmstadt.gdi1.gorillas.game.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Highscores implements Serializable{
	
	private static final long serialVersionUID = -7039567972752939967L;
	
	private static ArrayList<Score> scoreList = new ArrayList<Score>();
	
	private static final String filePath = "assets/gorillas/highscores/highscore.hsc";
	
	public static ArrayList<Score> getHighScoreList() {
		return scoreList;
	}
	
	/**
	 * Creates a Stream which reads the highscore.hsc file ArrayList
	 */
	@SuppressWarnings("unchecked")
	public static void readScore() {
	    
	    try(
	      InputStream file = new FileInputStream(filePath);
	      InputStream buffer = new BufferedInputStream(file);
	      ObjectInput input = new ObjectInputStream (file);
	    ){
	    	try {
		      ArrayList<Score> listInput = (ArrayList<Score>)input.readObject();
		      
		      scoreList = listInput; 
	    	}
	    	finally {
			      input.close();
			      buffer.close();
			      file.close();
			      System.gc();
	    	}
	    }
	    catch(ClassNotFoundException ex){
	    	ex.printStackTrace();
	    }
	    catch(IOException ex){
	    	ex.printStackTrace();
	    }
	    
	}
	
	/**
	 * Write the ArrayList into the file
	 */
	 private static void writeScore() { 
		    try (
		      OutputStream file = new FileOutputStream(filePath);
		      OutputStream buffer = new BufferedOutputStream(file);
		      ObjectOutput output = new ObjectOutputStream(buffer);
		    ){
		    	try {
		    		output.writeObject(scoreList);
		    	}
		    	finally {
		    		output.flush();
		    		buffer.flush();
		    		file.flush();
		    		output.close();
				    buffer.close();
				    file.close();
				    System.gc();
		    	}
		    }  
		    catch(IOException ex){
		    	ex.printStackTrace();
		    }
	    } 

	 /**
	  * Reset our file / Highscores
	  */
	public static void resetScore() {
		try (PrintWriter writer = new PrintWriter(filePath)) {
			writer.print("");
			writer.close();
			System.gc();
			readScore();
			
			System.out.println("List is " + (scoreList.size() == 0 ? "empty" : "not empty"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* -- OLD METHOD NO LONGER USED ----
	public static void updateScore(String player1Name, String player2Name, int playedRounds,
			int player1RoundsWon, int player2RoundsWon, int player1ShotsFired,
			int player2ShotsFired) {
		{	
			
			//Highscore auslesen
			readScore();
			
			// Variablen ob und wenn ja wo das Spielerprofil liegt
			boolean player1Exists = false;
			boolean player2Exists = false;
			int player1Position = 0;
			int player2Position = 0;
			
			int player1Won = 0;
			int player2Won = 0;
			double player1ThrowStats = (player1ShotsFired / player1RoundsWon);
			double player2ThrowStats = (player2ShotsFired / player2RoundsWon);
			
			
			// Check if player1 exists
			for(int i = 0; i < scoreList.size();i++) {
				
				if(player1Name.equals(scoreList.get(i).getPlayerName())) {
					player1Exists = true;
					player1Position = i;
				}
			}
			
			//Replace stats if player1 exists
			if(player1Exists) {
				int allRounds = playedRounds + scoreList.get(player1Position).getRoundsPlayed();
				int wonRounds = player1RoundsWon + scoreList.get(player1Position).getRoundsWon();
				int perRounds = Math.round(((float)wonRounds / (float)allRounds) * 100);
				double perThrow = (player1ThrowStats + scoreList.get(player1Position).getThrowStats())/2;
				Score player1 = new Score(player1Name,
										  allRounds, 
										  wonRounds,
										  perRounds,
										  perThrow);
				scoreList.remove(player1Position);
				scoreList.add(player1);
			}
			
			// Find player 2
			for(int i = 0; i < scoreList.size(); i++) {
				
				if(player2Name.equals(scoreList.get(i).getPlayerName())) {
					player2Exists = true;
					player2Position = i;
				}
			}
			
			// Replace if player2 exists
			if(player2Exists) {
				int allRounds = playedRounds + scoreList.get(player2Position).getRoundsPlayed();
				int wonRounds = player2RoundsWon + scoreList.get(player2Position).getRoundsWon();
				int perRounds = Math.round(((float)wonRounds / (float)allRounds) * 100);
				double perThrow = (player2ThrowStats + scoreList.get(player2Position).getThrowStats())/2;
				Score player2 = new Score(player2Name,
										  allRounds, 
										  wonRounds,
										  perRounds,
										  perThrow);
				scoreList.remove(player2Position);
				scoreList.add(player2);
			}
			
			
				
			//Spieler1 und/oder Spieler2 existiert nicht
			if(player1Exists == false) {
				Score player1 = new Score(player1Name,1,playedRounds,player1RoundsWon,player1ThrowStats);
				scoreList.add(player1);
			}
			if(player2Exists == false) {
				Score player2 = new Score(player2Name,1,playedRounds,player2RoundsWon,player2ThrowStats);
				scoreList.add(player2);
			}
			}
			
		//Aktualisieren der scoreArrays mit dem Highscore ArrayList
			Collections.sort(scoreList);
			writeScore();
	}*/
	
	
	/**
	 * Adds a new Highscore entry to the file
	 * @param name the name of the player
	 * @param numberOfRounds the total number of rounds the player has played
	 * @param roundsWon the number of rounds the player has won
	 * @param bananasThrown the number of bananas the player has thrown
	 */
	public static void addHighscore(String name, int numberOfRounds, int roundsWon,
			int bananasThrown) {
		
		//Highscore auslesen
		readScore();
		
		boolean playerExists = false;
		int listPosition = 0;
			
		double throwStats;
				
		if(roundsWon == 0)
			throwStats = bananasThrown;
		else
			throwStats = (bananasThrown / roundsWon);
		
		
		// Search for player
		for(int i = 0; i < scoreList.size();i++) {
			
			if(name.equals(scoreList.get(i).getPlayerName())) {
				playerExists = true;
				listPosition = i;
			}
		}
		
		if(playerExists) {
			int allRounds = numberOfRounds + scoreList.get(listPosition).getRoundsPlayed();
			int wonRounds = roundsWon + scoreList.get(listPosition).getRoundsWon();
			int perRounds = Math.round(((float)wonRounds / (float)allRounds) * 100);
			double newThrowStats = (throwStats + scoreList.get(listPosition).getThrowStats()) / 2;
			
			Score player1 = new Score(name, allRounds, wonRounds, perRounds, newThrowStats);
			
			scoreList.remove(listPosition);
			scoreList.add(player1);
		}
		else
		{
			int allRounds = numberOfRounds;
			int wonRounds = roundsWon;
			int perRounds = Math.round(((float)wonRounds / (float)allRounds) * 100);
			
			Score player1 = new Score(name, allRounds, wonRounds, perRounds, throwStats);
			
			scoreList.add(player1);
		}
		
	//Aktualisieren der scoreArrays mit dem Highscore ArrayList
		Collections.sort(scoreList);
		writeScore();
		
		readScore();
	}
}
