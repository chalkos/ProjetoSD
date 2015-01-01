
package client;

import packet.*;

import java.io.*;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;


public class Client {
    private Socket cSocket;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;
    private Scanner input;
    private boolean notLogged;

    public Client(int port) {
        try{
            cSocket = new Socket("localhost", 12345);
            outStream = new ObjectOutputStream(cSocket.getOutputStream());
            inStream = new ObjectInputStream(cSocket.getInputStream());
        }
        catch (IOException io) {
            System.out.println("Ups, IO problem!");
        }
        input = new Scanner(System.in);
        notLogged = true;
    }


    public static void main(String[] args) {
        Client client = new Client(12345);
        client.mainMenu();
    }

    public void mainMenu(){
        String str1 = "nothing yet";
        int cOption;

        System.out.println("\n1.Login\n2.CreateAccount");
        str1 = input.nextLine();
        tryLogin(goodInput(str1));

        System.out.println("\nWelcome " + str1);
        System.out.println("\n1.Create a new Task Type\n2.Start a Task\n3.Finish a Task\n4.See available Task Types"
                + "\n5.See running Tasks\n6.Subscribe to Task List\n7.Stock up Warehouse\n8.Log off");

        while( !(str1 = input.nextLine()).equals("8")){
            System.out.println("\n1.Create a new Task Type\n2.Start a Task\n3.Finish a Task\n4.See available Task Types"
                    + "\n5.See running Tasks\n6.Subscribe to Task List\n7.Stock up Warehouse\n8.Log off");

            if( (cOption = goodInput(str1)) > 0 ){
                switch(cOption){
                    case 1: createTaskType();
                        break;
                    case 2: startTask();
                        break;
                    case 3: finishTask();
                        break;
                    case 4: showTaskTypes();
                        break;
                    case 5: showRunningTasks();
                        break;
                    case 6: subscribeToTaskList();
                        break;
                    case 7: stockUpWarehouse();
                        break;
                    case 8:
                        break;
                }
            }
        }
    }


    public int goodInput(String str){
        int res;
        try{
            res = Integer.parseInt(str);
        } catch(NumberFormatException e){
            System.out.println("\nBad Input, but please, do try again.");
            return -1;
        }
        if(res>0 && res<9)
            return res;
        else return -1;
    }

    public void subscribeToTaskList(){
        //TODO
    }

    public void tryLogin(int i){
        Login lg = new Login();

        if(i == 2){
            lg.q_createUser = true;
            System.out.println("------Create Account------");
        }
        else
            System.out.println("------Login------");

        System.out.println("Username: ");
        lg.q_username = input.nextLine();

        System.out.print("Password: ");
        lg.q_password = input.nextLine();

        System.out.println("Contacting...");

        try {
            outStream.writeObject(lg);
        } catch (IOException e) {
            System.out.println("Exception while sending request");
        }

        try {
            lg = (Login) inStream.readObject();
        } catch (IOException e) {
            System.out.println("Exception when trying to obtain reply from server");
        } catch (ClassNotFoundException e) {
            System.out.println("Exception when trying to obtain reply from server - ClassNotFound");
        }

        if(lg.r_errors != null) {
            System.out.println("\nUps");
            for (String s : lg.r_errors)
                System.out.println(s);
            return;
        }

        if(lg.q_createUser)
            System.out.println("Account created!");
    }




    public void createTaskType(){
        String str1, str2="";
        HashMap<String,Integer> needs = new HashMap<>();
        CreateTaskType newTaskType = new CreateTaskType();

        System.out.println("\nNew TaskType name: ");
        newTaskType.q_name = input.nextLine();

        System.out.println("\nAdd TaskType need? (y) to continue");

        while((str1 = input.nextLine()).equals("y")){
            System.out.println("\nNeeded material name: ");
            str1 = input.nextLine();
            System.out.println("\nNeeded material quantity: ");
            needs.put(str1,goodInput(str2));
        }

        newTaskType.q_itens = new HashMap<String, Integer>(needs);

        System.out.println("Sending request to server...");

        try {
            outStream.writeObject(newTaskType);
        } catch (IOException e) {
            System.out.println("Exception while sending request");
        }

        try {
            newTaskType = (CreateTaskType) inStream.readObject();
        } catch (IOException e) {
            System.out.println("Exception when trying to obtain reply from server");
        } catch (ClassNotFoundException e) {
            System.out.println("Exception when trying to obtain reply from server - ClassNotFound");
        }

        if(newTaskType.r_errors != null) {
            System.out.println("\nUps");
            for (String s : newTaskType.r_errors)
                System.out.println(s);
            return;
        }

        System.out.println("Created a new taskType" + newTaskType.q_name);

    }



    public void startTask(){
        StartTask sTask = new StartTask();

        System.out.println("\nStart new task of type: ");
        sTask.q_name = input.nextLine();

        System.out.println("Sending request to server...");

        try {
            outStream.writeObject(sTask);
        } catch (IOException e) {
            System.out.println("Exception while sending request");
        }

        try {
            sTask = (StartTask) inStream.readObject();
        } catch (IOException e) {
            System.out.println("Exception when trying to obtain reply from server");
        } catch (ClassNotFoundException e) {
            System.out.println("Exception when trying to obtain reply from server - ClassNotFound");
        }

        if(sTask.r_errors != null) {
            System.out.println("\nUps");
            for (String s : sTask.r_errors)
                System.out.println(s);
            return;
        }

        System.out.println("Created a new task of type " + sTask.q_name);
    }

    public void finishTask(){
        FinishTask fTask = new FinishTask();

        System.out.println("\nFinish task with id: ");
        fTask.q_taskID = goodInput(input.nextLine());

        System.out.println("Sending request to server...");

        try {
            outStream.writeObject(fTask);
        } catch (IOException e) {
            System.out.println("Exception while sending request");
        }

        try {
            fTask = (FinishTask) inStream.readObject();
        } catch (IOException e) {
            System.out.println("Exception when trying to obtain reply from server");
        } catch (ClassNotFoundException e) {
            System.out.println("Exception when trying to obtain reply from server - ClassNotFound");
        }

        if(fTask.r_errors != null) {
            System.out.println("\nUps");
            for (String s : fTask.r_errors)
                System.out.println(s);
            return;
        }

        System.out.println("Finished task with id: " + fTask.q_taskID);
    }

    public void showTaskTypes(){
        System.out.println("Sending request to server...");

        ListAll la = new ListAll();

        try {
            outStream.writeObject(la);
        } catch (IOException e) {
            System.out.println("Exception while sending request");
        }

        try {
            la = (ListAll) inStream.readObject();
        } catch (IOException e) {
            System.out.println("Exception when trying to obtain reply from server");
        } catch (ClassNotFoundException e) {
            System.out.println("Exception when trying to obtain reply from server - ClassNotFound");
        }

        if(la.r_errors != null) {
            System.out.println("\nUps");
            for (String s : la.r_errors)
                System.out.println(s);
            return;
        }

        for(String t : la.r_instances.keySet()) {
            System.out.println("Tipo: " + t);
            Collection c = la.r_instances.get(t);
            for(Object i : c)
                System.out.println(" >id: " + i);
        }
    }

    public void showRunningTasks(){
        System.out.println("Sending request to server...");

        ListWorking lw = new ListWorking();

        try {
            outStream.writeObject(lw);
        } catch (IOException e) {
            System.out.println("Exception while sending request");
        }

        try {
            lw = (ListWorking) inStream.readObject();
        } catch (IOException e) {
            System.out.println("Exception when trying to obtain reply from server");
        } catch (ClassNotFoundException e) {
            System.out.println("Exception when trying to obtain reply from server - ClassNotFound");
        }

        if(lw.r_errors != null) {
            System.out.println("\nUps");
            for (String s : lw.r_errors)
                System.out.println(s);
            return;
        }

        for(String t : lw.r_instances.keySet()) {
            System.out.println("Tipo: " + t);
            Collection c = lw.r_instances.get(t);
            for(Object i : c)
                System.out.println(" >id: " + i);
        }
    }

    public void stockUpWarehouse(){
        packet.Store sW = new packet.Store();

        System.out.println("\nMaterial: ");
        sW.q_name = input.nextLine();

        System.out.println("\nMaterial: ");
        sW.q_quantity = goodInput(input.nextLine());

        System.out.println("Sending request to server...");

        try {
            outStream.writeObject(sW);
        } catch (IOException e) {
            System.out.println("Exception while sending request");
        }

        try {
            sW = (Store) inStream.readObject();
        } catch (IOException e) {
            System.out.println("Exception when trying to obtain reply from server");
        } catch (ClassNotFoundException e) {
            System.out.println("Exception when trying to obtain reply from server - ClassNotFound");
        }

        if(sW.r_errors != null) {
            System.out.println("\nUps");
            for (String s : sW.r_errors)
                System.out.println(s);
            return;
        }

        System.out.println("Added " + sW.q_quantity + " " + sW.q_name + " to Warehouse");
    }

}
