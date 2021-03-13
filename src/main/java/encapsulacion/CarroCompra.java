package encapsulacion;

import java.util.ArrayList;


public class CarroCompra {
    private String id;
    private ArrayList<ventaprod> listaProductos;
    private int cantidadDeproductos;


    public CarroCompra(String id) {
        this.id = id;
        this.listaProductos = new ArrayList<ventaprod>();
        this.cantidadDeproductos = 0;
    }

    public int getCantidadDeproductos() {
        return cantidadDeproductos;
    }

    public void setCantidadDeproductos(int cantidadDeproductos) {
        this.cantidadDeproductos = cantidadDeproductos;
    }
    public void updateCantidad(int cant){
        this.cantidadDeproductos += cant;
    }
    public void resetCantidad(){
        this.cantidadDeproductos = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<ventaprod> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(ventaprod ventaprod) {
        this.listaProductos.add(ventaprod);
    }

    public ventaprod buscarProductoenelcarritobyID(int id){
        ventaprod aux =  null;

        for(ventaprod tmp : this.listaProductos){
            if(tmp.getId() == id){
                aux = tmp;
                break;
            }
        }

        return aux;
    }
}
