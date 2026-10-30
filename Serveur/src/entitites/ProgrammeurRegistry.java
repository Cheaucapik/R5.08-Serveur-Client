package entitites;

import java.util.ArrayList;
import java.util.List;

public class ProgrammeurRegistry {
    private static List<Programmeur> programmeurList = new ArrayList<>();

    public static void addProgrammeur(Programmeur p){
        for(Programmeur prog : programmeurList){
            if(prog.getUser().equalsIgnoreCase(p.getUser())) throw new IllegalArgumentException("Erreur : Login déjà existant");
        }
        programmeurList.add(p);
    }

    public static Programmeur getProgrammeur(String login, String mdp){
        for(Programmeur p : programmeurList){
            if(p.getUser().equals(login) && p.getPassword().equals(mdp)){
                return p;
            }
        }
        return null;
    }

    public static List<Class<? extends Runnable>> getAllServices(){
        List<Class<? extends Runnable>> all = new ArrayList<>();
        for(Programmeur p : programmeurList){
            all.addAll(p.getServicesList());
        }
        return all;
    }

    public static String toStringue() {
        List<Class<? extends Runnable>> all = getAllServices();
        StringBuilder result = new StringBuilder("##Activités présentes :\n##");
        for (int i = 0; i < all.size(); i++) {
            result.append((i + 1)).append(") ").append(all.get(i).getSimpleName()).append("\n##");
        }
        return result.toString();
    }
}
