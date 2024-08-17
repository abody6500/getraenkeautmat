package com.example.getraenkeautomat.model;


public class Getraenk {
	private String name;
	private int preis;
	
	
	public Getraenk(String name, int preis) {
		this.name = name;
		this.preis = preis;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPreis() {
		return preis;
	}
	public void setPreis(int preis) {
		this.preis = preis;
	}
	
	
}
