/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM.Parser;

import InternalVM.Lexer.JTokenizeHelper;

/**
 *
 * @author luca.scarcia
 */
public abstract class JScriptingLanguageDescriptors {
    
    public final String Name;
    
    public JScriptingLanguageDescriptors(String name) {
        Name = name;
    }
    
    public abstract JScriptingLanguage createLanguageInstance();
    public abstract JTokenizeHelper createHelperInstance();
    
    
}
