
package bri;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurBRI implements Runnable {

    private int numPort;
    private ServerSocket serverSocket;
    private Class<? extends IBriService> serviceClass;

    public ServeurBRI(Class<? extends IBriService> serviceClass, int port) {
        this.numPort = port;
        this.serviceClass = serviceClass;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(numPort);
            System.out.println("Lancement du serveur au port " + numPort);

            while (true) {
                Socket client = serverSocket.accept();

                new Thread(() -> {
                    try {
                        IBriService service =
                                serviceClass.getDeclaredConstructor().newInstance();

                        service.runService(client);

                    } catch (Exception e) {
                        System.err.println("Erreur du service : " + e.getMessage());

                        try {
                            client.close();
                        } catch (IOException ex) {
                            System.err.println(ex.getMessage());
                        }
                    }
                }).start();
            }

        } catch (IOException e) {
            System.err.println("Erreur du serveur : " + e.getMessage());
        }
    }
}
