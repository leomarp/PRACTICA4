package servicios;
import encapsulacion.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class UsuarioServices extends GestionDB<Usuario>{
    private static UsuarioServices instancia;

    private UsuarioServices() { super(Usuario.class); }

    public static UsuarioServices getInstancia(){
        if(instancia==null){
            instancia = new UsuarioServices();
        }
        return instancia;
    }

    public List<Usuario> findAllByNombre(String nombre){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select U from USUARIO U where U.nombre like :nombre");
        query.setParameter("nombre", nombre+"%");
        List<Usuario> lista = query.getResultList();
        return lista;
    }

    public List<Usuario> consultaNativa(){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select * from USUARIO ", Usuario.class);
        //query.setParameter("nombre", apellido+"%");
        List<Usuario> lista = query.getResultList();
        return lista;
    }

    public Usuario buscarUsuariobyUser(String user){
        Usuario aux = null;
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select * from USUARIO", Usuario.class);
        //query.setParameter("user", user+"%");
        List<Usuario> lista = query.getResultList();

        for(Usuario u : lista){
            System.out.println(u.getNombre());
            if(u.getUsuario().equals(user)){
                aux = u;
                break;
            }
        }

        return aux;
    }

    public boolean confirmarUsuario(String user, String password){
        boolean aux = false;

        Usuario usuario_en_db = buscarUsuariobyUser(user);
        if(usuario_en_db != null){
            if(usuario_en_db.getPassword().equals(password)){
                aux = true;
            }
        }

        return aux;
    }
}
