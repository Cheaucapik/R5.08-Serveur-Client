package entitites;

import bri.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
    private List<Class<? extends bri.Service>> servicesList;

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
    public List<Class<? extends bri.Service>> getServicesList() { return servicesList; }

    public Class<? extends bri.Service> chargerFtp(String serviceName) throws Exception {
        URL[] urls = { new URL(UrlFTP) };

        try (URLClassLoader classLoader = new URLClassLoader(urls)){
            //chargement dynamique de la classe
            Class<?> loadedClass = classLoader.loadClass(serviceName);
            System.out.println("[DEBUG] Succès ! La classe a été trouvée et chargée : " + loadedClass.getName());

            //vérifier que c'est bien bri.Service
            Class<? extends bri.Service> maClasse = loadedClass.asSubclass(bri.Service.class);

            //vérifier que le package soit bien son login
            Package pkg = loadedClass.getPackage();
            if (pkg == null || !pkg.getName().equals(user)) {
                throw new SecurityException("La classe doit appartenir au package '" + user + "' !");
            }

            //vérifier que le constructeur a bien une socket en param et ne retourne pas d'exception
            Constructor<?> constructor = loadedClass.getConstructor(Socket.class);
            Class[] exception = constructor.getExceptionTypes();
            if(exception.length != 0) throw new IllegalArgumentException("Erreur : Le constructeur prenant une Socket ne doit déclarer aucune exception.");
            System.out.println("[DEBUG] Succès ! Constructeur avec Socket trouvé : " + constructor);

            //vérifier que la classe est bien publique et non abstract
            int modifier = loadedClass.getModifiers();
            if(!Modifier.isPublic(modifier)) throw new IllegalArgumentException("Erreur : La classe doit être publique");
            if(Modifier.isAbstract(modifier)) throw new IllegalArgumentException("Erreur : La classe doit ne doit pas être abstract");

            //Vérifier qu'il y ait bien un attribut Socket private final
            boolean isFieldSocket = false;
            Field[] fields = loadedClass.getDeclaredFields();
            for(Field f : fields){
                if(f.getType().equals(Socket.class)){
                    int modifierField = f.getModifiers();
                    if(Modifier.isPrivate(modifierField) && Modifier.isFinal(modifierField)){
                        isFieldSocket = true;
                        break;
                    }
                }
            }

            if(!isFieldSocket) throw new IllegalArgumentException("Erreur : L'attribut de type socket doit être private et final");

            //vérifier qu'il y ait une méthode toStringue() public static qui ne renvoie pas d'exception
            boolean isMethodToStringue = false;
            Method[] methods = loadedClass.getMethods();
            for(Method m : methods){
                if(m.getName().equals("toStringue")){
                    int modifierMethods = m.getModifiers();
                    if(Modifier.isStatic(modifierMethods) && m.getExceptionTypes().length == 0){
                        isMethodToStringue = true;
                        break;
                    }
                }
            }

            if(!isMethodToStringue) throw new IllegalArgumentException("Erreur : La classe toStringue doit être public static et ne renvoyer aucune exception");


            return maClasse;
        }
        catch (ClassCastException e) {
            System.err.println("[DEBUG] ECHEC : La classe ne respecte pas la norme BRi (doit implémenter bri.Service).");
            throw new IllegalArgumentException("Erreur : La classe doit implémenter bri.Service");
        }
        catch (ClassNotFoundException e) {
            System.err.println("[DEBUG] ECHEC : ClassNotFoundException. Java ne trouve pas le fichier .class sur le FTP.");
            throw e;
        }
        catch (NoClassDefFoundError e) {
            System.err.println("[DEBUG] Problème de casse ou nom de fichier incorrect.");
            throw new IllegalArgumentException("Erreur : Problème de nommage ou de casse (Vérifie les majuscules/minuscules dans ton fichier .class).");
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
        try{
            Class<? extends bri.Service> serviceClass = chargerFtp(serviceName);
            //Ne pas accepter les doublons
            if(!alreadyExists(serviceName)) servicesList.add(serviceClass);
            else throw new IllegalArgumentException("##Ce service existe déjà dans la liste");
        }
        catch (Exception e) {
            throw new Exception("##" + e.getMessage());
        }
    }

    public void updateServices(int num, String serviceName) throws Exception {
        Class<? extends bri.Service> serviceClass = chargerFtp(serviceName);

        if (num >= 0 && num < servicesList.size()) {
            servicesList.set(num, serviceClass);
        } else {
            throw new IndexOutOfBoundsException("##Index de service invalide.");
        }
    }

    public String getServicesByIndex(int num){
        Class<? extends bri.Service> classe = servicesList.get(num);
        return classe.getName();
    }

    private boolean alreadyExists(String name){
        for(Class<? extends bri.Service> c : servicesList){
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

    // Méthode appelée par ServiceProg pour désinstaller un service
    public void removeService(int index) {
        if (index >= 0 && index < servicesList.size()) {
            servicesList.remove(index);
        }
    }
}
