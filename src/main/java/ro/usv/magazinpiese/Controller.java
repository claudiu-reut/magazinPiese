package ro.usv.magazinpiese;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    @FXML
    private Label welcomeText;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void openPiese(ActionEvent e)throws IOException {
        System.out.println("piese");

        Parent root = FXMLLoader.load(getClass().getResource("tabel-piese.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void openStoc(ActionEvent e)throws IOException {
        System.out.println("stoc");

        Parent root = FXMLLoader.load(getClass().getResource("tabel-stoc.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void openDepozit(ActionEvent e)throws IOException {
        System.out.println("depozit");

        Parent root = FXMLLoader.load(getClass().getResource("tabel-depozit.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}