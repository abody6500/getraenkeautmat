package com.example.getraenkeautomat.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.example.getraenkeautomat.model.Getraenk;
import com.example.getraenkeautomat.model.GetraenkUndWechselgeld;
import com.example.getraenkeautomat.model.Muenze;
import com.example.getraenkeautomat.service.GetraenkeautomatService;

public class GetraenkeautomatServiceImpl implements GetraenkeautomatService {
	private Map<Getraenk, Integer> getraenkeBestand;;
	private Map<Muenze, Integer> muenzenBestand;;
	
	 public GetraenkeautomatServiceImpl(Map<Getraenk, Integer> getraenkeBestand, Map<Muenze, Integer> muenzenBestand) {
	        this.getraenkeBestand = getraenkeBestand;
	        this.muenzenBestand = muenzenBestand;
	    }
	 
	@Override
	public void GetraenkAuffuellen(Getraenk getraenkName, int preis) {
		getraenkeBestand.put(getraenkName, preis);
	}
	
	@Override
	public void Muenzenfuellen(Muenze muenze, int menge) {
		muenzenBestand.put(muenze, muenzenBestand.getOrDefault(muenze, 0) + menge);
	}
	
	@Override
	public GetraenkUndWechselgeld kaufen(Getraenk gewuenschteGetraenk, Muenze... einzahlung) {
		int eingezahlteMuenzen = Arrays.stream(einzahlung).mapToInt(Muenze::getWert).sum();
		if(!getraenkeBestand.containsKey(gewuenschteGetraenk)) {
			throw new IllegalArgumentException("Die ausgewählte Getrank " + gewuenschteGetraenk.getName() + " ist nicht verfügbar.");
		}
		
		if(eingezahlteMuenzen < gewuenschteGetraenk.getPreis()) {
			throw new IllegalArgumentException("Die eingezahlte Münzen reichen nicht aus für das Getränk " + gewuenschteGetraenk.getName());
		}
		
		int wechselGeld = eingezahlteMuenzen - gewuenschteGetraenk.getPreis();
		List<Muenze> wechselGeldMuenzen = berechnungWechselGeld(wechselGeld);
		
		if(wechselGeldMuenzen == null) {
			throw new IllegalArgumentException("Es gibt keine ausreichende Wechselgeld für das Getränk " + gewuenschteGetraenk.getName());
		}
		
		getraenkeBestand.put(gewuenschteGetraenk, getraenkeBestand.get(gewuenschteGetraenk) - 1);
		
		return new GetraenkUndWechselgeld(gewuenschteGetraenk, wechselGeldMuenzen);
	}
	
	private List<Muenze> berechnungWechselGeld(int wechselGeld) {
		List<Muenze> wechselGeldMenge = new ArrayList<>();
		Muenze[] muenzenArten = Muenze.values();
		
		// Sortierung von Große (2 Euro) zu kleine (10 Cent)
		Arrays.sort(muenzenArten, (a,b) -> Integer.compare(b.getWert(), a.getWert()));
		
		for(Muenze muenze : muenzenArten) {
			while(muenze.getWert() <= wechselGeld && muenzenBestand.getOrDefault(muenze, 0) > 0 ) {
				wechselGeldMenge.add(muenze);
				wechselGeld -= muenze.getWert();
				muenzenBestand.put(muenze, muenzenBestand.get(muenze) - 1);
			}
		}
		return wechselGeld == 0 ? wechselGeldMenge : null;
	}
}
