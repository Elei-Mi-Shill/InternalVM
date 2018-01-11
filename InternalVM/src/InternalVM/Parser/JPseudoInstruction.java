/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM.Parser;

import JScriptParser.E_ACTION;
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
    public static final JPseudoInstruction RETURN = new JPseudoInstruction(E_ACTION.EXIT_BLOCK);
    public static final JPseudoInstruction COMMENT = new JPseudoInstruction(E_ACTION.NOOP);
    public static final JPseudoInstruction NOOP = new JPseudoInstruction(E_ACTION.NOOP);
    public static final JPseudoInstruction BLOCK_END = new JPseudoInstruction(E_ACTION.CLEAN_BLOCK);

    public String Value;
    public E_ACTION Action;
    public List<Object> Parameters;
    
    JPseudoInstruction(E_ACTION action) {
        Action = action;
        Parameters = new ArrayList<>();
    }    
    
    public JPseudoInstruction(E_ACTION action, Object param1) {
        Action = action;
        Parameters = new ArrayList<>();
        Parameters.add(param1);
    }    

    public JPseudoInstruction(E_ACTION action, Object param1, Object param2) {
        Action = action;
        Parameters = new ArrayList<>();
        Parameters.add(param1);
        Parameters.add(param2);
    }    

    public JPseudoInstruction(E_ACTION action, Object[] paramList) {
        Action = action;
        Parameters = new ArrayList<>();
        if(paramList!=null) {
            Parameters.addAll(Arrays.asList(paramList));
        }
    }    

    public JPseudoInstruction(E_ACTION action, List<Object> paramList) {
        Action = action;
        Parameters = new ArrayList<>();
        if(paramList!=null) {
            Parameters.addAll(paramList);
        }
    }    
    
    public JPseudoInstruction(E_ACTION action, Iterator<Object> paramList) {
        Action = action;
        Parameters = new ArrayList<>();
        if(paramList!=null) {
            paramList.forEachRemaining(param -> {
                Parameters.add(param);
            });
        }
    }    
    
    void add(JPseudoParameter param) {
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
}
