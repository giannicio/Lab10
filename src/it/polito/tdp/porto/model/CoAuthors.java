package it.polito.tdp.porto.model;

public class CoAuthors {
	private int a1;
	private int a2;
	
	public CoAuthors(int a1, int a2) {
		super();
		this.a1 = a1;
		this.a2 = a2;
	}
	
	public int getA1() {
		return a1;
	}
	
	public void setA1(int a1) {
		this.a1 = a1;
	}
	
	public int getA2() {
		return a2;
	}
	

	public void setA2(int a2) {
		this.a2 = a2;
	}
	
	@Override
	public String toString() {
		return a1 + " - " + a2;
	}
}
