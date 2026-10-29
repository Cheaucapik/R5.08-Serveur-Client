package bri;

import java.net.Socket;

public interface IBriService {

    /**
     * Comme on va avoir plusieurs service, tous les développeur qui voudront implémenter une méthode n'auront qu'àimplements service qui n'aura qu'one méthode communne : méthode appelée
     * Facilite aussi notre serveur BriLaunch qui aura juste pour mission d'appeler la méthode de serviced
     */

    public abstract void runService(Socket socket);
}
