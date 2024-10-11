package codigo;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Hotel {
    public static ArrayList<Habitacion> habitaciones = new ArrayList<>();
    public static ArrayList<Reserva> reservas = new ArrayList<>();
    public static ArrayList<Cliente> clientes = new ArrayList<>();
    private static String driver = "com.mysql.cj.jdbc.Driver";
    private static String url = "jdbc:mysql://localhost:3306/hotelAntonio";
    private static String user = "root";
    private static String password = "1234";

    public static void recogerReservas() {
        Connection con;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            try {
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT * FROM reservas");
                while (res.next()) {
                    int id = res.getInt("id");
                    String dni = res.getString("dni");
                    LocalDate fechaEntrada = res.getDate("fechaEntrada").toLocalDate();
                    LocalDate fechaSalida = res.getDate("fechaSalida").toLocalDate();
                    int numHabitacion = res.getInt("numHabitacion");
                    double precio = res.getDouble("precio");

                    //se debe inicializar, pero luego si o si los va a encontrar ya que nuestra base de datos está referenciada correctamente
                    Habitacion aux = null;
                    Cliente aux2 = null;

                    //buscamos la habitación
                    for (Habitacion habitacion : habitaciones) {
                        if (habitacion.getIdentificador() == numHabitacion) {
                            aux = habitacion;
                            break;
                        }
                    }

                    // buscamos al cliente
                    for (Cliente cliente : clientes) {
                        if (cliente.getDni().equals(dni)) {
                            aux2 = cliente;
                            break;
                        }
                    }
                    reservas.add(new Reserva(id, aux2, fechaEntrada, fechaSalida, aux, precio));
                }
                JOptionPane.showMessageDialog(null, "Reservas cargadas correctamente");
                con.close();

            } catch (SQLException e3) {
                JOptionPane.showMessageDialog(null, "No se pudo hacer correctamente la query");
            }
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, "No se pudo conectar a la base de datos");
        } catch (ClassNotFoundException e2) {
            JOptionPane.showMessageDialog(null, "Clase no encontrada");
        }
    }

    public static void recogerClientes() {
        Connection con;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            try {
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT * FROM clientes");
                while (res.next()) {
                    String dni = res.getString("dni");
                    String nombre = res.getString("nombre");
                    String apellidos = res.getString("apellidos");
                    clientes.add(new Cliente(dni, nombre, apellidos));
                }
                JOptionPane.showMessageDialog(null, "Clientes cargados correctamente");
                con.close();

            } catch (SQLException e3) {
                JOptionPane.showMessageDialog(null, "No se pudo hacer correctamente la query");
            }
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, "No se pudo conectar a la base de datos");
        } catch (ClassNotFoundException e2) {
            JOptionPane.showMessageDialog(null, "Clase no encontrada");
        }
    }

    public static void recogerHabitaciones() {
        Connection con;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            try {
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT * FROM habitaciones");
                while (res.next()) {
                    int numHabitacion = res.getInt("numHabitacion");
                    double precioDia = res.getDouble("precioDia");
                    String tipo = res.getString("tipo");
                    if (tipo.equalsIgnoreCase("Individual")) {
                        habitaciones.add(new HabitacionIndividual(numHabitacion, precioDia));
                    } else {
                        if (tipo.equalsIgnoreCase("Doble")) {
                            habitaciones.add(new HabitacionDoble(numHabitacion, precioDia));
                        } else {
                            habitaciones.add(new Suite(numHabitacion, precioDia));
                        }
                    }
                }
                JOptionPane.showMessageDialog(null, "Habitaciones cargadas correctamente");
                con.close();

            } catch (SQLException e3) {
                JOptionPane.showMessageDialog(null, "No se pudo hacer correctamente la query");
            }
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, "No se pudo conectar a la base de datos");
        } catch (ClassNotFoundException e2) {
            JOptionPane.showMessageDialog(null, "Clase no encontrada");
        }
    }

    public static void ficheroClientes() {
        try {
            FileOutputStream creConFich = new FileOutputStream("Clientes.ser");
            ObjectOutputStream escFich = new ObjectOutputStream(creConFich);

            for (Cliente cliente : clientes) {
                escFich.writeObject(cliente);
            }

            escFich.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Ha habido un error con el fichero");
        }
    }

    public static void ficheroHabitaciones() {
        try {
            FileOutputStream creConFich = new FileOutputStream("Habitaciones.ser");
            ObjectOutputStream escFich = new ObjectOutputStream(creConFich);

            for (Habitacion habitacion : habitaciones) {
                escFich.writeObject(habitacion);
            }

            escFich.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Ha habido un error con el fichero");
        }
    }

    public static void ficheroReservas() {
        try {
            FileOutputStream creConFich = new FileOutputStream("Reservas.ser");
            ObjectOutputStream escFich = new ObjectOutputStream(creConFich);

            for (Reserva reserva : reservas) {
                escFich.writeObject(reserva);
            }

            escFich.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Ha habido un error con el fichero");
        }
    }

    public static void reservar(String dniCliente, String nombreCliente, String apellidosCliente, LocalDate fechaInicio, LocalDate fechafin, Habitacion habitacion) {
        //esta parte es para registrar los clientes
        boolean registrado = false;
        for (Cliente cliente : clientes) {
            if (cliente.getDni().equals(dniCliente)) {
                registrado = true;
            }
        }
        if (!registrado) {
            registrarClienteNoExistente(dniCliente, nombreCliente, apellidosCliente);
        }
        Cliente elDeLaReserva = null; //si no pongo esto no me deja luego ya que dice que puede que no esté inicializado
        //recogemos el cliente
        for (Cliente cliente : clientes) {
            if (cliente.getDni().equals(dniCliente)) { //siempre entrará aquí ya que si el cliente no está en el array antes lo creamos
                elDeLaReserva = cliente;
            }
        }

        //calculamos el precio
        double precio;
        if (habitacion instanceof Suite) {
            precio = ((Suite) habitacion).calcularPrecio(fechaInicio, fechafin);
        } else {
            if (habitacion instanceof HabitacionIndividual) {
                precio = ((HabitacionIndividual) habitacion).calcularPrecio(fechaInicio, fechafin);
            } else {
                precio = ((HabitacionDoble) habitacion).calcularPrecio(fechaInicio, fechafin);
            }
        }

        // primero accedemos metiendo la reserva en la base de datos para asi obtener el id y luego crear la reserva
        Connection con;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            try {
                Statement st = con.createStatement();
                PreparedStatement pst = con.prepareStatement("INSERT INTO reservas (dni,fechaEntrada,fechaSalida,numHabitacion,precio) VALUES (\'" + dniCliente + "\' ,\'" + fechaInicio + "\', \'" + fechafin + "\'," + habitacion.getIdentificador() + "," + precio + ");");
                pst.executeUpdate();
                ResultSet res = st.executeQuery("SELECT id FROM reservas WHERE numHabitacion=" + habitacion.getIdentificador() + " AND fechaEntrada=\'" + fechaInicio + "\';");
                while (res.next()) {
                    int id = res.getInt("id");
                    reservas.add(new Reserva(id, elDeLaReserva, fechaInicio, fechafin, habitacion, precio));
                }
                //hacemos la reserva
                ficheroReservas(); //para actualizar el fichero de reservas
                JOptionPane.showMessageDialog(null, "Reserva realizada");
                con.close();
            } catch (SQLException e3) {
                JOptionPane.showMessageDialog(null, "No se pudo hacer correctamente la query");
            }
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, "No se pudo conectar a la base de datos");
        } catch (ClassNotFoundException e2) {
            JOptionPane.showMessageDialog(null, "Clase no encontrada");
        }

    }

    public static void registrarClienteNoExistente(String dni, String nombre, String apellidos) {
        //añadimos el cliente en el arraylist
        clientes.add(new Cliente(dni, nombre, apellidos));

        //añadimos el cliente a la base de datos

        Connection con;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            try {
                PreparedStatement pst = con.prepareStatement("INSERT INTO clientes (dni,nombre,apellidos) VALUES (\'" + dni + "\' ,\'" + nombre + "\', \'" + apellidos + "\');");
                pst.executeUpdate();
                con.close();
                ficheroClientes(); //para actualizar el fichero de clientes
            } catch (SQLException e3) {
                JOptionPane.showMessageDialog(null, "No se pudo hacer correctamente la query");
            }
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, "No se pudo conectar a la base de datos");
        } catch (ClassNotFoundException e2) {
            JOptionPane.showMessageDialog(null, "Clase no encontrada");
        }

    }

    public static void borrarReserva(int id) {
        Connection con;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            try {
                PreparedStatement pst = con.prepareStatement("DELETE FROM reservas WHERE id="+id+";");
                pst.executeUpdate();
                ficheroReservas(); //para actualizar el fichero de reservas
                JOptionPane.showMessageDialog(null, "Reserva Eliminada Correctamente");
                con.close();
            } catch (SQLException e3) {
                JOptionPane.showMessageDialog(null, "No se pudo hacer correctamente la query");
            }
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, "No se pudo conectar a la base de datos");
        } catch (ClassNotFoundException e2) {
            JOptionPane.showMessageDialog(null, "Clase no encontrada");
        }
    }
}

