module ro.usv.magazinpiese {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;


    opens ro.usv.magazinpiese to javafx.fxml;
    exports ro.usv.magazinpiese;
}