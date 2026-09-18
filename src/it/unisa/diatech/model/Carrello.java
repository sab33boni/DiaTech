package it.unisa.diatech.model;

import java.util.ArrayList;
import java.util.List;

public class Carrello {

    private int                id;
    private int                idUtente;
    private List<RigaCarrello> righe;
    public Carrello() {
        this.righe = new ArrayList<>();
    }

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

    public List<RigaCarrello> getRighe() {
        return righe;
    }

    public void setRighe(List<RigaCarrello> righe) {
        this.righe = righe;
    }

    
    public void aggiungiProdotto(Prodotto prodotto, int quantita) {
        for (RigaCarrello riga : righe) {
            if (riga.getProdotto().getId() == prodotto.getId()) {
                riga.setQuantita(riga.getQuantita() + quantita);
                return;
            }
        }
        righe.add(new RigaCarrello(prodotto, quantita));
    }

    
    public void rimuoviProdotto(int idProdotto) {
        righe.removeIf(r -> r.getProdotto().getId() == idProdotto);
    }

    
    public void svuota() {
        righe.clear();
    }

    
    public double getTotale() {
        double totale = 0;
        for (RigaCarrello riga : righe) {
            totale += riga.getSubtotale();
        }
        return totale;
    }

    
    public int getNumeroProdotti() {
        int count = 0;
        for (RigaCarrello riga : righe) {
            count += riga.getQuantita();
        }
        return count;
    }

    
    public boolean isEmpty() {
        return righe.isEmpty();
    }

    @Override
    public String toString() {
        return "Carrello{righe=" + righe.size()
                + ", totale=" + getTotale() + "}";
    }
}
