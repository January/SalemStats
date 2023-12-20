module ink.drewf.salemstats {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;


    opens ink.drewf.salemstats to javafx.fxml;
    exports ink.drewf.salemstats;
}