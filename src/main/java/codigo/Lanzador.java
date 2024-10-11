package codigo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Lanzador extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //recogemos los datos de la base de datos
        Hotel.recogerClientes();
        Hotel.recogerHabitaciones();
        Hotel.recogerReservas();

        //creamos los ficheros serializables
        Hotel.ficheroClientes();
        Hotel.ficheroHabitaciones();
        Hotel.ficheroReservas();

        FXMLLoader fxmlLoader = new FXMLLoader(Lanzador.class.getResource("hotel.fxml"));
        Scene scene = new Scene(fxmlLoader.load()/*loadFXML()*/, 600, 500);
        stage.setTitle("Gestión Hotel");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

    }

    /*public static Parent loadFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Lanzador.class.getResource("hotel.fxml"));
        return fxmlLoader.load();
    }*/
}