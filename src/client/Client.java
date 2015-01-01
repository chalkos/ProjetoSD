
package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;


public class Client {
    private Socket cSocket;
    private boolean notLogged;
    private Scanner input;
    ObjectOutputStream outStream = new ObjectOutputStream(socketToServer.getOutputStream());


    public Client(int port) {
        try{
            cSocket = new Socket("localhost", 12345);
        }
        catch (IOException io) {
            System.out.println("Ups, IO problem!");
        }

        input = new Scanner(System.in);
        notLogged = true;
    }


    public static void main(String[] args) {
        Client client = new Client(12345);
        client.run();
    }

    public void run(){
        String str1 = "nothing yet", str2;
        int cOption;

        while(notLogged){
            System.out.println("------Login------");
            System.out.print("Username: ");
            str1 = input.nextLine();
            System.out.print("Password: ");
            str2 = input.nextLine();
            tryLogin(str1,str2);
        }

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
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        break;
                    case 9:
                        break;
                }
            }
        }
    }

    public void tryLogin(String un, String pw){
        notLogged = false;
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

    public void createTaskType(){
        String str1, str2, str3="";
        HashMap<String,Integer> needs = new HashMap<>();


        System.out.println("\nNew TaskType name: ");
        str1 = input.nextLine();

        System.out.println("\nAdd TaskType need? (y/n)");

        while((str2 = input.nextLine()).equals("y")){
            System.out.println("\nNeeded material name: ");
            str2 = input.nextLine();
            System.out.println("\nNeeded material quantity: ");
            needs.put(str2,goodInput(str3));
        }

        CreateTaskTypeObject cttobj = new CreateTaskTypeObject(str1,needs);

        System.out.println("Sending request to server...");
        outStream.writeObject("test message #"+i);
    }
}
