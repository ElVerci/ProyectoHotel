package codigo;

import java.io.Serializable;
import java.time.LocalDate;

public class Reserva implements Serializable {
    private int identificador;
    private Cliente cliente;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private Habitacion habitacion;
    private double precio;

    public Reserva(int identificador, Cliente cliente, LocalDate fechaEntrada, LocalDate fechaSalida, Habitacion habitacion, double precio) {
        this.identificador = identificador;
        this.cliente = cliente;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.habitacion = habitacion;
        this.precio=precio;
    }

    @Override
    public String toString() {


        return "\tReserva Nº: "+ identificador + "\tHabitación: "+ habitacion.getIdentificador() + "\tTipo: "+ tipoHabitacion() +"\tPrecio Total: "+precio +"€\n\t\t\tFecha Entrada: " + fechaEntrada +"\t\tFecha Salida: " + fechaSalida +"\n\t\t\t"+cliente.toString()+"\n\n";
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String tipoHabitacion () {
        if(this.habitacion instanceof Suite){
            return "Suite";
        }else{
            if(this.habitacion instanceof HabitacionDoble){
                return "Doble";
            }else{
                if(this.habitacion instanceof HabitacionIndividual) {
                    return "Individual";
                }else{
                    return null;
                }
            }
        }
    }
}
