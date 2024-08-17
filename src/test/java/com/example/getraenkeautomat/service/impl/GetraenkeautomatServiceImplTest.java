package com.example.getraenkeautomat.service.impl;

import com.example.getraenkeautomat.model.Getraenk;
import com.example.getraenkeautomat.model.GetraenkUndWechselgeld;
import com.example.getraenkeautomat.model.Muenze;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Diese Testklasse enthält Unit-Tests für die Klasse {@link GetraenkeautomatServiceImpl}
 */
class GetraenkeautomatServiceImplTest {

    private GetraenkeautomatServiceImpl getraenkeautomatService;
    private Map<Getraenk, Integer> getraenkeBestand;
    private Map<Muenze, Integer> muenzenBestand;
    /**
     * Initialisierung vor jedem Test.
     */
    @BeforeEach
    void setUp() {
        getraenkeBestand = new HashMap<>();
        muenzenBestand = new HashMap<>();

        getraenkeautomatService = new GetraenkeautomatServiceImpl(getraenkeBestand, muenzenBestand);
    }

    /**
     * Testet das Auffüllen von Getränken.
     */
    @Test
    void testGetraenkAuffuellen() {
        Getraenk cola = new Getraenk("Cola", 120);
        getraenkeautomatService.GetraenkAuffuellen(cola, 10);
        
        assertTrue(getraenkeBestand.containsKey(cola));
        assertEquals(10, getraenkeBestand.get(cola));
    }

    /**
     * Testet das Auffüllen von Münzen.
     */
    @Test
    void testMuenzenfuellen() {
        getraenkeautomatService.Muenzenfuellen(Muenze.EIN_EURO, 5);
        getraenkeautomatService.Muenzenfuellen(Muenze.ZWEI_EURO, 2);
        
        assertEquals(5, muenzenBestand.get(Muenze.EIN_EURO));
        assertEquals(2, muenzenBestand.get(Muenze.ZWEI_EURO));
    }

    /**
     * Testet einen erfolgreichen Getränkekauf.
     */
    @Test
    void testKaufen_Erfolgreich() {
        Getraenk cola = new Getraenk("Cola", 120);
        getraenkeautomatService.GetraenkAuffuellen(cola, 10);
        getraenkeautomatService.Muenzenfuellen(Muenze.ZWANZIG_CENT, 5);
        getraenkeautomatService.Muenzenfuellen(Muenze.ZEHN_CENT, 5);
        getraenkeautomatService.Muenzenfuellen(Muenze.FUENFZIG_CENT, 5);
        getraenkeautomatService.Muenzenfuellen(Muenze.EIN_EURO, 5);

        GetraenkUndWechselgeld result = getraenkeautomatService.kaufen(cola, Muenze.ZWEI_EURO);

        assertEquals(cola, result.getGetraenk());
        assertNotNull(result.getWechselgeld());
        assertFalse(result.getWechselgeld().isEmpty());
        assertEquals(80, result.getWechselgeld().stream().mapToInt(Muenze::getWert).sum());
        assertEquals(4, muenzenBestand.get(Muenze.ZWANZIG_CENT));
        assertEquals(4, muenzenBestand.get(Muenze.ZWANZIG_CENT));
        assertEquals(4, muenzenBestand.get(Muenze.ZWANZIG_CENT));
        assertEquals(5, muenzenBestand.get(Muenze.EIN_EURO));
    }

    /**
     * Testet einen Kauf mit unzureichenden Geld.
     */
    @Test
    void testKaufen_UnzureichendenGeld() {
        Getraenk wasser = new Getraenk("Wasser", 100);
        getraenkeautomatService.GetraenkAuffuellen(wasser, 10);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            getraenkeautomatService.kaufen(wasser, Muenze.FUENFZIG_CENT);
        });

        assertTrue(exception.getMessage().contains("Die eingezahlte Münzen reichen nicht aus für das Getränk " + wasser.getName()));
    }

    /**
     * Testet einen Kauf, wenn das Getränk nicht verfügbar ist.
     */
    @Test
    void testKaufen_GetraenkNichtVerfuegbar() {
        Getraenk saft = new Getraenk("Saft", 150);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            getraenkeautomatService.kaufen(saft, Muenze.ZWEI_EURO);
        });

        assertTrue(exception.getMessage().equals("Die ausgewählte Getrank " + saft.getName() + " ist nicht verfügbar."));
    }

    /**
     * Testet einen Kauf, wenn nicht genug Wechselgeld verfügbar ist.
     */
    @Test
    void testKaufen_WechselgeldNichtAusreichend() {
        Getraenk cola = new Getraenk("Cola", 120);
        getraenkeautomatService.GetraenkAuffuellen(cola, 10);

        getraenkeautomatService.Muenzenfuellen(Muenze.ZEHN_CENT, 1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            getraenkeautomatService.kaufen(cola, Muenze.ZWEI_EURO);
        });

        assertTrue(exception.getMessage().contains("Es gibt keine ausreichende Wechselgeld"));
    }

    /**
     * Testet einen Kauf mit exakt passendem Betrag.
     */
    @Test
    void testKaufen_ExactAmountNoChange() {
        Getraenk cola = new Getraenk("Cola", 120);
        getraenkeautomatService.GetraenkAuffuellen(cola, 10);

        GetraenkUndWechselgeld result = getraenkeautomatService.kaufen(cola, Muenze.EIN_EURO, Muenze.ZWANZIG_CENT);

        assertEquals(cola, result.getGetraenk());
        assertNotNull(result.getWechselgeld());
        assertTrue(result.getWechselgeld().isEmpty());
    }
}
