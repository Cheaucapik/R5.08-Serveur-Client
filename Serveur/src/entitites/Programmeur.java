package entitites;

import java.util.ArrayList;
import java.util.List;

public class Programmeur {
    final private String user;
    final private String password;
    final private String UrlFTP;
    private static List<Class<? extends Runnable>> servicesList;

    public Programmeur(String user, String password, String UrlFTP){
        this.user = user;
        this.password = password;
        this.UrlFTP = UrlFTP;
        this.servicesList = new ArrayList<Class<? extends Runnable>>();
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

    public static void addServices(String serviceName) throws ClassNotFoundException {
        try{
            Class<?> maClasse = Class.forName(serviceName);
            Class<? extends Runnable> serviceClass = (Class<? extends Runnable>) maClasse;

            //vérifier que c'est bien runnable
            //vérifier que le constructeur a bien une socket
            //Ajout du classLoader

            servicesList.add(serviceClass);
        }
        catch (ClassNotFoundException e){
            throw new ClassNotFoundException(e.getMessage());
        }
    }

    public static void getServiceClass(int numService) {

    }

    public static String toStringue() {
        String result = "Activités présentes :##";
        // todo
        return result;
    }
}
