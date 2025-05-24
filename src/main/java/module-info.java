module com.example.numberguessinggame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;
    requires org.json;


    opens com.example.numberguessinggame to javafx.fxml;
    exports com.example.numberguessinggame;
}