package client;

import java.util.HashMap;

/**
 * Created by tiago on 01-01-2015.
 */
public class CreateTaskTypeObject {
    private String name;
    private HashMap<String, Integer> needs;

    public CreateTaskTypeObject(String name, HashMap<String,Integer> needs){
        this.name = name;
        this.needs = new HashMap<String,Integer>(needs);
    }
}
