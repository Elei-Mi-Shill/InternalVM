/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM.Lexer;

import java.util.regex.Pattern;

/**
 *
 * @author luca.scarcia
 */
public enum ELexerTokenType {
    // Token types cannot have underscores
    ASSIGN("[=]"), 
    NUMBER("[-+]?[\\d]+[\\.]?[\\d]*([eE][-+]?[\\d]+)?"), 
    BINARYOP("[*|/|+|-]"), 
    WHITESPACE("\\s+"),
    UNEXPECTED("."),
    ENDCOMMAND("[;]"), 
    UNARYOP("[\\Q!\\E]"), 
    CODE_BLOCK("[{|}]"), 
    SEPARATOR("[,]"), 
    STRING("[\"].*[\"]"), 
    IDENTIFIER("[_A-Za-z][_A-Za-z0-9]*"), 
    COMPARATOR("[=|<|>]"), 
    KEYWORD("[_]?[A-Z][a-z]*"), 
    TOKEN("[_]?[A-Z][_A-Z0-9]"), 
    COMMENT_MULTILINE("\\Q/*\\E[.]*\\Q*/\\E"),
    COMMENT_SINGLELINE("\\Q//\\E[.]*$");

    public final Pattern pattern;

    private ELexerTokenType(String pattern) {
        this.pattern=Pattern.compile(pattern);
    }
}
