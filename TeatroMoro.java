import java.util.Scanner;

public class TeatroMoro {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        System.out.println("Bienvenido al Teatro Moro");

        while (continuar) {

            String[] menu = {"=== MENU PRINCIPAL ===", "1. Comprar entrada", "2. Salir"};

            for (int i = 0; i < menu.length; i++) {
                System.out.println(menu[i]);
            }

            System.out.print("\nSeleccione una opción: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Debe ingresar un número. Intente nuevamente.\n");
                scanner.next();
                continue;
            }

            int opcion = scanner.nextInt();

            if (opcion == 1) {

                System.out.println("\n=== PLANO DEL TEATRO ===");
                System.out.println("ZONA A - $25000 (Cerca del escenario)");
                System.out.println("ZONA B - $20000 (Zona media)");
                System.out.println("ZONA C - $15000 (Zona alta)");
                System.out.println("========================");

                String zona = "";
                int precioBase = 0;

                do {
                    System.out.print("\nIngrese la zona (A, B o C): ");
                    zona = scanner.next().toUpperCase();

                    if (zona.equals("A")) {
                        precioBase = 25000;
                    } else if (zona.equals("B")) {
                        precioBase = 20000;
                    } else if (zona.equals("C")) {
                        precioBase = 15000;
                    } else {
                        System.out.println("Zona inválida. Intente nuevamente.");
                        zona = "";
                    }
                } while (zona.equals(""));

                int edad = -1;
                while (edad < 0 || edad > 120) {
                    System.out.print("Ingrese su edad: ");
                    if (scanner.hasNextInt()) {
                        edad = scanner.nextInt();
                        if (edad < 0 || edad > 120) {
                            System.out.println("Edad inválida. Intente nuevamente.");
                        }
                    } else {
                        System.out.println("Debe ingresar un número válido.");
                        scanner.next();
                    }
                }

                double descuento = 0;
                String tipoDescuento = "Sin descuento";

                if (edad >= 18 && edad <= 25) {
                    System.out.print("¿Es estudiante? (1=Si, 2=No): ");
                    int esEstudiante = scanner.nextInt();
                    if (esEstudiante == 1) {
                        descuento = 0.10; // 10%
                        tipoDescuento = "Estudiante 10%";
                    }
                } else if (edad >= 65) {
                    descuento = 0.15; // 15%
                    tipoDescuento = "Tercera edad 15%";
                }

                double precioFinal;
                int contador = 0;
                do {
                    double montoDescuento = precioBase * descuento;
                    precioFinal = precioBase - montoDescuento;
                    contador++;
                } while (contador < 1);

                System.out.println("\n=== Resumen de Compra ===");
                System.out.println("Zona seleccionada: " + zona);
                System.out.printf("Precio base: $%.0f%n", (double)precioBase);
                System.out.println("Descuento: " + tipoDescuento);
                System.out.printf("PRECIO FINAL: $%.0f%n", precioFinal);
                System.out.println("========================");

                System.out.print("\n¿Desea realizar otra compra? (1=Si, 2=No): ");
                int otraCompra = scanner.nextInt();

                if (otraCompra == 2) {
                    continuar = false;
                }

            } else if (opcion == 2) {
                System.out.println("¡Gracias por visitarnos!");
                continuar = false;
            } else {

                System.out.println("Opción inválida. Intente nuevamente.\n");
            }
        }

        scanner.close();
    }
}
