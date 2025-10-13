import java.util.*;

public class Teatro {
    public static final String[] ubicaciones = {"vip", "palco", "platea baja", "platea alta", "galeria"};
    private List<Asiento> asientos;
    private List<Entrada> entradas;
    private Map<String, Double> precios;
    private Map<String, Double> descuentos;

    public Teatro() {
        asientos = new ArrayList<>();
        entradas = new ArrayList<>();
        precios = new HashMap<>();
        descuentos = new HashMap<>();

        // Inicializar precios
        precios.put("vip", 20000.0);
        precios.put("palco", 15000.0);
        precios.put("platea baja", 12000.0);
        precios.put("platea alta", 10000.0);
        precios.put("galeria", 8000.0);

        // Inicializar descuentos de forma dinámica
        inicializarDescuentos();
        inicializarAsientos();
    }

    private void inicializarDescuentos() {
        descuentos.put("niño", 0.05);
        descuentos.put("mujer", 0.07);
        descuentos.put("estudiante", 0.25);
        descuentos.put("tercera edad", 0.30);
        descuentos.put("adulto", 0.0);
    }

    private void inicializarAsientos() {
        int numPorSeccion = 10;
        for (String ubicacion : ubicaciones) {
            for (int i = 1; i <= numPorSeccion; i++) {
                asientos.add(new Asiento(ubicacion, i));
            }
        }
    }

    public List<Asiento> getAsientosDisponiblesPorIndice(int indiceUbicacion) {
        if (indiceUbicacion < 0 || indiceUbicacion >= ubicaciones.length) return Collections.emptyList();
        String ubicacion = ubicaciones[indiceUbicacion];
        List<Asiento> disponibles = new ArrayList<>();
        for (Asiento a : asientos) {
            if (a.getUbicacion().equals(ubicacion) && a.isDisponible()) {
                disponibles.add(a);
            }
        }
        return disponibles;
    }

    public double getPrecioPorIndice(int indiceUbicacion) {
        if (indiceUbicacion < 0 || indiceUbicacion >= ubicaciones.length) return 0.0;
        return precios.getOrDefault(ubicaciones[indiceUbicacion], 0.0);
    }

    public void agregarEntrada(Entrada entrada) {
        entradas.add(entrada);
    }

    public List<Entrada> getEntradas() {
        return entradas;
    }

    public void eliminarEntrada(Entrada entrada) {
        entradas.remove(entrada);
        entrada.getAsiento().liberar();
    }

    public double getDescuentoPorTipo(String tipoCliente) {
        return descuentos.getOrDefault(tipoCliente, 0.0);
    }
}
