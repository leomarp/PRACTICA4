package servicios;
import encapsulacion.Producto;
import encapsulacion.ventaprod;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;


public class ventaprodservices extends GestionDB<ventaprod> {


    private static servicios.ventaprodservices instancia;

    private ventaprodservices() { super(ventaprod.class); }

    public static servicios.ventaprodservices getInstancia(){
        if(instancia==null){
            instancia = new servicios.ventaprodservices();
        }
        return instancia;
    }

    public List<ventaprod> consultaNativa(){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("SELECT * FROM VENTPROD", Producto.class);
        List<ventaprod> lista = query.getResultList();
        return lista;
    }
}
