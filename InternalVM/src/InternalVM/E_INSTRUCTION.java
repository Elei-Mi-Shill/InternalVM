/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM;

/**
 *
 * @author luca.scarcia
 */
public enum E_INSTRUCTION {
    End         (0x00, "END","Forces end execution; END reason is specified on the register"),
    AllocateVar (0x01, "ALLOC", "Allocates a variable in the local VM memory"),
    MoveVarReg  (0x02, "MOVRV", "Moves the contents of the variable to a register, if compatible"),
    MoveRegVar  (0x03, "MOVVR", "Moves the contents of the reg to the variable if compatible"),
    MoveRegReg  (0x04, "MOVRR", "Moves the contents of the first register to the second register"),
    Increment   (0x05, "INC", "Increments the value of the register by a constant"),
    Decrement   (0x06, "DEC", "Decrements the value of the register by a constant"),
    Add         (0x07, "ADDC", "Adds the value of the constant to the register"),
    AddReg      (0x08, "ADDR", "Adds the value of the second register to the first"),
    IncrementReg(0x09, "INCR", "Increments the value of the register by the contents of the second register (sum)"),
    GetObject   (0x00, "GET", "Gets the object specified in the constant and puts it in the specified register"),
    Call        (0x00, "CALL", "Calls a function from the standard Provider"),
    ContextCall (0x00, "CALLC", "Calls a function from the current context"),
    SetContest  (0x02, "SETCO", "Sets the active contest");
    
    public final int OpCode;
    public final String Mnemonic;
    public final String Description;
    
    E_INSTRUCTION(int id, String mnemonic, String desc) {
        OpCode = id;
        Mnemonic = mnemonic;
        Description = desc;
    }
    
}
