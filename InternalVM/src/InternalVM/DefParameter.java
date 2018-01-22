/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM;

/**
 *
 * @author luca.scarcia
 */
public class DefParameter {
    
    public final E_VAR_TYPE Type;
    public final String Name;
    public final DefObject SubType;
    
    public DefParameter(String name, E_VAR_TYPE type) {
        Type = type;
        Name = name;
        SubType = null;
    }

    public DefParameter(String name, DefObject subType) {
        Type = E_VAR_TYPE.OBJ;
        Name = name;
        SubType = subType;
    }
}
