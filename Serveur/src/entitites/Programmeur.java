package entitites;

import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class Programmeur {
    final private String user;
    final private String password;
    private String UrlFTP;
    private List<Class<? extends Runnable>> servicesList;

    public Programmeur(String user, String password, String UrlFTP){
        this.user = user;
        this.password = password;
        this.UrlFTP = UrlFTP;
        this.servicesList = new ArrayList<>();
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
    public String getUrlFTP() {
        return UrlFTP;
    }

    public Class<? extends Runnable> chargerFtp(String serviceName) throws Exception {
        URL[] urls = { new URL(UrlFTP) };

        try (URLClassLoader classLoader = new URLClassLoader(urls)){
            //vérifier que c'est bien runnable

            //chargement dynamique de la classe
            Class<?> loadedClass = classLoader.loadClass(serviceName);
            System.out.println("[DEBUG] Succès ! La classe a été trouvée et chargée : " + loadedClass.getName());

            //vérifier que le package soit bien son login
            Package pkg = loadedClass.getPackage();
            if (pkg == null || !pkg.getName().equals(user)) {
                throw new SecurityException("La classe doit appartenir au package '" + user + "' !");
            }

            //vérifier que le constructeur a bien une socket
            Constructor<?> constructor = loadedClass.getDeclaredConstructor(Socket.class);
            System.out.println("[DEBUG] Succès ! Constructeur avec Socket trouvé : " + constructor);

            return (Class<? extends Runnable>) loadedClass;
        }
        catch (ClassNotFoundException e) {
            System.err.println("[DEBUG] ECHEC : ClassNotFoundException. Java ne trouve pas le fichier .class sur le FTP.");
            throw e;
        }
        catch (NoSuchMethodException e) {
            System.err.println("[DEBUG] ECHEC : NoSuchMethodException. La classe existe mais n'a pas de constructeur prenant une Socket(Socket.class).");
            throw e;
        }
        catch (MalformedURLException e) {
            System.err.println("[DEBUG] ECHEC : Impossible de se connecter au serveur FTP veuillez vérifier l'adresse ou la modifier");
            throw e;
        }
        catch (Exception e) {
            System.err.println("[DEBUG] ECHEC : Autre erreur -> " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public void addServices(String serviceName) throws Exception {
        Class<? extends Runnable> serviceClass = chargerFtp(serviceName);

        //Ne pas accepter les doublons
        if(!alreadyExists(serviceName)) servicesList.add(serviceClass);
        else throw new IllegalArgumentException("Ce service existe déjà dans la liste");
    }

    public void updateServices(int num, String serviceName) throws Exception {
        Class<? extends Runnable> serviceClass = chargerFtp(serviceName);

        if (num >= 0 && num < servicesList.size()) {
            servicesList.set(num, serviceClass);
        } else {
            throw new IndexOutOfBoundsException("Index de service invalide.");
        }
    }

    public String getServicesByIndex(int num){
        Class<? extends Runnable> classe = servicesList.get(num);
        return classe.getName();
    }

    private boolean alreadyExists(String name){
        for(Class<? extends Runnable> c : servicesList){
            if(c.getName().equals(name)) return true;
        }
        return false;
    }

    public String toStringue() {
        StringBuilder result = new StringBuilder("##Activités présentes :\n##");
        for (int i = 0; i < servicesList.size(); i++) {
            result.append((i + 1)).append(") ").append(servicesList.get(i).getSimpleName()).append("\n##");
        }
        return result.toString();
    }

    public void setFtp(String UrlFTP){
        this.UrlFTP = UrlFTP;
    }
}
