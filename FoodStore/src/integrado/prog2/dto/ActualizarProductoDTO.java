package foodstore.dto;

public class ActualizarProductoDTO {
    private Long id;
    private Double precio;
    private Integer stock;
    private Long categoriaId;

    public ActualizarProductoDTO(Long id, Double precio, Integer stock, Long categoriaId) {
        this.id = id;
        this.precio = precio;
        this.stock = stock;
        this.categoriaId = categoriaId;
    }

    public Long getId() { return id; }
    public Double getPrecio() { return precio; }
    public Integer getStock() { return stock; }
    public Long getCategoriaId() { return categoriaId; }
}