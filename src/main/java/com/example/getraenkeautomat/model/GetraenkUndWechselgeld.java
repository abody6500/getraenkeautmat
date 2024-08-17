package com.example.getraenkeautomat.model;

import java.util.List;

public class GetraenkUndWechselgeld {
	private Getraenk getraenk;
	private List<Muenze> wechselgeld;
	
	
	public GetraenkUndWechselgeld(Getraenk getraenk, List<Muenze> wechselgeld) {
		this.getraenk = getraenk;
		this.wechselgeld = wechselgeld;
	}

	public Getraenk getGetraenk() {
		return getraenk;
	}

	public List<Muenze> getWechselgeld() {
		return wechselgeld;
	}

	
	
}
