package it.unisa.diatech.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean che rappresenta il carrello di un utente.
 * Contiene la lista delle righe (prodotti scelti).
 * Corrisponde alla tabella 'carrello' del database.
 * Viene salvato in HttpSession per tutta la durata della visita.
 */
public class Carrello {

    private int                id;
    private int                idUtente;
    private List<RigaCarrello> righe;   // lista prodotti nel carrello

    // Costruttore vuoto: inizializza la lista vuota
    public Carrello() {
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

    public List<RigaCarrello> getRighe() {
        return righe;
    }

    public void setRighe(List<RigaCarrello> righe) {
        this.righe = righe;
    }

    /**
     * Aggiunge un prodotto al carrello.
     * Se il prodotto è già presente incrementa la quantità,
     * altrimenti crea una nuova riga.
     */
    public void aggiungiProdotto(Prodotto prodotto, int quantita) {
        for (RigaCarrello riga : righe) {
            if (riga.getProdotto().getId() == prodotto.getId()) {
                riga.setQuantita(riga.getQuantita() + quantita);
                return;
            }
        }
        righe.add(new RigaCarrello(prodotto, quantita));
    }

    /**
     * Rimuove una riga dal carrello tramite id del prodotto.
     */
    public void rimuoviProdotto(int idProdotto) {
        righe.removeIf(r -> r.getProdotto().getId() == idProdotto);
    }

    /**
     * Svuota completamente il carrello.
     * Chiamato dopo il completamento del checkout.
     */
    public void svuota() {
        righe.clear();
    }

    /**
     * Calcola il totale complessivo del carrello
     * sommando i subtotali di tutte le righe.
     */
    public double getTotale() {
        double totale = 0;
        for (RigaCarrello riga : righe) {
            totale += riga.getSubtotale();
        }
        return totale;
    }

    /**
     * Restituisce il numero totale di articoli nel carrello
     * (somma di tutte le quantità).
     */
    public int getNumeroProdotti() {
        int count = 0;
        for (RigaCarrello riga : righe) {
            count += riga.getQuantita();
        }
        return count;
    }

    /**
     * Restituisce true se il carrello non contiene prodotti.
     */
    public boolean isEmpty() {
        return righe.isEmpty();
    }

    @Override
    public String toString() {
        return "Carrello{righe=" + righe.size()
                + ", totale=" + getTotale() + "}";
    }
}
