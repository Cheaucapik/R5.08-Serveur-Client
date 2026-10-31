package bri;

import entitites.Programmeur;
import entitites.ProgrammeurRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.net.Socket;

public class ServiceAma implements Runnable {
    private Socket client;

    public ServiceAma(Socket client){
        this.client = client;
    }

    @Override
    public void run() {
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);

            //afficher tous les services disponibles
            out.println(ProgrammeurRegistry.toStringue());
            out.println("Tapez le numéro de service désiré :");
            String line = in.readLine();
            if (line != null) {
                try {
                    int choix = Integer.parseInt(line.trim());
                    if(choix >=1 && choix <= ProgrammeurRegistry.getAllServices().size()){
                        Class<? extends bri.Service> serviceClass = ProgrammeurRegistry.getAllServices().get(choix - 1);
                        Constructor<?> constructor = serviceClass.getConstructor(Socket.class);
                        new Thread((bri.Service) constructor.newInstance(client)).start();

                        return;
                    }
                    else out.println("##Veuillez choisir une des options");
                } catch (NumberFormatException e) {
                    out.println("##Erreur : Vous devez saisir un nombre entier.");
                }
            }
            else {
                out.println("##Erreur : Numéro de service invalide.");
            }
        }
        catch (Exception e){
            if (out != null) {
                out.println("Erreur lors du traitement : " + e.getMessage());
            }
        }

        try {client.close();} catch (IOException e2) {}
    }

    protected void finalize() throws Throwable {
        client.close();
    }
}
