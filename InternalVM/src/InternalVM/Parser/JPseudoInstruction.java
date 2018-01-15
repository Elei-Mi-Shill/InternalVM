/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM.Parser;

import JScriptParser.E_ACTION;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author luca.scarcia
 */
public class JPseudoInstruction {
    
    /**
     * NOOP (No Operation) is the pseudofunction for an empty line.
     */
    public static final JPseudoInstruction GET_THIS = new JPseudoInstruction(E_ACTION.GET_THIS);
    public static final JPseudoInstruction GET_NULL = new JPseudoInstruction(E_ACTION.GET_NULL);
    public static final JPseudoInstruction RETURN = new JPseudoInstruction(E_ACTION.EXIT_BLOCK);
    public static final JPseudoInstruction COMMENT = new JPseudoInstruction(E_ACTION.NOOP);
    public static final JPseudoInstruction NOOP = new JPseudoInstruction(E_ACTION.NOOP);
    public static final JPseudoInstruction BLOCK_END = new JPseudoInstruction(E_ACTION.CLEAN_BLOCK);

    public JPseudoVariable Value;
    public E_ACTION Action;
    public List<JPseudoInstruction> Parameters;
    
    JPseudoInstruction(E_ACTION action) {
        Action = action;
        Value = null;
        Parameters = new ArrayList<>();
    }    
    
    public JPseudoInstruction(E_ACTION action, String value) {
        Value = new JPseudoVariable();
        Action = action;
        Parameters = new ArrayList<>();
        Value.Name = value;
        Value.Type = JPseudoType.VOID;
    }    

    public JPseudoInstruction(E_ACTION action, String value, JPseudoType type) {
        Value = new JPseudoVariable();
        Action = action;
        Parameters = new ArrayList<>();
        Value.Name = value;
        Value.Type = type;
    }    

    public JPseudoInstruction(E_ACTION action, JPseudoInstruction param1) {
        Action = action;
        Parameters = new ArrayList<>();
        Value = null;
        Parameters.add(param1);
    }    

    public JPseudoInstruction(E_ACTION action, JPseudoInstruction param1, JPseudoInstruction param2) {
        Action = action;
        Parameters = new ArrayList<>();
        Value = null;
        Parameters.add(param1);
        Parameters.add(param2);
    }    

    public JPseudoInstruction(E_ACTION action, JPseudoInstruction[] paramList) {
        Action = action;
        Parameters = new ArrayList<>();
        Value = null;
        if(paramList!=null) {
            Parameters.addAll(Arrays.asList(paramList));
        }
    }    

    public JPseudoInstruction(E_ACTION action, List<JPseudoInstruction> paramList) {
        Action = action;
        Parameters = new ArrayList<>();
        Value = null;
        if(paramList!=null) {
            Parameters.addAll(paramList);
        }
    }    
    
    public JPseudoInstruction(E_ACTION action, Iterator<JPseudoInstruction> paramList) {
        Action = action;
        Parameters = new ArrayList<>();
        Value = null;
        if(paramList!=null) {
            paramList.forEachRemaining(param -> {
                Parameters.add(param);
            });
        }
    }    
    
    void add(JPseudoInstruction param) {
        Parameters.add(param);
    }
    
    void append(StringBuilder sb) {
        sb.append("[");
        sb.append(Action);
        sb.append("]");
        sb.append("(");
        Parameters.forEach(param -> {
            sb.append(param.toString());
        });
        sb.append(")");
        sb.append(System.lineSeparator());
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }

    public boolean isComplete() throws ParseException {
        switch(Action) {
            case UNDEFINED:
                throw new ParseException("Undefined instruction",-1);
            case NOOP:
                return Parameters.isEmpty() && Value==null;
            case EXIT_BLOCK:
                return Parameters.isEmpty() && Value==null;
            case GET_PROPERTY:
                return Parameters.size() == 2 && (Value==null);
            case GET_PROPERTY_ARRAY:
                return Parameters.size()==3 && (Value==null);
            case CALL_METHOD:
                return Parameters.size()>=2 && (Value==null);
            case ASSIGN:
                return Parameters.size()==2 && (Value==null);
            case COMPARE:
                return Parameters.size()==2 && (Value==null);
            case MATH_SUM:
                return Parameters.size()==2 && (Value==null);
            case MATH_SUBTRACT:
                return Parameters.size()==2 && (Value==null);
            case MATH_MULTIPLY:
                return Parameters.size()==2 && (Value==null);
            case MATH_DIVIDE:
                return Parameters.size()==2 && (Value==null);
            case LOGIC_AND:
                return Parameters.size()==2 && (Value==null);
            case LOGIC_OR:
                return Parameters.size()==2 && (Value==null);
            case BOOL_AND:
                return Parameters.size()==2 && (Value==null);
            case LOGIC_NOT:
                return Parameters.size()==1 && (Value==null);
            case MATH_MODULE:
                return Parameters.size()==2 && (Value==null);
            case SYS_DECLARE:
                return Parameters.size()==2 && (Value==null);
            case EXECUTE_BLOCK:
                return true;
            case CLEAN_BLOCK:
                return true;
            case GET_NULL:
                return Parameters.isEmpty() && (Value==null);
            case GET_THIS:
                return Parameters.isEmpty() && Value==null;
            case GET_VALUE:
                return Parameters.size()==1 && Value==null;
            case GET_VARIABLE:
                return Parameters.isEmpty() && Value!=null;
            case GET_CONSTANT_TOKEN:
                return Parameters.isEmpty() && (Value!=null);
            case GET_CONSTANT_STRING:
                return Parameters.isEmpty() && (Value!=null);
            case GET_CONSTANT_INTEGER:
                return Parameters.isEmpty() && (Value!=null);
            case GET_CONSTANT_FLOAT:
                return Parameters.isEmpty() && (Value!=null);
            case GET_OBJECT:
                return Parameters.isEmpty() && (Value!=null);
            default:
                throw new AssertionError(Action.name());
        }
    }

}
