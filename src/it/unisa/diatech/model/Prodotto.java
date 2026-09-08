package it.unisa.diatech.model;

/**
 * Bean che rappresenta un prodotto del catalogo.
 * Corrisponde alla tabella 'prodotto' del database.
 * Contiene oggetti Categoria e Brand annidati invece dei soli id.
 */
public class Prodotto {

    private int       id;
    private String    nome;
    private String    descrizione;
    private double    prezzo;
    private int       quantitaDisponibile;
    private String    immagine;
    private Categoria categoria;
    private Brand     brand;
    private boolean   cancellato;

    // Costruttore vuoto obbligatorio per i Bean Java
    public Prodotto() {}

    // --- Getter e Setter ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public int getQuantitaDisponibile() {
        return quantitaDisponibile;
    }

    public void setQuantitaDisponibile(int quantitaDisponibile) {
        this.quantitaDisponibile = quantitaDisponibile;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public boolean isCancellato() {
        return cancellato;
    }

    public void setCancellato(boolean cancellato) {
        this.cancellato = cancellato;
    }

    @Override
    public String toString() {
        return "Prodotto{id=" + id + ", nome='" + nome + "', prezzo=" + prezzo + "}";
    }
}
