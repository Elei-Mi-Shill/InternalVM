/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM.Parser;

import JScriptParser.E_VAR_TYPE;
import java.text.ParseException;

/**
 *
 * @author luca.scarcia
 */
public class JPseudoType {
    public final String SubType;
    public final E_VAR_TYPE Type;
    public final boolean IsArray;
    
    public static final JPseudoType VOID = new JPseudoType(E_VAR_TYPE.VOID, false);
    public static final JPseudoType INTEGER = new JPseudoType(E_VAR_TYPE.INTEGER, false);
    public static final JPseudoType FLOAT = new JPseudoType(E_VAR_TYPE.FLOAT, false);
    public static final JPseudoType STRING = new JPseudoType(E_VAR_TYPE.STRING, false);
    public static final JPseudoType TOKEN = new JPseudoType(E_VAR_TYPE.TOKEN, false);
    public static final JPseudoType CANONIC_OBJECT = new JPseudoType(E_VAR_TYPE.OBJ, false);
    public static final JPseudoType ARRAY_INTEGER = new JPseudoType(E_VAR_TYPE.INTEGER, true);
    public static final JPseudoType ARRAY_FLOAT = new JPseudoType(E_VAR_TYPE.FLOAT, true);
    public static final JPseudoType ARRAY_STRING = new JPseudoType(E_VAR_TYPE.STRING, true);
    public static final JPseudoType ARRAY_TOKEN = new JPseudoType(E_VAR_TYPE.TOKEN, true);
    public static final JPseudoType ARRAY_CANONIC_OBJECT = new JPseudoType(E_VAR_TYPE.OBJ, true);

    /**
     *
     * @param type
     * @param isArray
     * @return
     */
    public static JPseudoType createType(E_VAR_TYPE type, boolean isArray) {
        switch(type) {
            case INTEGER:
                if(isArray) return ARRAY_INTEGER; else return INTEGER;
            case FLOAT:
                if(isArray) return ARRAY_FLOAT; else return FLOAT;
            case STRING:
                if(isArray) return ARRAY_STRING; else return STRING;
            case OBJ:
                if(isArray) return ARRAY_CANONIC_OBJECT; else return CANONIC_OBJECT;
            case TOKEN:
                if(isArray) return ARRAY_TOKEN; else return TOKEN;
            case VOID:
                if(isArray) throw new IllegalArgumentException("You cannot declare an array of void"); else return VOID;
            default:
                throw new AssertionError(type.name());
            
        }
    }
    
    public static JPseudoType createType(E_VAR_TYPE type, String subType, boolean isArray) throws ParseException {
        if(subType==null || "".equals(subType))
            return createType(type, isArray);
        else if (type==E_VAR_TYPE.OBJ || type == E_VAR_TYPE.TOKEN) {
            return new JPseudoType(type, subType, isArray);
        } else throw new ParseException("Only Object and TOKEN types can have subtype",-1);
    }
    
    public JPseudoType(E_VAR_TYPE type) {
        Type = type;
        SubType = null;
        IsArray = false;
    }
    
    public JPseudoType(E_VAR_TYPE type, boolean isArray) {
        Type = type;
        SubType = null;
        IsArray = isArray;
    }
    
    public JPseudoType(E_VAR_TYPE type, String subType, boolean isArray) {
        if(type == E_VAR_TYPE.OBJ || type == E_VAR_TYPE.TOKEN) {
            Type = type;
            SubType = subType;
            IsArray = isArray;
        } else if(subType==null || "".equals(subType)) {
            Type = type;
            SubType = null;
            IsArray = isArray;
        } else
            throw new IllegalArgumentException("Only Object and Token can have a subtype");
    }

    void append(StringBuilder sb) {
        sb.append(Type);
        if(!(SubType==null || "".equals(SubType))) {
            sb.append(".");
            sb.append(SubType);
        }
        if(IsArray)
            sb.append("[]");
    }
    
}
