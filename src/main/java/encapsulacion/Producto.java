package encapsulacion;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Producto implements Serializable {
    @Id
    @Column(name = "prod_id")
    private int id_prod;
    @NotNull
    private String nombre;
    private int cantidad;

    @NotNull
    private BigDecimal precio;
    private String descripcion;

    @OneToMany(mappedBy = "producto", fetch = FetchType.EAGER)
    @Lob
    private List<Foto> fotos = new ArrayList<>();


    public Producto(){};

    public Producto(int id, String nombre, int cantidad, BigDecimal precio, String descripcion) {
        this.id_prod = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id_prod;
    }

    public void setId(int id) {
        this.id_prod = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public List<Foto> getFotos() {
        return fotos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
    }
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
}