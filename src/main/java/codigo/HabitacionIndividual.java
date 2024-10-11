package codigo;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class HabitacionIndividual extends Habitacion implements Reservable, Serializable {

    public HabitacionIndividual(int identificador, double precioDia) {
        super(identificador,precioDia);
    }

    @Override
    public boolean verificarDisponibilidad(LocalDate inicio, LocalDate fin) {
        for(Reserva reserva1 : Hotel.reservas){
            if(reserva1.getHabitacion().getIdentificador()==identificador){
                LocalDate aux=reserva1.getFechaEntrada();
                do{
                    if((aux.isAfter(inicio) && aux.isBefore(fin)) || aux.isEqual(inicio) || aux.isEqual(fin)){
                        return false;
                    }
                    aux=aux.plusDays(1);
                }while(!aux.isEqual(reserva1.getFechaSalida()));
            }
        }
        return true;
    }


    @Override
    public double calcularPrecio(LocalDate inicio, LocalDate fin) {
        double precio=0;
        LocalDate aux=inicio;
        do{ //dependiendo del dia tiene un valor o otro
            if(aux.getDayOfWeek().equals(DayOfWeek.SATURDAY) || aux.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                precio+=precioDia*1.6;
            }else{
                if(aux.getDayOfWeek().equals(DayOfWeek.MONDAY)){
                    precio+=precioDia*0.6;
                }else{
                    if(aux.getDayOfWeek().equals(DayOfWeek.FRIDAY)){
                        precio+=precioDia*1.4;
                    }else{
                        precio+=precioDia;
                    }
                }
            }
            aux=aux.plusDays(1);
        }while(!aux.isAfter(fin));
        precio= (double) Math.round(precio * 100) /100; //asi hacemos que solo saque dos decimales
        return precio;
    }

    @Override
    public String toString() {
        return "Número: " + identificador +" Tipo: Habitacion Individual" +  " Precio Habitual: " + precioDia;
    }

}
