package encapsulacion;


import javax.persistence.*;
import java.util.Date;

@Entity
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_comentario;
    private String contenido;
    private Usuario usuario;
    private Date fechaCreado;

    @ManyToOne (fetch = FetchType.EAGER)
    private Producto producto;

    public Comentario() {

    }

    public Comentario(String contenido, Usuario usuario, Date fechaCreado, Producto producto) {
        this.contenido = contenido;
        this.usuario = usuario;
        this.fechaCreado = fechaCreado;
        this.producto = producto;
    }

    public int getId_comentario() {
        return id_comentario;
    }

    public void setId_comentario(int id_comentario) {
        this.id_comentario = id_comentario;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFechaCreado() {
        return fechaCreado;
    }

    public void setFechaCreado(Date fechaCreado) {
        this.fechaCreado = fechaCreado;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
