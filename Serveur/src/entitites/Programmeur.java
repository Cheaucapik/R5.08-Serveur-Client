package entitites;

public class Programmeur {
    final private String user;
    final private String password;
    final private String UrlFTP;

    public Programmeur(String user, String password, String UrlFTP){
        this.user = user;
        this.password = password;
        this.UrlFTP = UrlFTP;
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
}
