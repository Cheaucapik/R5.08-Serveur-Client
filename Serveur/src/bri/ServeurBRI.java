
package bri;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurBRI implements Runnable {

    private int numPort;
    private ServerSocket serverSocket;
    private Class<? extends Runnable> serviceClass;

    public ServeurBRI(Class<? extends Runnable> serviceClass, int port) {
        this.numPort = port;
        this.serviceClass = serviceClass;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(numPort);
            System.out.println("Lancement du serveur au port " + numPort);

            while (true) {
                Socket client = this.serverSocket.accept();
                try{
                    new Thread(serviceClass.getDeclaredConstructor(Socket.class).newInstance(client)).start();
                }
                catch (Exception e)
                {
                    System.err.println("Erreur du serveur : " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur du serveur : " + e.getMessage());
        }
    }
}
