/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM;

import java.util.EnumSet;
import java.util.Stack;

/**
 *
 * @author luca.scarcia
 */
public class JRegisters {
    EnumSet<E_FLAG> Flags;
    
    E_VMSTATE State;
    
    Stack<JVariable> LocalStack;
    
    int IP;                     // instruction pointer
    JRegister Acc;              // AX
    JRegister Base;             // BX
    JRegister Counter;          // CX
    JRegister Data;             // DX
    double FAcc;                // AX
    double FBase;               // BX
    int DestinationIndex;       // DI
    int SourceIndex;            // SI
    int BasePointer;            // BP
    int StackPointer;           // SP;

    
}
