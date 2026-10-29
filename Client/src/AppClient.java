import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AppClient {
    private static int PORT;
    private static String HOST = "localhost";

    public static void main(String[] args) {
        BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in)); //ce qu'on tape

        if(args.length < 1){
            System.out.println("Usage: java AppClient heheh <port>");
            return;
        }

        PORT = Integer.parseInt(args[0]);

        Socket socket = null;
        try {
            socket = new Socket(HOST, PORT);
            BufferedReader sin = new BufferedReader (new InputStreamReader(socket.getInputStream ( ))); //ce que le serveur envoie
            PrintWriter sout = new PrintWriter (socket.getOutputStream ( ), true); //ce qu'on envoie au serveur

            System.out.println("Connecté au serveur " + socket.getInetAddress() + ":"+ socket.getPort());

            String reponse;
            String line;
            while((line = sin.readLine()) != null) {
                if(line.startsWith("##")){
                    System.out.println(line.substring(2).trim());
                }
                else{
                    System.out.println(line); //question
                    System.out.print("-> "); //flèche pour répondre
                    reponse = clavier.readLine(); //réponse entrée au clavier
                    if (reponse != null && reponse.equalsIgnoreCase("q")) {
                        System.out.println("Déconnexion demandée par l'utilisateur...");
                        sout.println(reponse);
                        break;
                    }
                    sout.println(reponse); //envoi de la réponse au serveur/service concerné
                }
            }

            socket.close();
        }
        catch (IOException e) { System.err.println("Fin du service"); }
    }
}