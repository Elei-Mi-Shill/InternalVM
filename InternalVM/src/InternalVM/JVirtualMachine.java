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
public class JVirtualMachine {
    /** 
     * object that invoked the VM. It's responsible to provide variables and
     * functions
     */
    VMProviderInterface Provider;
    JRegisters Registers;
    /** 
     * if specified, all call functions will be provided by this element;
     * if not, the calls will be responded by the standard Provider
     */
    IScriptableElement Context;
    
    JVirtualMachine(VMProviderInterface provider) {
        Provider = provider;
    }
    
    public boolean Excecute (VMProviderInterface Provider, JAssembly codeFragment) {
        return false;
    }
}
