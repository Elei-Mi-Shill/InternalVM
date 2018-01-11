/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JScriptParser;

/**
 *
 * @author luca.scarcia
 */
public class JInstruction {
    public final E_INSTRUCTION Instruction;
    public final float Param1;
    public final float Param2;
    
    
    public JInstruction(E_INSTRUCTION inst, float param1, float param2) {
        Instruction = inst;
        Param1 = param1;
        Param2 = param2;
    }
    
}
