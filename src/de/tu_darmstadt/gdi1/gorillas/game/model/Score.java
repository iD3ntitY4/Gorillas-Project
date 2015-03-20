package de.tu_darmstadt.gdi1.gorillas.game.model;

import java.io.Serializable;

public class Score implements Comparable<Score>, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String playerName;
	private int roundsPlayed;
	private int roundsWon;
	private int wonStats;
	private double throwStats;
	
	public Score(String name,  			// Name des Spielers
				int rp,					// Anzahl aller gespielten Runden
				int rw, 				// Anzahl der gewonnen Runden
				int wS,					// 
				double tS) {
			playerName = name;
			roundsPlayed = rp;
			roundsWon = rw;
			wonStats = wS;
			throwStats = tS;
		}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public int getRoundsPlayed() {
		return roundsPlayed;
	}
	
	public int getRoundsWon() {
		return roundsWon;
	}
	
	public int getWonStats() {
		return wonStats;
	}
	
	public double getThrowStats() {
		return throwStats;
	}
	
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public void setRoundsPlayed(int roundsPlayed) {
		this.roundsPlayed = roundsPlayed;
	}
	
	public void setRoundsWon(int roundsWon) {
		this.roundsWon = roundsWon;
	}
	
	public void setWonStats(int wonStats) {
		this.wonStats = wonStats;
	}
	
	public void setThrowStats(double throwStats) {
		this.throwStats = throwStats;
	}
	
	@Override
	public int compareTo(Score score) {
		
		// wenn Mehr Spiele gewonnen wurden oder wenn mehr Spiele gewonnen wurden und der throwStat kleiner ist
		if((this.getWonStats() < score.getWonStats()) || 
			((this.getWonStats() == score.getWonStats()) && (this.getThrowStats() > score.getThrowStats())))
			return 1;
		// wenn weniger Spiele gewonnen wurden oder gleich viel und der throwStat ist größer
		if((this.getWonStats() > score.getWonStats() ||
			((this.getWonStats() == score.getWonStats() && (this.getThrowStats() < score.getThrowStats())))))
			return -1;
		
		else return 0;
	}
}
