/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM.Parser;

import InternalVM.Lexer.JLexerToken;
import InternalVM.Lexer.JTokenizeHelper;
import InternalVM.Parser.JALPHI.JScrLanJALPHI;
import JScriptParser.VMProviderInterface;
import java.text.ParseException;
import java.util.Iterator;

/**
 *
 * @author luca.scarcia
 */
public abstract class JScriptingLanguage {
    
    public static JScriptingLanguage JALPHI = new JScrLanJALPHI();

    public abstract String getName();
    public abstract double getRevision();
    public abstract JTokenizeHelper getHelper();
    public abstract JPseudoProgram parse(Iterator<JLexerToken> tokens, VMProviderInterface provider) throws ParseException;
    
}
