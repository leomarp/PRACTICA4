package servicios;
import encapsulacion.Producto;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ProductoServices extends GestionDB<Producto>{
    private static ProductoServices instancia;

    private ProductoServices() { super(Producto.class); }

    public static ProductoServices getInstancia(){
        if(instancia==null){
            instancia = new ProductoServices();
        }
        return instancia;
    }
    public List<Producto> findAllById(int ID){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT P FROM PRODUCTO P WHERE (P.ID = :ID)");
        query.setParameter("ID", ID);
        List<Producto> lista = query.getResultList();
        return lista;
    }

    public List<Producto> consultaNativa(){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("SELECT * FROM PRODUCTO ", Producto.class);
        List<Producto> lista = query.getResultList();
        return lista;
    }
    public boolean verificarExisteId(int id){
        boolean aux = false;
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("SELECT * FROM PRODUCTO", Producto.class);
        List<Producto> lista = query.getResultList();

        for(Producto p : lista){
            if(p.getId() == id){
                aux = true;
                break;
            }
        }

        return aux;
    }

}
