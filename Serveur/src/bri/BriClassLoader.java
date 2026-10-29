
package bri;

import java.net.URL;

public class BriClassLoader extends java.net.URLClassLoader {

    public BriClassLoader(URL[] urls) {
        super(urls);
    }

    public Class<?> chargerClasse(String nomClasse)
            throws ClassNotFoundException {
        return loadClass(nomClasse);
    }
}
