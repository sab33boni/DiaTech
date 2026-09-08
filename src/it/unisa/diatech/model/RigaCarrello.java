package it.unisa.diatech.model;

/**
 * Bean che rappresenta una singola riga del carrello.
 * Ogni riga contiene un prodotto e la quantità scelta.
 * Corrisponde alla tabella 'riga_carrello' del database.
 */
public class RigaCarrello {

    private int     id;
    private int     idCarrello;
    private Prodotto prodotto;   // oggetto Prodotto completo (non solo l'id)
    private int     quantita;

    // Costruttore vuoto obbligatorio per i Bean Java
    public RigaCarrello() {}

    // Costruttore con parametri per comodità
    public RigaCarrello(Prodotto prodotto, int quantita) {
        this.prodotto = prodotto;
        this.quantita = quantita;
    }

    // --- Getter e Setter ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCarrello() {
        return idCarrello;
    }

    public void setIdCarrello(int idCarrello) {
        this.idCarrello = idCarrello;
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

    /**
     * Calcola il subtotale di questa riga:
     * quantita x prezzo del prodotto.
     * Usato nelle JSP per mostrare il costo parziale.
     */
    public double getSubtotale() {
        return quantita * prodotto.getPrezzo();
    }

    @Override
    public String toString() {
        return "RigaCarrello{prodotto=" + prodotto.getNome()
                + ", quantita=" + quantita
                + ", subtotale=" + getSubtotale() + "}";
    }
}
