package edu.escuelaing.arep.htttpServer;

import org.reflections.Reflections;

import java.io.*;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

public class HttpService {

    private static HashMap<String, Handler> listaURLHandler;
    private static Socket clientSocket;
    private static ServerSocket serverSocket = null;
    private static String address = "";
    private static BufferedReader in;
    private static Socket receiver;



    public void listen() throws Exception{
        while (true){
            serverSocket = runServer();
            receiver = receiveRequest(serverSocket);
            setRequest(receiver);
            postType(address,receiver);
            closeInput();
            receiver.close();
            serverSocket.close();
        }
    }

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

    public static Socket receiveRequest(ServerSocket serverSocket) {

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

    public static void postType(String address, Socket cs) throws Exception{
        clientSocket = cs;
        System.out.println("POST: " + address);
        if (address.contains("/app")) {
            postApp(address);
        }else if (address.contains(".html")){
            postHtml(address);
        }else if (address.contains(".png")){
            postImage(address);
        }else{
            notFound404();
        }
    }


    private static void postHtml(String request){
        try{
            String outputLine;
            String page = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html\r\n" +
                    "\r\n" + readHTML(request);
            outputLine = page;
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(outputLine);
            out.close();
        }catch (Exception ex){
            notFound404();
            System.err.println("Error: Archivo no encontrado");
        }
    }

    private static void postApp(String address) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        int limit = address.indexOf("/app");
        String resource = "";
        for (int k = limit; k < address.length() && address.charAt(k) != ' '; k++) {resource += address.charAt(k);}
        try {
            out.write("HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n" + "\r\n");
            if(resource.contains("?")) {
                System.out.println("Entro al primero");
                int i = resource.indexOf("?");
                String params=resource.substring(i+1);
                if(resource.contains("&")) {
                    String[] arrayP= params.split("&");
                    Object [] objects=new Object[arrayP.length];
                    int w=0;
                    for(String p: arrayP) {
                        objects[w]=p.split("=")[1];
                        w++;
                    }
                    out.write(listaURLHandler.get(resource.substring(0, i)).procesar(objects));
                }else {
                    String other=resource.split("=")[1];
                    out.write(listaURLHandler.get(resource.substring(0, i)).procesar(new Object[]{other}));
                }
            }
            else {
                out.write(listaURLHandler.get(resource).procesar());}
            out.close();
        } catch (Exception e) {
            notFound404();
            System.err.println("Error: No es posible cargar el recurso " + address);
        }
    }

    private static void postImage(String address){
        try {
            byte[] imageBytes;
            imageBytes = readImage(address);
            DataOutputStream imageCode;
            imageCode = new DataOutputStream(clientSocket.getOutputStream());
            imageCode.writeBytes("HTTP/1.1 200 OK \r\n");
            imageCode.writeBytes("Content-Type: image/png\r\n");
            imageCode.writeBytes("Content-Length: " + imageBytes.length);
            imageCode.writeBytes("\r\n\r\n");
            //La imagen se hace visible en el servidor
            imageCode.write(imageBytes);
            imageCode.close();
        } catch (IOException ex){

            notFound404();
            System.err.println("Error: No se encontro la imagen");
        }
    }

    private static void notFound404(){
        try{
            String outputLine;
            String page = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html\r\n" +
                    "\r\n" + readHTML("/notfound.html");

            outputLine = page;
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(outputLine);
            out.close();
        }catch (IOException ex){
            System.err.println("Error en notFound");
        }
    }

    public static void setRequest(Socket clientSocket) throws IOException{
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Received: " + inputLine);
            if (!in.ready()) {
                break;
            }
            if(inputLine.contains("GET")){
                address = inputLine.split(" ")[1];
            }
        }
    }

    public void closeInput() throws IOException {
        in.close();
    }

    public static String readHTML(String address) throws MalformedURLException {
        String html = "";
        try {
            FileReader file = new FileReader("src/main/java/app/html"+address);
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


    public static byte[] readImage(String address) throws MalformedURLException {
        byte[] imageBytes = null;
        try {
            File image = new File("src/main/java/app/images"+address);
            FileInputStream inputImage = new FileInputStream(image);
            imageBytes = new byte[(int) image.length()];
            inputImage.read(imageBytes);

        } catch (IOException io) {
            System.err.println(io);
        }
        return imageBytes;
    }


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

    public static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set (i.e. on localhost)
    }
}
