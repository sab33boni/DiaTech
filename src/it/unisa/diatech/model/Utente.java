package it.unisa.diatech.model;

/**
 * Bean che rappresenta un utente del sito.
 * Può essere un cliente registrato (ruolo REGISTRATO)
 * oppure un amministratore (ruolo ADMIN).
 * Corrisponde alla tabella 'utente' del database.
 */
public class Utente {

    private int    id;
    private String nome;
    private String cognome;
    private String email;
    private String passwordHash;
    private String indirizzo;
    private String citta;
    private String cap;
    private String telefono;
    private String ruolo;           // "REGISTRATO" oppure "ADMIN"
    private String dataRegistrazione;

    // Costruttore vuoto obbligatorio per i Bean Java
    public Utente() {}

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

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public String getDataRegistrazione() {
        return dataRegistrazione;
    }

    public void setDataRegistrazione(String dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    /**
     * Metodo di comodo: restituisce true se l'utente è amministratore.
     * Usato nelle Servlet per controllare i permessi di accesso.
     */
    public boolean isAdmin() {
        return "ADMIN".equals(this.ruolo);
    }

    /**
     * Restituisce il nome completo (nome + cognome).
     * Utile nelle JSP per mostrare il nome dell'utente loggato.
     */
    public String getNomeCompleto() {
        return nome + " " + cognome;
    }

    @Override
    public String toString() {
        return "Utente{id=" + id + ", email='" + email + "', ruolo='" + ruolo + "'}";
    }
}
