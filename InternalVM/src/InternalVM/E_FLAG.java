/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JScriptParser;

import java.util.EnumSet;

/**
 *
 * @author luca.scarcia
 */
enum E_FLAG {
    ZERO(1<<0),
    PARITY(1<<1),
    ADJUST(1<<2),
    SIGN(1<<3),
    TRAP(1<<4),
    INTERRUPT(1<<5),
    DIRECTION(1<<6),
    OVERFLOW(1<<7)
    ;
    
    final int ID;
    
    public static EnumSet<E_FLAG> toFlags(int flags) {
        EnumSet<E_FLAG> result = EnumSet.noneOf(E_FLAG.class);
        for(E_FLAG flag:E_FLAG.values()) {
            if((flag.ID & flags) == flag.ID)
                result.add(flag);
        }
        return result;    
    }
            
    public static int toInt(EnumSet<E_FLAG>flags) {
        int result=0;
        for(E_FLAG flag:flags) {
            result+=flag.ID;
        }
        return result;
    }
    
    E_FLAG(int id) {
        ID = id;
    }
}
