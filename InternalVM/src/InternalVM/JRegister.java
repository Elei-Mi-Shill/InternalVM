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
public class JRegister  {
    
    E_VAR_TYPE Type;
    Object Value;
    
    public E_VAR_TYPE getType() {
        return Type;
    }
    
    public Object getValue() {
        return Value;
    }
    
    public void setValue(JVariable var) {
        Type = var.Type;
        Value = var.Value;
    }
    
    public void setValue(E_VAR_TYPE type, Object value) {
        Type = type;
        Value = value;
    }
    
    
}
