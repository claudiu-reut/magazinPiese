package ro.usv.magazinpiese;

public class Piesa {
    private int id;
    private String denumire;
    private String marca;
    private String parteMontare;
    private String masiniCompatibile;
    private String data;

    public Piesa(int id, String denumire, String marca, String parteMontare, String masiniCompatibile, String data) {
        this.id = id;
        this.denumire = denumire;
        this.marca = marca;
        this.parteMontare = parteMontare;
        this.masiniCompatibile = masiniCompatibile;
        this.data = data;
    }
    public Piesa() {
        this.id = 0;
        this.denumire = "";
        this.marca = "";
        this.parteMontare = "";
        this.masiniCompatibile = "";
        this.data = "";
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
