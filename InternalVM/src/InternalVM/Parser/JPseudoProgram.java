/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM.Parser;

import InternalVM.E_CODE_TYPE;
import InternalVM.E_VAR_TYPE;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luca.scarcia
 */
public class JPseudoProgram {
    private E_CODE_TYPE Type = E_CODE_TYPE.INVALID;
    String Name;
    List<JPseudoVariable> Arguments=new ArrayList<>();
    List<JPseudoVariable> Variables=new ArrayList<>();
    List<JPseudoInstruction> Instructions=new ArrayList<>();
    JPseudoType ReturnType = null;
    
    
    public void setReturnType(JPseudoType type) throws ParseException {
        if(Type!=E_CODE_TYPE.FUNCTION)
            if(type!=JPseudoType.INTEGER)
                throw new ParseException("Programs, Fragments and Events can only return base Integer values", -1);
        ReturnType = type;
    }
    
    public boolean addInstructions(List<JPseudoInstruction> instructions) {
        return Instructions.addAll(instructions);
    }
    
    public void setReturnType(E_VAR_TYPE aType, String subType, boolean isArray) throws ParseException {
        if(subType==null)
            ReturnType = new JPseudoType(aType, isArray);
        else
            ReturnType = new JPseudoType(aType, subType, isArray);
    }
    
    public void setName(String name) {
        Name = name;
    }
    
    @Override
    public String toString() {
        boolean first = true;
        StringBuilder sb = new StringBuilder(2048);
        sb.append(Type);
        sb.append(" ");
        sb.append(Name);
        sb.append("(");
        for(JPseudoVariable argument:Arguments) {
            if(first) first = false; else sb.append(", ");
            argument.append(sb);
        }
        sb.append(")");
        if (ReturnType!=null) {
            sb.append(":");
            ReturnType.append(sb);
        }
        sb.append(" {");
        sb.append(System.lineSeparator());
        Instructions.forEach(instruction ->  {
            instruction.append(sb);
            sb.append(System.lineSeparator());
        });
        sb.append("}");
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    public void setType(E_CODE_TYPE type) {
        Type = type;
    }
    
    public E_CODE_TYPE getType() {
        return Type;
    }

    public void addArgument(JPseudoVariable THIS) {
        Arguments.add(THIS);
    }

    public void addLibraryImport(String libraryName) {
        if(!"System".equals(libraryName)) { // System library is implied, no need for explicit declaration
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
