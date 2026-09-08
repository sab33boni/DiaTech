package it.unisa.diatech.model;

/**
 * Bean che rappresenta un brand (produttore).
 * Corrisponde alla tabella 'brand' del database.
 */
public class Brand {

    private int    id;
    private String nome;

    // Costruttore vuoto obbligatorio per i Bean Java
    public Brand() {}

    // Costruttore con parametri per comodità
    public Brand(int id, String nome) {
        this.id   = id;
        this.nome = nome;
    }

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

    @Override
    public String toString() {
        return "Brand{id=" + id + ", nome='" + nome + "'}";
    }
}
