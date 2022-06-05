package ro.usv.magazinpiese;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Depozit {
    private Integer id;
    private String oras;
    private String judet;
    private String strada;
    private Integer numar;
    private String codPostal;
    private BooleanProperty enabled=new SimpleBooleanProperty();;

    public Depozit(int id, String oras, String judet, String strada, Integer numar, String codPostal) {
        this.id = id;
        this.oras = oras;
        this.judet = judet;
        this.strada = strada;
        this.numar = numar;
        this.codPostal = codPostal;
    }
    public Depozit() {
        this.id = 0;
        this.oras = "";
        this.judet = "";
        this.strada = "";
        this.numar = 0;
        this.codPostal = "";
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public BooleanProperty enabledProperty() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled.set(enabled);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOras() {
        return oras;
    }

    public void setOras(String oras) {
        this.oras = oras;
    }

    public String getJudet() {
        return judet;
    }

    public void setJudet(String judet) {
        this.judet = judet;
    }

    public String getStrada() {
        return strada;
    }

    public void setStrada(String strada) {
        this.strada = strada;
    }

    public Integer getNumar() {
        return numar;
    }

    public void setNumar(Integer numar) {
        this.numar = numar;
    }

    public String getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }
}
