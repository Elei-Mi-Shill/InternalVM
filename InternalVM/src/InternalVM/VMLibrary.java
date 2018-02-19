/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM;

/**
 *
 * This interface defines a library as a single class object; functions are its
 * methods.
 * 
 * @author luca.scarcia
 */
public interface VMLibrary {
    
    DefObject getDescriptor();
    /** 
     * functions used by the parser and compiler 
     **/
    
    /**
     * returns the library name;
     * @return 
     */

    String getName();
    public DefFunction decodeFunction(String functionName) throws IllegalArgumentException;
    public DefFunction decodeFunction(int funcionID) throws IllegalArgumentException;
    public DefObject decodeSubType(String typeName) throws IllegalArgumentException;
    public DefObject decodeSubType(int classID) throws IllegalArgumentException;
    public int decodeToken(String TokenType);
    
    public IScriptableElement getInstanceFromToken(JIdentifier id);
    public IScriptableElement getInstanceFromToken(DefObject subType, String id);
    public IScriptableElement getInstanceFromToken(int subType, int id);
    
    /**
     * actual VM functions
     */
    public IScriptableElement getInstance(String objectName) throws IllegalArgumentException;
    public IScriptableElement getInstance(JIdentifier objectID) throws IllegalArgumentException;
    public boolean callFunction(int Function, JRegisters Registers, JVariable[] Params) throws NoSuchMethodException;
    
}
