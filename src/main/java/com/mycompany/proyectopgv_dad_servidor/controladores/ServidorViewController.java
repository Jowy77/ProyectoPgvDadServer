package com.mycompany.proyectopgv_dad_servidor.controladores;

// Importación de las clases y paquetes necesarios
import com.google.gson.Gson;
import com.mycompany.proyectopgv_dad_servidor.modelo.ModeloServidor;
import com.mycompany.proyectopgv_dad_servidor.utils.Utils;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Controlador de la interfaz de usuario del servidor. Implementa la interfaz
 * Initializable de JavaFX.
 */
public class ServidorViewController implements Initializable {

    // Declaración de variables miembro
    String servidorCentralIP = "localhost";
    int puertoServidorCentral = 12345;
    String nombreServidor = "Servidor 1";
    boolean activo = true;
    private static final Gson gson = new Gson();

    // Componentes de la interfaz de usuario definidos en el archivo FXML
    @FXML
    private TextField nombreTextField;
    @FXML
    private Button iniciarServidorButton;
    @FXML
    private Button desconectarButton;
    @FXML
    private TextField ipTextField;

    /**
     * Método de inicialización del controlador. Se ejecuta al cargar la
     * interfaz de usuario.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    /**
     * Método para cerrar la aplicación al presionar el botón de salir.
     */
    @FXML
    private void exit(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Método para iniciar el servidor al presionar el botón correspondiente.
     */
    @FXML
    private void iniciarServidor(ActionEvent event) {

        // Obtener el nombre del servidor desde el campo de texto
        nombreServidor = nombreTextField.getText();

        // Crear un hilo para el servidor
        Thread servidorThread = new Thread(() -> {
            try {
                while (true) {
                    // Conexión con el servidor central
                    Socket servidorCentralSocket = new Socket(ipTextField.getText(), puertoServidorCentral);
                    System.out.println("Conectado al servidor central");

                    // Crear el objeto ModeloServidor
                    ModeloServidor datos = Utils.crearModelo();
                    datos.setNombreServidor(nombreServidor);

                    // Convertir el objeto a JSON
                    String json = gson.toJson(datos);

                    // Enviar el JSON al servidor central
                    OutputStreamWriter out = new OutputStreamWriter(servidorCentralSocket.getOutputStream());
                    out.write(json);
                    out.flush();

                    // Cerrar la conexión con el servidor central
                    servidorCentralSocket.close();

                    // Esperar 5 segundos antes de enviar el siguiente objeto
                    TimeUnit.SECONDS.sleep(5);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    Utils.mostrarInformacion("ERROR", "Error de conexion con el cliente.");
                });

                desconectarButton.setDisable(true);
                iniciarServidorButton.setDisable(false);
            }
        });

        // Habilitar el botón de desconectar y deshabilitar el botón de iniciar servidor
        desconectarButton.setDisable(false);
        iniciarServidorButton.setDisable(true);

        // Establecer el hilo del servidor como demonio y comenzar su ejecución
        servidorThread.setDaemon(true);
        servidorThread.start();
    }

    /**
     * Método para desconectar el servidor al presionar el botón
     * correspondiente.
     */
    @FXML
    private void desconectarServidor(ActionEvent event) {
        // Mostrar un mensaje informativo y cerrar la aplicación
        Utils.mostrarInformacion("INFO", "SERVIDOR DESCONECTADO");
        System.exit(0);
    }
}
