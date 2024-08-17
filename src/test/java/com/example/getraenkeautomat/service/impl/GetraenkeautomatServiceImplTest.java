package com.example.getraenkeautomat.service.impl;

import com.example.getraenkeautomat.model.Getraenk;
import com.example.getraenkeautomat.model.GetraenkUndWechselgeld;
import com.example.getraenkeautomat.model.Muenze;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class GetraenkeautomatServiceImplTest {

    private GetraenkeautomatServiceImpl getraenkeautomatService;

    @BeforeEach
    void setUp() {
        // Initialize the service with empty stock
        getraenkeautomatService = new GetraenkeautomatServiceImpl(new HashMap<>(), new HashMap<>());
    }

    @Test
    void testGetraenkAuffuellen() {
        Getraenk cola = new Getraenk("Cola", 120);
        getraenkeautomatService.GetraenkAuffuellen(cola, 10);

        // This test is a setup step, and correctness will be confirmed through purchase tests
    }

    @Test
    void testMuenzenfuellen() {
        getraenkeautomatService.Muenzenfuellen(Muenze.EIN_EURO, 5);
        getraenkeautomatService.Muenzenfuellen(Muenze.ZWEI_EURO, 2);

        // As with drinks, correctness of filling will be tested through purchase tests
    }

    @Test
    void testKaufen_SuccessfulPurchase() {
        // Arrange
        Getraenk cola = new Getraenk("Cola", 120);  // 1.20 Euro
        getraenkeautomatService.GetraenkAuffuellen(cola, 10);
        getraenkeautomatService.Muenzenfuellen(Muenze.ZWANZIG_CENT, 5);
        getraenkeautomatService.Muenzenfuellen(Muenze.ZEHN_CENT, 5);
        getraenkeautomatService.Muenzenfuellen(Muenze.FUENFZIG_CENT, 5);

        // Act
        GetraenkUndWechselgeld result = getraenkeautomatService.kaufen(cola, Muenze.ZWEI_EURO);

        // Assert
        assertEquals(cola, result.getGetraenk());
        assertNotNull(result.getWechselgeld());
        assertFalse(result.getWechselgeld().isEmpty());
        assertEquals(80, result.getWechselgeld().stream().mapToInt(Muenze::getWert).sum());  // 80 Cent change
    }

    @Test
    void testKaufen_InsufficientFunds() {
        // Arrange
        Getraenk wasser = new Getraenk("Wasser", 100);  // 1.00 Euro
        getraenkeautomatService.GetraenkAuffuellen(wasser, 10);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            getraenkeautomatService.kaufen(wasser, Muenze.FUENFZIG_CENT);  // Only 0.50 Euro
        });

        assertTrue(exception.getMessage().contains("Die eingezahlte Münzen reichen nicht aus"));
    }

    @Test
    void testKaufen_GetraenkNichtVerfuegbar() {
        // Arrange
        Getraenk saft = new Getraenk("Saft", 150);  // 1.50 Euro

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            getraenkeautomatService.kaufen(saft, Muenze.ZWEI_EURO);
        });

        assertTrue(exception.getMessage().contains("ist nicht verfügbar"));
    }

    @Test
    void testKaufen_WechselgeldNichtAusreichend() {
        // Arrange
        Getraenk cola = new Getraenk("Cola", 120);  // 1.20 Euro
        getraenkeautomatService.GetraenkAuffuellen(cola, 10);

        // Not enough change available
        getraenkeautomatService.Muenzenfuellen(Muenze.ZEHN_CENT, 1);  // Only 0.10 Euro change available

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            getraenkeautomatService.kaufen(cola, Muenze.ZWEI_EURO);  // Need 0.80 Euro change
        });

        assertTrue(exception.getMessage().contains("Es gibt keine ausreichende Wechselgeld"));
    }

    @Test
    void testKaufen_ExactAmountNoChange() {
        // Arrange
        Getraenk cola = new Getraenk("Cola", 120);  // 1.20 Euro
        getraenkeautomatService.GetraenkAuffuellen(cola, 10);

        // Act
        GetraenkUndWechselgeld result = getraenkeautomatService.kaufen(cola, Muenze.EIN_EURO, Muenze.ZWANZIG_CENT);

        // Assert
        assertEquals(cola, result.getGetraenk());
        assertNotNull(result.getWechselgeld());
        assertTrue(result.getWechselgeld().isEmpty());
    }
}
