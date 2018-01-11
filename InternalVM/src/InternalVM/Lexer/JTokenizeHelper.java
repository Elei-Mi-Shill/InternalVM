/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM.Lexer;

import java.text.ParseException;
import java.util.List;
import InternalVM.Lexer.JLexer.E_TOKENIZER_STATE;
import java.util.Collection;

/**
 *
 * @author luca.scarcia
 * 
 * This interface is made to help the Lexer in his work to tokenize the files
 * 
 * shile scanning the file character by character, the lexer interrogates the
 * helper to know what that character means, and where it should be put.
 * In fact, the helper constructs the tokens, while the Lexer scans for them and
 * puts them in one of the categories:
 * 
 *  ASSIGN          = in C or Java := in Delphi
 *  NUMBER          12.4e-10
 *  BINARYOP        + - / *
 *  UNARYOP         operations like NOT (! in Java) that operates only on one
 *                  object
 *  WHITESPACE      every kind of thing that is considered a space in your program
 *  ENDCOMMAND      This symbol tells the parser that the line ends here
 *                  In many language is the ; symbol (semicolon); certain languages
 *                  used the carrige return or new line characters
 *  CODE_BLOCK      code begin and code end (usually { } in C or Java, "begin" 
 *                  "end" in some languages); 
 *  SEPARATOR       it is used to identify a separator symbol, such as , (comma)
 *                  it's used to separate the 
 *  STRING          string constants such as "Hallo World"; helper should be able
 *                  to tell the string beginning, the string end, and every
 *                  escape character that is used inside such \" in C
 *  IDENTIFIER      identifiers represents names of variables, objects, functions
 *                  methods; it's usually made of characters and numbers but
 *                  can use some special characters too such as $ (BASIC to define
 *                  a String) _; 
 *  COMPARATOR      this symbol / sequence is condidered a comparator, such as
 *                  >= != == in Java; 
 *  KEYWORD         an identifier recognized as a keyword, such as integer or
 *                  void in C and Java
 *  TOKEN           a TOKEN is a special constant; I use them as constant 
 *                  identifiers; you can ignore them or use them as you please
 *  COMMENT_MULTILINE  multiline comment
 *  COMMENT_SINGLELINE single line comment
 *  UNEXPECTED      every non managed characters ends here; usually they should 
 *                  be reported as errors
 * 
 * N.B: the categories are hard coded, but you can decide to use them in a different
 * way! Those categories will be managed by your parser anyways, and won't 
 * be communicated to the compiler or the Virtual Machine; thwy are ment only to
 * help the parser in its job;
 */
public abstract interface JTokenizeHelper {
    
    /**
     * resets all the data in the factory
     */
    public abstract void reset();
    /**
     * The value must be set by all the "append" functions; if the character
     * was consumed (i.e. was a valid character for that type of data) this 
     * function must return to true 
     * 
     * @return true if the character must be consumed; false if must be reused
     */
    public abstract boolean consumeLastCharacter();
    /**
     * N.B.: it's expected that this function CONSUMES the value state and
     * resets to E_TOKENIZER_STATE.UNDEFINED!
     * 
     * The value must be set by all addXXXXX functions; returns the element type
     * we are expecting next; if you want to continue with the current state,
     * this function must return E_TOKENIZER_STATE.UNDEFINED.
     * 
     * Available states are:
     * UNDEFINED: continue with the current state
     * DEFAULT: reading non aggregable simbols
     * STRING: reading a string
     * IDENTIFIER: reading an identifier (or a keyword)
     * NUMBER reading a number
     * AGGREGABLES: reading aggregable symbols
     * WHIESPACE: reading whitespaces
     * COMMENT: reading a comment
     * 
     * @return a valid E_TOKENIZER_STATE or E_TOKENIZER_STATE.UNDEFINED if not state was defined
     */
    public abstract E_TOKENIZER_STATE continueWithState();
    
    /** isXXXX functions
     * those functions are ment to tell the Lexer that we are reading a managed
     * character; meaning it's not an unaggregable or undefined symbol, but
     * something more complex
     * It's used only for the FIRST character of a string of managed characters
     * Therefore, the isXXXX class functions are used ONLY in DEFAULT state
     * 
     * isXXXX class functions don't change consumeLastCharacter() or 
     * continueWithState() return values
     */
    
    /**
     * return true if c is a valid number start
     * @param c
     * @return true if valid start for a number
     */
    public abstract boolean isNumberStart(char c);
    /**
     * returns true if this character can be the first character of an identifier
     * @param c
     * @return
     */
    public boolean isIdentifierStart(char c);
    /**
     * returns true if the character can be considered a whitespace
     * @param c
     * @return true if the chararacter is considered a whitespace (thus can be ignored)
     */
    public abstract boolean isWhiteSpace(char c);
    /**
     * if the character starts a string, returns true; usually it is the double
     * quote character ("), but in some languages a single quote can be used (')
     * @param c
     * @return true if the character starts a string
     */
    public boolean isStringStart(char c);
    /**
     * this category is used to test characters that can be aggregated with
     * others for special meanings: an example is C // begin single line comment
     * 
     * if you have characters that can be aggregated with others, you use
     * this category to test them.
     * 
     * @param c character to test for the aggregable category
     * @return true if this character can be aggregate with others
     */
    public abstract boolean isAggregable(char c);

    public abstract boolean isCommentStart(char c);

    /**
     * initialyzeXXXX class functions
     * those functions are ment to le the helper do all internal initialyzatiion
     * procedures it needs to function properly. This function is called only
     * after the corresponding isXXXX class function returns true; the character 
     * is provided because if it's needed, it can be stored immediately into the 
     * storage for the append function
     * 
     * initialyzeXXXX class functions can change both consumeLastCharacter() and
     * continueWithState() return values
     */ 
    
    public abstract void initialyzeAggregable(char c) throws ParseException;

    public abstract void initialyzeWhitespace(char c) throws ParseException;

    public abstract void initialyzeNumber(char c) throws ParseException;

    public abstract void initialyzeIdentifier(char c) throws ParseException;

    public abstract void initialyzeString(char c)  throws ParseException;

    public void initialyzeComment(char c) throws ParseException;
    
    /**
     * appendToXXXX class functions
     * 
     * Those classes are ment to append the simbol to a string and return false
     * if the token is ready to be created;
     * 
     * if appendToXXXX returns true, the lexer will immediately call the appropriate
     * tokenizeXXXX function and set the next state to DEFAULT, consuming
     * the just read character; if the character was a terminator and you need
     * to tokenize it, change the consumeNextCharacter to false! Do not change
     * the continueWithState 'though, because this would prevent the lexer to
     * call the initialyzeXXXX function for the new character! If you do it, 
     * you must also call the correct initialyzer, without interferring with
     * the tokenizeXXXX function; doing so, will probably cause a lot of 
     * hard to locate bugs, so it's to be considered a <bad> practice!
     * 
     * IMPORTANT! appendToXXXX class functions are not called during the first
     * cycle when the relative isXXXX class function returns true. The first 
     * iteration must be handled by the appropriate initialyzeXXXX function!
     * 
     * appendXXXX class functions change consumeLastCharacter() and
     * continueWithState() return values!
     * 
     */
    
    /**
     * Append the character to a string; the function will also manage
     * escape characters, when needed
     * 
     * @param c
     * @return
     * @throws java.text.ParseException
     */
    public abstract boolean appendToString(char c) throws ParseException;
    /**
     * special character that can be alone or aggregated with other characters
     * in example > e = can be aggregated as >= 
     * @param c
     * @return
     * @throws java.text.ParseException
     */
    public abstract boolean appendToWhiteSpace(char c) throws ParseException;

    /**
     * append character c to the string of aggregables characters
     * 
     * N.B.: aggregables can be or can be not part of a sequence; that's why
     * 
     * @param c
     * @return true if an aggregable is ready to be read
     * @throws java.text.ParseException
     */
    public abstract boolean appendAggregable(char c) throws ParseException;

    public abstract boolean appendToIdentifier(char c)  throws ParseException;

    public abstract boolean appendToNumber(char c)  throws ParseException;

    public abstract boolean appendToComment(char c)  throws ParseException;;

    /**
     * tokenizeXXXX class functions
     * those functions returns a token out of the data appended by the 
     * appendToXXXX class functions. When the relative appendXXXX function 
     * returns true, the lexxer call the appropriate tokenizeXXXX function
     * and adds the result to the its token list.
     * exept for tokenizeSymbol, all other tokenizeXXXX functions return
     * a list of Tokens
     * 
     * tokenizeXXXX class functions don't change consumeLastCharacter() or 
     * continueWithState() return values
     */
    
    /**
     * Return the tokenyzed rapresentation of the specific symbol. A symbol is a
     * single character that is not part of aggregables, is not a valid digit or
     * a identifier character or a whitespace either
     * @param symbol symbol to tokenyze
     * @return
     * @throws ParseException
     */
    public abstract JLexerToken tokenizeSymbol(char symbol) throws ParseException;

    /**
     * returns a list of special character sequence, like >= or != in token
     * format
     * 
     * @return list of valid aggregates
     * @throws ParseException
     */
    
    public abstract Collection<JLexerToken> tokenizeAggregables() throws ParseException;
    /**
     * returns a valid identifier: the identifier will be a reserved word or a 
     * generic identifier; note that those symbols can be aggregated or can be
     * used alone; therefore, given the sequence, it can be returned as single
     * token or a list of tokens; in example => will return 2 different tokens,
     * one for = and one for >, while >= will return a single token >=
     * 
     * @return valid identifier
     * @throws ParseException
     */
    public abstract Collection<JLexerToken> tokenizeIdentifier() throws ParseException;

    public abstract Collection<JLexerToken> tokenizeNumber() throws ParseException;
    
    public abstract Collection<JLexerToken> tokenizeWhitespace() throws ParseException;

    public abstract Collection<JLexerToken> tokenizeString() throws ParseException;

    public abstract Collection<JLexerToken> tokenizeComment() throws ParseException;;


}
