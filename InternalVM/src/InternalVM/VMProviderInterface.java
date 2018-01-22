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
public interface VMProviderInterface {
    public boolean callFunction(int Function, JRegisters Registers, JVariable[] Params) throws NoSuchMethodException;
    public int decodeFunction(String functionName) throws IllegalArgumentException;
    public int decodeFunction(int ObjectID, String functionName) throws IllegalArgumentException;
    public int decodeTokenType(String subType);
    public int decodeObjectType(String typeName) throws IllegalArgumentException;
    public IScriptableElement getObject(String objectName) throws IllegalArgumentException;
    public DefObject[] getObjectList();
    public DefFunction[] getFunctionList(int ObjectID);
    public DefFunction[] getFunctionList(DefObject o);
    public int decodeToken(String TokenType);
}
