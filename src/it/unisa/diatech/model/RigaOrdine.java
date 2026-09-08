package it.unisa.diatech.model;

/**
 * Bean che rappresenta una singola riga di un ordine.
 * Contiene il prodotto acquistato, la quantità e il prezzo
 * CONGELATO al momento dell'acquisto (indipendente da future
 * modifiche al prezzo del prodotto nel catalogo).
 * Corrisponde alla tabella 'riga_ordine' del database.
 */
public class RigaOrdine {

    private int     id;
    private int     idOrdine;
    private Prodotto prodotto;        // oggetto Prodotto completo
    private int     quantita;
    private double  prezzoUnitario;  // prezzo CONGELATO al momento dell'acquisto

    // Costruttore vuoto obbligatorio per i Bean Java
    public RigaOrdine() {}

    // --- Getter e Setter ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public double getPrezzoUnitario() {
        return prezzoUnitario;
    }

    public void setPrezzoUnitario(double prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
    }

    /**
     * Calcola il subtotale della riga usando il prezzo congelato.
     * Non usa prodotto.getPrezzo() perche' il prezzo potrebbe
     * essere cambiato dopo l'acquisto.
     */
    public double getSubtotale() {
        return quantita * prezzoUnitario;
    }

    @Override
    public String toString() {
        return "RigaOrdine{prodotto=" + prodotto.getNome()
                + ", quantita=" + quantita
                + ", prezzoUnitario=" + prezzoUnitario + "}";
    }
}
