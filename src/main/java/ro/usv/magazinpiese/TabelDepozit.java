package ro.usv.magazinpiese;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ro.usv.magazinpiese.Depozit;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class TabelDepozit implements Initializable {
    @FXML
    private Label welcomeText;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML private TableView<Depozit> depozitTableView;
    @FXML private TableColumn<Depozit, Integer> DepozitId;
    @FXML private TableColumn<Depozit, String> Oras;
    @FXML private TableColumn<Depozit, String> Judet;
    @FXML private TableColumn<Depozit, String> Strada;
    @FXML private TableColumn<Depozit, Integer> Numar;
    @FXML private TableColumn<Depozit, String> codPostal;
    @FXML private TableColumn<Depozit, Boolean> activated;
    private String jdbcURL = "jdbc:oracle:thin:@80.96.123.131:1521:ora09";
    private Connection conn = null;
    private Statement stmt = null;
    @FXML private TextField txtOras;
    @FXML private TextField txtJudet;
    @FXML private TextField txtStrada;
    @FXML private TextField txtNumar;
    @FXML private TextField txtCod;
    @FXML private Label lblCount;
    ResultSet rs =null;
    String user ="hr" ;
    String passwd ="oracletest";
    Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DepozitId.setCellValueFactory(new PropertyValueFactory<Depozit, Integer>("id"));
        Oras.setCellValueFactory(new PropertyValueFactory<Depozit, String>("Oras"));
        Judet.setCellValueFactory(new PropertyValueFactory<Depozit, String>("Judet"));
        Strada.setCellValueFactory(new PropertyValueFactory<Depozit, String>("Strada"));
        Numar.setCellValueFactory(new PropertyValueFactory<Depozit, Integer>("Numar"));
        codPostal.setCellValueFactory(new PropertyValueFactory<Depozit, String>("codPostal"));
        depozitTableView.getItems().setAll(parseDepoList());
        activated.setCellFactory(CheckBoxTableCell.forTableColumn(activated));
        activated.setCellValueFactory(celldata->celldata.getValue().enabledProperty());
        txtCod.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
                if(txtCod.getText().length()<6||!pattern.matcher(txtCod.getText()).matches())
                    txtCod.setStyle("-fx-background-color: pink; ");
                else
                    txtCod.setStyle("-fx-background-color: white;");
            }
        });
        txtNumar.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {

                if(pattern.matcher(txtNumar.getText()).matches())
                    txtNumar.setStyle("-fx-background-color: white; ");
                else
                    txtNumar.setStyle("-fx-background-color: pink;");
            }
        });
    }
    private List<Depozit> parseDepoList(){
        // parse and construct User datamodel list by looping your ResultSet rs
        // and return the list
        ArrayList<Depozit> depozite= new ArrayList<>();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            //deschidere conexiune
            conn = DriverManager.getConnection(jdbcURL,user,passwd);

            stmt = conn.createStatement();
            String sqlCommand = "select * from DEPOZITE_RC";
            //executie comanda SQL
            rs = stmt.executeQuery(sqlCommand);
            while(rs.next())
            {
                Depozit depozit= new Depozit();
                depozit.setOras(rs.getString("ORAS"));
                depozit.setId(rs.getInt("DEPOZIT_ID"));
                depozit.setJudet(rs.getString("JUDET"));
                depozit.setStrada(rs.getString("STRADA"));
                depozit.setNumar(rs.getInt("NUMAR"));
                depozit.setCodPostal(rs.getString("COD_POSTAL"));
                boolean b= rs.getInt("activat")==1?true:false;
                depozit.setEnabled(b);
                depozite.add(depozit);
            }
            //prelucrare rezultat
            sqlCommand = "select COUNT(depozit_id) as numar from depozite_RC";
            //executie comanda SQL
            rs = stmt.executeQuery(sqlCommand);
            while(rs.next()){
                Integer i=rs.getInt("numar");
                lblCount.setText(i.toString()+" Înregistrări găsite ");
            }

        } catch(Exception e)
        {        Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(e.getMessage());
            a.show();
            System.out.println(e.getMessage());
        }
        return depozite;

    }
    public void deleteSelected(ActionEvent e){
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            Alert a = new Alert(Alert.AlertType.NONE);
            conn = DriverManager.getConnection(jdbcURL,user,passwd);
            Depozit d = depozitTableView.getSelectionModel().getSelectedItem();
            String sqlCommand = "delete from depozite_rc where depozit_id="+d.getId();
            stmt = conn.createStatement();
            int rezult = stmt.executeUpdate(sqlCommand);


            if(rezult > 0)
            {      depozitTableView.getItems().setAll(parseDepoList());
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
    public void deactivateItem(ActionEvent e){
        try{

            Depozit depozit=depozitTableView.getSelectionModel().getSelectedItem();
            depozit.setEnabled(!depozit.isEnabled());
            activated.setCellValueFactory(celldata->celldata.getValue().enabledProperty());

            conn = DriverManager.getConnection(jdbcURL,user,passwd);
            Integer integer=depozit.isEnabled()==true?1:0;
            String sqlCommand = "update depozite_rc set activat="+integer+" where depozit_id="+depozit.getId();
            stmt = conn.createStatement();
            int rezult = stmt.executeUpdate(sqlCommand);
        }catch (Exception ex){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(ex.getMessage());
            a.show();
            System.out.println(ex.getMessage());
        }
    }
    public void saveItem(ActionEvent e){
        try{
            if(txtCod.getText()==""||txtNumar.getText()==""||txtStrada.getText()==""||txtOras.getText()==""||txtJudet.getText()=="")
                throw new IllegalArgumentException("Completati toate casetele text");

            if(txtCod.getText().length()<6||!pattern.matcher(txtCod.getText()).matches())
                throw new IllegalArgumentException("Campul Cod Postal trebuie sa aiba 6 cifre");
            if(!pattern.matcher(txtNumar.getText()).matches())
                throw new IllegalArgumentException("Campul numar trebuie sa contina un numar intreg");
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            Alert a = new Alert(Alert.AlertType.NONE);
            conn = DriverManager.getConnection(jdbcURL,user,passwd);
           Depozit d = depozitTableView.getSelectionModel().getSelectedItem();
            String sqlCommand = "update depozite_rc ";
            sqlCommand+="SET oras='"+txtOras.getText()+"', judet='"+txtJudet.getText()+"', strada='"+txtStrada.getText()+"', numar="+txtNumar.getText()+",cod_postal='"+txtCod.getText()+"'";
            sqlCommand+=" where depozit_id="+d.getId();
            stmt = conn.createStatement();
            System.out.println(sqlCommand);
            int rezult = stmt.executeUpdate(sqlCommand);


            if(rezult > 0)
            {      depozitTableView.getItems().setAll(parseDepoList());
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
    public  void addItem(ActionEvent e){

        try{ if(txtCod.getText()==""||txtNumar.getText()==""||txtStrada.getText()==""||txtOras.getText()==""||txtJudet.getText()=="")
            throw new IllegalArgumentException("Completati toate casetele text");

            if(txtCod.getText().length()<6||!pattern.matcher(txtCod.getText()).matches())
                throw new IllegalArgumentException("Campul Cod Postal trebuie sa aiba 6 cifre");
            if(!pattern.matcher(txtNumar.getText()).matches())
                throw new IllegalArgumentException("Campul numar trebuie sa contina un numar intreg");

            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            Alert a = new Alert(Alert.AlertType.NONE);
            conn = DriverManager.getConnection(jdbcURL,user,passwd);

            stmt = conn.createStatement();


            String sqlCommand = "insert into DEPOZITE_RC values( ";
            sqlCommand +="SECV_DEP_RC.NEXTVAL, '" + txtOras.getText() + "', '" +  txtJudet.getText().toUpperCase()+ "', '" + txtStrada.getText() +"', "+txtNumar.getText()+",'"+txtCod.getText() +"'"+",1)";
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
            depozitTableView.getItems().setAll(parseDepoList());



        }catch (Exception ex){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(ex.getMessage());
            a.show();
            System.out.println(ex.getMessage());
        }
    }
    public void selectedItem(ActionEvent e){
        try {


            Depozit d = depozitTableView.getSelectionModel().getSelectedItem();
            if(d!=null){
                txtOras.setText(d.getOras());
                txtJudet.setText(d.getJudet());
                txtStrada.setText(d.getStrada());
                txtNumar.setText(d.getNumar().toString());
                txtCod.setText(d.getCodPostal());
            }
        }catch (Exception ex){ Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText(ex.getMessage());
            a.show();
            System.out.println(ex.getMessage());}
    }
    public void checkNumber(ActionEvent e){
        if(txtCod.getText().length()<6)
            txtCod.setStyle("-fx-background-color: red; ");
        else
            txtCod.setStyle("-fx-background-color: white;");

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
