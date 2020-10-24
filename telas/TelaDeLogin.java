package br.com.pokecalculator.telas;

import br.com.pokecalculator.conexao.Conexao;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author angel
 */
public class TelaDeLogin extends JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
     public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                TelaDeLogin login = new TelaDeLogin();
        }});
    }

    public TelaDeLogin() {
        
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setTitle("Enter the system");
        setSize(300, 220);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setIconImage(new ImageIcon(getClass().getResource("/br/com/pokecalculator/icones/logobulba.png")).getImage());
        
        conexao = Conexao.conector();
        componentesDaTela();
    }

    public void entrar() {
        String sql = "select * from tbtreinador where login = ? and senha = ?";
        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtUser.getText());
            pst.setString(2, txtPassword.getText());

            rs = pst.executeQuery();

            if (rs.next()) {
                TelaCalcular calcular = new TelaCalcular();
                calcular.txtCalcUsuario.setText(txtUser.getText());
                calcular.setVisible(true);
                
                this.dispose();
                conexao.close();

            } else {
                JOptionPane.showMessageDialog(null, "Nop! Username or password is invalid!", "Error", 1);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void componentesDaTela() {

        lblUser = new JLabel();
        txtUser = new JTextField();
        lblPassword = new JLabel();
        txtPassword = new JPasswordField();
        lblStatusText = new JLabel();
        btnEnter = new JButton();
        
        lblUser.setBounds(40, 40, 55, 15);
        lblUser.setFont(new Font("Calibri", 0, 12));
        lblUser.setText("User");
        add(lblUser);

        txtUser.setBounds(110, 35, 140, 30);
        add(txtUser);

        lblPassword.setBounds(40, 83, 55, 15);
        lblPassword.setFont(new Font("Calibri", 0, 12));
        lblPassword.setText("Password");
        add(lblPassword);

        txtPassword.setBounds(110, 75, 140, 30);
        add(txtPassword);

        lblStatusText.setBounds(35, 120, 140, 50);
        lblStatusText.setFont(new Font("Arial", 1, 11));

        if (conexao != null) {
            lblStatusText.setText("DB Connected");
            lblStatusText.setForeground(Color.green);
        } else {
            lblStatusText.setText("DB Disconnected");
            lblStatusText.setForeground(Color.RED);
        }
        add(lblStatusText);

        btnEnter.setBounds(175, 130, 90, 30);
        btnEnter.setFont(new Font("Arial", 1, 11));
        btnEnter.setText(" Enter");
        btnEnter.setIcon(new ImageIcon(getClass().getResource("/br/com/pokecalculator/icones/poke.png")));
        btnEnter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnterActionPerformed(evt);
            }
        });
        add(btnEnter);
    }
    
     private void btnEnterActionPerformed(java.awt.event.ActionEvent evt) {                                       
        entrar();
    } 
    
    private JLabel lblUser;
    private JTextField txtUser;
    private JLabel lblPassword;
    private JPasswordField txtPassword;
    private JLabel lblStatusText;
    private JButton btnEnter;
}
