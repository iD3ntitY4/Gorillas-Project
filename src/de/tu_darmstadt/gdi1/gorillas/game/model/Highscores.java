package de.tu_darmstadt.gdi1.gorillas.game.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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
import java.util.Iterator;

import de.tu_darmstadt.gdi1.gorillas.game.model.*;

public class Highscores implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7039567972752939967L;
	
	
	private static ArrayList<Score> scoreList = new ArrayList<Score>();
	
	
	public static ArrayList<Score> getHighScoreList() {
		return scoreList;
	}
	
	public static void readScore() {

		try {
			
		    FileInputStream fileStream = new FileInputStream("assets/other/highscore.hsc");
		    ObjectInputStream objectStream = new ObjectInputStream(fileStream);
		    scoreList = (ArrayList<Score>) objectStream.readObject();
		    objectStream.close(); 
		    
		    for(int i = 0; i < scoreList.size(); i++) {
		    	System.out.println("Read " + scoreList.get(i).getPlayerName());
		    	System.out.println("Read " + scoreList.get(i).getRoundsPlayed());
		    	System.out.println("Read " + scoreList.get(i).getRoundsWon());
		    	System.out.println("Read " + scoreList.get(i).getWonStats());
		    	System.out.println("Read " + scoreList.get(i).getThrowStats());
		    }
		    
		} catch(Exception ex) {
		    ex.printStackTrace();
		}
	}
	
	public static void readTwo() {
	    
		//deserialize the quarks.ser file
	    try(
	      InputStream file = new FileInputStream("assets/other/highscore.hsc");
	      InputStream buffer = new BufferedInputStream(file);
	      ObjectInput input = new ObjectInputStream (buffer);
	    ){
	      //deserialize the List
	      ArrayList<Score> listInput = (ArrayList<Score>)input.readObject();
	      
	      scoreList = listInput;
	      input.close();
	      buffer.close();
	      file.close();
	    }
	    catch(ClassNotFoundException ex){
	    	ex.printStackTrace();
	    }
	    catch(IOException ex){
	    	ex.printStackTrace();
	    }
	    
	}

	// Buffered Stream nötig?
	public static void writeScore() {
		
		try {
		    FileOutputStream fileStream = new FileOutputStream("output");
		    ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);   
		    objectStream.writeObject(scoreList);
		    objectStream.close(); 
		} catch(Exception ex) {
		    ex.printStackTrace();
		}

	}
	
	 private static void writeTwo() { 
		//serialize the List
		    try (
		      OutputStream file = new FileOutputStream("assets/other/highscore.hsc");
		      OutputStream buffer = new BufferedOutputStream(file);
		      ObjectOutput output = new ObjectOutputStream(buffer);
		    ){
		      output.writeObject(scoreList);
		    }  
		    catch(IOException ex){
		    	ex.printStackTrace();
		    }
	    } 

	public static void resetScore() {
		// try (ObjectOutputStream writer = new ObjectOutputStream(new
		// FileOutputStream("assets/other/test.txt"))) {

		// Reset mit FileWriter entfernt verlinkungen von ObjectOutputStream
		try (FileWriter reset = new FileWriter("assets/other/highscore.hsc")) {
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void updateScore(String player1Name, String player2Name,
			int player1RoundsWon, int player2RoundsWon, int player1ShotsFired,
			int player2ShotsFired) {
		{	
			
			//Highscore auslesen
			readTwo();
			
			// Variablen ob und wenn ja wo das Spielerprofil liegt
			boolean player1Exists = false;
			boolean player2Exists = false;
			int player1Position = 0;
			int player2Position = 0;
			
			int player1Won = 0;
			int player2Won = 0;
			float player1ThrowStats = (player1ShotsFired / player1RoundsWon);
			float player2ThrowStats = (player2ShotsFired / player2RoundsWon);
			
			// Sieg extrahieren
			if(player1RoundsWon == World.roundsToWin)
				player1Won = 1;
			else player2Won = 1;
			
			// Ueberpruefen der aktuellen Highscoreliste, ob Spielerprofil existiert
			for(int i = 0; i < scoreList.size();i++) {
				
				if(player1Name.equals(scoreList.get(i).getPlayerName())) {
					player1Exists = true;
					player1Position = i;
				}
				
				if(player2Name.equals(scoreList.get(i).getPlayerName())) {
					player2Exists = true;
					player2Position = i;
				}
			}
			
			//Spieler1 und/oder Spieler2 existieren
			if(player1Exists == true) {
				int allRounds = player1Won + scoreList.get(player1Position).getRoundsPlayed();
				int wonRounds = player1Won + scoreList.get(player1Position).getRoundsWon();
				float perRounds =(float) wonRounds / allRounds;
				float perThrow = (player1ThrowStats + scoreList.get(player1Position).getThrowStats())/2;
				Score player1 = new Score(player1Name,
										  allRounds, 
										  wonRounds,
										  perRounds,
										  perThrow);
				scoreList.remove(player1Position);
				scoreList.add(player1);
			}
			
			if(player2Exists == true) {
				int allRounds = player2Won + scoreList.get(player2Position).getRoundsPlayed();
				int wonRounds = player2Won + scoreList.get(player2Position).getRoundsWon();
				float perRounds = (float)wonRounds / allRounds;
				float perThrow = (player2ThrowStats + scoreList.get(player2Position).getThrowStats())/2;
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
			Score player1 = new Score(player1Name,1,player1Won,player1Won,player1ThrowStats);
			scoreList.add(player1);
			}
			if(player2Exists == false) {
			Score player2 = new Score(player2Name,1,player2Won,player2Won,player2ThrowStats);
			scoreList.add(player2);
			}
			}
			
		//Aktualisieren der scoreArrays mit dem Highscore ArrayList
			Collections.sort(scoreList);
			writeTwo();
	}

	
	

}
