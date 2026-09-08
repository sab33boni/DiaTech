package it.unisa.diatech.model;

/**
 * Bean che rappresenta la garanzia legale associata
 * a un prodotto acquistato (riga_ordine).
 * Corrisponde alla tabella 'garanzia' del database.
 */
public class Garanzia {

    private int    id;
    private int    idRigaOrdine;
    private String dataScadenza;   // data di scadenza della garanzia
    private String stato;          // "ATTIVA" oppure "SCADUTA"

    // Costruttore vuoto obbligatorio per i Bean Java
    public Garanzia() {}

    // Costruttore con parametri per comodità
    public Garanzia(int idRigaOrdine, String dataScadenza) {
        this.idRigaOrdine = idRigaOrdine;
        this.dataScadenza  = dataScadenza;
        this.stato         = "ATTIVA";
    }

    // --- Getter e Setter ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdRigaOrdine() {
        return idRigaOrdine;
    }

    public void setIdRigaOrdine(int idRigaOrdine) {
        this.idRigaOrdine = idRigaOrdine;
    }

    public String getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(String dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    /**
     * Restituisce true se la garanzia è ancora attiva.
     */
    public boolean isAttiva() {
        return "ATTIVA".equals(this.stato);
    }

    @Override
    public String toString() {
        return "Garanzia{id=" + id + ", scadenza='" + dataScadenza
                + "', stato='" + stato + "'}";
    }
}
