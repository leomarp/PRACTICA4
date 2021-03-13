package servicios;

import encapsulacion.Comentario;


import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class ComentariosServices extends GestionDB<Comentario> {
    private static ComentariosServices instancia;

    private ComentariosServices() { super(Comentario.class); }

    public static ComentariosServices getInstancia(){
        if(instancia==null){
            instancia = new ComentariosServices();
        }
        return instancia;
    }
    public List<Comentario> findAllById(int ID){
        EntityManager em = getEntityManager();
        List<Comentario> lista = new ArrayList<Comentario>();
        Query query = em.createNativeQuery("SELECT * FROM COMENTARIO ", Comentario.class);
        List<Comentario> aux =  query.getResultList();

        for(Comentario c : aux){
            if(c.getProducto().getId() == ID){
                lista.add(c);
            }
        }

        return lista;
    }

    public List<Comentario> consultaNativa(){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("SELECT * FROM COMENTARIO  ", Comentario.class);
        List<Comentario> lista = query.getResultList();
        return lista;
    }
    public boolean verificarExisteId(int id){
        boolean aux = false;
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("SELECT * FROM COMENTARIO ", Comentario.class);
        List<Comentario> lista = query.getResultList();

        for(Comentario p : lista){
            if(p.getId_comentario() == id){
                aux = true;
                break;
            }
        }

        return aux;
    }

}
