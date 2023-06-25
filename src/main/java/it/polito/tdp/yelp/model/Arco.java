package it.polito.tdp.yelp.model;

public class Arco {
	
	private String bID;
	private double avg;
	
	
	public Arco(String bID, double avg) {
		super();
		this.bID = bID;
		this.avg = avg;
	}


	public String getbID() {
		return bID;
	}


	public void setbID(String bID) {
		this.bID = bID;
	}


	public double getAvg() {
		return avg;
	}


	public void setAvg(double avg) {
		this.avg = avg;
	}
}
