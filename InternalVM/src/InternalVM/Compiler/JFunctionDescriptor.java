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
public class JFunctionDescriptor {
    public final JClassDescriptor ReturnType;
    public final String Name;
    public final int ID;
    public final JVariableDescriptor[] Parameters;

    JFunctionDescriptor(int id, String name) {
        ID=id;
        Name = name;
        ReturnType = null;
        Parameters = null;        
    }

    JFunctionDescriptor(int id, String name, JClassDescriptor returnType) {
        ID=id;
        Name = name;
        ReturnType = returnType;
        Parameters = null;        
    }

    JFunctionDescriptor(int id, String name, JVariableDescriptor[] parameters) {
        ID=id;
        Name = name;
        ReturnType = null;
        Parameters = parameters;        
    }

    JFunctionDescriptor(int id, String name, JVariableDescriptor[] parameters, JClassDescriptor returnType) {
        ID=id;
        Name = name;
        ReturnType = returnType;
        Parameters = parameters;        
    }

}
