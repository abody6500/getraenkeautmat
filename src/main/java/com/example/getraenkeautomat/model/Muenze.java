package com.example.getraenkeautomat.model;

/**
 * Enum für die verschiedenen Münzwerte, die in einem Getränkeautomaten verwendet werden.
 */
public enum Muenze {
    ZEHN_CENT(10),
    ZWANZIG_CENT(20),
    FUENFZIG_CENT(50),
    EIN_EURO(100),
    ZWEI_EURO(200);
    
    private final int wert;

    /**
     * Konstruktor für die Münzen-Enum.
     * 
     * @param wert Wert der Münze in Cent.
     */
    Muenze(int wert) {
        this.wert = wert;
    }

    /**
     * Gibt den Wert der Münze zurück.
     * 
     * @return Wert der Münze in Cent.
     */
    public int getWert() {
        return wert;
    }
}
