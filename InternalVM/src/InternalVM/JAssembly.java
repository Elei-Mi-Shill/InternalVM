/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JScriptParser;

import java.util.List;

/**
 *
 * @author luca.scarcia
 */
public class JAssembly {
    public final JRegisters Regs = new JRegisters();
    protected final JInstruction Code[];
    protected final JVariable Variables[];
    
    JAssembly(List<JInstruction> code, List<JVariable> variables) {
        Variables = new JVariable[variables.size()];
        int i=0;
        for (JVariable var : variables) {
            Variables[i]=var;
            i++;
        }
        
        i=0;
        Code = new JInstruction[code.size()];
        for (JInstruction inst : code) {
            Code[i]=inst;
            i++;
        }
        
    }

    public JInstruction get(int line) {
        if(line>=0 && line<Code.length) {
            return Code[line];
        } else
            return null;
    };
}
