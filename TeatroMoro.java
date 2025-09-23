
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TeatroMoro {
    // Variables estáticas para contadores globales
    static int totalAsientosDisponibles = 100;
    static int totalAsientosReservados = 0;
    static int totalAsientosVendidos = 0;

    // Variables de instancia para información básica
    String nombreTeatro = "Teatro Moro";
    int capacidadSala = 100;
    double precioUnitario = 25000.0;
    String[] estadoAsientos = new String[101];

    public TeatroMoro() {
        for (int i = 1; i <= 100; i++) {
            estadoAsientos[i] = "DISPONIBLE";
        }
    }

    public static void main(String[] args) {
        TeatroMoro teatro = new TeatroMoro();
        teatro.mostrarMenu();
    }

    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;
        boolean continuar = true;

        System.out.println("=== BIENVENIDO AL " + nombreTeatro.toUpperCase() + " ===");

        while (continuar) {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Reservar un asiento");
            System.out.println("2. Modificar una reserva");
            System.out.println("3. Comprar entradas");
            System.out.println("4. Imprimir boleta");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = scanner.nextInt();
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("Por favor, ingrese un número válido.");
                continue;
            }

            switch (opcion) {
                case 1:
                    reservarAsiento(scanner);
                    break;
                case 2:
                    modificarReserva(scanner);
                    break;
                case 3:
                    comprarEntradas(scanner);
                    break;
                case 4:
                    imprimirBoleta(scanner);
                    break;
                case 5:
                    System.out.println("¡Gracias por usar el sistema del " + nombreTeatro + "!");
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione del 1 al 5.");
            }
        }
        scanner.close();
    }

    // Método para reservar asiento
    public void reservarAsiento(Scanner scanner) {
        int numeroAsiento;
        boolean disponibilidad;

        System.out.print("Ingrese el número de asiento a reservar (1-100): ");

        try {
            numeroAsiento = scanner.nextInt();
            if (numeroAsiento < 1 || numeroAsiento > 100) {
                System.out.println("Número de asiento inválido. Debe estar entre 1 y 100.");
                return;
            }

            disponibilidad = estadoAsientos[numeroAsiento].equals("DISPONIBLE");

            if (disponibilidad) {
                estadoAsientos[numeroAsiento] = "RESERVADO";
                totalAsientosDisponibles--;
                totalAsientosReservados++;

                System.out.println("¡Asiento " + numeroAsiento + " reservado exitosamente!");
                System.out.println("Tiempo límite de reserva: 10 minutos");
            } else {
                String estado = estadoAsientos[numeroAsiento];
                System.out.println("El asiento " + numeroAsiento + " no está disponible.");
                System.out.println("Estado actual: " + estado);
            }

        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("Por favor, ingrese un número válido.");
        }
    }


    public void modificarReserva(Scanner scanner) {
        int asientoActual, asientoNuevo;

        System.out.print("Ingrese el número de asiento actual reservado: ");

        try {
            asientoActual = scanner.nextInt();

            // Validar que el asiento esté reservado
            if (asientoActual < 1 || asientoActual > 100 ||
                    !estadoAsientos[asientoActual].equals("RESERVADO")) {
                System.out.println("El asiento " + asientoActual + " no tiene una reserva activa.");
                return;
            }

            System.out.print("Ingrese el nuevo número de asiento (1-100): ");
            asientoNuevo = scanner.nextInt();

            // Validar nuevo asiento
            if (asientoNuevo < 1 || asientoNuevo > 100) {
                System.out.println("Número de asiento inválido.");
                return;
            }

            if (!estadoAsientos[asientoNuevo].equals("DISPONIBLE")) {
                System.out.println("El asiento " + asientoNuevo + " no está disponible.");
                return;
            }

            // Realizar el cambio
            estadoAsientos[asientoActual] = "DISPONIBLE";
            estadoAsientos[asientoNuevo] = "RESERVADO";

            System.out.println("Reserva modificada exitosamente!");
            System.out.println("Asiento anterior (" + asientoActual + ") liberado.");
            System.out.println("Nuevo asiento (" + asientoNuevo + ") reservado.");

        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("Error al procesar la modificación. Ingrese números válidos.");
        }
    }

    // Método para comprar entradas
    public void comprarEntradas(Scanner scanner) {
        int numeroAsiento;
        String accionDeseada; // Variable local para acción del usuario

        System.out.println("--- COMPRA DE ENTRADAS ---");
        System.out.println("1. Comprar asiento reservado");
        System.out.println("2. Comprar asiento directamente");
        System.out.print("Seleccione una opción: ");

        try {
            int opcionCompra = scanner.nextInt();

            // Estructura de control: If-else
            if (opcionCompra == 1) {
                // Comprar asiento reservado
                System.out.print("Ingrese el número de asiento reservado: ");
                numeroAsiento = scanner.nextInt();

                if (numeroAsiento >= 1 && numeroAsiento <= 100 &&
                        estadoAsientos[numeroAsiento].equals("RESERVADO")) {

                    estadoAsientos[numeroAsiento] = "VENDIDO";
                    totalAsientosReservados--;
                    totalAsientosVendidos++;
                    accionDeseada = "compra de reserva";

                    System.out.println("¡Compra realizada exitosamente!");

                } else {
                    System.out.println("El asiento no tiene una reserva activa.");
                    return;
                }

            } else if (opcionCompra == 2) {
                // Compra directa
                System.out.print("Ingrese el número de asiento a comprar: ");
                numeroAsiento = scanner.nextInt();

                if (numeroAsiento >= 1 && numeroAsiento <= 100 &&
                        estadoAsientos[numeroAsiento].equals("DISPONIBLE")) {

                    estadoAsientos[numeroAsiento] = "VENDIDO";
                    totalAsientosDisponibles--;
                    totalAsientosVendidos++;
                    accionDeseada = "compra directa";

                    System.out.println("¡Compra realizada exitosamente!");

                } else {
                    System.out.println("El asiento no está disponible para compra.");
                    return;
                }
            } else {
                System.out.println("Opción no válida.");
                return;
            }

            // Mostrar resumen de compra
            System.out.println("\n--- RESUMEN DE COMPRA ---");
            System.out.println("Teatro: " + nombreTeatro);
            System.out.println("Asiento: " + numeroAsiento);
            System.out.println("Precio: $" + String.format("%.0f", precioUnitario));
            System.out.println("Tipo de operación: " + accionDeseada);

        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("Error al procesar la compra. Ingrese números válidos.");
        }
    }

    // Método para imprimir boleta
    public void imprimirBoleta(Scanner scanner) {
        int numeroAsiento;

        System.out.print("Ingrese el número de asiento para imprimir boleta: ");

        try {
            numeroAsiento = scanner.nextInt();

            // Validar que el asiento esté vendido
            if (numeroAsiento < 1 || numeroAsiento > 100) {
                System.out.println("Número de asiento inválido.");
                return;
            }

            if (!estadoAsientos[numeroAsiento].equals("VENDIDO")) {
                System.out.println("No se puede imprimir boleta. El asiento no ha sido vendido.");
                return;
            }

            // Generar boleta
            System.out.println("Generando boleta...");
            generarBoleta(numeroAsiento, 1, precioUnitario);

        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("Error al procesar la solicitud. Ingrese un número válido.");
        }
    }

    // Método para generar boleta con información relevante
    public void generarBoleta(int numeroAsiento, int cantidadEntradas, double costoTotal) {
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        System.out.println("--- BOLETA DE ENTRADA ---");
        System.out.println("Teatro: " + nombreTeatro);
        System.out.println("Fecha y Hora: " + ahora.format(formatter));
        System.out.println("Número de Asiento: " + numeroAsiento);
        System.out.println("Cantidad de Entradas: " + cantidadEntradas);
        System.out.println("Precio Unitario: $" + String.format("%.0f", precioUnitario));
        System.out.println("Costo Total: $" + String.format("%.0f", costoTotal));
        System.out.println("Estado: PAGADO");
        System.out.println("¡Disfrute de la función!");
    }
}