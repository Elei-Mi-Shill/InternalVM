/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM.Lexer;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luca.scarcia
 */
public class JLexer {
    
    private int Line = 0;
    private int Row = 0;
    private int Total = 0; 
    private static JTokenizeHelper Helper;
    
    E_TOKENIZER_STATE State = E_TOKENIZER_STATE.DEFAULT;

    public JLexer(JTokenizeHelper helper) {
        Line = 0;
        Row = 0;
        Total = 0;
        State = E_TOKENIZER_STATE.DEFAULT;
        Helper = helper;
    }
    
    public static enum E_TOKENIZER_STATE {
        UNDEFINED, DEFAULT, STRING, IDENTIFIER, NUMBER, AGGREGABLES, WHIESPACE, COMMENT;
    }
    
    public static List<JLexerToken> tokenize(Reader data, JTokenizeHelper helper) throws IOException, ParseException {
        int total = 0;
        int line = 0;
        int row = 0;
        helper.reset();
        List<JLexerToken> list = new ArrayList<>();
        E_TOKENIZER_STATE State = E_TOKENIZER_STATE.DEFAULT;
        int cursorValue=data.read();
        JLexerToken tempToken=null;
        char c;
        while(cursorValue>=0) {
            E_TOKENIZER_STATE newState;
            c=(char)cursorValue;
            total++;
            if(c == '\n' || c=='\r') {
                line++;
                row=0;
            } else 
                row++;
            switch(State) {
                case DEFAULT:
                    if(helper.isIdentifierStart(c)) {
                        helper.initialyzeIdentifier(c);
                    } else if(helper.isNumberStart(c)) {
                        helper.initialyzeNumber(c);
                    } else if(helper.isStringStart(c)) {
                        helper.initialyzeString(c);
                    } else if(helper.isAggregable(c)) {
                        helper.initialyzeAggregable(c);
                    } else if(helper.isWhiteSpace(c)) {
                        helper.initialyzeWhitespace(c);
                    } else if(helper.isCommentStart(c)) {
                        helper.initialyzeComment(c);
                    } else {
                        tempToken = helper.tokenizeSymbol(c);
                        if(tempToken!=null)
                            list.add(tempToken);
                    }
                    break;
                case AGGREGABLES:
                    if(helper.appendAggregable(c)) {
                        list.addAll(helper.tokenizeAggregables());
                    }                         
                    break;
                case STRING:
                    if(helper.appendToString(c)) {
                        list.addAll(helper.tokenizeString());
                    }                         
                    break;
                case COMMENT:
                    if(helper.appendToComment(c)) {
                        list.addAll(helper.tokenizeComment());
                    }                         
                    break;
                case NUMBER:
                    if(helper.appendToNumber(c)) {
                        list.addAll(helper.tokenizeNumber());
                    }                         
                    break;
                case WHIESPACE:
                    if(helper.appendToWhiteSpace(c)) {
                        list.addAll(helper.tokenizeWhitespace());
                    }                         
                    break;
                case IDENTIFIER:
                    if(helper.appendToIdentifier(c)) {
                        list.addAll(helper.tokenizeIdentifier());
                    }                         
                    break;
                default:
                    throw new AssertionError(State.name());
            }
            if((newState = helper.continueWithState())!=E_TOKENIZER_STATE.UNDEFINED)
                State = newState;
            if(helper.consumeLastCharacter()) 
                cursorValue=data.read();
        }
        if(State != E_TOKENIZER_STATE.DEFAULT) {
            throw new ParseException("Unexpected end of file!", -1);
        }
        return list;
    }
    
    public static List<JLexerToken> tokenize(String data, JTokenizeHelper helper) throws ParseException, IOException {
        return tokenize(new StringReader(data), helper);
    }
}
