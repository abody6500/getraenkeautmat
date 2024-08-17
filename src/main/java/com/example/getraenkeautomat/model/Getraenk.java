package com.example.getraenkeautomat.model;

/**
 * Stellt ein Getränk mit Namen und Preis in einem Getränkeautomaten dar.
 */
public class Getraenk {
    private String name;
    private int preis;

    /**
     * Konstruktor für ein Getränk.
     * 
     * @param name  Name des Getränks.
     * @param preis Preis des Getränks in Cent.
     */
    public Getraenk(String name, int preis) {
        this.name = name;
        this.preis = preis;
    }

    /**
     * Gibt den Namen des Getränks zurück.
     * 
     * @return Name des Getränks.
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Getränks.
     * 
     * @param name Neuer Name des Getränks.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt den Preis des Getränks zurück.
     * 
     * @return Preis des Getränks in Cent.
     */
    public int getPreis() {
        return preis;
    }

    /**
     * Setzt den Preis des Getränks.
     * 
     * @param preis Neuer Preis des Getränks in Cent.
     */
    public void setPreis(int preis) {
        this.preis = preis;
    }
}
