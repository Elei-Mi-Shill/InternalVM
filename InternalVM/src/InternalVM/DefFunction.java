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
public class DefFunction {
    public final String Name;
    DefParameter[] Params;
    public final int ID;
    public final E_VAR_TYPE ReturnType;
    public final DefObject SubType;
    
    public DefFunction (int id, String name, DefParameter[] params, E_VAR_TYPE returnType, DefObject subType) {
        Name = name;
        this.Params = params;
        ID = id;
        ReturnType = returnType;
        SubType = subType;
    }
}
