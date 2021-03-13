package servicios;

import encapsulacion.Foto;

public class FotoServices extends GestionDB<Foto>{

    private static FotoServices instancia;

    private FotoServices() { super(Foto.class); }

    public static FotoServices getInstancia(){
        if(instancia==null){
            instancia = new FotoServices();
        }

        return instancia;
    }

    /**
     * METHODS FOR THIS CLASS
     */



}