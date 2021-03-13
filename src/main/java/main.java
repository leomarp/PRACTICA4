import encapsulacion.*;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import servicios.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

public class main {
    public static void main(String[] args) {

        CarroCompra carroCompra = new CarroCompra(null);
        AtomicBoolean status = new AtomicBoolean(false);

        BDservices.getInstancia().init();
        if(UsuarioServices.getInstancia().buscarUsuariobyUser("admin") == null){
            UsuarioServices.getInstancia().crear(new Usuario("admin","Administrador", "admin"));
        }

        Javalin app = Javalin.create(config ->{
            config.addStaticFiles("/Publico"); //desde la carpeta de resources
            config.registerPlugin(new RouteOverviewPlugin("/rutas")); //aplicando plugins de las rutas
        }).start(8000);

        /* MANEJO DE LA PAGINA PRINCIPAL*/
        app.get("/", ctx ->{ ctx.redirect("/Principal");});

        app.get("/Principal", ctx -> {

            Map<String, Object> modelo = new HashMap<>();
            List<Producto> lista = ProductoServices.getInstancia().consultaNativa();
            int aux = carroCompra.getCantidadDeproductos();
            modelo.put("cantidad",aux);
            modelo.put("titulo", "Listado de productos");
            modelo.put("lista", lista);
            //enviando al sistema de plantilla.

            ctx.render("Publico/principal.html", modelo);

        });




        /* MANEJO DEL LOGIN Y REGISTRAR*/

        app.before("/login", ctx -> {

            print("Entro a verificar el login");
            String usuario = ctx.formParam("user");
            String contrasena = ctx.formParam("password");


            if(!UsuarioServices.getInstancia().confirmarUsuario(usuario,contrasena)){
                ctx.redirect("login.html");
            }

        });

        app.get("/login", ctx -> {

            ctx.redirect("login.html");

        });

        app.post("/login", ctx -> {

            print("Entro a verificar el login");
            String usuario = ctx.formParam("user");
            String contrasena = ctx.formParam("password");
            if(UsuarioServices.getInstancia().confirmarUsuario(usuario, contrasena)){
                ctx.req.getSession().invalidate();
                ctx.sessionAttribute("user",usuario);
                print("Usuario: "+ usuario);
                String id = ctx.req.getSession().getId();
                carroCompra.setId(id);
            }else{
                ctx.redirect("/login");
            }

            print(carroCompra.getId());
            ctx.redirect("/");

        });
        /* REGISTRAR*/
        app.get("/registar", ctx -> {

            ctx.redirect("/nuevousuario.html");

        });

        app.post("/registar", ctx -> {

            String nombre = ctx.formParam("nombre");
            String user = ctx.formParam("usuario");
            String password = ctx.formParam("contrasena");
            Usuario tmp = new Usuario(user,nombre,password);
            UsuarioServices.getInstancia().crear(tmp);
            //control.crearUsuario(tmp);
            print("Se creo usuario");
            //print(""+control.getUsuarios().size());
            ctx.redirect("/login.html");

        });


        /* MANEJO DE LA PARTE DE GESTION DE PRODUCTOS*/
        app.before("/admprod", ctx -> {
            print("Entro a verificar");
            if(ctx.sessionAttribute("user")==null) {
                print("No usuario");
                ctx.redirect("/login.html");
            }
        });

        app.get("/admprod", ctx ->{

            List<Producto> lista = ProductoServices.getInstancia().consultaNativa();

            if(ctx.sessionAttribute("user").equals("admin")){
                Map<String, Object> modelo = new HashMap<>();
                modelo.put("titulo", "Lista de productos");
                modelo.put("lista", lista);
                ctx.render("Publico/admproductos.html", modelo);
            }else{
                ctx.result("No tienes permiso para acceder a esta pagina");
            }

        });
        app.get("/nuevoproducto", ctx -> {
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("titulo", "Crear nuevo producto");
            modelo.put("boton", "Crear");
            modelo.put("accion", "/nuevoproducto");
            modelo.put("error", "");
            ctx.render("Publico/editarcrearprod.html",modelo);
        });

        app.post("/nuevoproducto", ctx -> {
            Map<String, Object> modelo = new HashMap<>();

            int id = ctx.formParam("id", Integer.class).get();
            String nombre = ctx.formParam("nombre");
            int cantidad = ctx.formParam("cantidad", Integer.class).get();
            String auxprecio = ctx.formParam("precio");
            BigDecimal precio = new BigDecimal(auxprecio);
            String descripcion = ctx.formParam("descripcion");
            if(ProductoServices.getInstancia().verificarExisteId(id)){
                modelo.put("titulo", "Crear nuevo producto");
                modelo.put("boton", "Crear");
                modelo.put("error", "ERROR: YA EXISTE UN PRODUCTO CON ESE ID");
                ctx.render("Publico/editarcrearprod.html", modelo);

            }else {
                Producto prod = new Producto(id, nombre, cantidad, precio, descripcion);
                ProductoServices.getInstancia().crear(prod);
                modelo.put("titulo", "Crear nuevo producto");
                modelo.put("boton", "Crear");
                modelo.put("error", "");

                ctx.uploadedFiles("foto").forEach(uploadedFile -> {
                    try {

                        byte[] bytes = uploadedFile.getContent().readAllBytes();
                        String encodedString = Base64.getEncoder().encodeToString(bytes);
                        Foto foto = new Foto(uploadedFile.getFilename(), uploadedFile.getContentType(), encodedString, prod);
                        FotoServices.getInstancia().crear(foto);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    ctx.redirect("/admprod");
                ctx.render("Publico/editarcrearprod.html",modelo);

                });
            }
        });

        app.get("/editar/:id", ctx-> {
            Map<String, Object> modelo = new HashMap<>();
            Producto producto = ProductoServices.getInstancia().find(ctx.pathParam("id", Integer.class).get());
            modelo.put("titulo", "Editar producto");
            modelo.put("boton", "Editar");
            modelo.put("idval", Integer.toString(producto.getId()));
            modelo.put("nombreval", producto.getNombre());
            modelo.put("cantidadval", Integer.toString(producto.getCantidad()));
            modelo.put("precioval", producto.getPrecio().toString());
            modelo.put("descripcion", producto.getDescripcion());
            modelo.put("accion", "/editar");
            ctx.render("Publico/editarcrearprod.html", modelo);

        });

        app.post("/editar", ctx -> {
            Map<String, Object> modelo = new HashMap<>();

            int id = ctx.formParam("id", Integer.class).get();
            String nombre = ctx.formParam("nombre");
            int cantidad = ctx.formParam("cantidad", Integer.class).get();
            String auxprecio = ctx.formParam("precio");
            BigDecimal precio = new BigDecimal(auxprecio);
            String descripcion = ctx.formParam("descripcion");
            Producto tmp = new Producto(id,nombre,cantidad,precio, descripcion);

            if(ProductoServices.getInstancia().editar(tmp) == null){
                modelo.put("error","Error: Producto no encontrado");
                modelo.put("accion", "/editar");
                modelo.put("titulo", "Editar producto");
                modelo.put("boton", "Editar");
                ctx.render("Publico/editarcrearprod.html", modelo);
            }else{
                ctx.redirect("/admprod");
            }

        });

        app.get("/eliminar/:id", ctx-> {
            Map<String, Object> modelo = new HashMap<>();
            //Producto producto = ProductoServices.getInstancia().find(ctx.pathParam("id", Integer.class).get());
            ProductoServices.getInstancia().eliminar(ctx.pathParam("id", Integer.class).get());
            ctx.redirect("/admprod");

        });

        /*CONTROLANDO LOS ITEMS ANADIDOS AL CARRITO DE UN USUARIO PARTICULAR*/
        app.post("/agregarcar", ctx -> {
            Map<String, Object> modelo = new HashMap<>();

            Producto producto = ProductoServices.getInstancia().find(ctx.formParam("id", Integer.class).get());
            int cantidad = ctx.formParam("cantidad", Integer.class).get();
            carroCompra.updateCantidad(cantidad);
            ventaprod aux = carroCompra.buscarProductoenelcarritobyID(producto.getId());
            if(aux != null){
                print("ENCONTRO EL PRODUCTO");
                int pos = carroCompra.getListaProductos().indexOf(aux);
                carroCompra.getListaProductos().get(pos).updateCantidad(cantidad);
            }else{
                carroCompra.setListaProductos(new ventaprod(producto, cantidad));
            }




            ctx.redirect("/");

        });
        /*CONTROLANDO EL ACCESO AL CARRITO*/
        app.before("/carrito", ctx -> {
            print("Entro a verificar");
            if(ctx.sessionAttribute("user")==null) {
                print("No usuario");
                ctx.redirect("/login.html");
            }
        });

        app.get("/carrito", ctx -> {
            //tomando el parametro utl y validando el tipo.
            List<ventaprod> lista = carroCompra.getListaProductos();



            String u = ctx.sessionAttribute("user").toString();
            Usuario user = UsuarioServices.getInstancia().buscarUsuariobyUser(u);
            BigDecimal total = new BigDecimal(0);
            String auxp;
            for(ventaprod ca : lista){
                auxp = String.valueOf((float) ca.getCantidad() * ca.getProducto().getPrecio().floatValue());
                print(auxp);
                BigDecimal pass = new BigDecimal(auxp);
                total = total.add(pass);
            }
            print("Total: " + total);
            Map<String, Object> modelo = new HashMap<>();
            int aux = carroCompra.getCantidadDeproductos();
            modelo.put("cantidad",aux);
            modelo.put("titulo", "Carro de compras");
            modelo.put("nombrecliente", user.getNombre());
            modelo.put("total", total);
            modelo.put("lista", lista);
            //enviando al sistema de plantilla.
            ctx.render("Publico/carro.html", modelo);
        });

        app.get("/eliminaritemcarro/:id", ctx -> {
            Map<String, Object> modelo = new HashMap<>();
            Producto producto = ProductoServices.getInstancia().find(ctx.formParam("id", Integer.class).get());
            carroCompra.getListaProductos().remove(producto);
            ctx.redirect("/carrito");

        });
        app.before("/vender", ctx -> {
            print("Entro a verificar");
            if(ctx.sessionAttribute("user")==null) {
                print("No usuario");
                ctx.redirect("/login.html");
            }
        });
        app.get("/vender", ctx ->{
            String id = ctx.req.getSession().getId();
            Date fechacompra = new Date();
            String u = ctx.sessionAttribute("user").toString();
            Usuario user = UsuarioServices.getInstancia().buscarUsuariobyUser(u);
            if(user!=null){
                ArrayList<ventaprod> tmp = new ArrayList<>();

                BigDecimal total = new BigDecimal(0);
                String auxp;
                for(ventaprod ca : carroCompra.getListaProductos()){
                    auxp = String.valueOf((float) ca.getCantidad() * ca.getProducto().getPrecio().floatValue());
                    print(auxp);
                    BigDecimal pass = new BigDecimal(auxp);
                    total = total.add(pass);

                    tmp.add(ca);
                    ventaprodservices.getInstancia().crear(ca);

                }
                print(tmp.toString());
                VentasProductos venta = new VentasProductos(id, fechacompra, user.getNombre(),
                        tmp, total.floatValue());

                VentasServices.getInstancia().crear(venta);
                //control.setVentas(venta);
                //print("Cantidad de ventas: "+);
                carroCompra.getListaProductos().clear();
                carroCompra.resetCantidad();
            }

            ctx.redirect("/carrito");

        });
        /* Comentarios
         * */
        app.before("/comentario/:id", ctx -> {
            print("Entro a verificar");
            if(ctx.sessionAttribute("user")==null) {
                print("No usuario");
                ctx.redirect("/login.html");
            }
        });
        app.get("/comentario/:id", ctx -> {
            Producto producto = ProductoServices.getInstancia().find(ctx.pathParam("id", Integer.class).get());
            Map<String, Object> modelo = new HashMap<>();

            modelo.put("nombre", producto.getNombre());
            modelo.put("descripcion", producto.getDescripcion());
            modelo.put("id", producto.getId());
            modelo.put("fotos", producto.getFotos());
            modelo.put("listaComentarios", ComentariosServices.getInstancia().findAllById(producto.getId()));

            print("Nombre desde el comentario: "+ producto.getNombre());
            ctx.render("Publico/Verproductosclientes.html", modelo);

        });

        app.post("/nuevocomentario/:id", ctx -> {
            int id = ctx.pathParam("id", Integer.class).get();

            Producto producto = ProductoServices.getInstancia().find(ctx.pathParam("id", Integer.class).get());
            String contenido = ctx.formParam("contenido");
            String u = ctx.sessionAttribute("user").toString();
            Usuario user = UsuarioServices.getInstancia().buscarUsuariobyUser(u);
            Date fechacompra = new Date();

            print("Esta creando comentarios: " + contenido );
            ComentariosServices.getInstancia().crear(new Comentario(contenido, user, fechacompra, producto));

            ctx.redirect("/comentario/"+ id);

        });

        app.get("/eliminarComentario/:id", ctx -> {
            int id = ctx.pathParam("id", Integer.class).get();

            Comentario comentario = ComentariosServices.getInstancia().find(id);
            int id_producto = comentario.getProducto().getId();
            print("Este el id"+id_producto);
            String usuario = ctx.sessionAttribute("user");
            if(usuario.equals("admin") || usuario.equals(comentario.getUsuario().getUsuario())){
                ComentariosServices.getInstancia().eliminar(id);
                ctx.redirect("/comentario/"+ id_producto);
            }else{
                ctx.result("Solo puedes borrar un comentario tuyo, no de otra persona");
            }
        });

        /*MANEJA LA VISTA DE LAS VENTAS*/
        app.before("/verventas", ctx -> {
            print("Entro a verificar");
            if(ctx.sessionAttribute("user")==null) {
                print("No usuario");
                ctx.redirect("/login.html");
            }
        });


        app.get("/verventas", ctx -> {

            Map<String, Object> modelo = new HashMap<>();

            if(ctx.sessionAttribute("user").equals("admin")){
                List<VentasProductos> lista = VentasServices.getInstancia().consultaNativa();
                modelo.put("titulo", "Historial de ventas");
                modelo.put("lista", lista);
                ctx.render("Publico/verventas.html", modelo);
            }else{
                ctx.result("No tienes permiso para acceder a esta pagina");
            }
        });


    }
    public static void print (String string){

        System.out.println(string);
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 7000; //Retorna el puerto por defecto en caso de no estar en Heroku.
    }

}





