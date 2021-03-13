package servicios;

import org.h2.tools.Server;

import java.sql.SQLException;

public class BDservices {

        private static BDservices instancia;

        private BDservices(){

        }

        public static BDservices getInstancia(){
            if(instancia == null){
                instancia=new BDservices();
            }
            return instancia;
        }

        public void startDb() {
            try {
                //
                Server.createTcpServer("-tcpPort",
                        "9092",
                        "-tcpAllowOthers",
                        "-tcpDaemon").start();
                //Abriendo el cliente web.
                String status = Server.createWebServer("-trace", "-webPort", "0").start().getStatus();
                //
                System.out.println("Status Web: "+status);
            }catch (SQLException ex){
                System.out.println("Problema con la base de datos: "+ex.getMessage());
            }
        }

        public void init(){
            startDb();
        }
    }

