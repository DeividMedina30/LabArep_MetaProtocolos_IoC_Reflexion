package edu.escuelaing.arep.htttpServer;

import java.io.*;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class HttpServerWithThread {
    private static Socket socketCliente;
    private static HashMap<String, Handler> listaURLHandler;
    private static ServerSocket socketServer = null;
    private static BufferedReader in;
    private Socket receptor;
    private static String direccion = "";

    public HttpServerWithThread(Socket receptor){
        this.receptor = receptor;
    }

    public void run(){
        try {
            empezarAEscuchar();
            leyendoTipodeSolicitud(receptor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public static byte[] leerImagenPng() throws MalformedURLException {
        byte[] imageBytes = null;
        try {
            File imagen = new File("src/main/java/arep/resource/imagenesPng"+direccion);
            FileInputStream inputImage = new FileInputStream(imagen); // Es útil para leer datos del tipo primitivo de una forma portable.
            imageBytes = new byte[(int) imagen.length()]; //Obteniendo longitud de la imagen en bytes
            inputImage.read(imageBytes); //Leyendo la imagen
        } catch (IOException io) {
            System.err.println(io);
        }
        return imageBytes;
    }

    public static String leerHTML(String address) throws MalformedURLException {
        String html = "";
        try {
            FileReader file = new FileReader("src/main/java/arep/resource/html"+address);
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
}
