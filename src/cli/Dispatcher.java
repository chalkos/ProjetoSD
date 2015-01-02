package cli;

import packet.*;
import server.Server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

// Sends queries to server, through proxy or not
public class Dispatcher {
    Boolean isServer;
    Integer port;
    Server server;
    Socket clientSocket;

    // start in client mode and connect to server through socket
    Dispatcher(Integer port) throws IOException {
        isServer = false;
        this.port = port;
        this.server = null;
        clientSocket = new Socket("127.0.0.1", port);
    }

    // start in server mode and start a new server
    Dispatcher(Server server){
        isServer = true;
        this.port = null;
        this.server = server;
    }

    public void terminate(){
        if(isServer){
            server.stop();
        }else{
            try {
                clientSocket.shutdownOutput(); // Sends the 'FIN' on the network
            } catch (Exception e) {} // for when the stream is somehow damaged

            try {
                InputStream is = clientSocket.getInputStream(); // obtain stream
                while (is.read() >= 0) ; // "read()" returns '-1' when the 'FIN' is reached
            } catch (Exception e) {} // for when the stream is somehow damaged

            try {
                clientSocket.close(); // Now we can close the Socket
            } catch (Exception e) {} // for when something is somehow damaged

            clientSocket = null; //now it's closed!
        }
    }

    public void doCreateTaskType(CreateTaskType obj) {

    }

    public void doStartTask(StartTask obj) {

    }

    public void doFinishTask(FinishTask obj) {

    }

    public void doListAll(ListAll obj) {

    }

    public void doLogin(Login obj) {

    }

    public void doStore(Store obj) {

    }

    public void doSubscribe(Subscribe obj){

    }
}