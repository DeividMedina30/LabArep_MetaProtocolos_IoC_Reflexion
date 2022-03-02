package edu.escuelaing.arep.htttpServer;

import org.reflections.Reflections;

import java.io.*;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

/**
 * Esta clase define el httpService
 * @author: Deivid Medina
 * @version: 02/03/2022
 */


public class HttpService {
    private static Socket socketCliente;
    private static HashMap<String, Handler> listaURLHandler;
    private static ServerSocket socketServer = null;
    private static BufferedReader in;
    private Socket receptor;
    private static String direccion = "";

    /**
     * Clase que me mantiene siempre la escucha, ya que se tiene un while true.
     * @throws Exception - Control de excepciones.
     */
    public void listen() throws Exception{
        while (true){
            socketServer = runServer();
            receptor = recibiendoSolicitud(socketServer);
            empezarAEscuchar();
            leyendoTipodeSolicitud(receptor);
            in.close();
            receptor.close();
            socketServer.close();
        }
    }

    /**
     * Clase que me permite obtener el puerto además de empezar a escuchar.
     * @return ServerSocket - retorna el nuevo puerto.
     */
    public static ServerSocket runServer() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(getPort());
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + getPort());
            System.exit(1);
        }
        return serverSocket;
    }

    /**
     * Clase que permite que el Socket se prepare para recibir las solicitudes.
     * @param serverSocket - Se le envía un ServerSocket
     * @return request - Donde puede ser valida la conexión fallida.
     */
    public static Socket recibiendoSolicitud(ServerSocket serverSocket) {

        Socket request = null;
        try {
            System.out.println("Ready to receive...");
            request = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        return request;
    }

    /**
     * Clase que me retorna el puerto por el cual heroku se ejecute.
     * @return - Retorna por defecto el puerto 4567
     */
    public static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set (i.e. on localhost)
    }

    /**
     * Clase que me permite manejar los diferentes tipos de solicitudes que me soliciten.
     * @param recep - Entre las posibles opciones de solicitudes tenemos /app,.png y .html.
     * @throws IOException - Control de excepciones.
     */
    private void leyendoTipodeSolicitud(Socket recep) throws IOException {
        socketCliente = recep;
        if(direccion.contains("/app")){
            postApplicacion();
        }else if (direccion.contains(".png")){
            postPng();
        } else if (direccion.contains(".html")) {
            postHtml();
        }else{
            paginaNoEncontrada();
        }

    }

    /**
     * Función que me permite realizar la lectura de una imagen a tra vez de byte.
     * @return byte - retorna los byte de la imagen que se este solicitando.
     * @throws MalformedURLException - Control de excepciones.
     */
    public static byte[] leerImagenPng() throws MalformedURLException {
        byte[] imageBytes = null;
        try {
            File imagen = new File("src/main/java/app//imagenesPng"+direccion);
            FileInputStream inputImage = new FileInputStream(imagen); // Es útil para leer datos del tipo primitivo de una forma portable.
            imageBytes = new byte[(int) imagen.length()]; //Obteniendo longitud de la imagen en bytes
            inputImage.read(imageBytes); //Leyendo la imagen
        } catch (IOException io) {
            System.err.println(io);
        }
        return imageBytes;
    }

    /**
     * Función que me permite leer un html correspondiente y así hacer uso de él.
     * @param address - Nombre del html que se esta solicitando.
     * @return Retorna un string el cual contiene toda la información relacionada con el html.
     * @throws MalformedURLException - Control de excepciones
     */
    public static String leerHTML(String address) throws MalformedURLException {
        String html = "";
        try {
            FileReader file = new FileReader("src/main/java/app//html"+address);
            BufferedReader reader = new BufferedReader(file);
            String inputLine = "";
            while ((inputLine = reader.readLine()) != null) {
                html += inputLine + "\n";
            }
        } catch (IOException io) {
            System.err.println(io);
        }
        return html;
    }

    /**
     * En caso de no encontrar un recurso, se hace uso del html, paginaNoencontrada. que muestra una imagen de error.
     */
    private void paginaNoEncontrada() {
        try{
            String outputLine;
            String page = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/html\r\n"
                    + "\r\n" + leerHTML("/paginaNoEncontrada.html");
            outputLine = page;  //Guardando Html
            PrintWriter out = new PrintWriter(socketCliente.getOutputStream(), true); //Imprimir objeto en una secuencia como una salida de texto
            out.println(outputLine); //Imprimir Pagina html
            out.close();  //Cerrar Conexión
        }catch (IOException ex){
            System.err.println("Error en la solicitud.");
        }
    }

    /**
     * Clase que me permite realizar la conexión a peticiones html.
     */
    private void postHtml() {
        try{
            String outputLine;
            String page ="HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/html\r\n"
                    + "\r\n" + leerHTML(direccion);
            outputLine = page;  //Guardando Html
            PrintWriter out = new PrintWriter(socketCliente.getOutputStream(), true); //Imprimir objeto en una secuencia como una salida de texto
            out.println(outputLine); //Imprimir Pagina html
            out.close();  //Cerrar Conexión
        }catch (Exception ex){
            paginaNoEncontrada();
            System.err.println("Error: Archivo html indicado, no se encuentra.");
        }
    }

    /**
     * Clase que me permite realizar la conexión a peticiones de imagenes en formato PNG.
     */
    private void postPng() {
        try {
            byte[] imagen;
            imagen = leerImagenPng();
            DataOutputStream imageCode;
            imageCode = new DataOutputStream(socketCliente.getOutputStream()); //útil para escribir datos del tipo primitivo de una forma portable.
            imageCode.writeBytes("HTTP/1.1 200 OK \r\n");
            imageCode.writeBytes("Content-Type: image/png\r\n");
            imageCode.writeBytes("Content-Length: " + imagen.length);
            imageCode.writeBytes("\r\n\r\n");
            imageCode.write(imagen); //La imagen se hace visible en el servidor
            imageCode.close();
        } catch (IOException ex){
            paginaNoEncontrada();
            System.err.println("Error: No se pudo obtener la imagen solicitada.");
        }
    }

    /**
     * Clase que me permite realizar el llamado correspondientes a diferentes apps que se estén manejando.
     * @throws IOException - Control de excepciones.
     */
    private void postApplicacion() throws IOException {
        PrintWriter out = new PrintWriter(socketCliente.getOutputStream(), true); //PrintWriter imprime representaciones formateadas de objetos en una secuencia como una salida de texto.
        int limit = direccion.indexOf("/app");
        String resource = "";
        for (int i = limit; i < direccion.length() && direccion.charAt(i) != ' '; i++) { //charAt: devuelve en un nuevo String el carácter UTF-16 de una cadena.
            resource += direccion.charAt(i);
        }
        try {
            out.write("HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "\r\n");
            if(resource.contains("?")) {
                int i = resource.indexOf("?");
                String params=resource.substring(i+1);
                if(resource.contains("&")) {
                    String[] arrayP= params.split("&");
                    Object [] objects=new Object[arrayP.length];
                    int j=0;
                    for(String p: arrayP) {
                        objects[j]=p.split("=")[1];
                        j++;
                    }
                    out.write(listaURLHandler.get(resource.substring(0, i)).procesar(objects));
                }else {
                    String other=resource.split("=")[1];
                    out.write(listaURLHandler.get(resource.substring(0, i)).procesar(new Object[]{other}));
                }
            }else { out.write(listaURLHandler.get(resource).procesar());}
            out.close();
        } catch (Exception e) {
            paginaNoEncontrada();
            System.err.println("Error: El recurso solicitado no existe o no se pudo cargar.");
        }
    }

    /**
     * Clase que me permite empezar a escuchar las peticiones.
     * @throws IOException - Control de excepciones.
     */
    private void empezarAEscuchar() throws IOException {
        in = new BufferedReader(new InputStreamReader(receptor.getInputStream()));
        String inputline;
        String [] outpuline;
        while((inputline = in.readLine()) != null){
            if(!in.ready()){
                break;
            }
            if(inputline.contains("GET")){
                outpuline = inputline.split(" ");
                direccion = outpuline[1];
            }
        }
    }

    /**
     * Clase que permite que el Socket se prepare para recibir las solicitudes.
     * @param serverSocket - Se le envía un ServerSocket
     * @return request - Donde puede ser valida la conexión fallida.
     */
    public void receive(String direccion){
        try {
            Class<?> c= Class.forName(direccion);
            Method[] listM= c.getDeclaredMethods();
            for(Method e: listM) {
                if(e.isAnnotationPresent(Web.class)) {
                    Handler handler = new Handler(e);
                    listaURLHandler.put("/app/"+e.getDeclaredAnnotation(Web.class).value(), handler);
                    //System.out.println(handler.procesar());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Esta clase me permite inicializar el atributo listaURLHandler. La cual tendra todos los métodos del package app.
     */
    public void init(){
        try {
            listaURLHandler = new HashMap<String, Handler>();
            Reflections reflections = new Reflections("app");
            Set<Class<? extends Object>> classes= reflections.getTypesAnnotatedWith(Web.class);
            //System.out.println(classes.toString());
            for(Class<?> c:classes) {
                receive(c.getName());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}