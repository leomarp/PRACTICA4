package servicios;

import encapsulacion.Usuario;
import encapsulacion.VentasProductos;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class VentasServices extends GestionDB<VentasProductos>{
    private static VentasServices instancia;

    private VentasServices() { super(VentasProductos.class); }

    public static VentasServices getInstancia(){
        if(instancia==null){
            instancia = new VentasServices();
        }
        return instancia;
    }
    public List<VentasProductos> consultaNativa(){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select * from VENTASPRODUCTOS", VentasProductos.class);
        //query.setParameter("nombre", apellido+"%");
        List<VentasProductos> lista = query.getResultList();
        return lista;
    }

}
