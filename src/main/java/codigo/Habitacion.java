package codigo;

import java.io.Serializable;

abstract public class Habitacion implements Serializable {
    protected int identificador;
    protected double precioDia;

    public Habitacion(int identificador, double precioDia) {
        this.identificador = identificador;
        this.precioDia = precioDia;
    }

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public double getprecioDia() {
        return precioDia;
    }

    public void setprecioDia(double precioDia) {
        this.precioDia = precioDia;
    }
}
