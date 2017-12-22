package inleven;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientThread extends Thread{

    private static Socket ClientSock = null;
    private static ObjectOutputStream out = null;
    private static ObjectInputStream in = null;

    public ClientThread(Socket sock) {
        // TODO Auto-generated constructor stub
        ClientSock = sock;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        ActualState hr = null;

        try {
            out = new ObjectOutputStream(ClientSock.getOutputStream());
            in = new ObjectInputStream(ClientSock.getInputStream());

            DataBase db = new DataBase();
            boolean shutdown = false;

            while (shutdown == false) {

                hr = (ActualState) in.readObject();
                System.out.println("Input Object:" + hr);

                // Make login
                if (hr.typeRequest.equals("login")) {
                    int value = db.ConfirmLogin(hr.username, hr.password);
                    if (value == 0) {
                        hr.sucessLogin = false;
                        System.out.println("Login refused.");
                    }
                    if (value == 1){
                        hr.sucessLogin = true;
                        hr.patient = true;
                        System.out.println("Login patient accepted.");
                    }
                    if (value == 2) {
                        hr.sucessLogin = true;
                        hr.patient = false;
                        System.out.println("Login volunteer accepted.");
                    } 

                }
                if (hr.typeRequest.equals("register")) {
                    db.MakeRegister(hr.username, hr.password, hr.local, hr.patient);

                }
                // Make request
                if (hr.typeRequest.equals("request")) {
                    db.MakeRequest(hr.username, hr.local);
                }

                out.writeObject(hr);
                
            }
            
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
