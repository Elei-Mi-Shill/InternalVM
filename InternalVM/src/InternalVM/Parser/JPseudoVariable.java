/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM.Parser;

/**
 *
 * @author luca.scarcia
 */
public class JPseudoVariable {
    JPseudoType Type;
    String Name;
    
    public static JPseudoVariable THIS = new JPseudoVariable("this", JPseudoType.CANONIC_OBJECT);
    public static JPseudoVariable NULL = new JPseudoVariable("null", JPseudoType.VOID);

    JPseudoVariable() {
        Type=null;
        Name=null;
    }
    
    public JPseudoVariable(String name, JPseudoType type) {
        Name = name;
        Type = type;
    }
    
    void append(StringBuilder sb) {
        sb.append(Name);
        sb.append(":");
        Type.append(sb);
    }
}
