/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM.Lexer;

import InternalVM.Parser.JALPHI.JScrLanJALPHI;
import java.util.Arrays;

/**
 *
 * @author luca.scarcia
 */
    
public class JLexerToken {
    
    public final ELexerTokenType Type;
    public final String Data;
    public final int Extra;

    public JLexerToken(ELexerTokenType type, String data, int extra) {
        Type = type;
        Data = data;
        if(Type==ELexerTokenType.NUMBER) {
            if(!Type.pattern.matcher(Data).matches()) {
                throw new IllegalArgumentException("Invalid number: "+ Arrays.toString(Data.toCharArray()));
            }
        }
        Extra = extra;
    }

    @Override
    public String toString() {
        return String.format("<%s[%d]> %s ", Type.name(), Extra, Data);
    }
}
