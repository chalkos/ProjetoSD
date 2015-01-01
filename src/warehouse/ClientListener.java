package warehouse;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by joaorodrigues on 1 Jan 15.
 */
public class ClientListener {
    private BufferedReader reader;

    public ClientListener(BufferedReader r){
        reader = r;
    }

    public void run(){
        String line;
        try {
            while( (line = reader.readLine()) != null){
                System.out.print(line);
            }
        } catch (IOException e) {
            System.err.println("IO Error");
        }
    }
}
