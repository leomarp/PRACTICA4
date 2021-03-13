package encapsulacion;

import javax.persistence.*;
import java.io.Serializable;
import java.nio.MappedByteBuffer;

@Entity
public class ventaprod implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_venta;
    @Lob
    private Producto producto;
    private int cantidad;

    public ventaprod(){}
    public ventaprod(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public int getId() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void updateCantidad(int cantidad){
        this.cantidad += cantidad;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
