package talina;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class InversionTexte implements Runnable {
    private Socket socket;

    public InversionTexte(Socket socket) {
        this.socket = socket;
    }

    // --- MÉTHODE MAIN DE TEST ---
    @Override
    public void run() {
        try (
                // c'est mon oreille LOL qui me permet d'écouter ce que le client me dit , donc me permet d'entendre ou lire le texte que je devrais inverser
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //c'est ma bouche, qui va me permettre de réondre au client
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ) {

            // petit affichage dans la console
            //quand on aura les log ce serait cool de recup son id pour lui dire bienvenu X
            out.println("##Bienvenue dans le service d'INVERSION DE TEXTE");
            out.println("##Pour quitter le service, tapez 'exit' à tout moment.");
            out.println("Veuillez saisir le texte à inverser :");
            String ClientSaisie;

            while ((ClientSaisie = in.readLine()) != null && !ClientSaisie.isEmpty() && !ClientSaisie.equals("exit")) {

                if (ClientSaisie.length() < 2) {
                    out.println("Erreur ! Veuillez saisir au minimum deux caractères ");

                } else {
                    // On inverse le texte donné par la socket
                    String TexteInversed = new StringBuilder(ClientSaisie).reverse().toString();

                    // mtn on affiche
                    out.println("Voici le résultat de l'inversion : " + TexteInversed);
                }


            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
