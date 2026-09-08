package it.unisa.diatech.model;

/**
 * Bean che rappresenta una categoria di prodotti.
 * Corrisponde alla tabella 'categoria' del database.
 */
public class Categoria {

    private int    id;
    private String nome;

    // Costruttore vuoto obbligatorio per i Bean Java
    public Categoria() {}

    // Costruttore con parametri per comodità
    public Categoria(int id, String nome) {
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
        return "Categoria{id=" + id + ", nome='" + nome + "'}";
    }
}
