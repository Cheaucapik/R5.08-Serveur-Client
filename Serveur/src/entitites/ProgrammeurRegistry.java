package entitites;

import java.util.ArrayList;
import java.util.List;

public class ProgrammeurRegistry {
    private static List<Programmeur> programmeurList = new ArrayList<>();

    public static void addProgrammeur(Programmeur p){
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
}
