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
    
    public static DefParameter[] EmptyParameters = new DefParameter[0];
    
    public final E_VAR_TYPE Type;
    public final String Name;
    public final DefObject SubType;
    
    public DefParameter(String name, E_VAR_TYPE type) {
        Type = type;
        Name = name;
        SubType = null;
    }

    public DefParameter(String name, E_VAR_TYPE type, DefObject subType) {
        Type = type;
        Name = name;
        if(Type.CanSpecify) {
            SubType = subType;
        } else throw new IllegalArgumentException("Type "+type.name()+" cannot accept a specifier!");
    }
}
