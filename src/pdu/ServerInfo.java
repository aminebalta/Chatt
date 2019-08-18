package pdu;

import java.net.InetAddress;

/**
 * Created by Amine Balta and Matilda Nilsson on 2017-01-17.
 */
public class ServerInfo {

    InetAddress adress;
    int clients;
    String serverName;

    public ServerInfo(InetAddress adress, int clients, String serverName){
        this.adress = adress;
        this.clients = clients;
        this.serverName = serverName;
    }

    public InetAddress getAdress() {
        return adress;
    }

    public int getClients() {
        return clients;
    }

    public String getServerName() {
        return serverName;
    }
}
