import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TeatroMoro {

    // Arreglos para ventas, asientos y clientes
    private static Venta[] ventas;
    private static String[] asientos;
    private static String[] clientes; // Almacena el nombre del cliente y su asiento

    // Lista para eventos con lista anidada de ventas
    private static List<Evento> eventos;

    // Lista para descuentos y promociones
    private static List<Descuento> descuentos;

    private static int contadorVentas = 0;
    private static int contadorClientes = 0;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarSistema();
        mostrarMenu();
    }

    // Inicialización del sistema
    private static void inicializarSistema() {
        final int capacidad_maxima = 50;

        // Inicializar arreglos para elementos de tamaño fijo
        asientos = new String[capacidad_maxima];
        clientes = new String[capacidad_maxima];
        ventas = new Venta[capacidad_maxima]; // Arreglo de objetos Venta

        // Inicializar asientos como disponibles
        for (int i = 0; i < asientos.length; i++) {
            asientos[i] = "Disponible";
        }

        // Inicializar lista de eventos
        eventos = new ArrayList<>();

        // Crear evento inicial
        Evento eventoInicial = new Evento(1, "Función Principal", new ArrayList<>());
        eventos.add(eventoInicial);

        // Inicializar lista de descuentos
        descuentos = new ArrayList<>();
        descuentos.add(new Descuento("Estudiante", 10));
        descuentos.add(new Descuento("Tercera Edad", 15));
    }

    // Menú principal
    private static void mostrarMenu() {
        int opcion;

        do {
            System.out.println("\n=== SISTEMA DE VENTAS TEATRO MORO ===");
            System.out.println("1. Vender entrada");
            System.out.println("2. Ver asientos disponibles");
            System.out.println("3. Ver eventos y ventas");
            System.out.println("4. Ver descuentos disponibles");
            System.out.println("5. Actualizar información de cliente");
            System.out.println("6. Eliminar venta");
            System.out.println("7. Salir");
            System.out.print("Seleccione opción: ");

            // Validación de entrada menú
            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Opción inválida, intente nuevamente: ");
                scanner.nextLine();
                opcion = 0;
            }

            switch (opcion) {
                case 1:
                    venderEntrada();
                    break;
                case 2:
                    verAsientos();
                    break;
                case 3:
                    verEventosYVentas();
                    break;
                case 4:
                    verDescuentos();
                    break;
                case 5:
                    actualizarCliente();
                    break;
                case 6:
                    eliminarVenta();
                    break;
                case 7:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    if (opcion != 0) {
                        System.out.println("Opción inválida");
                    }
            }
        } while (opcion != 7);
    }

    // Función auxiliar para validación de descuentos
    private static String pedirRespuestaDcto(String mensaje) {
        String respuesta;
        boolean inputValido;

        do {
            System.out.print(mensaje + " (S/N): ");
            respuesta = scanner.nextLine().trim();

            if (respuesta.equalsIgnoreCase("S") || respuesta.equalsIgnoreCase("N")) {
                inputValido = true;
            } else {
                System.out.println("Respuesta inválida. Por favor, ingrese 'S' para Sí o 'N' para No: ");
                inputValido = false;
            }
        } while (!inputValido);

        return respuesta.toUpperCase(); // "S" o "N"
    }

    // Implementación de venta de entradas
    private static void venderEntrada() {
        // Validacion de entradas disponibles
        if (contadorVentas >= ventas.length) {
            System.out.println("¡Teatro lleno! no podemos procesar más ventas.");
            return;
        }

        // Ingreso y validación nombre cliente
        String nombreCliente;
        do {
            System.out.print("Ingrese nombre del cliente: ");
            nombreCliente = scanner.nextLine();

            if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
                System.out.println("Por favor, ingrese un nombre válido:");
            }
        } while (nombreCliente == null || nombreCliente.trim().isEmpty());

        // Ingreso y validación de Asiento
        int numAsiento = -1;
        boolean asientoValido = false;

        do {
            System.out.print("Ingrese número de asiento (0-" + (asientos.length - 1) + "): ");
            if (scanner.hasNextInt()) {
                numAsiento = scanner.nextInt();
                scanner.nextLine();

                // Validación de rango
                if (numAsiento < 0 || numAsiento >= asientos.length) {
                    System.out.println("Número de asiento fuera de rango, ingrese un número válido:");
                    // Referencias cruzadas - verificar disponibilidad
                } else if (!asientos[numAsiento].equals("Disponible")) {
                    System.out.println("Lo sentimos, el asiento " + numAsiento + " no está disponible. Por favor, seleccione otro:");
                } else {
                    asientoValido = true;
                }
            } else {
                System.out.println("Entrada inválida. Por favor, ingrese un número válido:");
                scanner.nextLine();
            }
        } while (!asientoValido);

        double precio = 0;
        boolean precioValido = false;

        // Ingreso y validación de Precio
        do {
            System.out.print("Precio de la entrada (monto base):");
            if (scanner.hasNextDouble()) {
                precio = scanner.nextDouble();
                scanner.nextLine();

                if (precio <= 0) {
                    System.out.println("El precio debe ser un valor positivo, intente nuevamente:");
                } else {
                    precioValido = true;
                }
            } else {
                System.out.println("Entrada inválida. Por favor, ingrese un valor numérico (ej: 15000.0):");
                scanner.nextLine();
            }
        } while (!precioValido);

        // Aplicación de Descuentos con uso de función auxiliar
        String esEstudiante = pedirRespuestaDcto("¿Es estudiante?");
        String esTerceraEdad = pedirRespuestaDcto("¿Es tercera edad?");

        double precioFinal = precio;
        String descuentoAplicado = "Ninguno";

        if (esTerceraEdad.equals("S")) {
            precioFinal = precio * 0.85;
            descuentoAplicado = "Tercera Edad (15%)";
        } else if (esEstudiante.equals("S")) {
            precioFinal = precio * 0.90;
            descuentoAplicado = "Estudiante (10%)";
        }

        int nuevaVentaID = contadorVentas + 1;

        // Crear objeto Venta
        Venta nuevaVenta = new Venta(nuevaVentaID, nombreCliente, numAsiento, precioFinal, descuentoAplicado);

        // Agregar a Arreglos (Ventas y Clientes)
        ventas[contadorVentas] = nuevaVenta;
        clientes[contadorVentas] = nombreCliente;

        // Actualizar Arreglo de Asientos
        asientos[numAsiento] = "Ocupado - Venta ID: " + nuevaVentaID + " - Cliente: " + nombreCliente;

        // Agregar venta al Evento (Lista anidada)
        if (!eventos.isEmpty()) {
            eventos.get(0).getVentas().add(nuevaVenta);
        }

        contadorVentas++;
        contadorClientes++;

        System.out.println("\n=== VENTA CONFIRMADA ===");
        System.out.println("ID Venta: " + nuevaVentaID);
        System.out.println("Cliente: " + nombreCliente);
        System.out.println("Asiento: " + numAsiento);
        System.out.println("Precio original: $" + precio);
        System.out.println("Descuento aplicado: " + descuentoAplicado);
        System.out.println("Precio final: $" + precioFinal);
    }

    // Ver asientos disponibles
    private static void verAsientos() {
        System.out.println("\n=== ESTADO DE ASIENTOS ===");
        for (int i = 0; i < asientos.length; i++) {
            System.out.println("Asiento " + i + ": " + asientos[i]);
        }
    }

    // Ver eventos y ventas (lista con lista anidada)
    private static void verEventosYVentas() {
        System.out.println("\n=== EVENTOS Y VENTAS ===");
        if (eventos.isEmpty()) {
            System.out.println("No hay eventos registrados.");
            return;
        }

        for (Evento evento : eventos) {
            System.out.println("\n=== Evento ID: " + evento.getId() + " ===");
            System.out.println("Nombre: " + evento.getNombre());
            System.out.println("Total entradas vendidas: " + evento.getVentas().size());

            if (!evento.getVentas().isEmpty()) {
                System.out.println("Detalle de ventas:");
                for (Venta venta : evento.getVentas()) {
                    System.out.println("  - Venta ID: " + venta.getId() +
                            ", Cliente: " + venta.getCliente() +
                            ", Asiento: " + venta.getAsiento() +
                            ", Monto: $" + String.format("%.2f", venta.getMonto()) +
                            ", Descuento: " + venta.getDescuentoAplicado());
                }
            } else {
                System.out.println("  (Este evento aún no tiene ventas registradas)");
            }
        }
    }

    // Ver descuentos disponibles
    private static void verDescuentos() {
        System.out.println("\n=== DESCUENTOS DISPONIBLES ===");
        for (Descuento descuento : descuentos) {
            System.out.println(descuento.getTipo() + ": " + descuento.getPorcentaje() + "%");
        }
    }

    // Actualizar elementos en arreglos
    private static void actualizarCliente() {
        System.out.println("\n=== ACTUALIZAR INFORMACIÓN DE CLIENTE ===");
        if (contadorClientes == 0) {
            System.out.println("No hay clientes registrados para actualizar.");
            return;
        }

        System.out.print("Ingrese ID de Venta del cliente a actualizar (1 a " + contadorVentas + "): ");
        if (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida.");
            scanner.nextLine();
            return;
        }
        int idVenta = scanner.nextInt();
        scanner.nextLine();

        int indiceVenta = -1;
        // Buscar el objeto Venta por ID
        for (int i = 0; i < contadorVentas; i++) {
            if (ventas[i] != null && ventas[i].getId() == idVenta) {
                indiceVenta = i;
                break;
            }
        }

        // Validación de existencia
        if (indiceVenta == -1) {
            System.out.println("Venta/Cliente no encontrado con ID: " + idVenta);
            return;
        }

        Venta ventaAActualizar = ventas[indiceVenta];
        String nombreAntiguo = ventaAActualizar.getCliente();

        System.out.print("Ingrese el nuevo nombre para el cliente '" + nombreAntiguo + "': ");
        String nuevoNombre = scanner.nextLine();

        // Validación de entrada
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            System.out.println("El nuevo nombre no puede estar vacío.");
            return;
        }

        // Actualizar clientes, asientos y ventas para coherencia de datos
        clientes[indiceVenta] = nuevoNombre;
        ventaAActualizar.setCliente(nuevoNombre);
        int numAsiento = ventaAActualizar.getAsiento();
        asientos[numAsiento] = "Ocupado - Venta ID: " + idVenta + " - Cliente: " + nuevoNombre;

        // Actualizar Lista anidada de Ventas
        if (!eventos.isEmpty()) {
            for (Venta v : eventos.get(0).getVentas()) {
                if (v.getId() == idVenta) {
                    v.setCliente(nuevoNombre);
                    break;
                }
            }
        }

        System.out.println("Cliente actualizado correctamente.");
        System.out.println("Antiguo nombre: " + nombreAntiguo);
        System.out.println("Nuevo nombre: " + nuevoNombre);
    }

    // Eliminar elementos de arreglos
    private static void eliminarVenta() {
        System.out.println("\n=== ELIMINAR VENTA ===");
        if (contadorVentas == 0) {
            System.out.println("No hay ventas para eliminar.");
            return;
        }

        System.out.print("Ingrese ID de venta a eliminar (1 a " + contadorVentas + "): ");
        if (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida.");
            scanner.nextLine();
            return;
        }
        int idVenta = scanner.nextInt();
        scanner.nextLine();

        int indiceAEliminar = -1;
        for (int i = 0; i < contadorVentas; i++) {
            if (ventas[i] != null && ventas[i].getId() == idVenta) {
                indiceAEliminar = i;
                break;
            }
        }

        if (indiceAEliminar == -1) {
            System.out.println("Venta no encontrada con ID: " + idVenta);
            return;
        }

        Venta ventaAEliminar = ventas[indiceAEliminar];
        int numAsientoLiberar = ventaAEliminar.getAsiento();

        // Liberar Asiento en el arreglo 'asientos'
        asientos[numAsientoLiberar] = "Disponible";
        System.out.println("Asiento " + numAsientoLiberar + " liberado.");

        // Eliminar la venta de la Lista anidada (Evento)
        if (!eventos.isEmpty()) {
            eventos.get(0).getVentas().removeIf(v -> v.getId() == idVenta);
        }
        System.out.println("Venta ID " + idVenta + " eliminada del evento.");

        // Desplazar elementos en arreglo 'ventas'
        for (int j = indiceAEliminar; j < contadorVentas - 1; j++) {
            ventas[j] = ventas[j + 1];
            clientes[j] = clientes[j + 1];
        }

        contadorVentas--;
        contadorClientes--;

        System.out.println("\n=== VENTA ELIMINADA CORRECTAMENTE ===");
    }
}

// Clase para Evento con lista anidada de ventas
class Evento {
    private int id;
    private String nombre;
    private List<Venta> ventas;

    public Evento(int id, String nombre, List<Venta> ventas) {
        this.id = id;
        this.nombre = nombre;
        this.ventas = ventas;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public List<Venta> getVentas() { return ventas; }
}

// Clase para Venta
class Venta {
    private int id;
    private String cliente;
    private int asiento;
    private double monto;
    private String descuentoAplicado;

    public Venta(int id, String cliente, int asiento, double monto, String descuentoAplicado) {
        this.id = id;
        this.cliente = cliente;
        this.asiento = asiento;
        this.monto = monto;
        this.descuentoAplicado = descuentoAplicado;
    }

    public int getId() { return id; }
    public String getCliente() { return cliente; }
    public int getAsiento() { return asiento; }
    public double getMonto() { return monto; }
    public String getDescuentoAplicado() { return descuentoAplicado; }

    public void setCliente(String cliente) { this.cliente = cliente; }
    public void setMonto(double monto) { this.monto = monto; }
}

// Clase para Descuento
class Descuento {
    private String tipo;
    private int porcentaje;

    public Descuento(String tipo, int porcentaje) {
        this.tipo = tipo;
        this.porcentaje = porcentaje;
    }

    public String getTipo() { return tipo; }
    public int getPorcentaje() { return porcentaje; }
}
