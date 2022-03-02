package edu.escuelaing.arep.htttpServer;

public class iniciarServicios {
    public static HttpService service;

    public static void main(String[] args) throws Exception {
        try {
            HttpServerWithThread.init();
            Subprocesos.start();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
