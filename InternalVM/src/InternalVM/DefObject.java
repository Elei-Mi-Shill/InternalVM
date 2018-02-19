/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author luca.scarcia
 */
public class DefObject {
    public final String Name;
    public final int ID;
    Map<String, DefFunction> IndexedFunctions = new TreeMap<>();
    List<DefFunction> Functions = new ArrayList<>();
    
    public DefObject(int id, String name) {
        ID = id;
        Name = name;
    }
    
    public DefObject(int id, String name, DefFunction[] functions) {
        ID = id;
        Name = name;
    }
    
    public DefFunction getFunction(String name) {
        return IndexedFunctions.get(name);
    }
    
    public DefFunction registerFunction(DefFunction function) throws IllegalArgumentException {
        IndexedFunctions.put(function.Name, function);
        return function;
    }
    
}
