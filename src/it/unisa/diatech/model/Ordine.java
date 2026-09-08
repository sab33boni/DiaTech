package it.unisa.diatech.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean che rappresenta un ordine effettuato da un cliente.
 * Contiene i dati di spedizione, il metodo di pagamento,
 * lo stato e la lista delle righe acquistate.
 * Corrisponde alla tabella 'ordine' del database.
 */
public class Ordine {

    private int              id;
    private int              idUtente;
    private String           dataOrdine;
    private String           stato;               // IN_LAVORAZIONE, SPEDITO, CONSEGNATO, ANNULLATO
    private double           totale;
    private String           indirizzoSpedizione;
    private String           citta;
    private String           cap;
    private String           metodoPagamento;
    private List<RigaOrdine> righe;               // prodotti acquistati

    // Costruttore vuoto: inizializza la lista righe vuota
    public Ordine() {
        this.righe = new ArrayList<>();
    }

    // --- Getter e Setter ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public String getDataOrdine() {
        return dataOrdine;
    }

    public void setDataOrdine(String dataOrdine) {
        this.dataOrdine = dataOrdine;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public double getTotale() {
        return totale;
    }

    public void setTotale(double totale) {
        this.totale = totale;
    }

    public String getIndirizzoSpedizione() {
        return indirizzoSpedizione;
    }

    public void setIndirizzoSpedizione(String indirizzoSpedizione) {
        this.indirizzoSpedizione = indirizzoSpedizione;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public List<RigaOrdine> getRighe() {
        return righe;
    }

    public void setRighe(List<RigaOrdine> righe) {
        this.righe = righe;
    }

    /**
     * Restituisce il numero di articoli distinti nell'ordine.
     */
    public int getNumeroProdotti() {
        return righe.size();
    }

    @Override
    public String toString() {
        return "Ordine{id=" + id + ", stato='" + stato
                + "', totale=" + totale + ", data='" + dataOrdine + "'}";
    }
}
