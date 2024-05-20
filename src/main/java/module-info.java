module org.example.dietitan360 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires com.fasterxml.jackson.databind;

    opens org.example.dietitan360 to javafx.fxml;
    exports org.example.dietitan360;
    exports org.example.dietitan360.Controllers;
    opens org.example.dietitan360.Controllers to javafx.fxml;
    opens org.example.dietitan360.Modules to com.fasterxml.jackson.databind;
}



//module org.example.dietitan360 {
//    requires javafx.controls;
//    requires javafx.fxml;
//    requires org.apache.httpcomponents.httpcore;
//    requires org.apache.httpcomponents.httpclient;
//    requires com.fasterxml.jackson.databind;
//
//    opens org.example.dietitan360 to javafx.fxml;
//    exports org.example.dietitan360;
//    exports org.example.dietitan360.Controllers;
//    opens org.example.dietitan360.Controllers to javafx.fxml;
//}