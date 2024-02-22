module com.mycompany.proyectopgv_dad_servidor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires com.github.oshi;
    requires com.google.gson;
    requires com.sun.jna;
    opens com.mycompany.proyectopgv_dad_servidor to javafx.fxml;
    exports com.mycompany.proyectopgv_dad_servidor;
    exports com.mycompany.proyectopgv_dad_servidor.controladores;
    opens com.mycompany.proyectopgv_dad_servidor.controladores to javafx.fxml;
    opens com.mycompany.proyectopgv_dad_servidor.modelo;
}
