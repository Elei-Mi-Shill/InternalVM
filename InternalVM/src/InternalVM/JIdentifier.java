/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM;

import java.util.regex.Pattern;

/**
 *
 * @author luca.scarcia
 */
public class JIdentifier implements Comparable<JIdentifier> {
    
    public Pattern ValidateID = Pattern.compile("_?[A-Z][A-Z0-9_]*");
    
    public final int Category;
    public final String ID; 
    public int Index;
    
    public JIdentifier(int cat, String id) {
        if(ValidateID.matcher(id).matches()) {
            Category=cat;
            ID = id;
        } else throw new IllegalArgumentException(id+" is not a valid identifier! Only Uppercase letters, numbers and anderscore are allowed!");
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof JIdentifier) {
            JIdentifier id = (JIdentifier)o;
            return id.Category == Category && ID.equalsIgnoreCase(id.ID);
        } if(o instanceof String) {
            return ID.equals(o);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }    

    @Override
    public int compareTo(JIdentifier j) {
        return ID.compareTo(j.ID);
    }

    public int compareTo(String s) {
        return ID.compareTo(s);
    }
    
}
