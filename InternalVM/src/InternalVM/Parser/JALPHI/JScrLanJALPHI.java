/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM.Parser.JALPHI;

import InternalVM.Compiler.JVMExceptionBuilder;
import InternalVM.E_ACTION;
import InternalVM.E_CODE_TYPE;
import InternalVM.E_VAR_TYPE;
import InternalVM.Lexer.ELexerTokenType;
import InternalVM.Lexer.JDocumentPosition;
import InternalVM.Lexer.JLexer;
import InternalVM.Lexer.JLexerPositionalToken;
import InternalVM.Lexer.JLexerToken;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import InternalVM.Lexer.JTokenizeHelper;
import InternalVM.Parser.JPseudoInstruction;
import InternalVM.Parser.JPseudoVariable;
import InternalVM.Parser.JPseudoProgram;
import InternalVM.Parser.JPseudoType;
import InternalVM.Parser.JScriptingLanguage;
import InternalVM.VMProviderInterface;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class JScrLanJALPHI extends JScriptingLanguage {
    
    private static final Pattern ISFLOAT = Pattern.compile("^\\d+([\\.][\\d]+)?([e|E][+-]?[\\d]+)?$");
    private static final Pattern ISLONG = Pattern.compile("^\\d+$");
    private static final Pattern ISHEX = Pattern.compile("^0x[\\dA-Ea-e]+$");
    
    private static enum JALPHI_TOKEN_TYPE {

        UNDEFINED(-1, "", ELexerTokenType.IDENTIFIER),
        
        WHITESPACE(" ", ELexerTokenType.WHITESPACE), 
        ASSIGN("=", ELexerTokenType.ASSIGN), 
        MARK_IDENTIFIER("@", ELexerTokenType.UNARYOP), 
        SELECTOR(".", ELexerTokenType.BINARYOP), 
        DECLARATOR(":", ELexerTokenType.BINARYOP), 
        SEPARATE(",", ELexerTokenType.SEPARATOR), 
        END_INSTRUCTION(";", ELexerTokenType.ENDCOMMAND), 
        PAR_OPEN("(", ELexerTokenType.SEPARATOR), 
        PAR_CLOSE(")", ELexerTokenType.SEPARATOR), 
        EMPTY_PARAMS("()", ELexerTokenType.UNARYOP), 
        GREATER(">", ELexerTokenType.COMPARATOR), 
        LESSER("<", ELexerTokenType.COMPARATOR), 
        QPAR_OPEN("[", ELexerTokenType.SEPARATOR), 
        QPAR_CLOSE("]", ELexerTokenType.SEPARATOR), 
        BOOL_NOT("!", ELexerTokenType.UNARYOP), 
        PLUS("+", ELexerTokenType.BINARYOP), 
        MINUS("-", ELexerTokenType.BINARYOP), 
        MULT("*", ELexerTokenType.BINARYOP),
        DIVIDE("/", ELexerTokenType.BINARYOP),
        REMINDER("%", ELexerTokenType.BINARYOP), 
        BITWISE_AND("&", ELexerTokenType.BINARYOP), 
        BITWISE_OR("|", ELexerTokenType.BINARYOP), 
        BITWISE_INVERT("~", ELexerTokenType.UNARYOP), 
        BITWISE_XOR("^", ELexerTokenType.BINARYOP),
        LINE_COMMENT("//", ELexerTokenType.COMMENT_SINGLELINE), 
        COMMENT_START("/*", ELexerTokenType.COMMENT_MULTILINE), 
        COMMENT_END("*/", ELexerTokenType.COMMENT_MULTILINE),
        BLOCK_BEGIN("{", ELexerTokenType.CODE_BLOCK),
        BLOCK_END("}", ELexerTokenType.CODE_BLOCK), 
        TOKEN_START("@", ELexerTokenType.UNARYOP), 
        ISEQUAL("==", ELexerTokenType.COMPARATOR), 
        NOTEQUAL("!=", ELexerTokenType.COMPARATOR), 
        SHIFTRIGHT(">>",ELexerTokenType.BINARYOP), 
        SHIFTLEFT("<<",ELexerTokenType.BINARYOP), 
        INCREMENT("++", ELexerTokenType.UNARYOP), 
        DECREMENT("--", ELexerTokenType.UNARYOP), 
        BOOL_AND("&&", ELexerTokenType.BINARYOP), 
        AS_ARRAY("[]", ELexerTokenType.UNARYOP), 
        BOOL_OR("||", ELexerTokenType.BINARYOP), 
        INCREMENT_BY("+=", ELexerTokenType.BINARYOP), 
        DECREMENT_BY("-=", ELexerTokenType.BINARYOP), 
        MULT_BY("*=", ELexerTokenType.BINARYOP), 
        DIV_BY("/=", ELexerTokenType.BINARYOP), 
        REMINDER_OF("%=",ELexerTokenType.BINARYOP), 
        BITWISE_XOR_WITH("^=", ELexerTokenType.BINARYOP), 
        BITWISE_OR_WITH("|=", ELexerTokenType.BINARYOP), 
        BITWISE_AND_WITH("&=", ELexerTokenType.BINARYOP),
        SHIFTRIGHT_BY(">>=", ELexerTokenType.BINARYOP), 
        SHIFTLEFT_BY("<<=", ELexerTokenType.BINARYOP), 
        RETURN("return", ELexerTokenType.KEYWORD), 
        CONTINUE("continue", ELexerTokenType.KEYWORD), 
        BREAK("break", ELexerTokenType.KEYWORD), 
        WHILE("while", ELexerTokenType.KEYWORD), 
        FOR("for", ELexerTokenType.KEYWORD), 
        ELSE_IF("elsif", ELexerTokenType.KEYWORD), 
        ELSE("else", ELexerTokenType.KEYWORD), 
        IF("if", ELexerTokenType.KEYWORD), 
        IS("is", ELexerTokenType.KEYWORD),
        AS("as", ELexerTokenType.KEYWORD), 
        THIS("this", ELexerTokenType.KEYWORD), 
        NULL("null", ELexerTokenType.KEYWORD), 
        TRUE("true", ELexerTokenType.KEYWORD), 
        FALSE("false", ELexerTokenType.KEYWORD), 
        TYPE_OBJECT("object", ELexerTokenType.KEYWORD), 
        TYPE_TOKEN("token", ELexerTokenType.KEYWORD), 
        TYPE_FLOAT("float", ELexerTokenType.KEYWORD), 
        TYPE_INTEGER("integer", ELexerTokenType.KEYWORD), 
        TYPE_STRING("string", ELexerTokenType.KEYWORD), 
        TYPE_VOID("void", ELexerTokenType.KEYWORD), 
        TYPE_BOOLEAN("boolean", ELexerTokenType.KEYWORD), 
        IMPORT("import", ELexerTokenType.KEYWORD), 
        EVENT("Event", ELexerTokenType.KEYWORD), 
        FUNCTION("Function", ELexerTokenType.KEYWORD), 
        FRAGMENT("Fragment", ELexerTokenType.KEYWORD), 
        LIBRARY("Library", ELexerTokenType.KEYWORD), 
        PROGRAM("Program", ELexerTokenType.KEYWORD),
        SYSTEM("System", ELexerTokenType.KEYWORD);
        
        private static int Counter=Integer.MIN_VALUE;
        
        public final int ID;
        public final String Symbol;
        public final ELexerTokenType Type;
        public final JLexerToken Token;
        
        private static int nextValue(int id) {
            return Counter=id;
        }

        private static int nextValue() {
            return Counter++;
        }
        
        JALPHI_TOKEN_TYPE(int id, String symbol, ELexerTokenType type) {
            ID = nextValue(id);
            System.out.println(JALPHI_TOKEN_TYPE.class.getName()+" Initialyzes "+this.name()+" as "+ID);
            Symbol = symbol;
            Type = type;
            Token = new JJalphiToken(Type, Symbol, this);
        }

        JALPHI_TOKEN_TYPE(String symbol, ELexerTokenType type) {
            ID = nextValue();
            Symbol = symbol;
            Type = type;
            Token = new JJalphiToken(Type, Symbol, this);
        }
    }
    
    private static class JJalphiToken extends JLexerToken {

        JALPHI_TOKEN_TYPE Instruction;
        
        public JJalphiToken(ELexerTokenType type, String data, JALPHI_TOKEN_TYPE localType) {
            super(type, data, localType.ID);
            Instruction = localType;
        }

        @Override
        public String toString() {
            if(Instruction!=JALPHI_TOKEN_TYPE.UNDEFINED)
                return String.format("<%s> %s", Type.name(), Instruction.name());
            else 
                return String.format("<%s> \"%s\"", Type.name(), Data);
                
        }
        
    }

    /**
     * Map to associate all reserved words with their token
     */
    public static final TreeMap<String, JLexerToken> RESERVED = new TreeMap<>();;
    public static final TreeMap<String, JLexerToken> SYMBOLS1 = new TreeMap<>();;
    public static final TreeMap<String, JLexerToken> SYMBOLS2 = new TreeMap<>();;
    public static final TreeMap<String, JLexerToken> SYMBOLS3 = new TreeMap<>();;
    
    private static void addToMap(Map<String, JLexerToken> map, JLexerToken Token) {
        if(map == null)
            throw new IllegalArgumentException("Map is null");
        if(Token == null)
            throw new IllegalArgumentException("Token is null");
        if(map.containsKey(Token.Data))
            throw new IllegalArgumentException("Cannot duplicate key ["+Token.Data+"]");
        map.put(Token.Data, Token);
    }
    
    static {
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.SYSTEM.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.PROGRAM.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.LIBRARY.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.FRAGMENT.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.FUNCTION.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.EVENT.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.IMPORT.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.TYPE_VOID.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.TYPE_STRING.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.TYPE_INTEGER.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.TYPE_FLOAT.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.TYPE_TOKEN.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.TYPE_OBJECT.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.TYPE_BOOLEAN.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.THIS.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.NULL.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.TRUE.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.FALSE.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.IS.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.AS.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.IF.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.ELSE.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.ELSE_IF.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.FOR.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.WHILE.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.BREAK.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.CONTINUE.Token);
        addToMap (RESERVED, JALPHI_TOKEN_TYPE.RETURN.Token);
        
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.SELECTOR.Token);
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.DECLARATOR.Token);
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.SEPARATE.Token);
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.END_INSTRUCTION.Token);
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.ASSIGN.Token);
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.PAR_OPEN.Token);
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.PAR_CLOSE.Token);
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.GREATER.Token);
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.LESSER.Token);
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.QPAR_OPEN.Token);
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.QPAR_CLOSE.Token);
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.BOOL_NOT.Token);
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.PLUS.Token);
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.MINUS.Token);
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.MULT.Token);
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.DIVIDE.Token);
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.REMINDER.Token);
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.BITWISE_AND.Token);
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.BITWISE_OR.Token);
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.BLOCK_BEGIN.Token);
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.BLOCK_END.Token);
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.TOKEN_START.Token);
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.BITWISE_INVERT.Token);
        addToMap(SYMBOLS1, JALPHI_TOKEN_TYPE.BITWISE_XOR.Token);

        addToMap(SYMBOLS2, JALPHI_TOKEN_TYPE.LINE_COMMENT.Token);
        addToMap(SYMBOLS2, JALPHI_TOKEN_TYPE.COMMENT_START.Token);
        addToMap(SYMBOLS2, JALPHI_TOKEN_TYPE.COMMENT_END.Token);
        addToMap(SYMBOLS2, JALPHI_TOKEN_TYPE.ISEQUAL.Token);
        addToMap(SYMBOLS2, JALPHI_TOKEN_TYPE.NOTEQUAL.Token);
        addToMap(SYMBOLS2, JALPHI_TOKEN_TYPE.SHIFTRIGHT.Token);
        addToMap(SYMBOLS2, JALPHI_TOKEN_TYPE.SHIFTLEFT.Token);
        addToMap(SYMBOLS2, JALPHI_TOKEN_TYPE.INCREMENT.Token);
        addToMap(SYMBOLS2, JALPHI_TOKEN_TYPE.DECREMENT.Token);
        addToMap(SYMBOLS2, JALPHI_TOKEN_TYPE.BOOL_AND.Token);
        addToMap(SYMBOLS2, JALPHI_TOKEN_TYPE.AS_ARRAY.Token);
        addToMap(SYMBOLS2, JALPHI_TOKEN_TYPE.BOOL_OR.Token);
        addToMap(SYMBOLS2, JALPHI_TOKEN_TYPE.INCREMENT_BY.Token);
        addToMap(SYMBOLS2, JALPHI_TOKEN_TYPE.DECREMENT_BY.Token);
        addToMap(SYMBOLS2, JALPHI_TOKEN_TYPE.MULT_BY.Token);
        addToMap(SYMBOLS2, JALPHI_TOKEN_TYPE.DIV_BY.Token);
        addToMap(SYMBOLS2, JALPHI_TOKEN_TYPE.REMINDER_OF.Token);
        addToMap(SYMBOLS2, JALPHI_TOKEN_TYPE.BITWISE_XOR_WITH.Token);
        addToMap(SYMBOLS2, JALPHI_TOKEN_TYPE.BITWISE_OR_WITH.Token);
        addToMap(SYMBOLS2, JALPHI_TOKEN_TYPE.BITWISE_AND_WITH.Token);
        addToMap(SYMBOLS2, JALPHI_TOKEN_TYPE.EMPTY_PARAMS.Token);
    
        addToMap(SYMBOLS3, JALPHI_TOKEN_TYPE.SHIFTRIGHT_BY.Token);
        addToMap(SYMBOLS3, JALPHI_TOKEN_TYPE.SHIFTLEFT_BY.Token);
    };

    @Override
    public JTokenizeHelper getHelper() {
        return new JJALPHIHelper();    
    }

    private Collection<JPseudoInstruction> parseParameters(Iterator<JLexerPositionalToken> tokens) {
        Collection<JPseudoInstruction> parametes = new LinkedList<>();
        JLexerPositionalToken token = null;
        while(token.Token != JALPHI_TOKEN_TYPE.PAR_CLOSE.Token) {
            token = consumeWhitespaces(tokens);
        }
        return parametes;
    }

    public static class JJALPHIHelper implements JTokenizeHelper{
        
        private StringBuilder sb = null;
        private static final String AGGREGABLES = "+-/*%&|^!=[]<>()";
        private static final String VALIDIDENTIFIERCHARS = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        private static final String VALIDIDENTIFIERSTARTCHARS = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final String VALIDDIGITS = "0123456789.eE+-";
        private static final String VALIDDIGITSSTART = "0123456789";
        private static final String SEPARATORS = "(),[]{};~'\"";
        private static final String WHITESPACES = " \b\f\n\r\t\0";
        private JLexer.E_TOKENIZER_STATE NextState = JLexer.E_TOKENIZER_STATE.DEFAULT;
        private boolean ConsumeChar=true;
        private boolean WaitForEscape;
        private boolean HasExponent;
        private boolean HasDecimals;
        private boolean FirstAfterExponent;
        private int CommentType;
        
        private JDocumentPosition Pos;

        private JLexerToken getKnownToken(String toTokenize) {
            if(toTokenize==null) {
                return null;
            } else if (toTokenize.length()==3) {
                return SYMBOLS3.get(toTokenize);
            } else if (toTokenize.length()==2) {
                return SYMBOLS2.get(toTokenize);
            } else if (toTokenize.length()==1) {
                return SYMBOLS1.get(toTokenize);
            } else {
                return null;
            }            
        }

        @Override
        public boolean isNumberStart(char c) {
            return Character.isDigit(c);
        }

        @Override
        public boolean isWhiteSpace(char c) {
            //return Character.isWhitespace(c);
            return WHITESPACES.indexOf(c)>=0;
        }
        
        @Override
        public boolean isIdentifierStart(char c) {
            return (VALIDIDENTIFIERSTARTCHARS.indexOf(c)>=0) || (c=='@');
        }

        @Override
        public JLexerPositionalToken tokenizeIdentifier() throws ParseException {
            String s = sb.toString();
            sb=null;
            JLexerToken tempToken = RESERVED.get(s);
            if(tempToken==null) {
                if(s.charAt(0)=='@')
                    tempToken = new JJalphiToken(ELexerTokenType.TOKEN, s.substring(1), JALPHI_TOKEN_TYPE.UNDEFINED);
                else
                    tempToken = new JJalphiToken(ELexerTokenType.IDENTIFIER, s, JALPHI_TOKEN_TYPE.UNDEFINED);
            } 
            if(tempToken!=null)
                return new JLexerPositionalToken(tempToken, Pos);
            else return null;
        }

        @Override
        public JLexerPositionalToken tokenizeSymbol(char symbol, int row, int col, int absolute) throws ParseException {
            Pos = new JDocumentPosition(absolute, row, col);
            String s=""+symbol;
            JLexerToken tempToken = getKnownToken(s);
            if(tempToken==null) {
                throw JVMExceptionBuilder.buildException("Unexpected character 0x"+Integer.toHexString(symbol)+" ("+symbol+")", Pos, -1);
            }
            return new JLexerPositionalToken(tempToken, Pos);
        }

        @Override
        public void reset() {
            ConsumeChar = true;
            NextState = JLexer.E_TOKENIZER_STATE.DEFAULT;
            sb=null;
        }

        @Override
        public boolean consumeLastCharacter() {
            if(ConsumeChar) 
                return true;
            else {
                ConsumeChar=true;
                return false;
            }
        }

        @Override
        public JLexer.E_TOKENIZER_STATE continueWithState() {
            JLexer.E_TOKENIZER_STATE lNextState = NextState;
            NextState = JLexer.E_TOKENIZER_STATE.UNDEFINED;
            return lNextState;
        }

        @Override
        public void initialyzeAggregable(char c, int row, int col, int absolute) throws ParseException {
            Pos = new JDocumentPosition(absolute, row, col);
            sb = new StringBuilder();
            sb.append(c);
            ConsumeChar = true;
            NextState = JLexer.E_TOKENIZER_STATE.AGGREGABLES;
        }

        @Override
        public boolean appendAggregable(char c) throws ParseException {
            if(AGGREGABLES.indexOf(c)>=0) {
                ConsumeChar=true;
                NextState = JLexer.E_TOKENIZER_STATE.AGGREGABLES;
                sb.append(c);
                JLexerToken token = getKnownToken(sb.toString());
                if(token == JALPHI_TOKEN_TYPE.COMMENT_START.Token) {
                    sb.delete(sb.length()-2, sb.length());
                    NextState = JLexer.E_TOKENIZER_STATE.COMMENT;
                    CommentType = 2;
                    return false;
                } else if(token == JALPHI_TOKEN_TYPE.LINE_COMMENT.Token) {
                    sb.delete(sb.length()-2, sb.length());
                    NextState = JLexer.E_TOKENIZER_STATE.COMMENT;
                    CommentType = 1;
                    return false;
                } else if(token == null) {
                    sb.delete(sb.length()-1, sb.length());
                    NextState = JLexer.E_TOKENIZER_STATE.DEFAULT;
                    ConsumeChar = false;
                    return true;
                }
                return false;
            } else {
                NextState = JLexer.E_TOKENIZER_STATE.DEFAULT;
                ConsumeChar=false;
                return true;
            }
        }

        @Override
        public boolean isAggregable(char c) {
            return AGGREGABLES.indexOf(c)>=0;
        }

        @Override
        public JLexerPositionalToken tokenizeAggregables() throws ParseException {
            JLexerToken tempToken = getKnownToken(sb.toString());
            if(tempToken==null) throw JVMExceptionBuilder.buildException("String \""+sb.toString()+"\" unexpectedly produced an invalid aggregable token!", Pos, -1);
            JLexerPositionalToken temp = new JLexerPositionalToken(tempToken, Pos);
            clearInternalVariables();
            return temp;
        }

        @Override
        public boolean isStringStart(char c) {
            return c=='"';
        }

        @Override
        public boolean appendToString(char c) throws ParseException {
            ConsumeChar = true;
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
        public void initialyzeIdentifier(char c, int row, int col, int absolute) throws ParseException {
            Pos = new JDocumentPosition(absolute, row, col);
            sb = new StringBuilder();
            sb.append(c);
            ConsumeChar = true;
            NextState = JLexer.E_TOKENIZER_STATE.IDENTIFIER;
        }

        @Override
        public void initialyzeString(char c, int row, int col, int absolute) throws ParseException {
            Pos = new JDocumentPosition(absolute, row, col);
            sb = new StringBuilder();
            ConsumeChar = true;
            NextState = JLexer.E_TOKENIZER_STATE.STRING;
        }

        @Override
        public JLexerPositionalToken tokenizeString() throws ParseException {
            JLexerPositionalToken tempToken = new JLexerPositionalToken(
                    new JJalphiToken(ELexerTokenType.STRING, sb.toString(),JALPHI_TOKEN_TYPE.UNDEFINED)
                    ,Pos);
            clearInternalVariables();
            return tempToken;
        }

        @Override
        public boolean appendToWhiteSpace(char c) {
            if(Character.isWhitespace(c)) {
                    NextState = JLexer.E_TOKENIZER_STATE.WHIESPACE;
                    ConsumeChar = true;
                    return false;
            } else {
                    NextState = JLexer.E_TOKENIZER_STATE.DEFAULT;
                    ConsumeChar = false;
                    return true;
            }
        }

        @Override
        public void initialyzeWhitespace(char c, int row, int col, int absolute) throws ParseException {
            Pos = new JDocumentPosition(absolute, row, col);
            sb = null;
            ConsumeChar = true;
            NextState = JLexer.E_TOKENIZER_STATE.WHIESPACE;
        }

        @Override
        public JLexerPositionalToken tokenizeWhitespace() {
            return new JLexerPositionalToken(JALPHI_TOKEN_TYPE.WHITESPACE.Token, Pos);
        }

        @Override
        public boolean appendToIdentifier(char c) {
            if(VALIDIDENTIFIERCHARS.indexOf(c)>=0) {
                sb.append(c);
                ConsumeChar = true;
                NextState = JLexer.E_TOKENIZER_STATE.IDENTIFIER;
                return false;
            } else {
                ConsumeChar = false;
                NextState = JLexer.E_TOKENIZER_STATE.DEFAULT;
                return true;
            }
        }

        @Override
        public void initialyzeNumber(char c, int row, int col, int absolute) throws ParseException {
            Pos = new JDocumentPosition(absolute, row, col);
            sb = new StringBuilder();
            sb.append(c);
            HasExponent = false;
            HasDecimals = false;
            ConsumeChar = true;
            NextState = JLexer.E_TOKENIZER_STATE.NUMBER;
        }

        @Override
        public JLexerPositionalToken tokenizeNumber() {
            return new JLexerPositionalToken(
                    new JJalphiToken(
                        ELexerTokenType.NUMBER, sb.toString(),JALPHI_TOKEN_TYPE.UNDEFINED
                    ), Pos
            );
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
                        ConsumeChar = false;
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
                    ConsumeChar = false;
                    return true;
            }
            NextState = JLexer.E_TOKENIZER_STATE.NUMBER;
            ConsumeChar = true;
            return false;
        }

        @Override
        public boolean appendToComment(char c)  throws ParseException {
            if(sb==null)
                sb=new StringBuilder();
            ConsumeChar = true;
            switch(c) {
                case '\n': case '\r': case '\f':
                    if(CommentType==1) {
                        NextState=JLexer.E_TOKENIZER_STATE.DEFAULT;
                        return true;
                    } else sb.append(c);
                    break;
                case '/':
                    if(CommentType==2 && sb.length()>0) {
                        if(sb.charAt(sb.length()-1)=='*') {
                            NextState=JLexer.E_TOKENIZER_STATE.DEFAULT;
                            sb.delete(sb.length() - 1, sb.length());
                            return true;
                        } else sb.append(c);
                    } 
                default:
                    sb.append(c);
            }
            NextState=JLexer.E_TOKENIZER_STATE.COMMENT;
            return false;
        }

        @Override
        public JLexerPositionalToken tokenizeComment() throws ParseException {
            switch (CommentType) {
                case 2:
                    CommentType = 0;
                    return new JLexerPositionalToken(new JJalphiToken(ELexerTokenType.COMMENT_MULTILINE, sb.toString(),JALPHI_TOKEN_TYPE.UNDEFINED),Pos);
                case 1:
                    CommentType = 0;
                    return new JLexerPositionalToken(new JJalphiToken(ELexerTokenType.COMMENT_SINGLELINE, sb.toString(),JALPHI_TOKEN_TYPE.UNDEFINED),Pos);
                default:
                    throw new ParseException("Unknown comment type "+CommentType, -1);
            
            }
        }

        // no one character comment 
        @Override
        public boolean isCommentStart(char c) {
            return false;
        }

        @Override
        public void initialyzeComment(char c, int row, int col, int absolute) throws ParseException {
            Pos = new JDocumentPosition(absolute, row, col);
            sb=new StringBuilder();
        }

        private void clearInternalVariables() {
            sb = null;
            Pos = null;
        }
    };

    
    private boolean foundWhitespace;
    private String Data;
    private int Size;
    private int Cursor;
    private char c;
    private int Scope = 0;
    
    private static final Pattern WHITESPACETOSPACE = Pattern.compile("\\s+");
    private static final Pattern VALIDATE_IDENTIFIER=Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
    private static final String VALIDDIGITS = "-.0123456789";
    private static final String OPERATIONS = "=+-*/<>|!&^";
    private static final String DELIMITERS = "()[]{}";
    private static final String SEPARATORS = ".:";
    private static final String TERMINATORS = ";";
    private static final String SYMBOLS = OPERATIONS + DELIMITERS + SEPARATORS + TERMINATORS;
    private static final String VALIDCHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_.0123456789";
    
    @Override
    public String getName() {
        return "JALPHI";
    }

    @Override
    public double getRevision() {
        return 0.1;
    }

    /**
     *
     * Construct the pseudoparameter tree able to obtain the 
     * reference to the pseudoparameter
     * 
     * @param identifier string containing the identifier
     * @return return the PseudoParameter tree
     */
    
    private boolean validateIdentifier(String identifier) throws ParseException {
        if(VALIDATE_IDENTIFIER.matcher(identifier).matches()) {
            if(RESERVED.containsKey(identifier)) 
                throw new ParseException("Invalid reserved word used as identifier \""+identifier+"\"", -1);
            return true;
        } else 
            return false;
    }

    /**
     * Consume all WHITESPACE tokens and return a valid token or null if the
     * iterator is consumed
     * 
     * Notice: this funtion modifies silently foundWhitespace to specify
     * if a whitespace was actually found.
     * 
     * @param tokens iterator containing the tokens
     * @return a non WHITESPACE token or 
     */
    public JLexerPositionalToken consumeWhitespaces(Iterator<JLexerPositionalToken> tokens) {
        foundWhitespace = false;
        JLexerPositionalToken item = tokens.next();
        while(item.Token.Type == ELexerTokenType.WHITESPACE) {
            foundWhitespace = true;
            if(!tokens.hasNext()) return null;
            item = tokens.next();
        }
        return item;
    }
    
    private void throwParseException(String message) throws ParseException {
        throw new ParseException(message, -1);
    }

    private void throwParseException(String message, JLexerToken Token) throws ParseException {
        throw new ParseException(String.format(message,new Object[] { Token.Type.name(), Token.Data }), -1);
    }
    
    private JPseudoInstruction parseParentesis(Iterator<JLexerPositionalToken> tokens, JLexerToken CloseToken) throws ParseException {
        JLexerPositionalToken token = consumeWhitespaces(tokens);
        throwEndProgramExceptionIfNull(token.Token, " while parsing parenthesis.");
        while(token.Token!=CloseToken) {
            token = consumeWhitespaces(tokens);
            throwEndProgramExceptionIfNull(token.Token, " while parsing parenthesis.");
        }
        return null;
    }

    private JPseudoInstruction parseExpression(Iterator<JLexerPositionalToken> tokens) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private List<JPseudoInstruction> parseInstructionsBlock(Iterator<JLexerPositionalToken> tokens, VMProviderInterface provider) throws ParseException {
        int MyScope = Scope++;
        
        E_PARSE_STATE State = E_PARSE_STATE.FIND_TARGET;
        JLexerPositionalToken item=consumeWhitespaces(tokens);
        JLexerToken token;
        JJalphiToken wToken;
        JJalphiToken wIdentifier;
        if(item==null)
            throw new ParseException("Unexpected end of file!",-1);
        List<JPseudoInstruction> block = new LinkedList<>();
        JPseudoInstruction instruction = null;
        
        JPseudoInstruction target = null;
        JPseudoInstruction source = null;
        JPseudoInstruction baseInstruction = null;
        
        while(!tokens.hasNext()) {
            item = tokens.next();
            token = item.Token;
            if(token instanceof JJalphiToken) {
                wToken = (JJalphiToken) token;
            } else 
                throw JVMExceptionBuilder.ParseException("Token <{4}> {3} was parsed by incompatible Lexer at ({1},{2})", item, -1);
            if(wToken.Type==null && wToken.Instruction==null) 
                throw JVMExceptionBuilder.UnexpectedException("Unexpected token type == null!", item.Pos);
            switch(State) {
                case FIND_TARGET:
                    switch (wToken.Type) {
                        case IDENTIFIER:
                            wIdentifier = wToken;
                            State = E_PARSE_STATE.HAS_TARGET_IDENTIFIER;
                            break;
                        case COMMENT_MULTILINE:
                        case COMMENT_SINGLELINE:
                            /** Do nothing **/
                            break;
                        case CODE_BLOCK:
                            switch (wToken.Instruction) {
                                case BLOCK_END:
                                    if(target==null) {
                                        return block;
                                    } else {
                                        throw JVMExceptionBuilder.buildException("Found end block before instruction was complete at ({1},{2}).", item.Pos, -1);
                                    }
                                case BLOCK_BEGIN:
                                    if(target!=null) {
                                        throw JVMExceptionBuilder.buildException("Expected <IDENTIFIER>, found <CODE_BLOCK> \"{\"  at({1},{2})", item.Pos, -1);
                                    } else {
                                        instruction = new JPseudoInstruction(E_ACTION.EXECUTE_BLOCK, parseInstructionsBlock(tokens, provider));
                                        block.add(instruction);
                                        instruction=null;
                                    }
                                    break;
                                default:
                                    throw new AssertionError(wToken.Instruction.name());
                            }
                            break;
                        case WHITESPACE:
                            if(target==null) {
                            } else throw JVMExceptionBuilder.ParseException("Expected <IDENTIFIER>, found <{4}> \"{3}\" as part of an identifier at ({1},{2})", item, -1);
                            break;
                        case KEYWORD:
                            switch(wToken.Instruction) {
                                case RETURN:
                                    if(baseInstruction==null) {
                                        baseInstruction = new JPseudoInstruction(E_ACTION.RETURN);
                                        State = E_PARSE_STATE.PARSE_SOURCE;
                                    } else throw JVMExceptionBuilder.ParseException("Invalid use of <{4}> \"{3}\" as part of an identifier at ({1},{2})", item, -1);
                                    break;
                                case CONTINUE:
                                    if(baseInstruction==null) {
                                        baseInstruction = new JPseudoInstruction(E_ACTION.CONTINUE);
                                        State = E_PARSE_STATE.PARSE_SOURCE;
                                    } else throw JVMExceptionBuilder.ParseException("Invalid use of <{4}> \"{3}\" as part of an identifier at ({1},{2})", item, -1);
                                    break;
                                case BREAK:
                                    if(baseInstruction==null) {
                                        baseInstruction = new JPseudoInstruction(E_ACTION.BREAK);
                                        State = E_PARSE_STATE.PARSE_SOURCE;
                                    } else throw JVMExceptionBuilder.ParseException("Invalid use of <{4}> \"{3}\" as part of an identifier at ({1},{2})", item, -1);
                                    break;
                                case WHILE:
                                case FOR:
                                case ELSE_IF:
                                case ELSE:
                                case IF:
                                    throw new UnsupportedOperationException("Not supprterd yet");
                                case THIS:
                                    if(baseInstruction==null) {
                                        baseInstruction = new JPseudoInstruction(E_ACTION.GET_THIS);
                                        State = E_PARSE_STATE.HAS_TARGET_IDENTIFIER;
                                    } else throw JVMExceptionBuilder.ParseException("Invalid use of <{4}> \"{3}\" as part of an identifier at ({1},{2})", item, -1);
                                    break;
                                case SYSTEM:
                                    if(baseInstruction==null) {
                                        baseInstruction = new JPseudoInstruction(E_ACTION.GET_SYSTEM);
                                        State = E_PARSE_STATE.HAS_TARGET_IDENTIFIER;
                                    } else throw JVMExceptionBuilder.ParseException("Invalid use of <{4}> \"{3}\" as part of an identifier at ({1},{2})", item, -1);
                                    break;
                                case NULL:
                                case TYPE_OBJECT:
                                case TYPE_TOKEN:
                                case TYPE_FLOAT:
                                case TYPE_INTEGER:
                                case TYPE_STRING:
                                case TYPE_VOID:
                                    throw JVMExceptionBuilder.ParseException("Invalid <{4}> \"{3}\" at the beginning of an instruction ({1},{2})", item, -1);
                                default:
                                    throw new AssertionError(wToken.Instruction.name());
                            }
                            wIdentifier = wToken;
                            break;
                        default:
                            throw JVMExceptionBuilder.ParseException("Unexpected <{4}> \"{3}\" at the beginning of an instruction ({1},{2})", item, -1);
                    }
                    break;
                case HAS_TARGET_IDENTIFIER:
                    switch(wToken.Instruction) {
                        case UNDEFINED:
                            throw JVMExceptionBuilder.ParseException("Unexpected <{4}> {3} at ({1},{2})", item, -1);
                        case WHITESPACE:
                            State = E_PARSE_STATE.SEARCH_INSTRUCTION;
                            break;
                        case ASSIGN:
                            break;
                        case MARK_IDENTIFIER:
                            break;
                        case SELECTOR:
                            break;
                        case DECLARATOR:
                            break;
                        case SEPARATE:
                            break;
                        case END_INSTRUCTION:
                            break;
                        case PAR_OPEN:
                            break;
                        case PAR_CLOSE:
                            break;
                        case EMPTY_PARAMS:
                            break;
                        case GREATER:
                            break;
                        case LESSER:
                            break;
                        case QPAR_OPEN:
                            break;
                        case QPAR_CLOSE:
                            break;
                        case BOOL_NOT:
                            break;
                        case PLUS:
                            break;
                        case MINUS:
                            break;
                        case MULT:
                            break;
                        case DIVIDE:
                            break;
                        case REMINDER:
                            break;
                        case BITWISE_AND:
                            break;
                        case BITWISE_OR:
                            break;
                        case BITWISE_INVERT:
                            break;
                        case BITWISE_XOR:
                            break;
                        case LINE_COMMENT:
                            break;
                        case COMMENT_START:
                            break;
                        case COMMENT_END:
                            break;
                        case BLOCK_BEGIN:
                            break;
                        case BLOCK_END:
                            break;
                        case TOKEN_START:
                            break;
                        case ISEQUAL:
                            break;
                        case NOTEQUAL:
                            break;
                        case SHIFTRIGHT:
                            break;
                        case SHIFTLEFT:
                            break;
                        case INCREMENT:
                            break;
                        case DECREMENT:
                            break;
                        case BOOL_AND:
                            break;
                        case AS_ARRAY:
                            break;
                        case BOOL_OR:
                            break;
                        case INCREMENT_BY:
                            break;
                        case DECREMENT_BY:
                            break;
                        case MULT_BY:
                            break;
                        case DIV_BY:
                            break;
                        case REMINDER_OF:
                            break;
                        case BITWISE_XOR_WITH:
                            break;
                        case BITWISE_OR_WITH:
                            break;
                        case BITWISE_AND_WITH:
                            break;
                        case SHIFTRIGHT_BY:
                            break;
                        case SHIFTLEFT_BY:
                            break;
                        case RETURN: case CONTINUE: case BREAK: case WHILE: case FOR:
                        case ELSE_IF: case ELSE: case IF: case THIS: case NULL: 
                        case IMPORT: case EVENT: case FUNCTION: case FRAGMENT:
                        case LIBRARY: case PROGRAM: case SYSTEM:
                            throw JVMExceptionBuilder.ParseException("Invalid use of <{4}> \"{3}\", expected operator", item, Size);
                        case IS:
                            break;
                        case AS:
                            break;
                        
                        case TYPE_OBJECT:
                            break;
                        case TYPE_TOKEN:
                            break;
                        case TYPE_FLOAT:
                            break;
                        case TYPE_INTEGER:
                            break;
                        case TYPE_STRING:
                            break;
                        case TYPE_VOID:
                            break;
                        default:
                            throw JVMExceptionBuilder.ParseException("Unexpected <{4}> {3} at ({1},{2})", item, -1);
                    }
                    break;
                default:
                    throw new AssertionError(State.name());
            }
        }
        throw new ParseException("Unexpected end of program!", -1);
    }

    private void throwEndProgramExceptionIfNull(JLexerToken token, String qualifier) throws ParseException {
        if(token==null) {
            if(qualifier!=null)  {
                throw new ParseException("Unexpecetd end of program: "+qualifier, -1);                
            } else {
            }
        }
    }
    
    private List<JPseudoVariable> parseArguments(Iterator<JLexerPositionalToken> tokens)  throws ParseException {
        List<JPseudoVariable> result = new ArrayList<>();
        JLexerPositionalToken item = null;
        JLexerToken token = null;
        boolean working = true; 
        while(token != JALPHI_TOKEN_TYPE.PAR_CLOSE.Token) {
            item = consumeWhitespaces(tokens);
            if(item!=null)
                token = item.Token;
            else
                throw JVMExceptionBuilder.buildEOFException("While parsing fragment arguments", -1);
            if(token.Type == ELexerTokenType.IDENTIFIER) {
                boolean isArray=false;
                String identifier = token.Data;
                E_VAR_TYPE type = null;
                String subType=null;
                item = consumeWhitespaces(tokens);
                if(item == null) {
                    token = null;
                    throwEndProgramExceptionIfNull(token, " while parsing fragment arguments");
                } else {
                    token = item.Token;
                }
                if(token == JALPHI_TOKEN_TYPE.DECLARATOR.Token) {
                    item = consumeWhitespaces(tokens);
                    if(item==null) {
                        token=null;
                        throwEndProgramExceptionIfNull(token, " while parsing fragment arguments");
                    } else {
                        token = item.Token;
                    }
                    if(token==JALPHI_TOKEN_TYPE.TYPE_INTEGER.Token) {
                        type = E_VAR_TYPE.INTEGER;
                    } else if (token == JALPHI_TOKEN_TYPE.TYPE_FLOAT.Token) {
                        type = E_VAR_TYPE.FLOAT;
                    } else if (token == JALPHI_TOKEN_TYPE.TYPE_STRING.Token) {
                        type = E_VAR_TYPE.STRING;
                    } else if (token == JALPHI_TOKEN_TYPE.TYPE_TOKEN.Token) {
                        type = E_VAR_TYPE.IDENTIFIER;
                    } else if (token == JALPHI_TOKEN_TYPE.TYPE_VOID.Token) {
                        type = E_VAR_TYPE.VOID;
                    } else if (token == JALPHI_TOKEN_TYPE.TYPE_OBJECT.Token) {
                        type = E_VAR_TYPE.OBJ;
                    } else throwParseException("Expected type, found <%s> %s.", token);
                    item = consumeWhitespaces(tokens);
                    if(item==null) {
                        token=null;
                        throwEndProgramExceptionIfNull(token, " while parsing fragment arguments");
                    } else {
                        token = item.Token;
                    }
                    if(token==JALPHI_TOKEN_TYPE.SELECTOR.Token) {
                        if(foundWhitespace)
                            throwParseException("Unexpected dot punctation '.' after a space");
                        if(type == E_VAR_TYPE.OBJ || type == E_VAR_TYPE.IDENTIFIER) {
                            if(!tokens.hasNext()) 
                                throwParseException("Unexpected end of file while parsing fragment arguments");
                            item = tokens.next();
                            token = item.Token;
                            if(token.Type == ELexerTokenType.IDENTIFIER) {
                                subType = token.Data;
                                item = consumeWhitespaces(tokens);
                                if(item==null) {
                                    token=null;
                                    throwEndProgramExceptionIfNull(token, " while parsing fragment arguments");
                                } else {
                                    token = item.Token;
                                }
                            } else  {
                                throwParseException("Unexpected <%s> %s instead of subtype identifier");
                            }
                        } else { 
                            if(type!=null)
                                throwParseException("Invalid subtype specifier after "+type.name());
                            else
                                throwParseException("Null pointer for type!");
                        }
                    }
                    if(token==JALPHI_TOKEN_TYPE.AS_ARRAY.Token) {
                        isArray=true;
                        item = consumeWhitespaces(tokens);
                        if(item==null) {
                            token=null;
                            throwEndProgramExceptionIfNull(token, " while parsing fragment arguments");
                        } else {
                            token = item.Token;
                        }
                    }
                    if((token==JALPHI_TOKEN_TYPE.SEPARATE.Token || token == JALPHI_TOKEN_TYPE.PAR_CLOSE.Token)) {
                        result.add(new JPseudoVariable(identifier, JPseudoType.createType(type, subType, isArray)));
                    } else
                        throwParseException("Expected declartion terminator symbol ')' ro separator ','! found <%s> %s", token);
                } else throwParseException("Expected declartion symbol ':'! found <%s> %s", token);
            } else {
                throwParseException("Unexpected <"+token.Type.name()+"> "+token.Data);
            }
        }
        return result;
    }
    
    /**
     * parse the first part of the program, all imports until the intestation
     * included. Will return the type of the fragment, the name, the parameters
     * passed when called
     * 
     * @param tokens iterator with the token parsed bu Lexer
     * @param Provider instruction provider
     * @return Pseudoprogram with intestation ready
     * @throws ParseException
     */
    private JPseudoProgram parseProgramIntestation(Iterator<JLexerPositionalToken> tokens, VMProviderInterface Provider) throws ParseException {
        if(!tokens.hasNext())
            return null;
        JPseudoProgram program = new JPseudoProgram();
        JLexerPositionalToken item = consumeWhitespaces(tokens);
        if(item == null)
            return null;
        JLexerToken token = item.Token;
        while(token==JALPHI_TOKEN_TYPE.IMPORT.Token) {
            item = consumeWhitespaces(tokens);
            if(item == null)
                throw JVMExceptionBuilder.buildEOFException("Unexpected end of file while parsing import", -1);
            token = item.Token;
            if(token.Type==ELexerTokenType.IDENTIFIER || token == JALPHI_TOKEN_TYPE.SYSTEM.Token) {
                program.addLibraryImport(token.Data);
            } else throw JVMExceptionBuilder.ParseException("Unexpected <{4}> \"{3}\" instad of <IDENTIFIER> at ({1},{2})", item, -1);
            item = consumeWhitespaces(tokens);
            if(item == null)
                throw JVMExceptionBuilder.buildEOFException("Unexpected end of file while parsing import", -1);
            token = item.Token;
            if(token!= JALPHI_TOKEN_TYPE.END_INSTRUCTION.Token)
                throw JVMExceptionBuilder.ParseException("Unexpected <{4}> \"{3}\" instad of <SEPARATOR> \";\" ({1},{2})", item, -1);
            item = consumeWhitespaces(tokens);
            if(item == null)
                throw JVMExceptionBuilder.buildEOFException("Unexpected end of file while parsing import", -1);
            token = item.Token;
        }
        if(token.Type==ELexerTokenType.KEYWORD) {
            if(token==JALPHI_TOKEN_TYPE.PROGRAM.Token) {
                program.setType(E_CODE_TYPE.PROGRAM);
            } else if(token==JALPHI_TOKEN_TYPE.EVENT.Token) {
                program.setType(E_CODE_TYPE.EVENT);
                program.addArgument(JPseudoVariable.THIS);
            } else if(token==JALPHI_TOKEN_TYPE.FRAGMENT.Token) {
                program.setType(E_CODE_TYPE.FRAGMENT);
                program.addArgument(JPseudoVariable.THIS);
            } else if(token==JALPHI_TOKEN_TYPE.FUNCTION.Token) {
                program.setType(E_CODE_TYPE.FUNCTION);
            } else
                throw JVMExceptionBuilder.ParseException("Invalid <{4}> \"{3}\", expected <KEYWORD> Fragment|Event at ({1},{2})", item, -1);
        } else throw JVMExceptionBuilder.ParseException("Invalid <{4}> \"{3}\", expected <KEYWORD> Fragment|Event at ({1},{2})", item, -1);
        item = consumeWhitespaces(tokens);
        if(item == null)
            throw JVMExceptionBuilder.buildEOFException("No fragment name found", -1);
        token = item.Token;
        if(token.Type==ELexerTokenType.IDENTIFIER) {
            program.setName(token.Data);
        } else {
            throw JVMExceptionBuilder.ParseException("Invalid <{4}> \"{3}\", expected <IDENTIFIER> at ({1},{2})", item, -1);
        }
        item = consumeWhitespaces(tokens);
        if(item == null)
            throw JVMExceptionBuilder.buildEOFException("No parameters list found", -1);
        token = item.Token;
        if(token == JALPHI_TOKEN_TYPE.PAR_OPEN.Token) {
            parseArguments(tokens).forEach(argument -> {
                program.addArgument(argument);
            });
        } else {
            throwParseException("Expected '(', found <"+token.Type.name()+"> "+token.Data);
        }
        item = consumeWhitespaces(tokens);
        if(item==null) {
            token=null;
            throwEndProgramExceptionIfNull(token, " while parsing fragment arguments");
        } else {
            token = item.Token;
        }
        if (token == JALPHI_TOKEN_TYPE.DECLARATOR.Token) {
            item = consumeWhitespaces(tokens);
            if(item==null) {
                token = null;
                throwEndProgramExceptionIfNull(token, " before parameters list");
            } else {
                token = item.Token;
            }
            String subType=null;
            boolean isArray=false;
            E_VAR_TYPE type=null;
            if(token==JALPHI_TOKEN_TYPE.TYPE_INTEGER.Token) {
                type = E_VAR_TYPE.INTEGER;
            } else if (token == JALPHI_TOKEN_TYPE.TYPE_FLOAT.Token) {
                type = E_VAR_TYPE.FLOAT;
            } else if (token == JALPHI_TOKEN_TYPE.TYPE_STRING.Token) {
                type = E_VAR_TYPE.STRING;
            } else if (token == JALPHI_TOKEN_TYPE.TYPE_TOKEN.Token) {
                type = E_VAR_TYPE.IDENTIFIER;
            } else if (token == JALPHI_TOKEN_TYPE.TYPE_VOID.Token) {
                type = E_VAR_TYPE.VOID;
            } else if (token == JALPHI_TOKEN_TYPE.TYPE_OBJECT.Token) {
                type = E_VAR_TYPE.OBJ;
            } else
                throw JVMExceptionBuilder.ParseException("Expected type, found <{4}> \"{3}\" at ({1},{2}).", item, -1);
            item = consumeWhitespaces(tokens);
            if(item==null) {
                throw JVMExceptionBuilder.buildEOFException("Before parameters list", -1);
            } else {
                token = item.Token;
            }
            if(token==JALPHI_TOKEN_TYPE.SELECTOR.Token) {
                if(foundWhitespace) {
                    throw JVMExceptionBuilder.ParseException("\"Found space before separator dot . at ({1},{2}).", item, -1);
                } else {
                    item = consumeWhitespaces(tokens);
                    if(item==null) {
                        throw JVMExceptionBuilder.buildEOFException("Before parameters list", -1);
                    } else {
                        token = item.Token;
                    }
                    if(token.Type==ELexerTokenType.IDENTIFIER) {
                        subType = token.Data;
                    } else 
                        throw JVMExceptionBuilder.ParseException("Expected <IDENTIFIER>, found <{4}> \"{3}\" at ({1},{2}).", item, -1);                }
            }
            if(token==JALPHI_TOKEN_TYPE.AS_ARRAY.Token) {
                if(program.getType()==E_CODE_TYPE.FUNCTION) {
                    isArray=true;
                } else throw new ParseException(program.getType().name()+" can't return an array type.",-1);
                item = consumeWhitespaces(tokens);
                if(item==null) {
                    token = null;
                    throwEndProgramExceptionIfNull(token, " before parameters list");
                } else {
                    token = item.Token;
                }
            }
            program.setReturnType(JPseudoType.createType(type, subType, isArray));
        }
        if(token ==JALPHI_TOKEN_TYPE.BLOCK_BEGIN.Token)
            return program;
        else { 
            throwParseException("Unexpected token <%s> %s at the end of framgnet intestation.", token);
            return null;
        }
    }
        
    @Override
    public JPseudoProgram parse(Iterator<JLexerPositionalToken> tokens, VMProviderInterface provider) throws ParseException {
        JPseudoProgram program;
        program = parseProgramIntestation(tokens, provider);
        program.addInstructions(parseInstructionsBlock(tokens, provider));
        JLexerPositionalToken token = consumeWhitespaces(tokens);
        if(token!=null)
            throwParseException("Unexpected <%s> %s after the end of the program!", token.Token);
        return program;
    }

    private enum E_PARSE_STATE {
        SEARCH_INSTRUCTION, EXPECT_FUNCTION, FIND_TARGET, PARSE_SOURCE, HAS_TARGET_IDENTIFIER;
    }
    
}
