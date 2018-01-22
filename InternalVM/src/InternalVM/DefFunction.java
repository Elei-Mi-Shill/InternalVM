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
    
    public DefFunction (String name, DefParameter[] params) {
        Name = name;
        this.Params = params;
    }
}
