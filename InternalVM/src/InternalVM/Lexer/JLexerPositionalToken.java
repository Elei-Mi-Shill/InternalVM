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
public class JLexerPositionalToken {
    public final JLexerToken Token;
    public final JDocumentPosition Pos;

    public JLexerPositionalToken(JLexerToken token, int total, int row, int col) {
        Token = token;
        Pos = new JDocumentPosition(total, row, col);
    }

    public JLexerPositionalToken(JLexerToken token, JDocumentPosition pos) {
        Token = token;
        Pos = new JDocumentPosition(pos.Absolute, pos.Row, pos.Col);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Pos.Absolute);
        sb.append("(");
        sb.append(Pos.Row);
        sb.append(",");
        sb.append(Pos.Col);
        sb.append(Token.toString());
        return sb.toString();
        
    }
    
    
}
