package it.unisa.diatech.model;

public class Brand {

    private int    id;
    private String nome;
    public Brand() {}
    public Brand(int id, String nome) {
        this.id   = id;
        this.nome = nome;
    }

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
