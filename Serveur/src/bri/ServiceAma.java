package bri;

import entitites.Programmeur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServiceAma implements Runnable {
    private Socket client;

    public ServiceAma(Socket client){
        this.client = client;
    }

    @Override
    public void run() {
        try{
            BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
            PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
            out.println(Programmeur.toStringue()+"##Tapez le numéro de service désiré :");
            int choix = Integer.parseInt(in.readLine());
        }
        catch (IOException e){

        }

        try {client.close();} catch (IOException e2) {}
    }

    protected void finalize() throws Throwable {
        client.close();
    }
}
