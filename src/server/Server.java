package server;

import packet.*;
import warehouse.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final Warehouse warehouse = new Warehouse();
    private ServerSocket serverSocket;

    public Server(int startingPort) {
        while (true) {
            try {
                this.serverSocket = new ServerSocket(startingPort);
                System.err.println("Server socket created on port " + startingPort);
                break;
            } catch (IOException e) {
                System.err.println("Port " + startingPort + " occupied. Trying again...");
                startingPort++;
            }
        }
    }

    public Worker newWorker() throws IOException {
        return new Worker(serverSocket.accept(), warehouse);
    }

    private class Worker implements Runnable {
        private Socket socket;
        private Warehouse warehouse;

        ObjectOutputStream out;
        ObjectInputStream in;

        Worker(Socket s, Warehouse w) throws IOException {
            socket = s;
            warehouse = w;

<<<<<<< HEAD
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        }

        private void send(Object obj) throws IOException {
            out.writeObject(obj);
            out.flush();
=======
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
        }

        public void write(String msg){
            out.println(msg);
>>>>>>> Incomplete Client - Pushing so jorod makes ServerListener
        }

        private Serializable receive() throws IOException, ClassNotFoundException {
            return (Serializable)in.readObject();
        }

        private void doCreateTaskType(CreateTaskType obj) throws IOException {
            try {
                warehouse.newTaskType(obj.q_name, obj.q_itens);
            } catch (ExistentTaskException e) {
                obj.r_errors.add(e.getUserMessage());
            }

            send(obj);
        }

        private void doStartTask(StartTask obj) throws IOException {
            try {
                warehouse.startTask(obj.q_name);
            } catch (InexistentTaskTypeException e) {
                obj.r_errors.add(e.getUserMessage());
            } catch (InexistentItemException e) {
                obj.r_errors.add(e.getUserMessage());
            }

            send(obj);
        }

        private void doFinishTask(FinishTask obj) throws IOException {
            try {
                warehouse.endTask(obj.q_taskID);
            } catch (InexistentTaskException e) {
                obj.r_errors.add(e.getUserMessage());
            } catch (InexistentItemException e) {
                obj.r_errors.add(e.getUserMessage());
            }

            send(obj);
        }

        private void doListAll(ListAll obj) throws IOException {
            obj.r_instances = warehouse.getRunningTasks();

            send(obj);
        }

        private void doListWorking(ListWorking obj){
            obj.r_instances = warehouse.getUserTasks(userid);

            send(obj);
        }

        private void doLogin(Login obj){

        }

        private void doStore(Store obj) throws IOException {
            try {
                warehouse.stockUp(obj.q_name, obj.q_quantity);
            } catch (InvalidItemQuantityException e) {
                obj.r_errors.add(e.getUserMessage());
            }

            send(obj);
        }

        private void doSubscribe(Subscribe obj){
            //TODO
        }

        @Override
        public void run() {
            // try to authenticate before anything else
            try {
                Serializable obj = receive();

                if (obj instanceof Login)
                    doLogin((Login) obj);
                else
                    throw new UnknownPacketException("Server received an unexpected packet.");

            } catch (Exception e) {
                e.printStackTrace();
            }

            if(/* failed login */){
                // disconnect and exit this thread
            }


            while(/* connection open */) {
                try {
                    Serializable obj = receive();

                    if (obj instanceof CreateTaskType)
                        doCreateTaskType((CreateTaskType) obj);
                    else if (obj instanceof StartTask)
                        doStartTask((StartTask) obj);
                    else if (obj instanceof FinishTask)
                        doFinishTask((FinishTask) obj);
                    else if (obj instanceof ListAll)
                        doListAll((ListAll) obj);
                    else if (obj instanceof ListWorking)
                        doListWorking((ListWorking) obj);
                    else if (obj instanceof Login)
                        doLogin((Login) obj);
                    else if (obj instanceof Store)
                        doStore((Store) obj);
                    else if (obj instanceof Subscribe)
                        doSubscribe((Subscribe) obj);
                    else
                        throw new UnknownPacketException("Server received an unexpected packet.");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException{
        Server server = new Server(4000);

        while(true)
            new Thread(server.newWorker()).start();
    }
}
