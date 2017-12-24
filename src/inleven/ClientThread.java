package inleven;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientThread extends Thread{

    private static Socket ClientSock = null;
    private static ObjectOutputStream out = null;
    private static ObjectInputStream in = null;

    public ClientThread(Socket sock) {
        // TODO Auto-generated constructor stub
        ClientThread.ClientSock = sock;
    }

    ClientThread(Socket ClientSock, HashMap<Socket, ClientThread> ClientsMap) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
                    hr = db.ConfirmLogin(hr);
                }
                //Make register
                if (hr.typeRequest.equals("register")) {
                    db.MakeRegister(hr.username, hr.password,hr.phone, hr.local, hr.patient);
                    hr = resetActualState(hr);
                }
                //Make help request
                if (hr.typeRequest.equals("helprequest")) {
                    db.MakeRequest(hr.username, hr.local,hr.helpType);
                }
                //set volunteer to request
                if(hr.typeRequest.equals("setvolunteer")){
                    db.SetVolunteerField(hr.username,hr.local);
                }
                //verify help request's
                if(hr.typeRequest.equals("verifyhelp")){
                    hr = db.VerifyHelpRequest(hr);
                }
                if(hr.typeRequest.equals("shutdown")){
                    shutdown = true;
                    System.out.println("Client disconecting...");
                    System.out.println("Client: " + hr.username);
                }
                out.writeObject(hr);
                
            }
            
            ClientThread.ClientSock.close();
            
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    private ActualState resetActualState(ActualState hr){
        hr.helpType = "0";
        hr.local = "0";
        hr.password = "0";
        hr.patient = false;
        hr.requestState = null;
        hr.sucessLogin = false;
        hr.typeRequest = "0";
        hr.username = "0";
        hr.volunteer = "0";
        return hr;
    }
}
