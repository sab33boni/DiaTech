package it.unisa.diatech.model;

public class RigaOrdine {

    private int     id;
    private int     idOrdine;
    private Prodotto prodotto;
    private int     quantita;
    private double  prezzoUnitario;
    public RigaOrdine() {}

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
