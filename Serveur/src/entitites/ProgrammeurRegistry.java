package entitites;

import java.lang.reflect.Method;
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

    public static List<Class<? extends bri.Service>> getAllServices(){
        List<Class<? extends bri.Service>> all = new ArrayList<>();
        for(Programmeur p : programmeurList){
            all.addAll(p.getServicesList());
        }
        return all;
    }

    public static String toStringue() throws Exception {
        List<Class<? extends bri.Service>> all = getAllServices();
        StringBuilder result = new StringBuilder("##Activités présentes :\n##");
        for (int i = 0; i < all.size(); i++) {
            Class<? extends bri.Service> classe = all.get(i);
            try{
                Method m = classe.getMethod("toStringue");
                String description = (String) m.invoke(null);
                result.append((i + 1)).append(") ").append(description).append("\n##");
            }
            catch (Exception e){
                result.append((i + 1)).append(") ").append(classe.getSimpleName()).append(" (Description indisponible)\n##");
            }
        }
        return result.toString();
    }
}
