/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM.Parser;

import InternalVM.Lexer.JLexerPositionalToken;
import InternalVM.Lexer.JTokenizeHelper;
import InternalVM.VMProviderInterface;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author luca.scarcia
 */
public abstract class JScriptingLanguage {
    
    private static List<JScriptingLanguageDescriptors> Languages;
    
    public abstract String getName();
    public abstract double getRevision();
    public abstract JTokenizeHelper getHelper();
    public abstract JPseudoProgram parse(Iterator<JLexerPositionalToken> tokens, VMProviderInterface provider) throws ParseException;
    
}
