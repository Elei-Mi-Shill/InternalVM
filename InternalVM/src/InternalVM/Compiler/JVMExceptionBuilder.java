/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM.Compiler;

import InternalVM.Lexer.JDocumentPosition;
import InternalVM.Lexer.JLexerPositionalToken;
import InternalVM.Lexer.JLexerToken;
import InternalVM.Parser.SimpleInitLanguage.JScrLanSimpleInit;
import java.text.MessageFormat;
import java.text.ParseException;

/**
 *
 * @author luca.scarcia
 */
public class JVMExceptionBuilder {
    
    /**
     *
     * String containing the message to format.
     * {0} is for the absolute position, 
     * {1} for the row
     * {2} for the column
     * {3} for the token data
     * {4} for the token type
     * 
     * @param format message to format
     * @param token token that caused the exception
     * @param severity severity of the exception
     * @return
     */
    public static ParseException ParseException(String format, JLexerPositionalToken token ,int severity) {
        String msg = MessageFormat.format(format,new Object[] {token.Pos.Absolute, token.Pos.Row, token.Pos.Col, token.Token.Data, token.Token.Type.name()} );
        return new ParseException(msg, severity);
    }

    public static ParseException ParseException(String msg, JLexerToken token, JDocumentPosition Pos, int severity) {
        msg = MessageFormat.format(msg,new Object[] {Pos.Absolute, Pos.Row, Pos.Col, token.Data, token.Type.name()} );
        return new ParseException(msg, severity);
    }
    
    /**
     * String containing the message to format.
     * {0} is for the absolute position, 
     * {1} for the row
     * {2} for the column
     *
     * @param format
     * @param pos
     * @param severity
     * @return
     */
    public static ParseException buildException(String format, JDocumentPosition pos,int severity) {
        String msg = MessageFormat.format(format,new Object[] {pos.Absolute, pos.Row, pos.Col} );
        return new ParseException(msg, severity);
    }

    public static ParseException buildEOFException(String format, int severity) {
        String msg = format.concat(" before the end of file!");
        return new ParseException(msg, severity);
    }

    public static UnsupportedOperationException UnexpectedException(String msg, JDocumentPosition pos) {
        msg = MessageFormat.format(msg,new Object[] {pos.Absolute, pos.Row, pos.Col} );
        throw new UnsupportedOperationException(msg);
    }

    public static IllegalArgumentException IllegalArgumentException(String msg) {
        throw new IllegalArgumentException(msg); //To change body of generated methods, choose Tools | Templates.
    }

    public static IllegalArgumentException IllegalArgumentException(String msg, JDocumentPosition pos) {
        msg = MessageFormat.format(msg,new Object[] {pos.Absolute, pos.Row, pos.Col} );
        throw new IllegalArgumentException(msg); //To change body of generated methods, choose Tools | Templates.
    }

    public static NullPointerException NullPointerException(String null_pointer) {
        throw new NullPointerException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
