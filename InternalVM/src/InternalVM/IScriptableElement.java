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
public interface IScriptableElement {
    
    public DefObject getDescriptor();
    
    /**
     * used by validateTOKEN
     */
    static final Pattern TOKENVALIDATOR = Pattern.compile("[A-Z_][A-Z_0-9]*");
    
    /**
     * Returns true if the TOKEN is valid
     * 
     * @param token uppercase string that rapresents this object (class) in the script
     * @return true if valid token, false if not valid
     */
    public static boolean ValidateTOKEN(String token) {
        return TOKENVALIDATOR.matcher(token).matches();
    }
    
    /**
     * Returns the parameter token identifier
     * @param index
     * @return
     */
    String getParameterToken(int index);
    
    /**
     * returns the float value of the property specified by index
     * @param index
     * @return parameter's value
     */
    JVariable getPropertyValue(int propertyID);
    JVariable getPropertyArrayValue(int propertyID, int Index);
    boolean getPropertyValueBoolean(int propertyID);
    double getPropertyValueFloat(int propertyID);
    long getPropertyValueInt(int propertyID);
    String getPropertyValueString(int propertyID);
    JVariable[] getPropertyArray(int propertyID);

    /**
     * Sets the value of the specified property
     * @param index index of the property to set
     * @param aValue new value for the property
     */
    void setPropertyValue(int propertyID, JVariable aValue);
    void setPropertyArrayValue(int propertyID, int index, JVariable aValue);
    void setPropertyValueFloat(int propertyID, double aValue);
    void setPropertyValueInteger(int propertyID, long aValue);
    void setPropertyValueString(int propertyID, String aValue);
    void setPropertyValueBoolean(int propertyID, boolean aValue);

    /**
     * returns the mnemonic identifier of this object to be used by the compiler;
     * notice that the token must be UPPERCASE string with no spaces or simbols
     * except _, can contain numbers, but no leading numbers (but _00xxx does 
     * work)
     * 
     * @return identifier token
     */
    String getToken();
    
    /**
     * Calls a function of the object identified by funcIndex, using the
     * registers of the Virtual Machine as parameters
     * 
     * @param funcIndex
     * @param regs
     * @return true if success, false if some errors occurred
     */
    boolean callMethod(int functionID, JRegisters regs);
    
    /**
     * This function is used inside the compiler and Virtual Machine to
     * determine if the object is a the same type or a subtype of the given 
     * type.
     * 
     * @param type the integer representation of the type inside the VMProvider
     * @return true if the vaiable is a type or a subtype of the specified Type
     */
    public boolean IsTypeOf(int type);

    /**
     * returns the current error message. The message is CONSUMED after the call, 
     * meaning that any subsequent calls will return ""
     * @return error message used by Virtual Machine log
     */
    String errmsg();
}
