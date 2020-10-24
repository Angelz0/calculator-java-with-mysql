package br.com.pokecalculator.telas;

import br.com.pokecalculator.conexao.Conexao;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author angel
 */
public class TelaEquipe extends JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public TelaEquipe() {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setTitle("Team");
        setSize(500, 240);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        //getContentPane().setBackground(Color.WHITE);
        setIconImage(new ImageIcon(getClass().getResource("/br/com/pokecalculator/icones/logobulba.png")).getImage());

        conexao = Conexao.conector();
        componentesDaTela();
    }

    public static void main(String[] args) {
        TelaEquipe equipe = new TelaEquipe();
        equipe.setVisible(true);
    }

    public void trazerDados() {
        String sql = "select * from tbpokemon where login = ?";

        try {
            modelo = (DefaultTableModel) tabela.getModel();
            modelo.setNumRows(0);

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtTeamUser.getText());

            rs = pst.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[]{rs.getString("pokemon"), rs.getInt("hp"), rs.getInt("attack"), rs.getInt("defense"), rs.getInt("spatt"), rs.               getInt("spdef"), rs.getInt("speed")});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void componentesDaTela() {

        tabela = new JTable();
        dados = new Object[][]{};
        coluna = new String[]{"", "Hp", "Attack", "Defense", "Sp. Att.", "Sp. Def.", "Speed"};
        
        tabela.setModel(new DefaultTableModel(dados, coluna) {
            @Override
            public boolean isCellEditable(int dados, int coluna) {
                return false;
            } 
        });
        
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
    	center.setHorizontalAlignment(SwingConstants.CENTER);
        tabela.getColumnModel().getColumn(0).setCellRenderer(center);
        tabela.getColumnModel().getColumn(1).setCellRenderer(center);
        tabela.getColumnModel().getColumn(2).setCellRenderer(center);
        tabela.getColumnModel().getColumn(3).setCellRenderer(center);
        tabela.getColumnModel().getColumn(4).setCellRenderer(center);
        tabela.getColumnModel().getColumn(5).setCellRenderer(center);
        tabela.getColumnModel().getColumn(6).setCellRenderer(center);
    	
        
        tabela.getTableHeader().setResizingAllowed(false);
        tabela.getTableHeader().setReorderingAllowed (false);
        tabela.setShowGrid(false);
        tabela.setForeground(Color.GRAY);
        //tabela.setPreferredScrollableViewportSize(new Dimension(385, 50));
        //tabela.setFillsViewportHeight(true);
        
        scroll = new JScrollPane(tabela);
        scroll.setBounds(0, 0, 500, 120);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll);
        
        txtTeamUser = new JTextField();
        txtTeamUser.setBounds(100, 160, 80, 28);
        add(txtTeamUser);
        txtTeamUser.setVisible(false);
        
        equipe = new JButton("  Team");
        equipe.setBounds(195, 160, 95, 30);
        equipe.setFont(new Font("Arial", 1, 11));
        equipe.setToolTipText("View Team");
        equipe.setIcon(new ImageIcon(getClass().getResource("/br/com/pokecalculator/icones/poke.png")));
        equipe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                equipeActionPerformed(evt);
            }
        });
        add(equipe);
    }
    
    private void equipeActionPerformed(ActionEvent evt) {
        trazerDados();
    }
    
    private JTable tabela;
    private JScrollPane scroll;
    private DefaultTableModel modelo;
    public JTextField txtTeamUser;
    private Object[][] dados;
    private String[] coluna;
    private JButton equipe;
}
