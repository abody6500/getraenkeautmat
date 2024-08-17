package com.example.getraenkeautomat.service;

import com.example.getraenkeautomat.model.Getraenk;
import com.example.getraenkeautomat.model.GetraenkUndWechselgeld;
import com.example.getraenkeautomat.model.Muenze;

public interface GetraenkeautomatService {
	void Muenzenfuellen(Muenze muenze, int menge);
	void GetraenkAuffuellen(Getraenk getraenk, int menge);
	GetraenkUndWechselgeld kaufen(Getraenk gewuenschteGetraenk, Muenze... einzahlung);
}
