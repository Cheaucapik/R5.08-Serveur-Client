package bri;

import appli.BRILaunch;
import entitites.Programmeur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServiceProg implements Runnable {
    private Socket client;

    public ServiceProg(Socket client){
        this.client = client;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
            PrintWriter out = new PrintWriter (client.getOutputStream ( ), true);
            out.println("Tapez un login :");
            String login = in.readLine();
            out.println("Tapez un mot de passe :");
            String mdp = in.readLine();

            //if(login ==)



        }
        catch (IOException e) {
            //Fin du service
        }

        try {client.close();} catch (IOException e2) {}
    }

    protected void finalize() throws Throwable {
        client.close();
    }
}
