/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM.Compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author luca.scarcia
 */
public class JClassDescriptor {
    public final String Name;
    public final int ID;
    public final TreeMap<String, JFunctionDescriptor> Methods = new TreeMap<>();
    public final List<JFunctionDescriptor> MethodArray = new ArrayList<>();
    public final TreeMap<String, JVariableDescriptor> Properties = new TreeMap<>();
    public final List<JVariableDescriptor> PropertyArray = new ArrayList<>();
    
    public final static JClassDescriptor VOID = new JClassDescriptor(-1, "void");
    public final static JClassDescriptor INTEGER = new JClassDescriptor(-2, "integer");
    public final static JClassDescriptor FLOAT = new JClassDescriptor(-3, "float"); 
    public final static JClassDescriptor STRING = new JClassDescriptor(-4, "string"); 
    public final static JClassDescriptor TOKEN = new JClassDescriptor(-5, "token"); 
    public final static JClassDescriptor OBJECT = new JClassDescriptor(-6, "token"); 
    

    JClassDescriptor(int id, String name) {
        Name = name;
        ID = id;
    }

    JClassDescriptor(int id, String name, JFunctionDescriptor[] methods) {
        Name = name;
        ID = id;
        for(JFunctionDescriptor method:methods)
            Methods.put(method.Name, method);
    }

    JClassDescriptor(int id, String name, JFunctionDescriptor[] methods, JVariableDescriptor[] properties) {
        Name = name;
        ID = id;
        for(JFunctionDescriptor method:methods) {
            Methods.put(method.Name, method);
            MethodArray.add(method.ID, method);
        }
        for(JVariableDescriptor property:properties) {
            Properties.put(property.Name, property);
            PropertyArray.add(property.ID, property);
        }        
    }
    
    public boolean addMethod(JFunctionDescriptor method) {
        if(method.ID<MethodArray.size())
            if(MethodArray.get(method.ID)!=null) {
                return false;
            }
        if(Methods.containsKey(method.Name))
            return false;
        MethodArray.add(method.ID, method);
        Methods.put(method.Name, method);
        return true;
    }
    
    public boolean addProperty(JVariableDescriptor property) {
        if(property.ID<PropertyArray.size())
            if(MethodArray.get(property.ID)!=null) {
                return false;
            }
        if(Properties.containsKey(property.Name))
            return false;
        PropertyArray.add(property.ID, property);
        Properties.put(property.Name, property);
        return true;
    }
    
    JVariableDescriptor getProperty(String name) {
        return Properties.get(name);
    }
    
    JVariableDescriptor getProperty(int id) {
        return PropertyArray.get(id);
    }
    
    JFunctionDescriptor getMethod(String name) {
        return Methods.get(name);
    }
    
    JFunctionDescriptor getMethod(int id) {
        return MethodArray.get(id);
    }
    
    static {
        INTEGER.addMethod(new JFunctionDescriptor(0, "toString", STRING));
        INTEGER.addMethod(new JFunctionDescriptor(1, "toFloat", FLOAT));

        INTEGER.addMethod(new JFunctionDescriptor(0, "toString", STRING));
        INTEGER.addMethod(new JFunctionDescriptor(1, "toInteger", INTEGER));

        STRING.addMethod(new JFunctionDescriptor(0, "toInteger", INTEGER));
        STRING.addMethod(new JFunctionDescriptor(1, "toFloat", FLOAT));
        STRING.addMethod(new JFunctionDescriptor(2, "contains", INTEGER));
        STRING.addMethod(new JFunctionDescriptor(3, "substr", new JVariableDescriptor[] {
                    new JVariableDescriptor(0, "start", INTEGER),
                    new JVariableDescriptor(0, "end", INTEGER)
                }
                ,STRING));

        TOKEN.addMethod(new JFunctionDescriptor(0, "toString", STRING));

        OBJECT.addMethod(new JFunctionDescriptor(0, "toString", STRING));
    }

}
