package it.unisa.diatech.model;

public class Garanzia {

    private int    id;
    private int    idRigaOrdine;
    private String dataScadenza;
    private String stato;
    public Garanzia() {}
    public Garanzia(int idRigaOrdine, String dataScadenza) {
        this.idRigaOrdine = idRigaOrdine;
        this.dataScadenza  = dataScadenza;
        this.stato         = "ATTIVA";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdRigaOrdine() {
        return idRigaOrdine;
    }

    public void setIdRigaOrdine(int idRigaOrdine) {
        this.idRigaOrdine = idRigaOrdine;
    }

    public String getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(String dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    
    public boolean isAttiva() {
        return "ATTIVA".equals(this.stato);
    }

    @Override
    public String toString() {
        return "Garanzia{id=" + id + ", scadenza='" + dataScadenza
                + "', stato='" + stato + "'}";
    }
}
