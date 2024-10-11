package codigo;

import java.io.Serializable;

public class Cliente implements Serializable {
    private String nombre;
    private String apellidos;
    private String dni;

    public Cliente(String dni, String nombre, String apellidos) {
        this.nombre = nombre;
        this.dni = dni;
        this.apellidos = apellidos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    @Override
    public String toString() {
        return "Cliente: "+dni+"\t\t\t"+nombre+" "+apellidos;
    }
}
