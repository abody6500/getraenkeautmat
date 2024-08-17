package com.example.getraenkeautomat.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.example.getraenkeautomat.model.Getraenk;
import com.example.getraenkeautomat.model.GetraenkUndWechselgeld;
import com.example.getraenkeautomat.model.Muenze;
import com.example.getraenkeautomat.service.GetraenkeautomatService;

/**
 * Implementierung des Getränkeautomatenservices.
 */
public class GetraenkeautomatServiceImpl implements GetraenkeautomatService {
	private Map<Getraenk, Integer> getraenkeBestand;;
	private Map<Muenze, Integer> muenzenBestand;;

	public GetraenkeautomatServiceImpl(Map<Getraenk, Integer> getraenkeBestand, Map<Muenze, Integer> muenzenBestand) {
		this.getraenkeBestand = getraenkeBestand;
		this.muenzenBestand = muenzenBestand;
	}

	/**
	 * Füllt ein Getränk auf.
	 * 
	 * @param getraenk Das Getränk.
	 * @param menge    Die Menge des Getränks.
	 */
	@Override
	public void GetraenkAuffuellen(Getraenk getraenk, int menge) {
		getraenkeBestand.put(getraenk, getraenkeBestand.getOrDefault(getraenk, 0) + menge);
	}

	/**
	 * Füllt Münzen auf.
	 * 
	 * @param muenze Die Münze.
	 * @param menge  Die Menge der Münzen.
	 */
	@Override
	public void Muenzenfuellen(Muenze muenze, int menge) {
		muenzenBestand.put(muenze, muenzenBestand.getOrDefault(muenze, 0) + menge);
	}

	/**
	 * Kauft ein Getränk und gibt Wechselgeld zurück.
	 * 
	 * @param gewuenschteGetraenk Das Getränk.
	 * @param einzahlung          Die eingeworfenen Münzen.
	 * @return Das Getränk und das Wechselgeld.
	 * @throws IllegalArgumentException Bei unzureichendem Betrag oder fehlendem
	 *                                  Wechselgeld.
	 */
	@Override
	public GetraenkUndWechselgeld kaufen(Getraenk gewuenschteGetraenk, Muenze... einzahlung) {
		int eingezahlteMuenzen = Arrays.stream(einzahlung).mapToInt(Muenze::getWert).sum();
		if (!getraenkeBestand.containsKey(gewuenschteGetraenk)) {
			throw new IllegalArgumentException(
					"Die ausgewählte Getrank " + gewuenschteGetraenk.getName() + " ist nicht verfügbar.");
		}

		if (eingezahlteMuenzen < gewuenschteGetraenk.getPreis()) {
			throw new IllegalArgumentException(
					"Die eingezahlte Münzen reichen nicht aus für das Getränk " + gewuenschteGetraenk.getName());
		}

		int wechselGeld = eingezahlteMuenzen - gewuenschteGetraenk.getPreis();
		List<Muenze> wechselGeldMuenzen = berechnungWechselGeld(wechselGeld);

		if (wechselGeldMuenzen == null) {
			throw new IllegalArgumentException(
					"Es gibt keine ausreichende Wechselgeld für das Getränk " + gewuenschteGetraenk.getName());
		}

		getraenkeBestand.put(gewuenschteGetraenk, getraenkeBestand.get(gewuenschteGetraenk) - 1);

		return new GetraenkUndWechselgeld(gewuenschteGetraenk, wechselGeldMuenzen);
	}

	/**
	 * Berechnet das Wechselgeld.
	 * 
	 * @param wechselGeld Der Wechselgeldbetrag in Cent.
	 * @return Die Liste der Münzen für das Wechselgeld.
	 */
	private List<Muenze> berechnungWechselGeld(int wechselGeld) {
		List<Muenze> wechselGeldMenge = new ArrayList<>();
		Muenze[] muenzenArten = Muenze.values();

		// Sortierung von großen (2 Euro) zu kleinen (10 Cent)
		Arrays.sort(muenzenArten, (a, b) -> Integer.compare(b.getWert(), a.getWert()));

		for (Muenze muenze : muenzenArten) {
			while (muenze.getWert() <= wechselGeld && muenzenBestand.getOrDefault(muenze, 0) > 0) {
				wechselGeldMenge.add(muenze);
				wechselGeld -= muenze.getWert();
				muenzenBestand.put(muenze, muenzenBestand.get(muenze) - 1);
			}
		}
		return wechselGeld == 0 ? wechselGeldMenge : null;
	}
}
