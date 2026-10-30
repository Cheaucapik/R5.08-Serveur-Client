package appli;

import bri.ServeurBRI;
import bri.ServiceAma;
import bri.ServiceProg;

import static java.lang.Integer.parseInt;

public class BRILaunch {
//    static void main(String[] args) throws Exception {
//        if (args.length < 2) {
//            System.out.println("Usage: java BRILaunch <NomDeLaClasseDuServeur> <port>");
//            return;
//        }
//
//        int numPort = parseInt(args[1]);
//        String nomClasse = args[0];
//
//        try{
//            Class<?> maClasse = Class.forName(nomClasse);
//            Class<?>[] interfaces = maClasse.getInterfaces();
//
//            if (!isInterface("Runnable", interfaces)) {
//                System.out.println("Erreur : La classe " + nomClasse + " ne respecte pas le contrat (doit implémenter Runnable).");
//            }
//            else {
//                Class<? extends Runnable> serviceClass = (Class<? extends Runnable>) maClasse;
//                new Thread(new ServeurBRI(serviceClass, numPort)).start();
//            }
//        }
//        catch (Exception e){
//            throw new Exception(e.getMessage());
//        }
//    }

    static void main(String[] args) {
        // Lance le serveur pour les Programmeurs sur le port 5001
        new Thread(new ServeurBRI(ServiceProg.class, 5001)).start();
        System.out.println("Serveur Programmeur lancé sur le port 5001");

        // Lance le serveur pour les Amateurs sur le port 5000
        new Thread(new ServeurBRI(ServiceAma.class, 5000)).start();
        System.out.println("Serveur Amateur lancé sur le port 5000");
    }

    public static boolean isInterface(String targetInterfaceName, Class<?>[] interfaces){
        for(Class<?> i : interfaces){
            if(i.getName().endsWith(targetInterfaceName)) return true;
        }
        return false;
    }
}
