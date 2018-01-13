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
     * returns the value of the calling object
     */
    GET_THIS, 

    COMPARE,
    MATH_SUM,
    MATH_SUBTRACT,
    MATH_MULTIPLY,
    MATH_DIVIDE, 
    LOGIC_AND, 
    LOGIC_OR,
    BOOL_AND,
    /**
     * Unary not; the only parameter is the object to be inverted
     */
    LOGIC_NOT,
    MATH_MODULE,
    SYS_DECLARE,
    EXECUTE_BLOCK, 
    /**
     * this function returns a variable
     * @param Value String containing the variable name
     */
    GET_VARIABLE,
    /**
     * this function indicates that we are exiting from the scope, so the
     * variables inside it are no longer valid
     */
    CLEAN_BLOCK, 
    /**
     * returns the value stored in Value
     * 
     * @param Value Value to return
     */
    GET_VALUE,
    /**
     * returns the token name stored in Value
     * 
     * @param Value Value to return
     */
    GET_CONSTANT_TOKEN,
    /**
     * returns the String value stored in Value
     * 
     * @param Value Value to return
     */
    GET_CONSTANT_STRING,
    /**
     * returns the integer value stored in Value
     * 
     * @param Value Value to return
     */
    GET_CONSTANT_INTEGER,
    /**
     * returns the floating point value stored in Value
     * 
     * @param Value Value to return
     */
    GET_CONSTANT_FLOAT,
    /**
     * this function returns an identifier
     * @param Value String containing the identifier name
     */
    GET_IDENTIFIER, 
    GET_NULL; 

}
