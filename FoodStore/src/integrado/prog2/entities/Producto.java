package integrado.prog2.entities;

public class Producto extends Base {
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private String imagen;
    private boolean disponible;
    private Long categoriaId; // Relación por ID

    public Producto() {
        super();
    }

    public Producto(String nombre, String descripcion, double precio, int stock, String imagen, boolean disponible, Long categoriaId) {
        super();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.imagen = imagen;
        this.disponible = disponible;
        this.categoriaId = categoriaId;
    }

    // Constructor completo (con ID, para cuando listamos desde la BD)
    public Producto(Long id, String nombre, String descripcion, double precio, int stock, String imagen, boolean disponible, boolean eliminado, Long categoriaId) {
        super(id);
        this.setEliminado(eliminado);
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.imagen = imagen;
        this.disponible = disponible;
        this.categoriaId = categoriaId;
    }

    // Getters y Setters específicos de Producto
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }

    @Override
    public String toString() {
        return "Producto [" +
                "ID=" + getId() +  // Viene de Base
                ", Nombre='" + nombre + '\'' +
                ", Precio=$" + precio +
                ", Stock=" + stock +
                ", Cat_ID=" + categoriaId +
                ", Disp=" + disponible +
                ", Elim=" + isEliminado() + // Viene de Base
                ']';
    }
}
