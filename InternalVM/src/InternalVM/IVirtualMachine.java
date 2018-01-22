package InternalVM;

import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author luca.scarcia
 */
public interface IVirtualMachine {

    public boolean execute(JAssembly assembly);
    public List<IScriptableElement> getRegisteredObjects();
    
}
