package edu.escuelaing.arep.htttpServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Subprocesos {
    public static ExecutorService executorService = Executors.newCachedThreadPool();
    public static ServerSocket serverSocket= null;

    public static void start() throws IOException {
        for (;;) {
            serverSocket = HttpService.runServer();
            Socket clientSocket = HttpService.recibiendoSolicitud(serverSocket);
            executorService.execute((Runnable) new HttpServerWithThread(clientSocket));
            serverSocket.close();

        }
    }
}
