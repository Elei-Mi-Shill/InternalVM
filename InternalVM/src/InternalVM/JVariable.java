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
public class JVariable {
    public final E_VAR_TYPE Type;
    public final String Name;
    Object Value;
    
    JVariable(String name, E_VAR_TYPE type) {
        Name = name;
        Type = type;
        switch(Type) {
            case INTEGER:
                Value = 0;
                break;
            case FLOAT:
                Value = 0.0;
                break;
            case STRING:
                Value = "";
                break;
            case OBJ:
                Value = null;
                break;
        }
    }
    
}
