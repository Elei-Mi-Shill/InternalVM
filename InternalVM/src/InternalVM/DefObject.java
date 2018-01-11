/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JScriptParser;

/**
 *
 * @author luca.scarcia
 */
public class DefObject {
    public final String Name;
    int ID;
    DefFunction[] Functions;
    
    public DefObject(String name) {
        Name = name;
    }
    
    public DefObject(String name, DefFunction[] functions) {
        Name = name;
        Functions = functions;
    }
    
    public DefFunction[] getFunctions() {
        return Functions;
    }
    
}
