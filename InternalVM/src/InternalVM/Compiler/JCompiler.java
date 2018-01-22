/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM.Compiler;

import InternalVM.JAssembly;
import InternalVM.Lexer.JLexer;
import InternalVM.Lexer.JLexerPositionalToken;
import InternalVM.Parser.JPseudoProgram;
import InternalVM.Parser.JScriptingLanguage;
import InternalVM.VMProviderInterface;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.List;

/**
 *
 * @author luca.scarcia
 */
public class JCompiler {

    /**
     * This compiler expects instruction like those:
     * 
     * someObject.doSomething (var1: value1, var2: value2) ;
     * 
     * result = doSomeCalculations (var1: value1);
     * 
     * someObject[3].someChild {
     *      someMethodOfChild(var1: value1, var2: value2, var3:value3); 
     *      someMethodOfChild(var1: value1, var2: value2, var3:value3); 
     *      someMethodOfChild(var1: value1, var2: value2, var3:value3); 
     * }
     * 
     * the above can be read as 
     * 
     * someObject[3].someChild.someMethodOfChild(var1: value1, var2: value2, var3:value3); 
     * someObject[3].someChild.someMethodOfChild(var1: value1, var2: value2, var3:value3); 
     * someObject[3].someChild.someMethodOfChild(var1: value1, var2: value2, var3:value3); 
     * 
     * Notice, all instructions must be terminated with ;
     * 
     * an exception is if the function starts a block; in that case the block end 
     * symbol } will work as instruction terminator
     * 
     * {} delimitate a block; it can be a block from a flow control like if or for
     * or a group of operations done on an object
     * 
     * () delimitates parameters; notiche that parameters are always a couple
     * paramName:paramValue
     * 
     * The name must be always specified and must be a valid name for a parameter
     * of the funcion
     * 
     * : separates the name from the value
     * 
     * [] indicates an array
     * 
     */
    
    /**
     * Variable declarations
     * 
     * as in every language, vairables must be declared at the beginning
     * 
     * The syntax for declaration is
     * 
     * type name;
     * 
     * for null initialyzed variables
     * 
     * type name = const;
     * 
     * for user initialyzed variables
     * 
     * Valid types are int, float, String, Object, Token
     * 
     */
    
     /**
      * Reserved functions are:
      * 
      * if() {
      * }
      * 
      * if is a function! it has only one implicit value that is boolean.
      * 
      * for (initialyze: condition: increment) {}
      * 
      * is also a function with 3 parameters;
      * 
      */ 
    
    /**
     *
     * Compile the source contained in source using the provided Language and
     * advanced function calls/data from Provider
     * 
     * @param Provider a VMProviderInterface object that provides high level definitions for objects and functions
     * @param source source code
     * @param Language Language used to preparse the program
     * @return An assembly that can be executed by the JVMMachine
     * @throws java.text.ParseException
     */
    public static JAssembly compile(VMProviderInterface Provider, String source , JScriptingLanguage Language) throws ParseException, IOException {
        List<JLexerPositionalToken> tokens = JLexer.tokenize(new StringReader(source), Language.getHelper());
        JPseudoProgram tempProgram = Language.parse(tokens.iterator(), Provider);
        return null;
    }
    
    /**
     * Validate an identifier
     * 
     * @param identifier string containing the identifier to validate
     * @return true if valid, false if malformed
     */
    public static boolean validateIdentifier(String identifier) {
        return false;
    }
    
}
