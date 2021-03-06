/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM.Parser.JALPHI;

import JScriptParser.E_ACTION;
import JScriptParser.E_CODE_TYPE;
import JScriptParser.E_VAR_TYPE;
import InternalVM.Lexer.ELexerTokenType;
import InternalVM.Lexer.JLexer;
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
import JScriptParser.VMProviderInterface;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class JScrLanJALPHI extends JScriptingLanguage {
    
    public static final JLexerToken WHITESPACE = new JLexerToken(ELexerTokenType.WHITESPACE, " ");

    /**
     * "\@" symbol defines the start of a token
     */
    public static final JLexerToken TOKENSTART = new JLexerToken(ELexerTokenType.COMPARATOR, "$");
    /**
     * "." is used to separate parent and child in a object identifier
     */
    public static final JLexerToken DOT = new JLexerToken(ELexerTokenType.BINARYOP, ".");
    /**
     * ":" is used in variable and argument declarations
     */
    public static final JLexerToken DECLARE = new JLexerToken(ELexerTokenType.ASSIGN, ":");
    public static final JLexerToken SEPARATE = new JLexerToken(ELexerTokenType.SEPARATOR, ",");
    public static final JLexerToken END_INSTRUCTION = new JLexerToken(ELexerTokenType.ENDCOMMAND, ";");
    public static final JLexerToken ASSIGN = new JLexerToken(ELexerTokenType.ASSIGN, "=");
    public static final JLexerToken PAR_OPEN = new JLexerToken(ELexerTokenType.SEPARATOR, "(");
    public static final JLexerToken PAR_CLOSE = new JLexerToken(ELexerTokenType.SEPARATOR, ")");
    public static final JLexerToken GREATERTHAN = new JLexerToken(ELexerTokenType.COMPARATOR, ">");
    public static final JLexerToken LESSERTHAN = new JLexerToken(ELexerTokenType.COMPARATOR, "<");
    public static final JLexerToken QPAR_OPEN = new JLexerToken(ELexerTokenType.SEPARATOR, "[");
    public static final JLexerToken QPAR_CLOSE = new JLexerToken(ELexerTokenType.SEPARATOR, "]");
    public static final JLexerToken BOOL_NOT = new JLexerToken(ELexerTokenType.UNARYOP, "!");
    public static final JLexerToken ADD = new JLexerToken(ELexerTokenType.BINARYOP, "+");
    public static final JLexerToken SUB = new JLexerToken(ELexerTokenType.BINARYOP, "-");
    public static final JLexerToken MULT = new JLexerToken(ELexerTokenType.BINARYOP, "*");
    public static final JLexerToken DIV = new JLexerToken(ELexerTokenType.BINARYOP, "/");
    public static final JLexerToken MOD = new JLexerToken(ELexerTokenType.BINARYOP, "%");
    public static final JLexerToken AND = new JLexerToken(ELexerTokenType.BINARYOP, "&");
    public static final JLexerToken OR = new JLexerToken(ELexerTokenType.BINARYOP, "|");
    public static final JLexerToken LINE_COMMENT = new JLexerToken(ELexerTokenType.CODE_BLOCK, "//");
    public static final JLexerToken COMMENT_START = new JLexerToken(ELexerTokenType.CODE_BLOCK, "/*");
    public static final JLexerToken COMMENT_END = new JLexerToken(ELexerTokenType.CODE_BLOCK, "*/");
    public static final JLexerToken ISEQUAL = new JLexerToken(ELexerTokenType.COMPARATOR, "==");
    public static final JLexerToken NOTEQUAL = new JLexerToken(ELexerTokenType.COMPARATOR, "!=");
    public static final JLexerToken SHIFTRIGHT = new JLexerToken(ELexerTokenType.BINARYOP, ">>");
    public static final JLexerToken SHIFTLEFT = new JLexerToken(ELexerTokenType.BINARYOP, "<<");
    public static final JLexerToken SHIFTRIGHT_BY = new JLexerToken(ELexerTokenType.BINARYOP, ">>=");
    public static final JLexerToken SHIFTLEFT_BY = new JLexerToken(ELexerTokenType.BINARYOP, "<<=");
    public static final JLexerToken INCREMENT = new JLexerToken(ELexerTokenType.UNARYOP, "++");
    public static final JLexerToken DECREMENT = new JLexerToken(ELexerTokenType.UNARYOP, "--");
    public static final JLexerToken BOOL_AND = new JLexerToken(ELexerTokenType.BINARYOP, "&&");
    public static final JLexerToken AS_ARRAY = new JLexerToken(ELexerTokenType.UNARYOP, "[]");
    public static final JLexerToken BOOL_OR = new JLexerToken(ELexerTokenType.BINARYOP, "||");
    public static final JLexerToken INCREMENT_BY = new JLexerToken(ELexerTokenType.BINARYOP, "+=");
    public static final JLexerToken DECREMENT_BY = new JLexerToken(ELexerTokenType.BINARYOP, "-=");
    public static final JLexerToken MULT_BY = new JLexerToken(ELexerTokenType.BINARYOP, "*=");
    public static final JLexerToken DIV_BY = new JLexerToken(ELexerTokenType.BINARYOP, "/=");
    public static final JLexerToken MOD_OF = new JLexerToken(ELexerTokenType.BINARYOP, "%=");
    public static final JLexerToken ASSIGNDIV = new JLexerToken(ELexerTokenType.BINARYOP, "/=");
    public static final JLexerToken NOT = new JLexerToken(ELexerTokenType.UNARYOP, "~");
    public static final JLexerToken XOR = new JLexerToken(ELexerTokenType.BINARYOP, "^");
    public static final JLexerToken XOR_WITH = new JLexerToken(ELexerTokenType.BINARYOP, "^=");
    public static final JLexerToken OR_WITH = new JLexerToken(ELexerTokenType.BINARYOP, "|=");
    public static final JLexerToken AND_WITH = new JLexerToken(ELexerTokenType.BINARYOP, "&=");
    public static final JLexerToken BLOCK_BEGIN = new JLexerToken(ELexerTokenType.CODE_BLOCK, "{");
    public static final JLexerToken BLOCK_END = new JLexerToken(ELexerTokenType.CODE_BLOCK, "}");

    public static final JLexerToken PROGRAM = new JLexerToken(ELexerTokenType.KEYWORD, "Program");
    public static final JLexerToken LIBRARY = new JLexerToken(ELexerTokenType.KEYWORD, "Library");
    public static final JLexerToken FUNCTION = new JLexerToken(ELexerTokenType.KEYWORD, "Function");
    public static final JLexerToken FRAGMENT = new JLexerToken(ELexerTokenType.KEYWORD, "Fragment");
    public static final JLexerToken EVENT = new JLexerToken(ELexerTokenType.KEYWORD, "Event");
    public static final JLexerToken IMPORT = new JLexerToken(ELexerTokenType.KEYWORD, "import");
    public static final JLexerToken TYPE_VOID = new JLexerToken(ELexerTokenType.KEYWORD, "void");
    public static final JLexerToken TYPE_STRING = new JLexerToken(ELexerTokenType.KEYWORD, "string");
    public static final JLexerToken TYPE_TOKEN = new JLexerToken(ELexerTokenType.KEYWORD, "token");
    public static final JLexerToken TYPE_FLOAT = new JLexerToken(ELexerTokenType.KEYWORD, "float");
    public static final JLexerToken TYPE_INTEGER = new JLexerToken(ELexerTokenType.KEYWORD, "integer");
    public static final JLexerToken TYPE_OBJECT = new JLexerToken(ELexerTokenType.KEYWORD, "object");
    
    public static final JLexerToken REFERENCE_THIS = new JLexerToken(ELexerTokenType.KEYWORD, "this");
    public static final JLexerToken REFERENCE_NULL = new JLexerToken(ELexerTokenType.KEYWORD, "null");

    public static final JLexerToken IF = new JLexerToken(ELexerTokenType.KEYWORD, "if");
    public static final JLexerToken ELSE = new JLexerToken(ELexerTokenType.KEYWORD, "else");
    public static final JLexerToken ELSE_IF = new JLexerToken(ELexerTokenType.KEYWORD, "elsif");
    public static final JLexerToken FOR = new JLexerToken(ELexerTokenType.KEYWORD, "for");
    public static final JLexerToken WHILE = new JLexerToken(ELexerTokenType.KEYWORD, "while");
    public static final JLexerToken BREAK = new JLexerToken(ELexerTokenType.KEYWORD, "break");
    public static final JLexerToken CONTINUE = new JLexerToken(ELexerTokenType.KEYWORD, "continue");
    public static final JLexerToken RETURN = new JLexerToken(ELexerTokenType.KEYWORD, "return");

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
        addToMap (RESERVED, PROGRAM);
        addToMap (RESERVED, LIBRARY);
        addToMap (RESERVED, FRAGMENT);
        addToMap (RESERVED, FUNCTION);
        addToMap (RESERVED, EVENT);
        addToMap (RESERVED, IMPORT);
        addToMap (RESERVED, TYPE_VOID);
        addToMap (RESERVED, TYPE_STRING);
        addToMap (RESERVED, TYPE_INTEGER);
        addToMap (RESERVED, TYPE_FLOAT);
        addToMap (RESERVED, TYPE_TOKEN);
        addToMap (RESERVED, TYPE_OBJECT);
        addToMap (RESERVED, REFERENCE_THIS);
        addToMap (RESERVED, REFERENCE_NULL);
        addToMap (RESERVED, IF);
        addToMap (RESERVED, ELSE);
        addToMap (RESERVED, ELSE_IF);
        addToMap (RESERVED, FOR);
        addToMap (RESERVED, WHILE);
        addToMap (RESERVED, BREAK);
        addToMap (RESERVED, CONTINUE);
        addToMap (RESERVED, RETURN);
    };

    @Override
    public JTokenizeHelper getHelper() {
        return new JJALPHIHelper();    
    }

    //public static JTokenizeHelper HELPER = new JTokenizeHelper() {
    public static class JJALPHIHelper implements JTokenizeHelper{
        
        private StringBuilder sb = null;
        private static final String AGGREGABLES = "+-/*%&|^!=[]<>";
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
        
        private final List<JLexerToken> supportList = new LinkedList<>();

        private JLexerToken getKnownToken(String toTokenize) {
            if(toTokenize==null)
                return null;
            else if (toTokenize.length()==2) {
                switch(toTokenize) {
                    case "[]":
                        return AS_ARRAY;
                    case "!=":
                        return NOTEQUAL;
                    case "==":
                        return ISEQUAL;
                    case "||":
                        return BOOL_OR;
                    case "&&":
                        return BOOL_AND;
                    case ">>":
                        return SHIFTRIGHT;
                    case "<<":
                        return SHIFTLEFT;
                    case "++":
                        return INCREMENT;
                    case "//":
                        return LINE_COMMENT;
                    case "/*":
                        return COMMENT_START;
                    case "*/":
                        CommentType = 0;
                        NextState = JLexer.E_TOKENIZER_STATE.DEFAULT;
                        return COMMENT_END;
                    case "--":
                        return DECREMENT;
                    case "+=":
                        return INCREMENT_BY;
                    case "-=":
                        return DECREMENT_BY;
                    case "*=":
                        return MULT_BY;
                    case "/=":
                        return DIV_BY;
                    case "^=":
                        return XOR_WITH;
                    case "&=":
                        return AND_WITH;
                    case "|=":
                        return OR_WITH;
                    case "%=":
                        return MOD_OF;
                    default:
                        return null;
                }
            } else if (toTokenize.length()==1) {
                switch(toTokenize) {
                    case "!":
                        return BOOL_NOT;
                    case ";":
                        return END_INSTRUCTION;
                    case ",":
                        return SEPARATE;
                    case ".":
                        return DOT;
                    case "=":
                        return ASSIGN;
                    case ":":
                        return DECLARE;
                    case "+":
                        return ADD;
                    case "(":
                        return PAR_OPEN;
                    case ")":
                        return PAR_CLOSE;
                    case "[":
                        return QPAR_OPEN;
                    case "]":
                        return QPAR_CLOSE;
                    case "{":
                        return BLOCK_BEGIN;
                    case "}":
                        return BLOCK_END;
                    case "-":
                        return SUB;
                    case "*":
                        return MULT;
                    case "/":
                        return DIV;
                    case "%":
                        return MOD;
                    case "&":
                        return AND;
                    case "|":
                        return OR;
                    case "^":
                        return XOR;
                    case ">":
                        return GREATERTHAN;
                    case "<":
                        return LESSERTHAN;
                    default:
                        return null;
                }
            } else if (toTokenize.length()==1) {
                switch(toTokenize) {
                    case "<<=":
                        return SHIFTLEFT_BY;
                    case ">>=":
                        return SHIFTRIGHT_BY;
                    default:
                        return null;
                }
            }
            return null;
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
        public Collection<JLexerToken> tokenizeIdentifier() throws ParseException {
            supportList.clear();
            String s = sb.toString();
            sb=null;
            JLexerToken tempToken = RESERVED.get(s);
            if(tempToken==null) {
                if(s.charAt(0)=='@') 
                    supportList.add( new JLexerToken(ELexerTokenType.TOKEN, s.substring(1)));
                else
                    supportList.add(new JLexerToken(ELexerTokenType.IDENTIFIER, s));
            } else supportList.add(tempToken);
            return Collections.unmodifiableCollection(supportList);
        }

        @Override
        public JLexerToken tokenizeSymbol(char symbol) throws ParseException {
            String s=""+symbol;
            JLexerToken tempToken = getKnownToken(s);
            if(tempToken==null) {
                throw new ParseException("Unexpected character 0x"+Integer.toHexString(symbol)+" ("+symbol+")",-1);
                //return new JLexerToken(ELexerTokenType.UNEXPECTED, s);
            }
            return tempToken;
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
        public void initialyzeAggregable(char c) {
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
                if(sb.length()>=2) {
                    String testComment = sb.substring(sb.length()-2, sb.length());
                    if(testComment.equals(COMMENT_START.Data)) {
                        sb.delete(sb.length()-2, sb.length());
                        NextState = JLexer.E_TOKENIZER_STATE.COMMENT;
                        CommentType = 2;
                        return true;
                    } else if(testComment.equals(LINE_COMMENT.Data)) {
                        sb.delete(sb.length()-2, sb.length());
                        NextState = JLexer.E_TOKENIZER_STATE.COMMENT;
                        CommentType = 1;
                        return true;
                    }
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
        public Collection<JLexerToken> tokenizeAggregables() throws ParseException {
            String aggregate = sb.toString();
            sb=new StringBuilder();
            supportList.clear();
            int i = 0;
            int len = aggregate.length();
            JLexerToken tempToken;
            while(i<len) {
                String temp;
                switch (len-i) {
                    case 0: throw new IllegalAccessError("This value cannot be 0!");
                    case 1:
                        temp = aggregate.substring(i,i+1);
                        break;
                    case 2:
                        temp = aggregate.substring(i,i+2);
                        break;
                    default:
                        temp = aggregate.substring(i,i+3);
                }
                boolean searching = true;
                while(searching) {
                    tempToken = getKnownToken(temp);
                    if(tempToken == null) {
                        if(temp.length()==1) {
                            char c = temp.charAt(0);
                            if(Character.isDefined(c))
                                throw new ParseException("Illegal character '"+c+"'", -1);
                            else
                                throw new ParseException("Illegal character 0x'"+Integer.toHexString(c)+"'", -1);
                        } else {
                            temp=temp.substring(0,temp.length()-1);
                        }
                    } else {
                        searching = false;                    
                        supportList.add(tempToken);
                    }
                }
                i+=temp.length();
            }
            sb=null;
            return Collections.unmodifiableCollection(supportList);
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
        public void initialyzeIdentifier(char c) throws ParseException {
            sb = new StringBuilder();
            sb.append(c);
            ConsumeChar = true;
            NextState = JLexer.E_TOKENIZER_STATE.IDENTIFIER;
        }

        @Override
        public void initialyzeString(char c) throws ParseException {
            sb = new StringBuilder();
            ConsumeChar = true;
            NextState = JLexer.E_TOKENIZER_STATE.STRING;
        }

        @Override
        public Collection<JLexerToken> tokenizeString() throws ParseException {
            supportList.clear();
            supportList.add(new JLexerToken(ELexerTokenType.STRING, sb.toString()));
            sb=null;
            return Collections.unmodifiableCollection(supportList);
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
        public void initialyzeWhitespace(char c) {
            ConsumeChar = true;
            NextState = JLexer.E_TOKENIZER_STATE.WHIESPACE;
        }

        @Override
        public List<JLexerToken> tokenizeWhitespace() {
            supportList.clear();
            supportList.add(WHITESPACE);
            return Collections.unmodifiableList(supportList);
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
        public void initialyzeNumber(char c) {
            sb = new StringBuilder();
            sb.append(c);
            HasExponent = false;
            HasDecimals = false;
            ConsumeChar = true;
            NextState = JLexer.E_TOKENIZER_STATE.NUMBER;
        }

        @Override
        public Collection<JLexerToken> tokenizeNumber() {
            supportList.clear();
            supportList.add(new JLexerToken(ELexerTokenType.NUMBER, sb.toString()));
            sb=null;
            return Collections.unmodifiableCollection(supportList);
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
        public Collection<JLexerToken> tokenizeComment()  throws ParseException {
            supportList.clear();
            switch (CommentType) {
                case 2:
                    CommentType = 0;
                    supportList.add(new JLexerToken(ELexerTokenType.COMMENT_MULTILINE, sb.toString()));
                    break;
                case 1:
                    CommentType = 0;
                    supportList.add(new JLexerToken(ELexerTokenType.COMMENT_SINGLELINE, sb.toString()));
                    break;
                default:
                    throw new ParseException("Unknown comment type "+CommentType, -1);
            
            }
            return Collections.unmodifiableCollection(supportList);
        }

        // no one character comment 
        @Override
        public boolean isCommentStart(char c) {
            return false;
        }

        @Override
        public void initialyzeComment(char c) throws ParseException {
            sb=new StringBuilder();
        }
    };

    
    private boolean foundWhitespace;
    private String Data;
    private int Size;
    private int Cursor;
    private char c;
    
    private static final String[] PREPROCESSOR = new String[] {IMPORT.Data};
    private static final String[] CODE_TYPES = new String[] {
        FRAGMENT.Data, EVENT.Data, FUNCTION.Data
    };
    private static final String[] VAR_TYPES = new String[] {
        TYPE_VOID.Data, 
        TYPE_TOKEN.Data, 
        TYPE_INTEGER.Data, 
        TYPE_FLOAT.Data,
        TYPE_STRING.Data,
        TYPE_OBJECT.Data
    };
    private static final String[] SPECIAL_VAR = new String[] {
        REFERENCE_NULL.Data,
        REFERENCE_THIS.Data
    };
    private static final String[] FLOW_CONTROL = new String[] {
        IF.Data,
        ELSE.Data,
        ELSE_IF.Data,
        FOR.Data,
        WHILE.Data
    };
    
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
     * this function is used only for test purposes
     * 
     * @deprecated
     * @param source
     * @param provider
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public JPseudoProgram parse(Reader source, VMProviderInterface provider) throws ParseException, IOException {
        JJALPHIHelper helper = new JJALPHIHelper();
        List<JLexerToken> tokenized = JLexer.tokenize(source, helper);
        JPseudoProgram program=parse(tokenized.iterator(), provider);
        return program;
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
    public JLexerToken consumeWhitespaces(Iterator<JLexerToken> tokens) {
        foundWhitespace = false;
        JLexerToken token = tokens.next();
        while(token.Type == ELexerTokenType.WHITESPACE) {
            foundWhitespace = true;
            if(!tokens.hasNext()) return null;
            token = tokens.next();
        }
        return token;
    }
    
    private void throwParseException(String message) throws ParseException {
        throw new ParseException(message, -1);
    }

    private void throwParseException(String message, JLexerToken Token) throws ParseException {
        throw new ParseException(String.format(message,new Object[] { Token.Type.name(), Token.Data }), -1);
    }
    
    private JPseudoInstruction parseParentesis(Iterator<JLexerToken> tokens, JLexerToken CloseToken) throws ParseException {
        JLexerToken token = consumeWhitespaces(tokens);
        throwEndProgramExceptionIfNull(token, " while parsing parenthesis.");
        while(token!=CloseToken) {
        
            token = consumeWhitespaces(tokens);
            throwEndProgramExceptionIfNull(token, " while parsing parenthesis.");
        }
        return null;
    }

    private JPseudoInstruction parseExpression(Iterator<JLexerToken> tokens) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private List<JPseudoInstruction> parseInstructionsBlock(Iterator<JLexerToken> tokens, VMProviderInterface provider) throws ParseException {
        if(!tokens.hasNext())
            return null;
        JLexerToken token;
        List<JPseudoInstruction> block = new LinkedList<>();
        JPseudoInstruction instruction = null;
        JPseudoInstruction baseInstruction = null;
        while((token = consumeWhitespaces(tokens))!=BLOCK_END) {
            throwEndProgramExceptionIfNull(token, "Unexpected end of program");
            switch(token.Type) {
                case COMMENT_MULTILINE: // Comments are treated as non existing
                case COMMENT_SINGLELINE: 
                    break;
                case ASSIGN:
                    if(baseInstruction==null) throwParseException("Unexpected <%s> '%s': assignation must start with a target variable ", token);
                    baseInstruction = new JPseudoInstruction(E_ACTION.ASSIGN, baseInstruction, parseExpression(tokens));
                    break;
                case NUMBER:
                    Object o;
                    try { 
                        o=Long.parseLong(token.Data);
                    } catch (Exception e) {
                    }
                    instruction = new JPseudoInstruction(E_ACTION.GET_CONSTANT, token.Data);
                    break;
                case BINARYOP:
                    break;
                case WHITESPACE:
                    break;
                case UNEXPECTED:
                    break;
                case ENDCOMMAND:
                    if(token==END_INSTRUCTION) {
                        block.add(baseInstruction);
                    }
                    break;
                case UNARYOP:
                    break;
                case CODE_BLOCK:
                    break;
                case SEPARATOR:
                    break;
                case STRING:
                    break;
                case IDENTIFIER:
                    break;
                case COMPARATOR:
                    break;
                case KEYWORD:
                    break;
                case TOKEN:
                    break;
                default:
                    throw new AssertionError(token.Type.name());
            }
        }
        return block;
    }

    private void throwEndProgramExceptionIfNull(JLexerToken token, String qualifier) throws ParseException {
        if(token==null) {
            if(qualifier!=null)  {
                throw new ParseException("Unexpecetd end of program: "+qualifier, -1);                
            } else {
            }
        }
    }
    
    private List<JPseudoVariable> parseArguments(Iterator<JLexerToken> tokens)  throws ParseException {
        List<JPseudoVariable> result = new ArrayList<>();
        JLexerToken token = null;
        boolean working = true; 
        while(token != PAR_CLOSE) {
            token = consumeWhitespaces(tokens);
            throwEndProgramExceptionIfNull(token, " while parsing fragment arguments");
            if(token.Type == ELexerTokenType.IDENTIFIER) {
                boolean isArray=false;
                String identifier = token.Data;
                E_VAR_TYPE type = null;
                String subType=null;
                token = consumeWhitespaces(tokens);
                throwEndProgramExceptionIfNull(token, " while parsing fragment arguments");
                if(token == DECLARE) {
                    token = consumeWhitespaces(tokens);
                    throwEndProgramExceptionIfNull(token, " while parsing fragment arguments");
                    if(token==TYPE_INTEGER) {
                        type = E_VAR_TYPE.INTEGER;
                    } else if (token == TYPE_FLOAT) {
                        type = E_VAR_TYPE.FLOAT;
                    } else if (token == TYPE_STRING) {
                        type = E_VAR_TYPE.STRING;
                    } else if (token == TYPE_TOKEN) {
                        type = E_VAR_TYPE.TOKEN;
                    } else if (token == TYPE_VOID) {
                        type = E_VAR_TYPE.VOID;
                    } else if (token == TYPE_OBJECT) {
                        type = E_VAR_TYPE.OBJ;
                    } else throwParseException("Expected type, found <%s> %s.", token);
                    token = consumeWhitespaces(tokens);
                    throwEndProgramExceptionIfNull(token, " while parsing fragment arguments");
                    if(token==DOT) {
                        if(foundWhitespace)
                            throwParseException("Unexpected dot punctation '.' after a space");
                        if(type == E_VAR_TYPE.OBJ || type == E_VAR_TYPE.TOKEN) {
                            if(!tokens.hasNext()) 
                                throwParseException("Unexpected end of file while parsing fragment arguments");
                            token = tokens.next();
                            if(token.Type == ELexerTokenType.IDENTIFIER) {
                                subType = token.Data;
                                token = consumeWhitespaces(tokens);
                                throwEndProgramExceptionIfNull(token, " while parsing fragment arguments");
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
                    if(token==AS_ARRAY) {
                        isArray=true;
                        token = consumeWhitespaces(tokens);
                        throwEndProgramExceptionIfNull(token, " while parsing fragment arguments");
                    }
                    if((token==SEPARATE || token == PAR_CLOSE)) {
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
    
    private JPseudoProgram parseProgramIntestation(Iterator<JLexerToken> tokens, VMProviderInterface Provider) throws ParseException {
        if(!tokens.hasNext())
            return null;
        JPseudoProgram program = new JPseudoProgram();
        JLexerToken token = consumeWhitespaces(tokens);
        if(token == null)
            return null;
        if(token.Type==ELexerTokenType.KEYWORD) {
            if(token==PROGRAM) {
                program.setType(E_CODE_TYPE.PROGRAM);
            } else if(token==EVENT) {
                program.setType(E_CODE_TYPE.EVENT);
                program.addArgument(JPseudoVariable.THIS);
            } else if(token==FRAGMENT) {
                program.setType(E_CODE_TYPE.FRAGMENT);
                program.addArgument(JPseudoVariable.THIS);
            } else if(token==FUNCTION) {
                program.setType(E_CODE_TYPE.FUNCTION);
            } else
                throwParseException("Invalid "+token.Type.name()+"<"+token.Data+">, expected Fragment|Event keyword");
        } else throwParseException("Invalid "+token.Type.name()+"<"+token.Data+">, expected Fragment|Event <KEYWORD>");
        token = consumeWhitespaces(tokens);
        throwEndProgramExceptionIfNull(token," missing name!");
        if(token.Type==ELexerTokenType.IDENTIFIER) {
            program.setName(token.Data);
        } else {
            throwParseException("Invalid "+token.Type.name()+"<"+token.Data+">, expected <IDENTIFIER>");
        }
        token = consumeWhitespaces(tokens);
        throwEndProgramExceptionIfNull(token, " before parameters list");
        if(token == PAR_OPEN) {
            parseArguments(tokens).forEach(argument -> {
                program.addArgument(argument);
            });
        } else {
            throwParseException("Expected '(', found <"+token.Type.name()+"> "+token.Data);
        }
        token = consumeWhitespaces(tokens);
        throwEndProgramExceptionIfNull(token, " before parameters list");
        if (token == DECLARE) {
            token = consumeWhitespaces(tokens);
            throwEndProgramExceptionIfNull(token, " before parameters list");
            String subType=null;
            boolean isArray=false;
            E_VAR_TYPE type=null;
            if(token==TYPE_INTEGER) {
                type = E_VAR_TYPE.INTEGER;
            } else if (token == TYPE_FLOAT) {
                type = E_VAR_TYPE.FLOAT;
            } else if (token == TYPE_STRING) {
                type = E_VAR_TYPE.STRING;
            } else if (token == TYPE_TOKEN) {
                type = E_VAR_TYPE.TOKEN;
            } else if (token == TYPE_VOID) {
                type = E_VAR_TYPE.VOID;
            } else if (token == TYPE_OBJECT) {
                type = E_VAR_TYPE.OBJ;
            } else throwParseException("Expected type, found <%s> %s.", token);
            token = consumeWhitespaces(tokens);
            throwEndProgramExceptionIfNull(token, " while looking for return type");
            if(token==DOT) {
                if(foundWhitespace) {
                    throwParseException("Found space before separator dot . ");
                } else {
                    token = consumeWhitespaces(tokens);
                    throwEndProgramExceptionIfNull(token, " while looking for return type");
                    if(token.Type==ELexerTokenType.IDENTIFIER) {
                    } else throwParseException("", token);
                }
            }
            if(token==AS_ARRAY) {
                if(program.getType()==E_CODE_TYPE.FUNCTION) {
                    isArray=true;
                } else throw new ParseException(program.getType().name()+" can't return an array type.",-1);
                token = consumeWhitespaces(tokens);
                throwEndProgramExceptionIfNull(token, " while looking for return type");
            }
            program.setReturnType(JPseudoType.createType(type, subType, isArray));
        }
        if(token ==BLOCK_BEGIN)
            return program;
        else { 
            throwParseException("Unexpected token <%s> %s at the end of framgnet intestation.", token);
            return null;
        }
    }
    
    
    @Override
    public JPseudoProgram parse(Iterator<JLexerToken> tokens, VMProviderInterface provider) throws ParseException {
        JPseudoProgram program;
        program = parseProgramIntestation(tokens, provider);
        program.addInstructions(parseInstructionsBlock(tokens, provider));
        JLexerToken token = consumeWhitespaces(tokens);
        if(token!=null)
            throwParseException("Unexpected <%s> %s after the end of the program!", token);
        return program;
    }

    private enum E_PARSE_STATE {
        SEARCH_INSTRUCTION, EXPECT_FUNCTION, PARSE_TARGET, PARSE_SOURCE;
    }
    
}
