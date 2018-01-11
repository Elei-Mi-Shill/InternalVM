/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InternalVM.Lexer;

/**
 *
 * @author luca.scarcia
 */
class JTextPos {
    public int Line = 0;
    public int Row = 0;
    public int Total = 0; 
    JLexer.E_TOKENIZER_STATE State = JLexer.E_TOKENIZER_STATE.DEFAULT;
}
