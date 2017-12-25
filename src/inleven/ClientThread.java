package inleven;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientThread extends Thread{

    private static Socket ClientSock = null;


    public ClientThread(Socket sock) {
        // TODO Auto-generated constructor stub
        ClientThread.ClientSock = sock;
    }

    @Override
    public synchronized void run() {
        // TODO Auto-generated method stub
        try {
            boolean shutdown = false;

            ObjectOutputStream out = new ObjectOutputStream(ClientSock.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(ClientSock.getInputStream());

            DataBase db = new DataBase();

            while (shutdown == false) {


                ActualState hr = (ActualState) in.readObject();
                System.out.println();
                System.out.println(ClientThread.ClientSock.toString());
                System.out.println("Input Object:");
                PrintMessage(hr);
                
                // Make login
                if (hr.typeRequest.equals("login")) {
                    hr = db.ConfirmLogin(hr);
                }
                //Make register
                if (hr.typeRequest.equals("register")) {
                    db.MakeRegister(hr.username, hr.password,hr.phone, hr.local, hr.patient);
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
                //check volunteer details
                if(hr.typeRequest.equals("volunteerdetails")){
                    hr = db.SearchVolunteerDetails(hr);
                }
                if(hr.typeRequest.equals("shutdown")){
                    shutdown = true;
                    System.out.println("Client disconecting...");
                    System.out.println("Client: " + hr.username);
                }
                out.writeObject(hr);
                out.flush();
                System.out.println("Outout Object:");
                PrintMessage(hr);
                
            }
            out.close();
            in.close();
            ClientThread.ClientSock.close();
            
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    private void PrintMessage(ActualState hr){
        System.out.println("Type Request: " + hr.typeRequest);
        System.out.println("Username: " + hr.username + ", Password: " + hr.password);
        System.out.println("Localization: " + hr.local + ", Phone:" + hr.phone);
    }
}
