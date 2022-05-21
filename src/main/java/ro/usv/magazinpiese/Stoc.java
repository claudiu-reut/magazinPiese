package ro.usv.magazinpiese;

public class Stoc {


    private Integer bucati;
    private Double pret;
    private int id;
    private String denumire ;
    private String marca;
    private String masiniCompatibile;
    private String oras;
    private String judet;
    private Integer depoId;


    public Stoc( Integer bucati, Double pret) {

        this.bucati = bucati;
        this.pret = pret;
    }
    public Stoc() {

        this.bucati = 0;
        this.pret = 0.0;
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

    public String getMasiniCompatibile() {
        return masiniCompatibile;
    }

    public void setMasiniCompatibile(String masiniCompatibile) {
        this.masiniCompatibile = masiniCompatibile;
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

    public Integer getBucati() {
        return bucati;
    }

    public void setBucati(Integer bucati) {
        this.bucati = bucati;
    }

    public Double getPret() {
        return pret;
    }

    public void setPret(Double pret) {
        this.pret = pret;
    }

    public Integer getDepoId() {
        return depoId;
    }

    public void setDepoId(Integer depoId) {
        this.depoId = depoId;
    }
}
