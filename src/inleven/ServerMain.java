package inleven;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerMain {

    private static ServerSocket serverSock;
    private static HashMap<Socket, Thread> ClientsMap = new HashMap<>();

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        int ServerPort = 5050;
        serverSock = new ServerSocket(ServerPort);

        while (serverSock.isClosed() == false) {
            System.out.println("Wanting  for connections...");
            Socket ClientSock = serverSock.accept();
            System.out.println("Connection on Socket: " + ClientSock.toString());
            ClientThread ClientT = new ClientThread(ClientSock);
            ClientT.start();
            //ClientsMap.put(ClientSock, ClientT);
        }

    }

}
