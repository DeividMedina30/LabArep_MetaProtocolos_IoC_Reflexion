package edu.escuelaing.arep.htttpServer;

public class iniciarServicios {
    public static HttpService service;

    /**
     * Clase principal la cual llama las clases HttpServerWithThread y Subprocesos
     * @param args - String [].
     * @throws Exception - Control de excepciones.
     */
    public static void main(String[] args) throws Exception {
        try {
            HttpServerWithThread.init();
            Subprocesos.start();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
