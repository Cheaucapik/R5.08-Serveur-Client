package bri;

import entitites.Programmeur;
import entitites.ProgrammeurRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServiceProg implements Runnable {
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private Programmeur p;

    public ServiceProg(Socket client){
        this.client = client;
        p = null;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader (new InputStreamReader(client.getInputStream ( )));
            out = new PrintWriter (client.getOutputStream ( ), true);

            while(p == null){
                out.println("##1) Se connecter \n##2) S'inscrire \nq : quitter");

                String line = lireReponse();
                if (line == null) return;
                try {
                    int num = Integer.parseInt(line.trim());
                    if(num == 1) p = connect(in, out);
                    else if(num == 2) signup(in, out);
                    else out.println("##Veuillez choisir une des options");
                } catch (NumberFormatException e) {
                    out.println("##Erreur : Vous devez saisir un nombre entier.");
                }
            }

            services(in, out);
        }
        catch (IOException e) {
            //Fin du service
        }

        try {client.close();} catch (IOException e2) {}
    }

    private Programmeur connect(BufferedReader in, PrintWriter out){
        Programmeur p = null;
        try{
            out.println("Tapez un login :");
            String login = in.readLine().trim();
            out.println("Tapez un mot de passe :");
            String mdp = in.readLine().trim();

            p = ProgrammeurRegistry.getProgrammeur(login, mdp);
            if(p == null) out.println("##Erreur : Identifiants incorrects");
            else {
                out.println("##Connecté avec succès");
            }
        }
        catch (Exception e){
        }
        return p;
    }

    private void signup(BufferedReader in, PrintWriter out){
        try{
            out.println("Tapez un login :");
            String login = in.readLine().trim();
            out.println("Tapez un mot de passe :");
            String mdp = in.readLine().trim();
            out.println("Tapez votre adresse FTP :");
            String ftp = in.readLine().trim();

            ProgrammeurRegistry.addProgrammeur(new Programmeur(login, mdp, ftp));
            out.println("##Utilisateur créé avec succès. Veuillez désormais vous connecter.");
        }
        catch (Exception e){
        }
    }

    private String lireReponse() throws IOException {
        String line = in.readLine();
        if (line == null || line.equalsIgnoreCase("q")) {
            System.out.println("Le client a arrete la session");
            return null;
        }
        return line.trim();
    }

    private void services(BufferedReader in, PrintWriter out){
        try{
            while(true) {
                out.println(p.toStringue());
                out.println("##1) Fournir un nouveau service \n##2) Mettre-à-Jour un service \n##3) Déclarer un changement d'adresse du serveur ftp \nq : quitter");

                String line = lireReponse();
                if (line == null) return;
                try {
                    int num = Integer.parseInt(line.trim());
                    if (num == 1) newService(in, out);
                    else if (num == 2) MAJ(in, out);
                    else if (num == 3) changeFTP(in, out);
                    else out.println("##Veuillez choisir une des options");
                } catch (NumberFormatException e) {
                    out.println("##Erreur : Vous devez saisir un nombre entier.");
                }
            }
        }
        catch (Exception e){
        }
    }

    private void MAJ(BufferedReader in, PrintWriter out) {
        try{
            out.println(p.toStringue());
            out.println("Veuillez entrer le numéro associé au service à mettre à jour : ");
            String line = in.readLine();
            int num = Integer.parseInt(line.trim());
            String className = p.getServicesByIndex(num - 1);
            p.updateServices(num, className);
        }
        catch (NumberFormatException e) {
            out.println("##Erreur : Vous devez saisir un nombre entier.");
        }
        catch (IndexOutOfBoundsException e){
            out.println("##Erreur : Veuillez entrer un numéro associé aux options.");
        }
        catch (Exception e){
            if(e instanceof ClassNotFoundException) out.println("## Erreur : .class introuvable");
            else if (e instanceof NoSuchMethodException) {
                out.println("##Erreur : Pas de constructeur Socket.");
            }
            else if(e instanceof SecurityException){
                out.println("##Erreur : Pas de package au nom du login : " + p.getUser());
            }
            else {
                out.println("##Erreur générale : " + e.getMessage());
            }
        }
    }

    private void changeFTP(BufferedReader in, PrintWriter out) {
        try{
            out.println("Veuillez entrer le nouvel url de votre serveur FTP : ");
            String classe = in.readLine();
            p.setFtp(classe);
            out.println("##Nouvelle url validée : " + p.getUrlFTP());
        }
        catch (Exception e){
        }
    }

    private void newService(BufferedReader in, PrintWriter out){
        try{
            out.println("Veuillez entrer la classe du service : ");
            String classe = in.readLine();
            p.addServices(classe);
        }
        catch (Exception e){
            if(e instanceof ClassNotFoundException) out.println("## Erreur : .class introuvable");
            else if (e instanceof  IllegalArgumentException){
                out.println("##Erreur : Ce service existe déjà, veuillez modifier son nom ou en importer un autre.");
            }
            else if (e instanceof NoSuchMethodException) {
                out.println("##Erreur : Pas de constructeur Socket.");
            }
            else if(e instanceof SecurityException){
                out.println("##Erreur : Pas de package au nom du login : " + p.getUser());
            }
            else {
                out.println("##Erreur générale : " + e.getMessage());
            }
        }
    }

    protected void finalize() throws Throwable {
        client.close();
    }
}
