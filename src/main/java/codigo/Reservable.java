package codigo;

import java.time.LocalDate;

public interface Reservable {
    boolean verificarDisponibilidad(LocalDate inicio, LocalDate fin);
    double calcularPrecio(LocalDate inicio, LocalDate fin);
}
