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
public enum E_VAR_TYPE {

    /**
     * Boolean type
     */
    BOOLEAN(false),
    /**
     * Boolean[] type
     */
    ARRAY_BOOLEAN(false, true),
    /**
     * Integer type
     */
    INTEGER(false),
    /**
     * Integer[] type
     */
    ARRAY_INTEGER(false, true),
    /**
     * Double type
     */
    FLOAT(false),
    /**
     * Double[] type
     */
    ARRAY_FLOAT(false, true),
    /**
     * String object
     */
    STRING(false),
    /**
     * String[] object
     */
    ARRAY_STRING(false, true),
    /**
     * IScriptableObject
     */
    OBJ(true),
    /**
     * IScriptableObject
     */
    ARRAY_OBJ(true, true),
    /**
     * Token object
     */
    IDENTIFIER(true), 
    /**
     * Token[] object
     */
    ARRAY_IDENTIFIER(true, true), 
    /**
     * Void Object
     */
    VOID(false),
    /**
     * Similar to Java Class
     * @unsupported
     */
    CUSTOM(false);
    
    public final boolean CanSpecify;
    public final boolean IsArray;
    
    E_VAR_TYPE(boolean canSpecify) {
        CanSpecify = canSpecify;
        IsArray = false;
    }

    E_VAR_TYPE(boolean canSpecify, boolean isArray) {
        CanSpecify = canSpecify;
        IsArray = isArray;
    }
    
}
