/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM.Compiler;

/**
 *
 * @author luca.scarcia
 */
public class JVariableDescriptor {

    public final int ID;
    public final String Name;
    public final JClassDescriptor Type;

    public JVariableDescriptor(int id, String name, JClassDescriptor type) {
        this.ID = id;
        this.Name = name;
        this.Type = type;            
    }
    
}
