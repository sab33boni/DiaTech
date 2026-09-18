package it.unisa.diatech.model;

public class RigaCarrello {

    private int     id;
    private int     idCarrello;
    private Prodotto prodotto;
    private int     quantita;
    public RigaCarrello() {}
    public RigaCarrello(Prodotto prodotto, int quantita) {
        this.prodotto = prodotto;
        this.quantita = quantita;
    }

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
