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
public class JDocumentPosition {
    public final int Absolute;
    public final int Row;
    public final int Col;
    
    public JDocumentPosition(int absolute, int row, int col) {
        Row = row;
        Col = col;
        Absolute = absolute;
    }
}
