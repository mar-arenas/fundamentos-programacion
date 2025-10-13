public class Asiento {
    private String ubicacion;
    private int numero;
    private boolean disponible;

    public Asiento(String ubicacion, int numero) {
        this.ubicacion = ubicacion;
        this.numero = numero;
        this.disponible = true;
    }

    public String getUbicacion() { return ubicacion; }
    public int getNumero() { return numero; }
    public boolean isDisponible() { return disponible; }
    public void ocupar() { this.disponible = false; }
    public void liberar() { this.disponible = true; }

    public boolean esAsientoNumero(int numeroAsiento) {
        return this.numero == numeroAsiento;
    }
}
