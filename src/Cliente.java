public class Cliente {
    private String nombre;
    private int edad;
    private String tipo; // "niño", "mujer", "estudiante", "tercera edad", "adulto"

    public Cliente(String nombre, int edad, String tipo) {
        this.nombre = nombre;
        this.edad = edad;
        this.tipo = tipo;
    }

    public String getNombre() { return nombre; }
    public int getEdad() { return edad; }
    public String getTipo() { return tipo; }

    // Métodos setters para actualización
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEdad(int edad) { this.edad = edad; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
