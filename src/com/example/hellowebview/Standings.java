package com.example.hellowebview;

class Standings implements Comparable {

	private String dname;
	private int wins;
	private int losses;
	private double winpercent;
	private int teamNo;

	public int getTeamNo() {
		return teamNo;
	}

	public void setTeamNo(int teamNo) {
		this.teamNo = teamNo;
	}

	public Standings(String dname, int wins, int losses, double winpercent,
			int teamNo) {
		this.dname = dname;
		this.wins = wins;
		this.losses = losses;
		this.winpercent = winpercent;
		this.teamNo = teamNo;
	}

	public String getdname() {
		return this.dname;
	}

	public int getwins() {
		return this.wins;
	}

	public int getlosses() {
		return this.losses;
	}

	public double getwinpercent() {
		return this.winpercent;
	}

	// this method must be defined if we are implementing Comparable interface
	public int compareTo(Object otherStandings) {
		Standings tempStandings = (Standings) otherStandings;
		return tempStandings.wins - this.wins;
	}

	@Override
	public String toString() {
		System.out.println("Standing created with name " + dname
				+ " and win percent " + this.winpercent);
		return super.toString();
	}
}