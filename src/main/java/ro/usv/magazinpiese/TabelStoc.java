package ro.usv.magazinpiese;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class TabelStoc implements Initializable {
    @FXML
    private Label welcomeText;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML private TableView<Stoc> stocTableView;
    @FXML private TableColumn<Stoc,Integer> piesaID;
    @FXML private TableColumn<Stoc, String> Denumire;
    @FXML private TableColumn<Stoc, String> Marca;
    @FXML private TableColumn<Stoc, String> masiniCompatibile;
    @FXML private TableColumn<Stoc, String> Oras;
    @FXML private TableColumn<Stoc, String> Judet;
    @FXML private TableColumn<Stoc,Integer> Bucati;
    @FXML private TableColumn<Stoc,Double> Pret;
    private String jdbcURL = "jdbc:oracle:thin:@80.96.123.131:1521:ora09";
    private Connection conn = null;
    private Statement stmt = null;
    ResultSet rs =null;
    String user ="hr" ;
    String passwd ="oracletest";
    @FXML private TextField txtBuc;
    @FXML private TextField txtPret;
    @FXML private ComboBox cmbPiese;
    @FXML private ComboBox cmbDepo;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        piesaID.setCellValueFactory(new PropertyValueFactory<>("id"));
        Denumire.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        Marca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        Oras.setCellValueFactory(new PropertyValueFactory<>("oras"));
        Judet.setCellValueFactory(new PropertyValueFactory<>("judet"));
        masiniCompatibile.setCellValueFactory(new PropertyValueFactory<>("masiniCompatibile"));
        Pret.setCellValueFactory(new PropertyValueFactory<Stoc,Double>("pret"));
        Bucati.setCellValueFactory(new PropertyValueFactory<Stoc,Integer>("bucati"));
        stocTableView.getItems().setAll(parseStocList());

        txtBuc.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
                if(!pattern.matcher(txtBuc.getText()).matches())
                    txtBuc.setStyle("-fx-background-color: pink; ");
                else
                    txtBuc.setStyle("-fx-background-color: white;");
            }
        });
        txtPret.textProperty().addListener(new ChangeListener<String>(){
            Pattern pattern = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+");
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {

                if(pattern.matcher(txtPret.getText()).matches())
                    txtPret.setStyle("-fx-background-color: white; ");
                else
                    txtPret.setStyle("-fx-background-color: pink;");
            }
        });

    }
    private Integer getSelectedDepoId(){
        String txt=cmbDepo.getSelectionModel().getSelectedItem().toString();
        String[] arr=txt.split(" ");
        Integer integer= Integer.parseInt(arr[1]);
        return integer;
    }
    private Integer getSelectedPiesaId(){
        String txt=cmbPiese.getSelectionModel().getSelectedItem().toString();
        String[] arr=txt.split(" ");
        Integer integer= Integer.parseInt(arr[1]);
        return integer;
    }

    private List<Stoc> parseStocList(){
        // parse and construct User datamodel list by looping your ResultSet rs
        // and return the list
        ArrayList<Stoc> stocuri= new ArrayList<>();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            //deschidere conexiune
            conn = DriverManager.getConnection(jdbcURL,user,passwd);

            stmt = conn.createStatement();
            String sqlCommand = "SELECT p.piesa_id, d.depozit_id, p.denumire, p.marca, p.masini_compatibile, s.bucati, s.pret, d.oras, d.judet\n" +
                    "FROM PIESA_RC p, DEPOZITE_RC d, STOC_3132A_RC s\n" +
                    "WHERE p.piesa_id=s.piesa_id AND d.depozit_id=s.depozit_id";
            //executie comanda SQL
            rs = stmt.executeQuery(sqlCommand);
            while(rs.next())
            {
               Stoc stoc= new Stoc();

               stoc.setId(rs.getInt("piesa_id"));
                stoc.setDenumire(rs.getString("denumire"));
                stoc.setMarca(rs.getString("marca"));
                stoc.setMasiniCompatibile(rs.getString("masini_compatibile"));
                stoc.setOras(rs.getString("oras"));
                stoc.setJudet(rs.getString("judet"));
                stoc.setPret(rs.getDouble("pret"));
                stoc.setBucati(rs.getInt("bucati"));
                stoc.setDepoId(rs.getInt("depozit_id"));



                stocuri.add(stoc);
            }
            sqlCommand="select * from piesa_rc";
            rs=stmt.executeQuery(sqlCommand);

            while(rs.next())
            {

                ArrayList<String> arr= new ArrayList<>();

                Piesa piesa= new Piesa();
                piesa.setDenumire(rs.getString("DENUMIRE"));
                piesa.setId(rs.getInt("PIESA_ID"));
                piesa.setData(rs.getDate("DATA_ADAUGARII").toString());
                piesa.setMarca(rs.getString("MARCA"));
                piesa.setParteMontare(rs.getString("PARTE_MONTARE").toString());
                piesa.setMasiniCompatibile(rs.getString("MASINI_COMPATIBILE"));
                String s=" "+piesa.getId()+" "+piesa.getDenumire()+" "+piesa.getMarca()+" ";
                arr.add(s);
                ObservableList<String> obs=  FXCollections.observableArrayList(arr);

                cmbPiese.getItems().add(obs);
            }
            //prelucrare rezultat
            sqlCommand="select * from depozite_rc";
            rs=stmt.executeQuery(sqlCommand);

            while(rs.next())
            {

                ArrayList<String> arr= new ArrayList<>();

                Depozit depozit= new Depozit();
                depozit.setOras(rs.getString("ORAS"));
                depozit.setId(rs.getInt("DEPOZIT_ID"));
                depozit.setJudet(rs.getString("JUDET"));
                depozit.setStrada(rs.getString("STRADA"));
                depozit.setNumar(rs.getInt("NUMAR"));
                depozit.setCodPostal(rs.getString("COD_POSTAL"));
                String s=" "+depozit.getId()+" "+depozit.getOras()+" "+depozit.getJudet()+" ";
                arr.add(s);
                ObservableList<String> obs=  FXCollections.observableArrayList(arr);

                cmbDepo.getItems().add(obs);
            }

        } catch(Exception e)
        {        Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(e.getMessage());
            a.show();
            System.out.println(e.getMessage());
        }
        return stocuri;

    }

    public void selectedItem(ActionEvent e){
        try {


            Stoc s = stocTableView.getSelectionModel().getSelectedItem();
            if(s!=null){
               txtPret.setText(s.getPret().toString());
               txtBuc.setText(s.getBucati().toString());
               String str=" "+s.getId()+" "+s.getDenumire()+" "+s.getMarca()+" ";
               cmbPiese.getSelectionModel().select(str);
               str="[ "+s.getDepoId()+" "+s.getOras()+" "+s.getJudet()+" ]";
               cmbDepo.getSelectionModel().select(str);
            }
        }catch (Exception ex){ Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(ex.getMessage());
            a.show();
            System.out.println(ex.getMessage());}
    }
    public  void addItem(ActionEvent e) {
        getSelectedDepoId();

        try{ if(txtBuc.getText()==""||txtPret.getText()==""||cmbDepo.getSelectionModel().getSelectedItem()==null ||cmbPiese.getSelectionModel().getSelectedItem()==null)
            throw new IllegalArgumentException("Completati toate casetele text");
            Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
            Pattern pattern2 = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+");
            if(!pattern.matcher(txtBuc.getText()).matches())
                throw new IllegalArgumentException("Campul bucati trebuie sa fie un numar intreg");
            if(!pattern2.matcher(txtPret.getText()).matches())
                throw new IllegalArgumentException("Campul pret trebuie sa contina un numar intreg sau cu virgula flotanta");

            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            Alert a = new Alert(Alert.AlertType.NONE);
            conn = DriverManager.getConnection(jdbcURL,user,passwd);

            stmt = conn.createStatement();


            String sqlCommand = "insert into STOC_3132A_RC values( ";
            sqlCommand +=getSelectedPiesaId() + ", " +  txtBuc.getText()+ ", " + txtPret.getText() +", "+getSelectedDepoId() +")";
            System.out.println(sqlCommand);
            int rezult = stmt.executeUpdate(sqlCommand);


            if(rezult > 0)
            {       a.setAlertType(Alert.AlertType.CONFIRMATION);
                a.setContentText("Item adaugat cu succes.");
                a.show();
            }
            else
            {
                throw new RuntimeException("Itemul nu a fost adaugat");
            }
            stmt.close();
            conn.close();
           stocTableView.getItems().setAll(parseStocList());



        }catch (Exception ex){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(ex.getMessage());
            a.show();
            System.out.println(ex.getMessage());
        }


    }
    public void saveItem(ActionEvent e){
        try{
            if(txtBuc.getText()==""||txtPret.getText()==""||cmbDepo.getSelectionModel().getSelectedItem()==null ||cmbPiese.getSelectionModel().getSelectedItem()==null)
                throw new IllegalArgumentException("Completati toate casetele text");
            Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
            Pattern pattern2 = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+");
            if(!pattern.matcher(txtBuc.getText()).matches())
                throw new IllegalArgumentException("Campul bucati trebuie sa fie un numar intreg");
            if(!pattern2.matcher(txtPret.getText()).matches())
                throw new IllegalArgumentException("Campul pret trebuie sa contina un numar intreg sau cu virgula flotanta");
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            Alert a = new Alert(Alert.AlertType.NONE);
            conn = DriverManager.getConnection(jdbcURL,user,passwd);
            Stoc s = stocTableView.getSelectionModel().getSelectedItem();
            String sqlCommand = "update stoc_3132a_rc ";
            sqlCommand+="SET pret="+txtPret.getText()+", bucati="+txtBuc.getText();
            sqlCommand+=" where piesa_id="+s.getId()+" and depozit_id="+s.getDepoId();
            stmt = conn.createStatement();
            System.out.println(sqlCommand);
            int rezult = stmt.executeUpdate(sqlCommand);


            if(rezult > 0)
            {      stocTableView.getItems().setAll(parseStocList());
            }
            else
            {
                throw new RuntimeException("Itemul nu a fost sters");
            }


        }catch (Exception ex){ Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(ex.getMessage());
            a.show();
            System.out.println(ex.getMessage());}
    }
    public void deleteSelected(ActionEvent e){
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            Alert a = new Alert(Alert.AlertType.NONE);
            conn = DriverManager.getConnection(jdbcURL,user,passwd);
            Stoc s = stocTableView.getSelectionModel().getSelectedItem();
            String sqlCommand = "delete from stoc_3132a_rc where piesa_id="+s.getId()+" and depozit_id="+s.getDepoId();
            stmt = conn.createStatement();
            int rezult = stmt.executeUpdate(sqlCommand);


            if(rezult > 0)
            {      stocTableView.getItems().setAll(parseStocList());
            }
            else
            {
                throw new RuntimeException("Itemul nu a fost sters");
            }


        }catch (Exception ex){Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(ex.getMessage());
            a.show();
            System.out.println(ex.getMessage());}
    }
    public void goBack(ActionEvent e)throws IOException {
        System.out.println("piese");

        Parent root = FXMLLoader.load(getClass().getResource("main-menu.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
