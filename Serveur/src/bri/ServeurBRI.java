package bri;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
                new Thread(serviceClass.getDeclaredConstructor(Socket.class).newInstance(client)).start();
            }
        } catch (IOException | NoSuchMethodException e) {
            System.out.println(e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected void finalize() throws Throwable{
        try{this.serverSocket.close();}
        catch (IOException e){}
    }
}
