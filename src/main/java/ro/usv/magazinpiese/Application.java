package ro.usv.magazinpiese;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Application extends javafx.application.Application {
    private Stage stage;
    static final ClassLoader loader = Application.class.getClassLoader();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main-menu.fxml"));
        Parent root=fxmlLoader.load();
        Scene scene = new Scene(root);
        String css=this.getClass().getResource("application.css").toExternalForm();
        Font.loadFont(this.getClass().getResource("Montserrat-Regular.ttf").toExternalForm(), 10);
        scene.getStylesheets().add(css);

        stage.setTitle("Magazin Piese Auto");
        loader.getResource("ro/usv/magazinpiese/auto-parts.png");
        //URL url=getClass().getResource("ro/usv/magazinpiese/auto-parts.png");
        Path p = Paths.get("src/main/resources/ro/usv/magazinpiese/auto-parts.png");

       // System.out.println(p.toFile().getAbsolutePath());
        stage.getIcons().add(new Image(p.toFile().getAbsolutePath()));
        stage.setScene(scene);
        stage.show();
        this.stage=stage;
    }
    public  void changeScene(String fxml){
        try {
            Parent pane = FXMLLoader.load(
                    getClass().getResource(fxml));

            stage.getScene().setRoot(pane);
        }catch (Exception ex){
        System.out.println(ex.getMessage());}
    }
    public static void main(String[] args) {
        launch();
    }
}