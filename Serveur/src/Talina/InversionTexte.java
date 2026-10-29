package Talina;

import bri.IBriService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class InversionTexte implements IBriService {

    // Création du constructeur
    public InversionTexte(){}

    @Override
    public void runService(Socket socket) {

        try(
                // c'est mon oreille LOL qui me permet d'écouter ce que le client me dit , donc me permet d'entendre ou lire le texte que je devrais inverser
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //c'est ma bouche, qui va me permettre de réondre au client
                PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                ) {

            // petit affichage dans la console
            out.println("Veuillez saisir le texte à inverser : ");
            String ClientSaisie;

                while ((ClientSaisie = in.readLine()) !=null && !ClientSaisie.isEmpty() && !ClientSaisie.equals("exit")){


                    // On inverse le texte donné par la socket
                    String TexteInversed = new StringBuilder(ClientSaisie).reverse().toString();

                    // mtn on affiche
                    out.println("Voici le résultat de l'inversion : " + TexteInversed);

                }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // --- MÉTHODE MAIN DE TEST ---
    public static void main(String[] args) {
        int portTest = 5000;

        try (java.net.ServerSocket serverTest = new java.net.ServerSocket(portTest)) {
            System.out.println("... SERVEUR DE TEST LANCÉ SUR LE PORT " + portTest + " ===");
            System.out.println("Attente de la connexion d'un client ...");


            Socket clientSocket = serverTest.accept();
            System.out.println("-> Client connecté ! Exécution de runService...");


            InversionTexte service = new InversionTexte();
            service.runService(clientSocket);

            System.out.println("FIN DU SERVICE.... ");

        } catch (IOException e) {
            System.err.println("Erreur du serveur de test : " + e.getMessage());
        }
    }
}
