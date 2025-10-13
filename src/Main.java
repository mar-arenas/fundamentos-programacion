import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Teatro teatro = new Teatro();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Sistema de Venta de Entradas Teatro Moro ---");
            System.out.println("1. Comprar entrada");
            System.out.println("2. Ver entradas vendidas");
            System.out.println("3. Actualizar entrada");
            System.out.println("4. Eliminar entrada");
            System.out.println("5. Ver estadísticas");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();
            switch (opcion) {
                case "1":
                    comprarEntrada();
                    break;
                case "2":
                    mostrarEntradas();
                    break;
                case "3":
                    actualizarEntrada();
                    break;
                case "4":
                    eliminarEntrada();
                    break;
                case "5":
                    mostrarEstadisticas();
                    break;
                case "6":
                    System.out.println("Gracias por usar el sistema. ¡Hasta luego!");
                    return;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
    }

    private static void comprarEntrada() {
        try {
            String nombre;
            do {
                System.out.print("Ingrese nombre del cliente: ");
                nombre = scanner.nextLine();
                if (nombre == null || nombre.trim().isEmpty()) {
                    System.out.println("El nombre no puede estar vacio:");
                }
            } while (nombre == null || nombre.trim().isEmpty());

            int edad = leerEntero("Edad del cliente: ");
            if (edad < 0 || edad > 120) {
                throw new IllegalArgumentException("Edad debe estar entre 0 y 120 años");
            }

            String tipoCliente = obtenerTipoCliente(edad);
            System.out.println("Tipo de cliente detectado: " + tipoCliente);

            mostrarSeccionesConPrecios();

            int indiceUbicacion = leerEntero("Seleccione la sección (1-" + Teatro.ubicaciones.length + "): ") - 1;
            if (indiceUbicacion < 0 || indiceUbicacion >= Teatro.ubicaciones.length) {
                throw new IllegalArgumentException("Sección inválida");
            }

            List<Asiento> disponibles = teatro.getAsientosDisponiblesPorIndice(indiceUbicacion);
            if (disponibles.isEmpty()) {
                System.out.println("No hay asientos disponibles en esta sección.");
                return;
            }

            System.out.print("Asientos disponibles: ");
            for (Asiento a : disponibles) {
                System.out.print(a.getNumero() + " ");
            }

            int numAsiento = leerEntero("\nSeleccione el número de asiento: ");
            Asiento asientoSeleccionado = null;
            for (Asiento a : disponibles) {
                if (a.esAsientoNumero(numAsiento)) {
                    asientoSeleccionado = a;
                    break;
                }
            }

            if (asientoSeleccionado == null) {
                throw new IllegalArgumentException("Asiento no disponible");
            }

            asientoSeleccionado.ocupar();
            double precioBase = teatro.getPrecioPorIndice(indiceUbicacion);
            double descuento = calcularDescuento(tipoCliente);

            Entrada entrada = new Entrada(new Cliente(nombre, edad, tipoCliente), asientoSeleccionado, precioBase, descuento);
            teatro.agregarEntrada(entrada);
            imprimirBoleta(entrada);

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    private static void mostrarSeccionesConPrecios() {
        System.out.println("Secciones disponibles: ");
        for (int i = 0; i < Teatro.ubicaciones.length; i++) {
            double precio = teatro.getPrecioPorIndice(i);
            System.out.printf("%d. %s - $%.0f\n", i + 1, Teatro.ubicaciones[i], precio);
        }
    }

    private static void mostrarEntradas() {
        List<Entrada> entradas = teatro.getEntradas();
        if (entradas.isEmpty()) {
            System.out.println("No hay entradas vendidas.");
            return;
        }
        int i = 1;
        for (Entrada e : entradas) {
            System.out.printf("%d. %s - %s - Asiento %d\n",
                i++, e.getCliente().getNombre(),
                e.getAsiento().getUbicacion(),
                e.getAsiento().getNumero());
        }
    }

    private static void actualizarEntrada() {
        mostrarEntradas();
        List<Entrada> entradas = teatro.getEntradas();
        if (entradas.isEmpty()) return;

        try {
            int idx = leerEntero("Ingrese el número de la entrada a actualizar: ");
            if (idx < 1 || idx > entradas.size()) {
                throw new IllegalArgumentException("Índice inválido");
            }
            Entrada entrada = entradas.get(idx - 1);

            System.out.print("Nuevo nombre del cliente (actual: " + entrada.getCliente().getNombre() + ", presione Enter para mantener): ");
            String nuevoNombre = scanner.nextLine().trim();
            if (!nuevoNombre.isEmpty()) {
                entrada.getCliente().setNombre(nuevoNombre);
            }

            System.out.print("Nueva edad del cliente (actual: " + entrada.getCliente().getEdad() + ", -1 para mantener): ");
            int nuevaEdad = leerEntero("");
            if (nuevaEdad >= 0 && nuevaEdad <= 120) {
                entrada.getCliente().setEdad(nuevaEdad);
                String nuevoTipoCliente = obtenerTipoCliente(nuevaEdad);
                entrada.getCliente().setTipo(nuevoTipoCliente);

                // Recalcular descuento con nuevo tipo
                double nuevoDescuento = calcularDescuento(nuevoTipoCliente);
                entrada.setDescuento(nuevoDescuento);
            }

            System.out.print("¿Cambiar asiento? (s/n, actual: " + entrada.getAsiento().getUbicacion() + " - Asiento " + entrada.getAsiento().getNumero() + "): ");
            String cambiarAsiento = scanner.nextLine().trim().toLowerCase();
            if (cambiarAsiento.equals("s")) {
                // Liberar asiento actual primero
                entrada.getAsiento().liberar();

                // Mostrar secciones disponibles
                mostrarSeccionesConPrecios();
                int nuevaUbicacion = leerEntero("Seleccione la nueva sección (1-" + Teatro.ubicaciones.length + "): ") - 1;

                if (nuevaUbicacion >= 0 && nuevaUbicacion < Teatro.ubicaciones.length) {
                    List<Asiento> nuevosDisponibles = teatro.getAsientosDisponiblesPorIndice(nuevaUbicacion);

                    if (!nuevosDisponibles.isEmpty()) {
                        // Mostrar asientos disponibles en la nueva sección
                        System.out.print("Asientos disponibles en " + Teatro.ubicaciones[nuevaUbicacion] + ": ");
                        for (Asiento a : nuevosDisponibles) {
                            System.out.print(a.getNumero() + " ");
                        }

                        // Permitir selección específica de asiento
                        int nuevoNumAsiento = leerEntero("\nSeleccione el número de asiento: ");
                        Asiento asientoSeleccionado = null;

                        for (Asiento a : nuevosDisponibles) {
                            if (a.esAsientoNumero(nuevoNumAsiento)) {
                                asientoSeleccionado = a;
                                break;
                            }
                        }

                        if (asientoSeleccionado != null) {
                            // Asignar el nuevo asiento
                            entrada.setAsiento(asientoSeleccionado);
                            entrada.getAsiento().ocupar();

                            // Actualizar precio base
                            double nuevoPrecioBase = teatro.getPrecioPorIndice(nuevaUbicacion);
                            entrada.setPrecioBase(nuevoPrecioBase);

                            System.out.println("Asiento actualizado exitosamente.");
                        } else {
                            // Si el asiento seleccionado no está disponible, reasignar el asiento original
                            entrada.getAsiento().ocupar();
                            throw new IllegalArgumentException("Asiento " + nuevoNumAsiento + " no disponible. Manteniendo asiento original.");
                        }
                    } else {
                        // Si no hay asientos disponibles, reasignar el asiento original
                        entrada.getAsiento().ocupar();
                        System.out.println("No hay asientos disponibles en esa sección. Manteniendo asiento actual.");
                    }
                } else {
                    // Si la sección es inválida, reasignar el asiento original
                    entrada.getAsiento().ocupar();
                    throw new IllegalArgumentException("Sección inválida. Manteniendo asiento actual.");
                }
            }

            System.out.println("Entrada actualizada exitosamente:");
            imprimirBoleta(entrada);

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    private static void eliminarEntrada() {
        mostrarEntradas();
        List<Entrada> entradas = teatro.getEntradas();
        if (entradas.isEmpty()) return;

        try {
            int idx = leerEntero("Ingrese el número de la entrada a eliminar: ");
            if (idx < 1 || idx > entradas.size()) {
                throw new IllegalArgumentException("Índice inválido");
            }

            Entrada entrada = entradas.get(idx - 1);

            // Mostrar descripción de la entrada a eliminar
            System.out.printf("\nEntrada a eliminar: %s - %s - Asiento %d\n",
                entrada.getCliente().getNombre(),
                entrada.getAsiento().getUbicacion(),
                entrada.getAsiento().getNumero());

            // Pedir confirmación
            System.out.print("¿Está seguro que desea eliminar esta entrada? (s/n): ");
            String confirmacion = scanner.nextLine().trim().toLowerCase();

            if (confirmacion.equals("s") || confirmacion.equals("si")) {
                teatro.eliminarEntrada(entrada);
                System.out.println("Entrada eliminada exitosamente y asiento liberado.");
            } else {
                System.out.println("Eliminación cancelada. La entrada se mantiene.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    private static void mostrarEstadisticas() {
        int totalEntradas = teatro.getEntradas().size();
        double ingresosTotales = 0;
        Map<String, Integer> resumenPorTipo = new HashMap<>();

        for (Entrada e : teatro.getEntradas()) {
            ingresosTotales += e.getPrecioFinal();
            String tipoCliente = e.getCliente().getTipo();
            resumenPorTipo.put(tipoCliente, resumenPorTipo.getOrDefault(tipoCliente, 0) + 1);
        }

        System.out.println("\n--- Estadísticas ---");
        System.out.println("Total de entradas vendidas: " + totalEntradas);
        System.out.printf("Ingresos totales: $%.2f\n", ingresosTotales);
        System.out.println("Resumen por tipo de cliente:");
        for (Map.Entry<String, Integer> entry : resumenPorTipo.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " entradas");
        }
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido.");
            }
        }
    }

    private static String obtenerTipoCliente(int edad) {
        List<String> tiposAplicables = new ArrayList<>();

        // Verificar todos los tipos de descuento aplicables
        if (edad < 12) {
            tiposAplicables.add("niño");
        }

        System.out.print("¿Es mujer? (s/n): ");
        String esMujer = scanner.nextLine().trim().toLowerCase();
        if (esMujer.equals("s")) {
            tiposAplicables.add("mujer");
        }

        System.out.print("¿Es estudiante? (s/n): ");
        String esEstudiante = scanner.nextLine().trim().toLowerCase();
        if (esEstudiante.equals("s")) {
            tiposAplicables.add("estudiante");
        }

        if (edad >= 60) {
            tiposAplicables.add("tercera edad");
        }

        // Si no hay descuentos aplicables, es adulto
        if (tiposAplicables.isEmpty()) {
            tiposAplicables.add("adulto");
        }

        // Encontrar el tipo con mayor descuento
        String mejorTipo = "adulto";
        double mayorDescuento = 0.0;

        for (String tipo : tiposAplicables) {
            double descuento = teatro.getDescuentoPorTipo(tipo);
            if (descuento > mayorDescuento) {
                mayorDescuento = descuento;
                mejorTipo = tipo;
            }
        }

        // Mostrar información de descuentos aplicables si hay más de uno
        if (tiposAplicables.size() > 1) {
            System.out.println("Descuentos aplicables:");
            for (String tipo : tiposAplicables) {
                double descuento = teatro.getDescuentoPorTipo(tipo);
                System.out.printf("- %s: %.0f%%\n", tipo, descuento * 100);
            }
            System.out.printf("Se aplicará el mayor descuento: %s (%.0f%%)\n", mejorTipo, mayorDescuento * 100);
        }

        return mejorTipo;
    }

    private static double calcularDescuento(String tipoCliente) {
        return teatro.getDescuentoPorTipo(tipoCliente);
    }

    private static void imprimirBoleta(Entrada entrada) {
        System.out.println("\n--- Boleta ---");
        System.out.println("Cliente: " + entrada.getCliente().getNombre());
        System.out.println("Edad: " + entrada.getCliente().getEdad());
        System.out.println("Tipo: " + entrada.getCliente().getTipo());
        System.out.println("Sección: " + entrada.getAsiento().getUbicacion());
        System.out.println("Asiento: " + entrada.getAsiento().getNumero());
        System.out.printf("Precio base: $%.0f\n", entrada.getPrecioBase());
        System.out.printf("Descuento: %.0f%%\n", entrada.getDescuento() * 100);
        System.out.printf("Total a pagar: $%.0f\n", entrada.getPrecioFinal());
        System.out.println("----------------");
    }
}
