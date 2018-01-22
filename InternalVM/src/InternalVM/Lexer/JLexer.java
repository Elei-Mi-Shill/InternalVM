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
    
    public static List<JLexerPositionalToken> tokenize(Reader data, JTokenizeHelper helper) throws IOException, ParseException {
        int total = 0;
        int line = 0;
        int col = 0;
        helper.reset();
        List<JLexerPositionalToken> list = new ArrayList<>();
        E_TOKENIZER_STATE State = E_TOKENIZER_STATE.DEFAULT;
        int cursorValue=data.read();
        JLexerPositionalToken tempToken=null;
        char c;
        while(cursorValue>=0) {
            E_TOKENIZER_STATE newState= E_TOKENIZER_STATE.UNDEFINED;
            c=(char)cursorValue;
            switch(State) {
                case DEFAULT:
                    if(helper.isIdentifierStart(c)) {
                        helper.initialyzeIdentifier(c, line, col, total);
                    } else if(helper.isNumberStart(c)) {
                        helper.initialyzeNumber(c, line, col, total);
                    } else if(helper.isStringStart(c)) {
                        helper.initialyzeString(c, line, col, total);
                    } else if(helper.isAggregable(c)) {
                        helper.initialyzeAggregable(c, line, col, total);
                    } else if(helper.isWhiteSpace(c)) {
                        helper.initialyzeWhitespace(c, line, col, total);
                    } else if(helper.isCommentStart(c)) {
                        helper.initialyzeComment(c, line, col, total);
                    } else {
                        tempToken = helper.tokenizeSymbol(c, line, col, total);
                        if(tempToken!=null)
                            list.add(tempToken);
                    }
                    break;
                case AGGREGABLES:
                    if(helper.appendAggregable(c)) {
                        list.add(helper.tokenizeAggregables());
                    }                         
                    break;
                case STRING:
                    if(helper.appendToString(c)) {
                        list.add(helper.tokenizeString());
                    }                         
                    break;
                case COMMENT:
                    if(helper.appendToComment(c)) {
                        list.add(helper.tokenizeComment());
                    }                         
                    break;
                case NUMBER:
                    if(helper.appendToNumber(c)) {
                        list.add(helper.tokenizeNumber());
                    }                         
                    break;
                case WHIESPACE:
                    if(helper.appendToWhiteSpace(c)) {
                        list.add(helper.tokenizeWhitespace());
                    }                         
                    break;
                case IDENTIFIER:
                    if(helper.appendToIdentifier(c)) {
                        list.add(helper.tokenizeIdentifier());
                    }                         
                    break;
                default:
                    throw new AssertionError(State.name());
            }
            if((newState = helper.continueWithState())!=E_TOKENIZER_STATE.UNDEFINED)
                State = newState;
            if(helper.consumeLastCharacter()) {
                cursorValue=data.read();
                total++;
                if((char)cursorValue == '\n' || (char)cursorValue=='\r') {
                    line++;
                    col=0;
                } else 
                    col++;
            }
        }
        if(State != E_TOKENIZER_STATE.DEFAULT && State!=E_TOKENIZER_STATE.WHIESPACE) {
            throw new ParseException("Unexpected end of file while in state "+State.name()+"!", -1);
        }
        return list;
    }
    
    public static List<JLexerPositionalToken> tokenize(String data, JTokenizeHelper helper) throws ParseException, IOException {
        return tokenize(new StringReader(data), helper);
    }
}
