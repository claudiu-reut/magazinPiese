package ro.usv.magazinpiese;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Piesa {
    private int id;
    private String denumire ;
    private String marca;
    private String parteMontare;
    private String masiniCompatibile;
    private String data;
    private BooleanProperty enabled=new SimpleBooleanProperty();;

    public Piesa(int id, String denumire, String marca, String parteMontare, String masiniCompatibile, String data) {
        this.id = id;
        this.denumire = denumire;
        this.marca = marca;
        this.parteMontare = parteMontare;
        this.masiniCompatibile = masiniCompatibile;
        this.data = data;
        this.enabled.set(true);
    }
    public Piesa() {
        this.id = 0;
        this.marca = "";
        this.denumire="";
        this.parteMontare = "";
        this.masiniCompatibile = "";
        this.data = "";
        this.enabled.set(true);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getMarca() {
        return marca;
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public void setEnabled(boolean enabled) {
        this.enabled.set( enabled);
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getParteMontare() {
        return parteMontare;
    }

    public void setParteMontare(String parteMontare) {
        this.parteMontare = parteMontare;
    }

    public String getMasiniCompatibile() {
        return masiniCompatibile;
    }

    public void setMasiniCompatibile(String masiniCompatibile) {
        this.masiniCompatibile = masiniCompatibile;
    }
    public BooleanProperty enabledProperty() {
        return enabled ;
    }
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
