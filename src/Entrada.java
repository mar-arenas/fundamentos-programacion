public class Entrada {
    private Cliente cliente;
    private Asiento asiento;
    private double precioBase;
    private double descuento;
    private double precioFinal;

    public Entrada(Cliente cliente, Asiento asiento, double precioBase, double descuento) {
        this.cliente = cliente;
        this.asiento = asiento;
        this.precioBase = precioBase;
        this.descuento = descuento;
        this.precioFinal = precioBase - (precioBase * descuento);
    }

    public Cliente getCliente() { return cliente; }
    public Asiento getAsiento() { return asiento; }
    public double getPrecioBase() { return precioBase; }
    public double getDescuento() { return descuento; }
    public double getPrecioFinal() { return precioFinal; }

    // Métodos setters para actualización
    public void setAsiento(Asiento asiento) { this.asiento = asiento; }
    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }
    public void setDescuento(double descuento) {
        this.descuento = descuento;
        this.precioFinal = precioBase - (precioBase * descuento);
    }
}
