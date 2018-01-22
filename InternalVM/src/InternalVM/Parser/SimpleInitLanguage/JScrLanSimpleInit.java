/**
 * Simple Initialyzer language is a simple procedural language aimed to 
 * initializations. It does not use variables, and prefers a single line 
 * procedure to more complex sintaxes. Standard operatiosns are not provided, 
 * neither are variables.
 * 
 * But you can call procedures from object using the with syntax
 * 
 * getObject(object_name) {
 *  method_call();
 *  method_call();
 *  method_call();
 *  method_call();
 * }
 * 
 * dot synax is not allowed
 * 
 * object.method();
 * 
 * is NOT a valid program line.
 * 
 * Parameters can use specifiers.
 * 
 * In example the followingis a valid syntax. 
 * 
 * getObject(count:2, name:"Item") {}
 * 
 * the following is NOT a valid syntax, because parameter order is not predefined
 * 
 * getObject(2, "Item") {}
 * 
 */
package InternalVM.Parser.SimpleInitLanguage;

import InternalVM.Compiler.JVMExceptionBuilder;
import InternalVM.E_CODE_TYPE;
import InternalVM.Lexer.ELexerTokenType;
import InternalVM.Lexer.JDocumentPosition;
import InternalVM.Lexer.JLexer;
import InternalVM.Lexer.JLexer.E_TOKENIZER_STATE;
import InternalVM.Lexer.JLexerPositionalToken;
import InternalVM.Lexer.JLexerToken;
import InternalVM.Lexer.JTokenizeHelper;
import InternalVM.Parser.JPseudoProgram;
import InternalVM.Parser.JScriptingLanguage;
import InternalVM.VMProviderInterface;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author luca.scarcia
 */
public class JScrLanSimpleInit extends JScriptingLanguage {
    
    static final TreeMap<String, JLexerToken> Keywords = new TreeMap<>();
    static final TreeMap<String, JLexerToken> Symbols = new TreeMap<>();
    
    private boolean hasSpaces = false;
    private JDocumentPosition Pos = null;

    private static void addToMap(Map<String, JLexerToken> map, JLexerToken Token) {
        if(map == null)
            throw JVMExceptionBuilder.NullPointerException("Map is null");
        if(Token == null)
            throw JVMExceptionBuilder.NullPointerException("Token is null");
        if(map.containsKey(Token.Data))
            throw JVMExceptionBuilder.IllegalArgumentException("Cannot duplicate key ["+Token.Data+"]");
        map.put(Token.Data, Token);
    }
    

    private static enum E_SIMPLE_ENTITIES {
        
        UNDEFINED(-1, ELexerTokenType.IDENTIFIER ,""),
        WHITESPACE(ELexerTokenType.WHITESPACE , " "),
        BLOCK_BEGIN(ELexerTokenType.CODE_BLOCK, "{"),
        BLOCK_END(ELexerTokenType.CODE_BLOCK, "}"),
        PAR_OPEN(ELexerTokenType.SEPARATOR, "("),
        PAR_CLOSE(ELexerTokenType.SEPARATOR, ")"),
        PAR_EMPTY(ELexerTokenType.SEPARATOR, "()"),
        SPECIFIER(ELexerTokenType.SEPARATOR, ":"),
        END_COMMAND(ELexerTokenType.ENDCOMMAND, ";"),
        SEPARATOR(ELexerTokenType.SEPARATOR, ","),
        COMMENT(ELexerTokenType.COMMENT_SINGLELINE, "//"),
        FRAGMENT(ELexerTokenType.KEYWORD, "Fragment"),
        TRUE(ELexerTokenType.KEYWORD, "true"),
        FALSE(ELexerTokenType.KEYWORD, "false"),
        ;

        private static int Counter;
        
        private static int StartCounter(int start) {
            return Counter = start;
        }
        
        private static int getID() {
            return ++Counter;
        }
        
        public final int ID;
        public final JLexerToken Token;
        
        private E_SIMPLE_ENTITIES(int id, ELexerTokenType type, String text) {
            ID = StartCounter(id);
            Token = new JSimpleLexerToken(type, text, this);
        }
        
        private E_SIMPLE_ENTITIES(ELexerTokenType type, String text) {
            ID = getID();
            Token = new JSimpleLexerToken(type, text, this);
        }
    }
    
    static {
        addToMap(Keywords, E_SIMPLE_ENTITIES.FRAGMENT.Token);
        addToMap(Keywords, E_SIMPLE_ENTITIES.TRUE.Token);
        addToMap(Keywords, E_SIMPLE_ENTITIES.FALSE.Token);
        addToMap(Symbols, E_SIMPLE_ENTITIES.BLOCK_BEGIN.Token);
        addToMap(Symbols, E_SIMPLE_ENTITIES.BLOCK_END.Token);
        addToMap(Symbols, E_SIMPLE_ENTITIES.END_COMMAND.Token);
        addToMap(Symbols, E_SIMPLE_ENTITIES.SEPARATOR.Token);
        addToMap(Symbols, E_SIMPLE_ENTITIES.SPECIFIER.Token);
        addToMap(Symbols, E_SIMPLE_ENTITIES.PAR_OPEN.Token);
        addToMap(Symbols, E_SIMPLE_ENTITIES.PAR_CLOSE.Token);
        addToMap(Symbols, E_SIMPLE_ENTITIES.PAR_EMPTY.Token);
    }

    private static class JSimpleLexerToken extends JLexerToken {
        
        E_SIMPLE_ENTITIES Entity;
    
        public JSimpleLexerToken(ELexerTokenType type, String data, E_SIMPLE_ENTITIES entity) {
            super(type, data, entity.ID);
            Entity = entity;
        }  

    }

    public static class JSimpleHelper implements JTokenizeHelper {

        StringBuilder sb;
        JDocumentPosition Pos;
        private boolean ConsumeCharacter;
        private E_TOKENIZER_STATE NextState;
        private boolean WaitForEscape;

        static String AGGREGABLES = "/()";
        static String VALIDIDENTIFIERFIRSTCHAR = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        static String VALIDIDENTIFIERCHARS = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        private boolean FirstAfterExponent;
        private boolean HasExponent;
        private boolean HasDecimals;
        private JLexerToken getKnownSymbol(String sequence) {
            return Symbols.get(sequence);
        }

        public JSimpleHelper() {
            this.NextState = E_TOKENIZER_STATE.UNDEFINED;
            this.ConsumeCharacter = true;
            sb = null;
        }

        @Override
        public void reset() {
            this.NextState = E_TOKENIZER_STATE.UNDEFINED;
            this.ConsumeCharacter = true;
            sb = null;
        }

        @Override
        public boolean consumeLastCharacter() {
            if(ConsumeCharacter) 
                return true;
            else {
                ConsumeCharacter = true;
                return false;
            }
        }

        @Override
        public E_TOKENIZER_STATE continueWithState() {
            E_TOKENIZER_STATE temp = NextState;
            NextState = E_TOKENIZER_STATE.UNDEFINED;
            return temp;            
        }

        @Override
        public boolean isNumberStart(char c) {
            return "-0123456789".indexOf(c)>=0;
        }

        @Override
        public boolean isIdentifierStart(char c) {
            return "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(c)>=0;
        }

        @Override
        public boolean isWhiteSpace(char c) {
            return Character.isWhitespace(c);
        }

        @Override
        public boolean isStringStart(char c) {
            return c=='\"';
        }

        @Override
        public boolean isAggregable(char c) {
            return AGGREGABLES.indexOf(c)>=0;
        }

        @Override
        public boolean isCommentStart(char c) {
            return false;
        }

        @Override
        public void initialyzeAggregable(char c, int row, int col, int absolute) throws ParseException {
            sb=new StringBuilder();
            setPosition(absolute, row, col);
            sb.append(c);
            Pos = new JDocumentPosition(absolute, row, col);
            NextState = E_TOKENIZER_STATE.AGGREGABLES;

        }

        @Override
        public void initialyzeWhitespace(char c, int row, int col, int absolute) throws ParseException {
            sb = null;
            setPosition(absolute, row, col);
            NextState = E_TOKENIZER_STATE.WHIESPACE;
        }

        @Override
        public void initialyzeNumber(char c, int row, int col, int absolute) throws ParseException {
            sb=new StringBuilder();
            setPosition(absolute, row, col);
            sb.append(c);
            HasExponent = false;
            HasDecimals = false;
            Pos = new JDocumentPosition(absolute, row, col);
            NextState = E_TOKENIZER_STATE.NUMBER;

        }

        @Override
        public void initialyzeIdentifier(char c, int row, int col, int absolute) throws ParseException {
            sb=new StringBuilder();
            setPosition(absolute, row, col);
            sb.append(c);
            Pos = new JDocumentPosition(absolute, row, col);
            NextState = E_TOKENIZER_STATE.IDENTIFIER;
        }

        @Override
        public void initialyzeString(char c, int row, int col, int absolute) throws ParseException {
            sb=new StringBuilder();
            setPosition(absolute, row, col);
            Pos = new JDocumentPosition(absolute, row, col);
            NextState = E_TOKENIZER_STATE.STRING;
        }

        @Override
        public void initialyzeComment(char c, int row, int col, int absolute) throws ParseException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean appendToString(char c) throws ParseException {
            if(WaitForEscape) {
                WaitForEscape = false;
                switch(c) {
                    case '0':
                        sb.append('\0');
                        break;
                    case '\\':
                        sb.append('\\');
                        break;
                    case '\'':
                        sb.append('\'');
                        break;
                    case '\"':
                        sb.append('\"');
                        break;
                    case 'b':
                        sb.append('\n');
                        break;
                    case 'f':
                        sb.append('\f');
                        break;
                    case 'n':
                        sb.append('\n');
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                }
            } else if(c=='\\') {
                this.WaitForEscape = true;
            } else if(c=='"') {
                NextState = JLexer.E_TOKENIZER_STATE.DEFAULT;
                return true;
            } else if(c=='\r' || c=='\f' || c=='\n') {
                throw new ParseException("String not closed \""+sb.toString(), 0);
            } else {
                sb.append(c);
            }
            NextState = JLexer.E_TOKENIZER_STATE.STRING;
            return false;
        }

        @Override
        public boolean appendToWhiteSpace(char c) throws ParseException {
            if(Character.isWhitespace(c)) {
                return false;
            } else {
                ConsumeCharacter = false;
                NextState = JLexer.E_TOKENIZER_STATE.DEFAULT;
                return true;
            }
        }

        @Override
        public boolean appendAggregable(char c) throws ParseException {
            if(AGGREGABLES.indexOf(c)>=0) {
                ConsumeCharacter=true;
                NextState = JLexer.E_TOKENIZER_STATE.AGGREGABLES;
                sb.append(c);
                JLexerToken token = getKnownSymbol(sb.toString());
                if(token == E_SIMPLE_ENTITIES.COMMENT.Token) {
                    sb.delete(sb.length()-2, sb.length());
                    NextState = JLexer.E_TOKENIZER_STATE.COMMENT;
/*                    CommentType = 2;
                    return false;
                } else if(token == JScrLanJALPHI.JALPHI_TOKEN_TYPE.LINE_COMMENT.Token) {
                    sb.delete(sb.length()-2, sb.length());
                    NextState = JLexer.E_TOKENIZER_STATE.COMMENT;
                    CommentType = 1;*/
                    return false;
                } else if(token == null) {
                    sb.delete(sb.length()-1, sb.length());
                    NextState = JLexer.E_TOKENIZER_STATE.DEFAULT;
                    ConsumeCharacter = false;
                    return true;
                }
                return false;
            } else {
                NextState = JLexer.E_TOKENIZER_STATE.DEFAULT;
                ConsumeCharacter=false;
                return true;
            }
        }

        @Override
        public boolean appendToIdentifier(char c) throws ParseException {
            if(VALIDIDENTIFIERCHARS.indexOf(c)>=0) {
                sb.append(c);
                NextState = E_TOKENIZER_STATE.IDENTIFIER;
                return false;
            } else {
                ConsumeCharacter = false;
                NextState = E_TOKENIZER_STATE.DEFAULT;
                return true;            
            }
        }

        @Override
        public boolean appendToNumber(char c) throws ParseException {
            switch(c) {
                case '0': case '1': case '2': case '3': case '4':
                case '5': case '6': case '7': case '8': case '9':
                    FirstAfterExponent = false;
                    sb.append(c);
                    break;
                case 'e': case 'E':
                    if(HasExponent) {
                        throw new ParseException("Invalid number with double exponent "+sb.toString()+c,-1);
                    } else {
                        HasExponent=true;
                        FirstAfterExponent = true;
                        sb.append('e');
                    }
                    break;
                case '-': case '+':
                    if(HasExponent && FirstAfterExponent) {
                        sb.append(c);
                        FirstAfterExponent = false;
                    } else {
                        ConsumeCharacter = false;
                        NextState = JLexer.E_TOKENIZER_STATE.DEFAULT;
                        return true;
                    }
                    break;
                case '.':
                    if(this.HasDecimals) {
                        throw new ParseException("Invalid number with double decimal start "+sb.toString()+c,-1);
                    } else if (HasExponent) {
                        throw new ParseException("Decimals are not allowed in the exponent part: "+sb.toString()+c,-1);
                    } else {
                        HasDecimals=true;
                        sb.append('.');
                    }
                    break;
                default:
                    NextState = JLexer.E_TOKENIZER_STATE.DEFAULT;
                    ConsumeCharacter = false;
                    return true;
            }
            NextState = JLexer.E_TOKENIZER_STATE.NUMBER;
            ConsumeCharacter = true;
            return false;
        }

        @Override
        public boolean appendToComment(char c) throws ParseException {
            if(c!='\n' || c!='\r') {
                sb.append(c);
                return false;
            } else {
                return true;
            }
        }

        @Override
        public JLexerPositionalToken tokenizeSymbol(char symbol, int row, int col, int absolute) throws ParseException {
            JLexerToken temp = getKnownSymbol(""+symbol);
            if(temp==null) {
                throw JVMExceptionBuilder.buildException("Unexpected character 0x"+Integer.toHexString(symbol)+" ("+symbol+")", Pos, -1);
            }
            setPosition(absolute, row, col);
            return returnReadyToken(temp);
        }

        @Override
        public JLexerPositionalToken tokenizeAggregables() throws ParseException {
            JLexerToken temp = getKnownSymbol(sb.toString());
            if(temp==null) throw JVMExceptionBuilder.buildException("String \""+sb.toString()+"\" unexpectedly produced an invalid aggregable token!", Pos, -1);
            return returnReadyToken(temp);
        }

        @Override
        public JLexerPositionalToken tokenizeIdentifier() throws ParseException {
            String data = sb.toString();
            if("".equals(data)) {
                throw JVMExceptionBuilder.buildException("Invalid empty identifier string.", Pos, -1);
            }
            JLexerToken temp = Keywords.get(data);
            if(temp==null)
                temp = new JSimpleLexerToken(ELexerTokenType.IDENTIFIER, data, E_SIMPLE_ENTITIES.UNDEFINED);
            return returnReadyToken(temp);
        }

        @Override
        public JLexerPositionalToken tokenizeNumber() throws ParseException {
            JLexerToken temp = new JSimpleLexerToken(ELexerTokenType.NUMBER, sb.toString(), E_SIMPLE_ENTITIES.UNDEFINED);
            return returnReadyToken(temp);
        }

        @Override
        public JLexerPositionalToken tokenizeWhitespace() throws ParseException {
            return returnReadyToken(E_SIMPLE_ENTITIES.WHITESPACE.Token);
        }

        @Override
        public JLexerPositionalToken tokenizeString() throws ParseException {
            JLexerToken temp = new JSimpleLexerToken(ELexerTokenType.STRING, sb.toString(), E_SIMPLE_ENTITIES.UNDEFINED);
            return returnReadyToken(temp);
        }

        @Override
        public JLexerPositionalToken tokenizeComment() throws ParseException {
            JLexerToken temp = new JSimpleLexerToken(ELexerTokenType.COMMENT_SINGLELINE, sb.toString(), E_SIMPLE_ENTITIES.UNDEFINED);
            return returnReadyToken(temp);
        }

        private JLexerPositionalToken returnReadyToken(JLexerToken token) {
            JLexerPositionalToken temp = new JLexerPositionalToken(token, Pos);
            sb = null;
            Pos = null;
            return temp;
        }

        private void setPosition(int absolute, int row, int col) {
            Pos = new JDocumentPosition(absolute, row, col);
        }
    }

    @Override
    public String getName() {
        return "Simple initializator language.";
    }

    @Override
    public double getRevision() {
        return 1.0;
    }

    @Override
    public JTokenizeHelper getHelper() {
        return new JSimpleHelper();
    }

    private JSimpleLexerToken consumeAllWhiteSpaces(Iterator<JLexerPositionalToken> tokens) throws ParseException {
        JLexerPositionalToken item;
        hasSpaces=false;
        if(!tokens.hasNext())
            return null;
        item = tokens.next();
        while(item.Token==E_SIMPLE_ENTITIES.WHITESPACE.Token) {
            if(!tokens.hasNext())
                return null;
            item = tokens.next();
            if(!hasSpaces) 
                hasSpaces = item.Token == E_SIMPLE_ENTITIES.WHITESPACE.Token;
        }
        Pos = item.Pos;
        if(item.Token instanceof JSimpleLexerToken) {
            return (JSimpleLexerToken) item.Token;
        } else 
            throw JVMExceptionBuilder.UnexpectedException("Unexpecte instance of "+item.Token.getClass().getName()+" at ({1},{2})", Pos);
    }
    
    private JPseudoProgram parseProgramIntestation(Iterator<JLexerPositionalToken> tokens, VMProviderInterface provider) throws ParseException {
        JSimpleLexerToken token = consumeAllWhiteSpaces(tokens);
        if(token==null)
            throw JVMExceptionBuilder.buildEOFException("Unexpected end of program while parsing intestation", -1);
        if(token.Entity==E_SIMPLE_ENTITIES.FRAGMENT) {
            JPseudoProgram program = new JPseudoProgram();
            program.setType(E_CODE_TYPE.FRAGMENT);
            token = consumeAllWhiteSpaces(tokens);
            if(token==null)
                throw JVMExceptionBuilder.buildEOFException("Unexpected end of program while parsing intestation", -1);
            if(token.Type!=ELexerTokenType.IDENTIFIER) 
                throw JVMExceptionBuilder.ParseException("Unexpecterd <{4}> \"{3}\". instead of <IDENTIFIER> at ({1},{2})", token, Pos, -1);
            program.setName(token.Data);
            token = consumeAllWhiteSpaces(tokens);
            if(token==null)
                throw JVMExceptionBuilder.buildEOFException("Unexpected end of program while parsing intestation", -1);
            if(token.Entity!=E_SIMPLE_ENTITIES.PAR_EMPTY) 
                throw JVMExceptionBuilder.ParseException("Unexpecterd <{4}> \"{3}\". instead of <SEPARATOR> \"()\" at ({1},{2})", token, Pos, -1);
            token = consumeAllWhiteSpaces(tokens);
            if(token==null)
                throw JVMExceptionBuilder.buildEOFException("Unexpected end of program while parsing intestation", -1);
            if(token.Entity!=E_SIMPLE_ENTITIES.BLOCK_BEGIN) 
                throw JVMExceptionBuilder.ParseException("Unexpecterd <{4}> \"{3}\". instead of <CODE_BLOCK> \"{\" at ({1},{2})", token, Pos, -1);
            return program;
        } else 
            throw JVMExceptionBuilder.ParseException("Unexpected <{3}> \"{4}\", expected <KEYWOR> Fragment at ({1},{2})", token, Pos, -1);
    }
    
    @Override
    public JPseudoProgram parse(Iterator<JLexerPositionalToken> tokens, VMProviderInterface provider) throws ParseException {
        JPseudoProgram program;
        program = parseProgramIntestation(tokens, provider);
        return program;
    }

    
}
