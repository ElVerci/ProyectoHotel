package codigo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controlador implements Initializable {


    @FXML
    private AnchorPane panelPrincipal;

    @FXML
    private TabPane principal;


    @FXML
    private AnchorPane panelReservar;

    @FXML
    private AnchorPane panelConsultar;

    @FXML
    private Button bReservar;

    @FXML
    private TextField tDNI;

    @FXML
    private DatePicker dPFechaEntrada;

    @FXML
    private DatePicker dPFechaSalida;

    @FXML
    private ListView<Habitacion> listaHabitaciones;

    @FXML
    private TextField tNombre;

    @FXML
    private TextField tApellidos;

    @FXML
    private TextField tPrecioTotal;

    @FXML
    private ListView<Reserva> listaReservas;

    @FXML
    private TextField tBuscDni;

    @FXML
    private TextField tIdReserva;

    @FXML
    private ComboBox<Integer> cbNumHabitaciones=new ComboBox<>();

    @FXML
    private Button bBorrarReserva;



    @FXML
    private ComboBox<String> cBTipoHabitacion=new ComboBox<>();

    private final ObservableList<Integer> numHabitaciones = FXCollections.observableArrayList(
            0,100,101,102,103,104,105,200,201,202,203,204,205,300,301,302,303,304,305
    );

    private final ObservableList<String> tiposHabitacion = FXCollections.observableArrayList(
            "Individual","Doble","Suite","TODO"
    );



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cBTipoHabitacion.setItems(tiposHabitacion);
        cBTipoHabitacion.getSelectionModel().clearSelection();
        cbNumHabitaciones.setItems(numHabitaciones);
        cbNumHabitaciones.getSelectionModel().clearSelection();
    }


    public static boolean validarDni(String dni){
        Pattern pattern = Pattern.compile("\\d{8}[A-Z]");
        Matcher matcher = pattern.matcher(dni);
        return matcher.matches();
    }

    public void mostrarPrecio(){
        if(listaHabitaciones.getSelectionModel().getSelectedItem()==null){

        }else{
            if(listaHabitaciones.getSelectionModel().getSelectedItem() instanceof HabitacionIndividual){
                tPrecioTotal.setText(String.valueOf(((HabitacionIndividual)listaHabitaciones.getSelectionModel().getSelectedItem()).calcularPrecio(dPFechaEntrada.getValue(),dPFechaSalida.getValue()))+" €");
            }else{
                if(listaHabitaciones.getSelectionModel().getSelectedItem() instanceof HabitacionDoble){
                    tPrecioTotal.setText(String.valueOf(((HabitacionDoble)listaHabitaciones.getSelectionModel().getSelectedItem()).calcularPrecio(dPFechaEntrada.getValue(),dPFechaSalida.getValue()))+" €");
                }else{
                    tPrecioTotal.setText(String.valueOf(((Suite)listaHabitaciones.getSelectionModel().getSelectedItem()).calcularPrecio(dPFechaEntrada.getValue(),dPFechaSalida.getValue()))+" €");
                }
            }
        }

    }

    public void mostrarHabitacionesDisponibles(){ //es un metodo que utilizan tres componentes
        listaHabitaciones.getItems().clear();
        tPrecioTotal.setText("");
        if(dPFechaEntrada.getValue() == null || dPFechaSalida.getValue() == null){ //sin fechas no hacemos nada

        }else{
            if(dPFechaSalida.getValue().isBefore(LocalDate.now()) || dPFechaEntrada.getValue().isBefore(LocalDate.now())){
                JOptionPane.showMessageDialog(null,"Una de las fechas introducidas ya ha pasasdo.");
                dPFechaSalida.setValue(null);
                dPFechaEntrada.setValue(null);
            }else{
                if(dPFechaEntrada.getValue().isAfter(dPFechaSalida.getValue())){ // la fecha de salida no puede ser anterior a la de entrada
                    JOptionPane.showMessageDialog(null,"La fecha de entrada no puede estar después de la de salida");
                    dPFechaSalida.setValue(null);
                }else{
                    if(cBTipoHabitacion.getValue()==null || cBTipoHabitacion.getSelectionModel().getSelectedIndex()==3){ //si no se selecciona el tipo de habitacion (por que el cliente le da igual el tipo de habitacion por ejemplo) o si se selecciona todo
                        for(Habitacion habitacion: Hotel.habitaciones){ //sacamos todas las habitaciones disponibles en esas fechas
                            if(habitacion instanceof Suite){ //necesitamos hacer las instancias para poder hacer el verificar y porque el precio y tostring cambian
                                if(((Suite) habitacion).verificarDisponibilidad(dPFechaEntrada.getValue(),dPFechaSalida.getValue())) {
                                    listaHabitaciones.getItems().add(habitacion);
                                }
                            }else{
                                if(habitacion instanceof HabitacionDoble){
                                    if(((HabitacionDoble) habitacion).verificarDisponibilidad(dPFechaEntrada.getValue(),dPFechaSalida.getValue())) {
                                        listaHabitaciones.getItems().add(habitacion);
                                    }
                                }else{
                                    if(((HabitacionIndividual) habitacion).verificarDisponibilidad(dPFechaEntrada.getValue(),dPFechaSalida.getValue())) {
                                        listaHabitaciones.getItems().add(habitacion);
                                    }
                                }
                            }
                        }
                    }else{ // vamos con el caso de que sí seleccione algun tipo de habitacion especifico
                        if(cBTipoHabitacion.getSelectionModel().getSelectedIndex()==0){ //habitacion individual
                            for(Habitacion habitacion: Hotel.habitaciones){
                                if(habitacion instanceof HabitacionIndividual && ((HabitacionIndividual) habitacion).verificarDisponibilidad(dPFechaEntrada.getValue(),dPFechaSalida.getValue())){
                                    listaHabitaciones.getItems().add(habitacion);
                                }
                            }
                        }else{
                            if(cBTipoHabitacion.getSelectionModel().getSelectedIndex()==1){ //habitacion doble
                                for(Habitacion habitacion: Hotel.habitaciones){
                                    if(habitacion instanceof HabitacionDoble && ((HabitacionDoble) habitacion).verificarDisponibilidad(dPFechaEntrada.getValue(),dPFechaSalida.getValue())){
                                        listaHabitaciones.getItems().add(habitacion);
                                    }
                                }
                            }else{
                                for(Habitacion habitacion: Hotel.habitaciones){  //suite
                                    if(habitacion instanceof Suite && ((Suite) habitacion).verificarDisponibilidad(dPFechaEntrada.getValue(),dPFechaSalida.getValue())){
                                        listaHabitaciones.getItems().add(habitacion);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }

    }

    public void crearReserva() {
        if(tApellidos.getText().isEmpty() || tNombre.getText().isEmpty() || tDNI.getText().isEmpty()){ //comprobamos que los campos no estén vacios
            JOptionPane.showMessageDialog(null,"Faltan Campos a Introducir.");
        }else{
            if(!validarDni(tDNI.getText())){ // vemos que el dni sea valido
                JOptionPane.showMessageDialog(null,"El DNI no tiene formato correcto.");
            }else{
                if(listaHabitaciones.getSelectionModel().getSelectedItem()==null){ //vemos que se haya seleccionado algun item
                    JOptionPane.showMessageDialog(null,"Debes seleccionar la habitación para poder reservarla.");
                }else{ //este else ya es cuando está comprobado
                    Hotel.reservar(tDNI.getText(),tNombre.getText(),tApellidos.getText(),dPFechaEntrada.getValue(),dPFechaSalida.getValue(),listaHabitaciones.getSelectionModel().getSelectedItem());

                    //reiniciamos los valores del formulario
                    tNombre.setEditable(true);
                    tApellidos.setEditable(true);
                    tNombre.setText("");
                    tApellidos.setText("");
                    tDNI.setText("");
                    dPFechaEntrada.setValue(null);
                    dPFechaSalida.setValue(null);
                    listaHabitaciones.getItems().clear();
                    cBTipoHabitacion.setValue(null);
                    tPrecioTotal.setText("");
                }
            }
        }
    }

    public void comprobarDNICliente(){ //de esta forma evitamos que un cliente que ya estaba en la base de datos se introduzca con otro nombre y apellido
        tNombre.setEditable(true);
        tApellidos.setEditable(true);
        //para que no se introduzca un dni de mas de 9 caracteres
        if(tDNI.getText().length()>9){
            tDNI.setText(tDNI.getText().substring(0,9));
        }
        for(Cliente cliente: Hotel.clientes){
            if(tDNI.getText().equals(cliente.getDni())){
                tNombre.setEditable(false);
                tApellidos.setEditable(false);
                tApellidos.setText(cliente.getApellidos());
                tNombre.setText(cliente.getNombre());
                break;
            }
        }
    }

    public void mostrarReservas(){
        listaReservas.getItems().clear();
        cbNumHabitaciones.setDisable(false);
        if(tBuscDni.getText().isEmpty() && tIdReserva.getText().isEmpty() && (cbNumHabitaciones.getValue()==null || cbNumHabitaciones.getValue()==0)){
            for(Reserva reserva: Hotel.reservas){
                listaReservas.getItems().add(reserva);
            }
        }else{
           if(tIdReserva.getText().isEmpty()){
                if(cbNumHabitaciones.getValue()==null || cbNumHabitaciones.getValue()==0){
                    String aux =".*"+tBuscDni.getText().toUpperCase() + ".*";
                    for(Reserva reserva: Hotel.reservas){
                        if(Pattern.matches(aux,reserva.getCliente().getDni())){
                            listaReservas.getItems().add(reserva);
                        }
                    }
                }else{
                    String aux =".*"+tBuscDni.getText().toUpperCase() + ".*";
                    for(Reserva reserva: Hotel.reservas){
                        if(Pattern.matches(aux,reserva.getCliente().getDni()) && reserva.getHabitacion().getIdentificador()==cbNumHabitaciones.getValue()){
                            listaReservas.getItems().add(reserva);
                        }
                    }
                }
           }else{
               cbNumHabitaciones.setDisable(true);
               tBuscDni.setText("");
               listaReservas.getItems().clear();
               for(Reserva reserva: Hotel.reservas){
                   try{
                       if(reserva.getIdentificador()==Integer.parseInt(tIdReserva.getText())){
                           listaReservas.getItems().add(reserva);
                       }
                   }catch (NumberFormatException e1){
                       JOptionPane.showMessageDialog(null,"El id de reserva no debe tener letras");
                       listaReservas.getItems().clear();
                       for(Reserva res: Hotel.reservas){
                           listaReservas.getItems().add(res);
                       }
                       cbNumHabitaciones.setDisable(false);
                       tIdReserva.setText("");
                       cbNumHabitaciones.setValue(null);
                       break;
                   }

               }
           }
        }
    }

    public void borrarReserva() {
        if(listaReservas.getSelectionModel().getSelectedItem()==null){
            JOptionPane.showMessageDialog(null,"No has seleccionado ninguna reserva");
        }else{
            if(listaReservas.getSelectionModel().getSelectedItem().getFechaEntrada().isBefore(LocalDate.now()) || listaReservas.getSelectionModel().getSelectedItem().getFechaSalida().isBefore(LocalDate.now())){
                JOptionPane.showMessageDialog(null,"No se puede borrar la reserva ya que esta es anterior a hoy o el cliente está en el hotel");
            }else{
                Hotel.borrarReserva(listaReservas.getSelectionModel().getSelectedItem().getIdentificador());
                Hotel.reservas.remove(listaReservas.getSelectionModel().getSelectedItem());
                tIdReserva.setText("");
                tBuscDni.setText("");
                cbNumHabitaciones.setValue(null);
                cbNumHabitaciones.setDisable(false);
                tBuscDni.setEditable(true);
                listaReservas.getItems().clear();
                for(Reserva reserva: Hotel.reservas){
                    listaReservas.getItems().add(reserva);
                }
            }
        }
    }

}