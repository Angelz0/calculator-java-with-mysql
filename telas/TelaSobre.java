package br.com.pokecalculator.telas;

import br.com.pokecalculator.conexao.Conexao;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.accessibility.AccessibleContext;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
/**
 *
 * @author angel
 */
public class TelaSobre extends JFrame {
    
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public TelaSobre() {
      
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setTitle("About");
        setSize(280, 220);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(new ImageIcon(getClass().getResource("/br/com/pokecalculator/icones/logobulba.png")).getImage());
        conexao = Conexao.conector();
        componentesDaTela();
    }

    public void setAccessibleContext(AccessibleContext accessibleContext) {
        this.accessibleContext = accessibleContext;
    }
    
    /* public static void main(String[] args) {
        TelaSobre sobre = new TelaSobre();
        sobre.setVisible(true);
    } */
    
    private void componentesDaTela() {
        
        description1 = new JLabel("Effort Values Calculator ");
        description1.setBounds(78, 60, 190, 15);
        description1.setFont(new Font("Arial", 0, 11));
        add(description1);
        
        description2 = new JLabel("to help you in RPG Pokémon games.");
        description2.setBounds(53, 80, 190, 15);
        description2.setFont(new Font("Arial", 0, 11));
        add(description2);
        
        developer = new JLabel("Developed by Angelz");
        developer.setBounds(83, 115, 190, 15);
        developer.setFont(new Font("Arial", 0, 11));
        add(developer);
    }
    
    private JLabel description1;
    private JLabel description2;
    private JLabel developer;
}
