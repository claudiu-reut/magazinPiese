package ro.usv.magazinpiese;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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

import java.sql.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TabelPiese implements Initializable {
        @FXML
        private Label welcomeText;
        private Stage stage;
        private Scene scene;
        private Parent root;
        @FXML private TableView<Piesa> tableView;
        @FXML private TableColumn<Piesa, Integer> PiesaId;
        @FXML private TableColumn<Piesa, String> Denumire;
        @FXML private TableColumn<Piesa, String> Marca;
        @FXML private TableColumn<Piesa, String> parteMontare;
        @FXML private TableColumn<Piesa, String> masiniCompatibile;
        @FXML private TableColumn<Piesa, String> data;
        @FXML private TextField txtMarca;
        @FXML private TextField txtDenumire;
        @FXML private TextField txtMontare;
        @FXML private TextField txtMasini;
        private String jdbcURL = "jdbc:oracle:thin:@80.96.123.131:1521:ora09";
        private Connection conn = null;
        private Statement stmt = null;
        ResultSet rs =null;
        String user ="hr" ;
        String passwd ="oracletest";
                @Override
                public void initialize(URL location, ResourceBundle resources) {
                        PiesaId.setCellValueFactory(new PropertyValueFactory<Piesa, Integer>("id"));
                        Denumire.setCellValueFactory(new PropertyValueFactory<Piesa, String>("Denumire"));
                        Marca.setCellValueFactory(new PropertyValueFactory<Piesa, String>("Marca"));
                        parteMontare.setCellValueFactory(new PropertyValueFactory<Piesa, String>("parteMontare"));
                        masiniCompatibile.setCellValueFactory(new PropertyValueFactory<Piesa, String>("masiniCompatibile"));
                        data.setCellValueFactory(new PropertyValueFactory<Piesa, String>("data"));
                        tableView.getItems().setAll(parsePieseList());
                }


        private List<Piesa> parsePieseList(){
                // parse and construct User datamodel list by looping your ResultSet rs
                // and return the list
                ArrayList<Piesa> piese= new ArrayList<>();
                try {
                        Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                        //deschidere conexiune
                        conn = DriverManager.getConnection(jdbcURL,user,passwd);

                        stmt = conn.createStatement();
                        String sqlCommand = "select * from PIESA_RC";
                        //executie comanda SQL
                        rs = stmt.executeQuery(sqlCommand);
                        while(rs.next())
                        {
                                Piesa piesa= new Piesa();
                                piesa.setDenumire(rs.getString("DENUMIRE"));
                                piesa.setId(rs.getInt("PIESA_ID"));
                                piesa.setData(rs.getDate("DATA_ADAUGARII").toString());
                                piesa.setMarca(rs.getString("MARCA"));
                                piesa.setParteMontare(rs.getString("PARTE_MONTARE").toString());
                                piesa.setMasiniCompatibile(rs.getString("MASINI_COMPATIBILE"));
                                piese.add(piesa);
                        }
                        //prelucrare rezultat

                } catch(Exception e)
                {        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setContentText(e.getMessage());
                        a.show();
                        System.out.println(e.getMessage());
                }
                return piese;

        }
        public  void addItem(ActionEvent e){

                        try{ if(txtMontare.getText()==""||txtDenumire.getText()==""||txtMasini.getText()==""||txtMarca.getText()=="")
                                throw new IllegalArgumentException("Completati toate casetele text");
                                Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                                Alert a = new Alert(Alert.AlertType.NONE);
                                conn = DriverManager.getConnection(jdbcURL,user,passwd);

                                stmt = conn.createStatement();


                                String sqlCommand = "insert into piesa_rc (piesa_id, denumire, marca, parte_montare,masini_compatibile) values( ";
                                sqlCommand +="SECV_RC.NEXTVAL, '" + txtDenumire.getText() + "', '" +  txtMarca.getText()+ "', '" + txtMontare.getText() +"', '"+txtMasini.getText()+ "')";
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
                                tableView.getItems().setAll(parsePieseList());



                        }catch (Exception ex){
                                Alert a = new Alert(Alert.AlertType.ERROR);
                                a.setContentText(ex.getMessage());
                                a.show();
                                System.out.println(ex.getMessage());
                        }
        }
        public void deleteSelected(ActionEvent e){
                        try{
                                Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                                Alert a = new Alert(Alert.AlertType.NONE);
                                conn = DriverManager.getConnection(jdbcURL,user,passwd);
                                Piesa p = tableView.getSelectionModel().getSelectedItem();
                                String sqlCommand = "delete from piesa_rc where piesa_id="+p.getId();
                                stmt = conn.createStatement();
                                int rezult = stmt.executeUpdate(sqlCommand);


                                if(rezult > 0)
                                {      tableView.getItems().setAll(parsePieseList());
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
        public void saveItem(ActionEvent e){
                        try{    if(txtMontare.getText()==""||txtDenumire.getText()==""||txtMasini.getText()==""||txtMarca.getText()=="")
                                throw new IllegalArgumentException("Completati toate casetele text");
                                Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                                Alert a = new Alert(Alert.AlertType.NONE);
                                conn = DriverManager.getConnection(jdbcURL,user,passwd);
                                Piesa p = tableView.getSelectionModel().getSelectedItem();
                                String sqlCommand = "update piesa_rc ";
                                sqlCommand+="SET denumire='"+txtDenumire.getText()+"', marca='"+txtMarca.getText()+"', parte_montare='"+txtMontare.getText()+"', masini_compatibile='"+txtMasini.getText()+"'";
                                sqlCommand+=" where piesa_id="+p.getId();
                                stmt = conn.createStatement();
                                System.out.println(sqlCommand);
                                int rezult = stmt.executeUpdate(sqlCommand);


                                if(rezult > 0)
                                {      tableView.getItems().setAll(parsePieseList());
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
        public void selectedItem(ActionEvent e){
                try {


                        Piesa p = tableView.getSelectionModel().getSelectedItem();
                        if(p!=null){
                        txtDenumire.setText(p.getDenumire());
                        txtMarca.setText(p.getMarca());
                        txtMasini.setText(p.getMasiniCompatibile());
                        txtMontare.setText(p.getParteMontare());

                        }
                }catch (Exception ex){ Alert a = new Alert(Alert.AlertType.ERROR);
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
