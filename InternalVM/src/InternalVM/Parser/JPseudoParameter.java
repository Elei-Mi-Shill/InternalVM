/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author luca.scarcia
 */
class JPseudoParameter {
    String ParamID; // used for actual function parameters, usually is null
    Object Value;
    E_PARAM_TYPE Type;
    
    public static JPseudoParameter NULL = new JPseudoParameter(E_PARAM_TYPE.NULL, "null", null);
    public static JPseudoParameter THIS = new JPseudoParameter(E_PARAM_TYPE.THIS, "this", null);
    
    static JPseudoParameter createConstantParameter(boolean isString, String value) {
        if(isString)
            return new JPseudoParameter(E_PARAM_TYPE.CONSTANT_STRING, null, value);
        else
            return new JPseudoParameter(E_PARAM_TYPE.CONSTANT_NUMBER, null, value);
    }

    JPseudoParameter(E_PARAM_TYPE type, String id, String value) {
        Type = type;
        Value = value;
        ParamID = id;
    }

    JPseudoParameter(String id, List<JPseudoInstruction> fragment) {
        Type = E_PARAM_TYPE.CODE_FRAGMENT;
        ParamID = id;
        Value = new ArrayList<>(fragment);
    }
    
    JPseudoParameter(String id, JPseudoInstruction[] fragment) {
        Type = E_PARAM_TYPE.CODE_FRAGMENT;
        ParamID = id;
        List<JPseudoInstruction> subCode = new ArrayList<>(Arrays.asList(fragment));
        Value = subCode;
    }
    
    JPseudoParameter(String id, JPseudoInstruction Instruction) {
        Type = E_PARAM_TYPE.CODE_FRAGMENT;
        ParamID = id;
        Value = Instruction;
    }

    JPseudoParameter(String id, JPseudoType type) {
        ParamID = id;
        Type = E_PARAM_TYPE.TYPE;
        Value = type;
    }
    
    String getValueAsString() {
        if(Value instanceof String)
            return (String)Value;
        else 
            return null;
    }

    JPseudoType getValueAsType() {
        if(Value instanceof JPseudoType)
            return (JPseudoType)Value;
        else 
            return null;
    }

    JPseudoInstruction getValueAsInstruction() {
        if(Value instanceof JPseudoInstruction)
            return (JPseudoInstruction)Value;
        else 
            return null;
    }

    List<JPseudoInstruction> getValueAsSubCode() {
        if(Value instanceof List)
            return (List<JPseudoInstruction>)Value;
        else 
            return null;
    }

    void append(StringBuilder sb) {
        if(ParamID!=null) {
            sb.append(ParamID);
            sb.append(":");
        }
        switch(Type) {
            case CONSTANT_STRING:
                sb.append("\"");
                sb.append((String)Value);
                sb.append("\"");
                break;
            case IDENTIFIER:
            case CONSTANT_NUMBER:
                sb.append((String)Value);
                break;
            case PROPERTY:
            case VARIABLE:
                sb.append(Value);
                break;
            case CODE_FRAGMENT:
                sb.append(Value);
                sb.append("{");
                List<JPseudoInstruction> subCode = getValueAsSubCode();
                if(subCode!=null) {
                    subCode.forEach(instruction -> {
                        instruction.append(sb);
                    });
                }
                sb.append("}");
                break;
            case INSTRUCTION:
                sb.append("{");
                JPseudoInstruction instruction = getValueAsInstruction();
                if(instruction!=null) {
                    instruction.append(sb);
                }
                sb.append("}");
                break;
            case NULL:
                sb.append("null");
                break;
            case THIS:
                sb.append("this");
                break;
            case TYPE:
                JPseudoType type = getValueAsType();
                type.append(sb);
                break;
            default:
                throw new AssertionError(Type.name());
        }
    }

    public enum E_PARAM_TYPE {
        CONSTANT_STRING,
        CONSTANT_NUMBER,
        PROPERTY,
        INSTRUCTION,
        CODE_FRAGMENT, 
        NULL, // NULL is a CONSTANT type
        THIS, // this is a VARIABLE type; you need to get the value
        VARIABLE, 
        IDENTIFIER, 
        TYPE;
    }
    
}
