/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JScriptParser;

/**
 *
 * @author luca.scarcia
 */
public enum E_ACTION {

    /**
     * internal use only;
     */
    UNDEFINED,
    /**
     * NO OPeration; means it's an empty line
     */
    NOOP,
    /**
     * Exits a block
     */
    EXIT_BLOCK, 
    /**
     * @param param1 IScriptableObject: parent object
     * @param param2 String: property identifier
     */
    GET_PROPERTY,
    /**
     * @param param1 IScriptableObject: parent object
     * @param param2 String: property identifier
     * @param param3 int: array index 
     */
    GET_PROPERTY_ARRAY,
    /**
     * @param param1 IScriptableObject: parent object
     * @param param2 String: method identifier
     * @param param Object[]: list of parameters the function wuill call
     */
    CALL_METHOD,
    /**
     * NB: a type check will be made on parameters. They must be both the same
     * type
     * 
     * @param param1 JPseudoInstruction: item that must be assigned
     * @param param2 JPseudoInstruction: value to assign
     */
    ASSIGN,
    /**
     * returns the value of the specified object
     * 
     * @param param1 JPseudoInstruction: object that contains the value
     */
    GET_VALUE,
    /**
     * returns a constant value
     * 
     * @param param1 Object: value of the constant
     */
    GET_CONSTANT,
    /**
     * returns the value of the calling object
     */
    GET_THIS, 

    COMPARE,
    SUM,
    MATH_SUBTRACT,
    MATH_MULTIPLY,
    MATH_DIVIDE, 
    LOGIC_AND, 
    LOGIC_OR, 
    /**
     * Unary not; the only parameter is the object to be inverted
     */
    LOGIC_NOT,
    MATH_MODULE,
    SYS_DECLARE,
    EXECUTE_BLOCK, 
    GET_VARIABLE,
    /**
     * this function indicates that we are exiting from the scope, so the
     * variables inside it are no longer valid
     */
    CLEAN_BLOCK;

}
