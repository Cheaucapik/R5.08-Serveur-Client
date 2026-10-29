package bri;



import java.io.*;
import java.net.Socket;
import java.net.URL;

public class TestFTP {

    public static String lireReponse(BufferedReader lecteur)
            throws IOException {

        String ligne = lecteur.readLine();
        System.out.println("Serveur : " + ligne);

        if (ligne != null && ligne.length() >= 4
                && ligne.charAt(3) == '-') {

            String code = ligne.substring(0, 3);

            do {
                ligne = lecteur.readLine();
                System.out.println("Serveur : " + ligne);

            } while (ligne != null && !ligne.startsWith(code + " "));
        }

        return ligne;
    }


    public static int calculerPortPassif(String reponse) {
        int debut = reponse.indexOf('(');
        int fin = reponse.indexOf(')');

        String contenu = reponse.substring(debut + 1, fin);
        String[] nombres = contenu.split(",");

        int p1 = Integer.parseInt(nombres[4]);
        int p2 = Integer.parseInt(nombres[5]);

        return p1 * 256 + p2;
    }

    public static void main(String[] args) throws Exception {

        try(Socket socket = new Socket("localhost", 21)){
            BufferedReader lecteur = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            PrintWriter user = new PrintWriter(socket.getOutputStream(),true);
            lireReponse(lecteur);

            user.print("USER talina\r\n");
            user.flush();
            lireReponse(lecteur);


            user.print("PASS 1234\r\n");
            user.flush();
            lireReponse(lecteur);

            user.print("PASV\r\n");
            user.flush();

// Lire la réponse du serveur


            String reponse = lireReponse(lecteur);
            int port = calculerPortPassif(reponse);

            System.out.println("Port de transfert : " + port);

            Socket socketDonnees = new Socket("127.0.0.1", port);

            System.out.println("Connexion de données établie !");


            user.print("RETR talina/InversionTexte.class\r\n");
            user.flush();

            lireReponse(lecteur);

            try (InputStream entree = socketDonnees.getInputStream();
                 FileOutputStream sortie = new FileOutputStream("InversionTexte.class")) {

                byte[] buffer = new byte[4096];
                int octetsLus;

                while ((octetsLus = entree.read(buffer)) != -1) {
                    sortie.write(buffer, 0, octetsLus);
                }
            }

            socketDonnees.close();

            lireReponse(lecteur);

            File fichier = new File("InversionTexte.class");

            System.out.println("Fichier téléchargé : " + fichier.exists());
            System.out.println("Taille du fichier : " + fichier.length() + " octets");
            System.out.println("Chemin : " + fichier.getAbsolutePath());
        };


        File dossier = new File("C:/BRI/service");

        try (BriClassLoader loader =
                     new BriClassLoader(new URL[]{dossier.toURI().toURL()})) {

            Class<?> classe = loader.chargerClasse("talina.InversionTexte");

            System.out.println("Classe chargée : " + classe.getName());
        }

    }
}
