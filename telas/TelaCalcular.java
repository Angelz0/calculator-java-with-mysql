package br.com.pokecalculator.telas;

import javax.swing.JFrame;
import br.com.pokecalculator.conexao.Conexao;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.*;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import br.com.pokecalculator.telas.TelaEquipe;

/**
 *
 * @author angel
 */
public class TelaCalcular extends JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public TelaCalcular() {

        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setTitle("EV Calculator");
        setSize(390, 390);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(new ImageIcon(getClass().getResource("/br/com/pokecalculator/icones/logobulba.png")).getImage());

        conexao = Conexao.conector();
        componentesDaTela();
    }

    public void pesquisar() {
        String sql = "select * from tbpokemon where login = ? and pokemon = ?";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCalcUsuario.getText());
            pst.setString(2, txtPokemon.getText());

            rs = pst.executeQuery();

            if (rs.next()) {
                txtHp.setText(rs.getString(3));
                txtAttack.setText(rs.getString(4));
                txtDefense.setText(rs.getString(5));
                txtSpAtt.setText(rs.getString(6));
                txtSpDef.setText(rs.getString(7));
                txtSpeed.setText(rs.getString(8));
                barraProgresso.setValue(getSomaEv());

            } else {
                JOptionPane.showMessageDialog(null, "Pokémon not registered!");
                txtHp.setText(null);
                txtAttack.setText(null);
                txtDefense.setText(null);
                txtSpAtt.setText(null);
                txtSpDef.setText(null);
                txtSpeed.setText(null);
                barraProgresso.setValue(0);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void adicionar() {
        String sql = "insert into tbpokemon(login, pokemon, hp, attack, defense, spatt, spdef, speed) values(?, ?, ?, ?, ?, ?, ?, ?)";

        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCalcUsuario.getText());
            pst.setString(2, txtPokemon.getText());
            pst.setString(3, txtHp.getText());
            pst.setString(4, txtAttack.getText());
            pst.setString(5, txtDefense.getText());
            pst.setString(6, txtSpAtt.getText());
            pst.setString(7, txtSpDef.getText());
            pst.setString(8, txtSpeed.getText());
            /* barraProgresso.setValue(getSomaEv()); */

            if (txtPokemon.getText().isEmpty() || txtHp.getText().isEmpty() || txtAttack.getText().isEmpty()
                    || txtDefense.getText().isEmpty() || txtSpAtt.getText().isEmpty() || txtSpDef.getText().isEmpty() || txtSpeed.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Fill in all fields.");

            } else if (Integer.parseInt(txtHp.getText()) > 252 || Integer.parseInt(txtAttack.getText()) > 252 || Integer.parseInt(txtDefense.getText()) > 252 || Integer.parseInt(txtSpAtt.getText()) > 252 || Integer.parseInt(txtSpDef.getText()) > 252 || Integer.parseInt(txtSpeed.getText()) > 252 || getSomaEv() > 510) {
                JOptionPane.showMessageDialog(null, "Maximum ev per stat = 252. Maximum ev per Pokémon = 510.");
            } else {
                int adicionar = pst.executeUpdate();
                if (adicionar > 0) {
                    JOptionPane.showMessageDialog(null, "Pokémon added successfully");
                    txtPokemon.setText(null);
                    txtHp.setText(null);
                    txtAttack.setText(null);
                    txtDefense.setText(null);
                    txtSpAtt.setText(null);
                    txtSpDef.setText(null);
                    txtSpeed.setText(null);
                    barraProgresso.setValue(0);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void editar() {
        String sql = "update tbpokemon set hp=?, attack=?, defense=?, spatt=?, spdef=?, speed=? where pokemon=? and login=?";

        try {
            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtHp.getText());
            pst.setString(2, txtAttack.getText());
            pst.setString(3, txtDefense.getText());
            pst.setString(4, txtSpAtt.getText());
            pst.setString(5, txtSpDef.getText());
            pst.setString(6, txtSpeed.getText());
            pst.setString(7, txtPokemon.getText());
            pst.setString(8, txtCalcUsuario.getText());
            /* barraProgresso.setValue(getSomaEv()); */

            if (Integer.parseInt(txtHp.getText()) > 252 || Integer.parseInt(txtAttack.getText()) > 252 || Integer.parseInt(txtDefense.getText()) > 252 || Integer.parseInt(txtSpAtt.getText()) > 252 || Integer.parseInt(txtSpDef.getText()) > 252 || Integer.parseInt(txtSpeed.getText()) > 252 || getSomaEv() > 510) {
                JOptionPane.showMessageDialog(null, "Maximum ev per stat = 252. Maximum ev per Pokémon = 510.");
            } else {
                int alterar = pst.executeUpdate();
                if (alterar > 0) {
                    JOptionPane.showMessageDialog(null, "Successfully changed!");
                    txtPokemon.setText(null);
                    txtHp.setText(null);
                    txtAttack.setText(null);
                    txtDefense.setText(null);
                    txtSpAtt.setText(null);
                    txtSpDef.setText(null);
                    txtSpeed.setText(null);
                    barraProgresso.setValue(0);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void apagar() {

        String[] opcoes = {"Yes", "No"};

        int apagar = JOptionPane.showOptionDialog(null, "Are you sure you want to delete this pokémon?", "Attention", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, opcoes, opcoes[0]);

        if (apagar == JOptionPane.YES_OPTION) {
            String sql = "delete from tbpokemon where pokemon = ?";

            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtPokemon.getText());

                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Pokémon successfully deleted!");
                    txtPokemon.setText(null);
                    txtHp.setText(null);
                    txtAttack.setText(null);
                    txtDefense.setText(null);
                    txtSpAtt.setText(null);
                    txtSpDef.setText(null);
                    txtSpeed.setText(null);
                    barraProgresso.setValue(0);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void adiciona1Hp() {
        if (Integer.parseInt(txtHp.getText()) >= 252 || getSomaEv() >= 510) {
            JOptionPane.showMessageDialog(null, "Congratulations! Finished training!");
        } else {
            txtHp.setValue(Integer.parseInt(txtHp.getText()) + 1);

            String sql = "update tbpokemon set hp=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtHp.getText());
                pst.setString(2, txtPokemon.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void adiciona2Hp() {
        if (Integer.parseInt(txtHp.getText()) >= 252 || getSomaEv() >= 510) {
            JOptionPane.showMessageDialog(null, "Congratulations! Finished training!");
        } else if (Integer.parseInt(txtHp.getText()) >= 251 || getSomaEv() >= 509) {
            JOptionPane.showMessageDialog(null, "The Pokémon cannot receive 2 Ev points.");
        } else {
            txtHp.setValue(Integer.parseInt(txtHp.getText()) + 2);

            String sql = "update tbpokemon set hp=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtHp.getText());
                pst.setString(2, txtPokemon.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void adiciona3Hp() {
        if (Integer.parseInt(txtHp.getText()) >= 252 || getSomaEv() >= 510) {
            JOptionPane.showMessageDialog(null, "Congratulations! Finished training!");
        } else if (Integer.parseInt(txtHp.getText()) >= 250 || getSomaEv() >= 508) {
            JOptionPane.showMessageDialog(null, "The Pokémon cannot receive 3 Ev points.");
        } else {
            txtHp.setValue(Integer.parseInt(txtHp.getText()) + 3);

            String sql = "update tbpokemon set hp=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtHp.getText());
                pst.setString(2, txtPokemon.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void adiciona1Attack() {
        if (Integer.parseInt(txtAttack.getText()) >= 252 || getSomaEv() >= 510) {
            JOptionPane.showMessageDialog(null, "Congratulations! Finished training!");
        } else {
            txtAttack.setValue(Integer.parseInt(txtAttack.getText()) + 1);

            String sql = "update tbpokemon set attack=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtAttack.getText());
                pst.setString(2, txtPokemon.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void adiciona2Attack() {
        if (Integer.parseInt(txtAttack.getText()) >= 252 || getSomaEv() >= 510) {
            JOptionPane.showMessageDialog(null, "Congratulations! Finished training!");
        } else if (Integer.parseInt(txtAttack.getText()) >= 251 || getSomaEv() >= 509) {
            JOptionPane.showMessageDialog(null, "The Pokémon cannot receive 2 Ev points.");
        } else {
            txtAttack.setValue(Integer.parseInt(txtAttack.getText()) + 2);

            String sql = "update tbpokemon set attack=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtAttack.getText());
                pst.setString(2, txtPokemon.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void adiciona3Attack() {
        if (Integer.parseInt(txtAttack.getText()) >= 252 || getSomaEv() >= 510) {
            JOptionPane.showMessageDialog(null, "Congratulations! Finished training!");
        } else if (Integer.parseInt(txtAttack.getText()) >= 250 || getSomaEv() >= 508) {
            JOptionPane.showMessageDialog(null, "The Pokémon cannot receive 3 Ev points.");
        } else {
            txtAttack.setValue(Integer.parseInt(txtAttack.getText()) + 3);

            String sql = "update tbpokemon set attack=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtAttack.getText());
                pst.setString(2, txtPokemon.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void adiciona1Defense() {
        if (Integer.parseInt(txtDefense.getText()) >= 252 || getSomaEv() >= 510) {
            JOptionPane.showMessageDialog(null, "Congratulations! Finished training!");
        } else {
            txtDefense.setValue(Integer.parseInt(txtDefense.getText()) + 1);

            String sql = "update tbpokemon set defense=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtDefense.getText());
                pst.setString(2, txtPokemon.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void adiciona2Defense() {
        if (Integer.parseInt(txtDefense.getText()) >= 252 || getSomaEv() >= 510) {
            JOptionPane.showMessageDialog(null, "Congratulations! Finished training!");
        } else if (Integer.parseInt(txtDefense.getText()) >= 251 || getSomaEv() >= 509) {
            JOptionPane.showMessageDialog(null, "The Pokémon cannot receive 2 Ev points.");
        } else {
            txtDefense.setValue(Integer.parseInt(txtDefense.getText()) + 2);

            String sql = "update tbpokemon set defense=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtDefense.getText());
                pst.setString(2, txtPokemon.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void adiciona3Defense() {
        if (Integer.parseInt(txtDefense.getText()) >= 252 || getSomaEv() >= 510) {
            JOptionPane.showMessageDialog(null, "Congratulations! Finished training!");
        } else if (Integer.parseInt(txtDefense.getText()) >= 250 || getSomaEv() >= 508) {
            JOptionPane.showMessageDialog(null, "The Pokémon cannot receive 3 Ev points.");
        } else {
            txtDefense.setValue(Integer.parseInt(txtDefense.getText()) + 3);

            String sql = "update tbpokemon set defense=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtDefense.getText());
                pst.setString(2, txtPokemon.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void adiciona1SpAtt() {
        if (Integer.parseInt(txtSpAtt.getText()) >= 252 || getSomaEv() >= 510) {
            JOptionPane.showMessageDialog(null, "Congratulations! Finished training!");
        } else {
            txtSpAtt.setValue(Integer.parseInt(txtSpAtt.getText()) + 1);

            String sql = "update tbpokemon set spatt=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtSpAtt.getText());
                pst.setString(2, txtPokemon.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void adiciona2SpAtt() {
        if (Integer.parseInt(txtSpAtt.getText()) >= 252 || getSomaEv() >= 510) {
            JOptionPane.showMessageDialog(null, "Congratulations! Finished training!");
        } else if (Integer.parseInt(txtSpAtt.getText()) >= 251 || getSomaEv() >= 509) {
            JOptionPane.showMessageDialog(null, "The Pokémon cannot receive 2 Ev points.");
        } else {
            txtSpAtt.setValue(Integer.parseInt(txtSpAtt.getText()) + 2);

            String sql = "update tbpokemon set spatt=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtSpAtt.getText());
                pst.setString(2, txtPokemon.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void adiciona3SpAtt() {
        if (Integer.parseInt(txtSpAtt.getText()) >= 252 || getSomaEv() >= 510) {
            JOptionPane.showMessageDialog(null, "Congratulations! Finished training!");
        } else if (Integer.parseInt(txtSpAtt.getText()) >= 250 || getSomaEv() >= 508) {
            JOptionPane.showMessageDialog(null, "The Pokémon cannot receive 3 Ev points.");
        } else {
            txtSpAtt.setValue(Integer.parseInt(txtSpAtt.getText()) + 3);

            String sql = "update tbpokemon set spatt=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtSpAtt.getText());
                pst.setString(2, txtPokemon.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void adiciona1SpDef() {
        if (Integer.parseInt(txtSpDef.getText()) >= 252 || getSomaEv() >= 510) {
            JOptionPane.showMessageDialog(null, "Congratulations! Finished training!");
        } else {
            txtSpDef.setValue(Integer.parseInt(txtSpDef.getText()) + 1);

            String sql = "update tbpokemon set spdef=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtSpDef.getText());
                pst.setString(2, txtPokemon.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void adiciona2SpDef() {
        if (Integer.parseInt(txtSpDef.getText()) >= 252 || getSomaEv() >= 510) {
            JOptionPane.showMessageDialog(null, "Congratulations! Finished training!");
        } else if (Integer.parseInt(txtSpDef.getText()) >= 251 || getSomaEv() >= 509) {
            JOptionPane.showMessageDialog(null, "The Pokémon cannot receive 2 Ev points.");
        } else {
            txtSpDef.setValue(Integer.parseInt(txtSpDef.getText()) + 2);

            String sql = "update tbpokemon set spdef=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtSpDef.getText());
                pst.setString(2, txtPokemon.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void adiciona3SpDef() {
        if (Integer.parseInt(txtSpDef.getText()) >= 252 || getSomaEv() >= 510) {
            JOptionPane.showMessageDialog(null, "Congratulations! Finished training!");
        } else if (Integer.parseInt(txtSpDef.getText()) >= 250 || getSomaEv() >= 508) {
            JOptionPane.showMessageDialog(null, "The Pokémon cannot receive 3 Ev points.");
        } else {
            txtSpDef.setValue(Integer.parseInt(txtSpDef.getText()) + 3);

            String sql = "update tbpokemon set spdef=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtSpDef.getText());
                pst.setString(2, txtPokemon.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void adiciona1Speed() {
        if (Integer.parseInt(txtSpeed.getText()) >= 252 || getSomaEv() >= 510) {
            JOptionPane.showMessageDialog(null, "Congratulations! Finished training!");
        } else {
            txtSpeed.setValue(Integer.parseInt(txtSpeed.getText()) + 1);

            String sql = "update tbpokemon set speed=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtSpeed.getText());
                pst.setString(2, txtPokemon.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void adiciona2Speed() {
        if (Integer.parseInt(txtSpeed.getText()) >= 252 || getSomaEv() >= 510) {
            JOptionPane.showMessageDialog(null, "Congratulations! Finished training!");
        } else if (Integer.parseInt(txtSpeed.getText()) >= 251 || getSomaEv() >= 509) {
            JOptionPane.showMessageDialog(null, "The Pokémon cannot receive 2 Ev points.");
        } else {
            txtSpeed.setValue(Integer.parseInt(txtSpeed.getText()) + 2);

            String sql = "update tbpokemon set speed=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtSpeed.getText());
                pst.setString(2, txtPokemon.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void adiciona3Speed() {
        if (Integer.parseInt(txtSpeed.getText()) >= 252 || getSomaEv() >= 510) {
            JOptionPane.showMessageDialog(null, "Congratulations! Finished training!");
        } else if (Integer.parseInt(txtSpeed.getText()) >= 250 || getSomaEv() >= 508) {
            JOptionPane.showMessageDialog(null, "The Pokémon cannot receive 3 Ev points.");
        } else {
            txtSpeed.setValue(Integer.parseInt(txtSpeed.getText()) + 3);

            String sql = "update tbpokemon set speed=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtSpeed.getText());
                pst.setString(2, txtPokemon.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void remove1Hp() {
        if (Integer.parseInt(txtHp.getText()) > 0) {
            txtHp.setValue(Integer.parseInt(txtHp.getText()) - 1);

            String sql = "update tbpokemon set hp=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtHp.getText());
                pst.setString(2, txtPokemon.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void remove1Attack() {
        if (Integer.parseInt(txtAttack.getText()) > 0) {
            txtAttack.setValue(Integer.parseInt(txtAttack.getText()) - 1);

            String sql = "update tbpokemon set attack=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtAttack.getText());
                pst.setString(2, txtPokemon.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void remove1Defense() {
        if (Integer.parseInt(txtDefense.getText()) > 0) {
            txtDefense.setValue(Integer.parseInt(txtDefense.getText()) - 1);

            String sql = "update tbpokemon set defense=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtDefense.getText());
                pst.setString(2, txtPokemon.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void remove1SpAtt() {
        if (Integer.parseInt(txtSpAtt.getText()) > 0) {
            txtSpAtt.setValue(Integer.parseInt(txtSpAtt.getText()) - 1);

            String sql = "update tbpokemon set spatt=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtSpAtt.getText());
                pst.setString(2, txtPokemon.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void remove1SpDef() {
        if (Integer.parseInt(txtSpDef.getText()) > 0) {
            txtSpDef.setValue(Integer.parseInt(txtSpDef.getText()) - 1);

            String sql = "update tbpokemon set spdef=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtSpDef.getText());
                pst.setString(2, txtSpDef.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void remove1Speed() {
        if (Integer.parseInt(txtSpeed.getText()) > 0) {
            txtSpeed.setValue(Integer.parseInt(txtSpeed.getText()) - 1);

            String sql = "update tbpokemon set speed=? where pokemon=?";

            try {
                pst = conexao.prepareStatement(sql);

                pst.setString(1, txtSpeed.getText());
                pst.setString(2, txtSpDef.getText());
                barraProgresso.setValue(getSomaEv());

                pst.executeUpdate();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    public void exit() {
        String[] opcoes = {"Yes", "No"};

        int sair = JOptionPane.showOptionDialog(null, "Are you sure you want to leave?", "Attention", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, opcoes, opcoes[0]);

        if (sair == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    /*public static void main(String args[]) {
        TelaCalcular calcular = new TelaCalcular();
        calcular.setVisible(true);
    } */

    private void componentesDaTela() {

        txtCalcUsuario = new JTextField();
        txtCalcUsuario.setBounds(87, 2, 100, 25);
        add(txtCalcUsuario);
        txtCalcUsuario.setVisible(false);

        lblPokemon = new JLabel("Pokémon");
        lblPokemon.setBounds(32, 34, 50, 15);
        lblPokemon.setFont(new Font("Calibri", 0, 12));
        add(lblPokemon);

        txtPokemon = new JTextField();
        txtPokemon.setBounds(87, 27, 100, 28);
        add(txtPokemon);

        btnPesquisar = new JButton();
        btnPesquisar.setBounds(192, 27, 30, 28);
        btnPesquisar.setIcon(new ImageIcon(getClass().getResource("/br/com/pokecalculator/icones/search.png")));
        btnPesquisar.setToolTipText("Search");
        btnPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarActionPerformed(evt);
            }
        });
        add(btnPesquisar);

        btnAdicionar = new JButton();
        btnAdicionar.setBounds(240, 27, 30, 28);
        btnAdicionar.setIcon(new ImageIcon(getClass().getResource("/br/com/pokecalculator/icones/add.png")));
        btnAdicionar.setToolTipText("Add");
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });
        add(btnAdicionar);

        btnEditar = new JButton();
        btnEditar.setBounds(280, 27, 30, 28);
        btnEditar.setIcon(new ImageIcon(getClass().getResource("/br/com/pokecalculator/icones/edit.png")));
        btnEditar.setToolTipText("Edit");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        add(btnEditar);

        btnDelete = new JButton();
        btnDelete.setBounds(320, 27, 30, 28);
        btnDelete.setIcon(new ImageIcon(getClass().getResource("/br/com/pokecalculator/icones/delete.png")));
        btnDelete.setToolTipText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        add(btnDelete);

        lblHp = new JLabel("Hp");
        lblHp.setBounds(35, 85, 20, 15);
        lblHp.setFont(new Font("Calibri", 0, 12));
        add(lblHp);

        txtHp = new JFormattedTextField();
        txtHp.setBounds(115, 75, 35, 28);
        txtHp.setHorizontalAlignment(SwingConstants.CENTER);
        add(txtHp);

        btnHp1 = new JButton("1");
        btnHp1.setBounds(160, 75, 40, 28);
        btnHp1.setFont(new Font("Arial", 0, 11));
        btnHp1.setToolTipText("Add 1");
        btnHp1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHp1ActionPerformed(evt);
            }
        });
        add(btnHp1);

        btnHp2 = new JButton("2");
        btnHp2.setBounds(210, 75, 40, 28);
        btnHp2.setFont(new Font("Arial", 0, 11));
        btnHp2.setToolTipText("Add 2");
        btnHp2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHp2ActionPerformed(evt);
            }
        });
        add(btnHp2);

        btnHp3 = new JButton("3");
        btnHp3.setBounds(260, 75, 40, 28);
        btnHp3.setFont(new Font("Arial", 0, 11));
        btnHp3.setToolTipText("Add 3");
        btnHp3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHp3ActionPerformed(evt);
            }
        });
        add(btnHp3);

        btnHpApagar = new JButton();
        btnHpApagar.setBounds(310, 75, 40, 28);
        btnHpApagar.setToolTipText("Remove 1");
        btnHpApagar.setIcon(new ImageIcon(getClass().getResource("/br/com/pokecalculator/icones/exclude.png")));
        btnHpApagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHpApagarActionPerformed(evt);
            }
        });
        add(btnHpApagar);

        lblAttack = new JLabel("Attack");
        lblAttack.setBounds(35, 118, 60, 15);
        lblAttack.setFont(new Font("Calibri", 0, 12));
        add(lblAttack);

        txtAttack = new JFormattedTextField();
        txtAttack.setBounds(115, 108, 35, 28);
        txtAttack.setHorizontalAlignment(SwingConstants.CENTER);
        add(txtAttack);

        btnAttack1 = new JButton("1");
        btnAttack1.setBounds(160, 108, 40, 28);
        btnAttack1.setFont(new Font("Arial", 0, 11));
        btnAttack1.setToolTipText("Add 1");
        btnAttack1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAttack1ActionPerformed(evt);
            }
        });
        add(btnAttack1);

        btnAttack2 = new JButton("2");
        btnAttack2.setBounds(210, 108, 40, 28);
        btnAttack2.setFont(new Font("Arial", 0, 11));
        btnAttack2.setToolTipText("Add 2");
        btnAttack2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAttack2ActionPerformed(evt);
            }
        });
        add(btnAttack2);

        btnAttack3 = new JButton("3");
        btnAttack3.setBounds(260, 108, 40, 28);
        btnAttack3.setFont(new Font("Arial", 0, 11));
        btnAttack3.setToolTipText("Add 3");
        btnAttack3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAttack3ActionPerformed(evt);
            }
        });
        add(btnAttack3);

        btnAttackApagar = new JButton();
        btnAttackApagar.setBounds(310, 108, 40, 28);
        btnAttackApagar.setToolTipText("Remove 1");
        btnAttackApagar.setIcon(new ImageIcon(getClass().getResource("/br/com/pokecalculator/icones/exclude.png")));
        btnAttackApagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAttackApagarActionPerformed(evt);
            }
        });
        add(btnAttackApagar);

        lblDefense = new JLabel("Defense");
        lblDefense.setBounds(35, 151, 60, 15);
        lblDefense.setFont(new Font("Calibri", 0, 12));
        add(lblDefense);

        txtDefense = new JFormattedTextField();
        txtDefense.setBounds(115, 141, 35, 28);
        txtDefense.setHorizontalAlignment(SwingConstants.CENTER);
        add(txtDefense);

        btnDefense1 = new JButton("1");
        btnDefense1.setBounds(160, 141, 40, 28);
        btnDefense1.setFont(new Font("Arial", 0, 11));
        btnDefense1.setToolTipText("Add 1");
        btnDefense1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDefense1ActionPerformed(evt);
            }
        });
        add(btnDefense1);

        btnDefense2 = new JButton("2");
        btnDefense2.setBounds(210, 141, 40, 28);
        btnDefense2.setFont(new Font("Arial", 0, 11));
        btnDefense2.setToolTipText("Add 2");
        btnDefense2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDefense2ActionPerformed(evt);
            }
        });
        add(btnDefense2);

        btnDefense3 = new JButton("3");
        btnDefense3.setBounds(260, 141, 40, 28);
        btnDefense3.setFont(new Font("Arial", 0, 11));
        btnDefense3.setToolTipText("Add 3");
        btnDefense3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDefense3ActionPerformed(evt);
            }
        });
        add(btnDefense3);

        btnDefenseApagar = new JButton();
        btnDefenseApagar.setBounds(310, 141, 40, 28);
        btnDefenseApagar.setToolTipText("Remove 1");
        btnDefenseApagar.setIcon(new ImageIcon(getClass().getResource("/br/com/pokecalculator/icones/exclude.png")));
        btnDefenseApagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDefenseApagarActionPerformed(evt);
            }
        });
        add(btnDefenseApagar);

        lblSpAtt = new JLabel("Sp. Attack");
        lblSpAtt.setBounds(35, 184, 60, 15);
        lblSpAtt.setFont(new Font("Calibri", 0, 12));
        add(lblSpAtt);

        txtSpAtt = new JFormattedTextField();
        txtSpAtt.setBounds(115, 174, 35, 28);
        txtSpAtt.setHorizontalAlignment(SwingConstants.CENTER);
        add(txtSpAtt);

        btnSpAtt1 = new JButton("1");
        btnSpAtt1.setBounds(160, 174, 40, 28);
        btnSpAtt1.setFont(new Font("Arial", 0, 11));
        btnSpAtt1.setToolTipText("Add 1");
        btnSpAtt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpAtt1ActionPerformed(evt);
            }
        });
        add(btnSpAtt1);

        btnSpAtt2 = new JButton("2");
        btnSpAtt2.setBounds(210, 174, 40, 28);
        btnSpAtt2.setFont(new Font("Arial", 0, 11));
        btnSpAtt2.setToolTipText("Add 2");
        btnSpAtt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpAtt2ActionPerformed(evt);
            }
        });
        add(btnSpAtt2);

        btnSpAtt3 = new JButton("3");
        btnSpAtt3.setBounds(260, 174, 40, 28);
        btnSpAtt3.setFont(new Font("Arial", 0, 11));
        btnSpAtt3.setToolTipText("Add 3");
        btnSpAtt3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpAtt3ActionPerformed(evt);
            }
        });
        add(btnSpAtt3);

        btnSpAttApagar = new JButton();
        btnSpAttApagar.setBounds(310, 174, 40, 28);
        btnSpAttApagar.setToolTipText("Remove 1");
        btnSpAttApagar.setIcon(new ImageIcon(getClass().getResource("/br/com/pokecalculator/icones/exclude.png")));
        btnSpAttApagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpAttApagarActionPerformed(evt);
            }
        });
        add(btnSpAttApagar);

        lblSpDef = new JLabel("Sp. Defense");
        lblSpDef.setBounds(35, 217, 70, 15);
        lblSpDef.setFont(new Font("Calibri", 0, 12));
        add(lblSpDef);

        txtSpDef = new JFormattedTextField();
        txtSpDef.setBounds(115, 207, 35, 28);
        txtSpDef.setHorizontalAlignment(SwingConstants.CENTER);
        add(txtSpDef);

        btnSpDef1 = new JButton("1");
        btnSpDef1.setBounds(160, 207, 40, 28);
        btnSpDef1.setFont(new Font("Arial", 0, 11));
        btnSpDef1.setToolTipText("Add 1");
        btnSpDef1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpDef1ActionPerformed(evt);
            }
        });
        add(btnSpDef1);

        btnSpDef2 = new JButton("2");
        btnSpDef2.setBounds(210, 207, 40, 28);
        btnSpDef2.setFont(new Font("Arial", 0, 11));
        btnSpDef2.setToolTipText("Add 2");
        btnSpDef2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpDef2ActionPerformed(evt);
            }
        });
        add(btnSpDef2);

        btnSpDef3 = new JButton("3");
        btnSpDef3.setBounds(260, 207, 40, 28);
        btnSpDef3.setFont(new Font("Arial", 0, 11));
        btnSpDef3.setToolTipText("Add 3");
        btnSpDef3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpDef3ActionPerformed(evt);
            }
        });
        add(btnSpDef3);

        btnSpDefApagar = new JButton();
        btnSpDefApagar.setBounds(310, 207, 40, 28);
        btnSpDefApagar.setToolTipText("Remove 1");
        btnSpDefApagar.setIcon(new ImageIcon(getClass().getResource("/br/com/pokecalculator/icones/exclude.png")));
        btnSpDefApagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpDefApagarActionPerformed(evt);
            }
        });
        add(btnSpDefApagar);

        lblSpeed = new JLabel("Speed");
        lblSpeed.setBounds(35, 250, 60, 15);
        lblSpeed.setFont(new Font("Calibri", 0, 12));
        add(lblSpeed);

        txtSpeed = new JFormattedTextField();
        txtSpeed.setBounds(115, 240, 35, 28);
        txtSpeed.setHorizontalAlignment(SwingConstants.CENTER);
        add(txtSpeed);

        btnSpeed1 = new JButton("1");
        btnSpeed1.setBounds(160, 240, 40, 28);
        btnSpeed1.setFont(new Font("Arial", 0, 11));
        btnSpeed1.setToolTipText("Add 1");
        btnSpeed1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpeed1ActionPerformed(evt);
            }
        });
        add(btnSpeed1);

        btnSpeed2 = new JButton("2");
        btnSpeed2.setBounds(210, 240, 40, 28);
        btnSpeed2.setFont(new Font("Arial", 0, 11));
        btnSpeed2.setToolTipText("Add 2");
        btnSpeed2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpeed2ActionPerformed(evt);
            }
        });
        add(btnSpeed2);

        btnSpeed3 = new JButton("3");
        btnSpeed3.setBounds(260, 240, 40, 28);
        btnSpeed3.setFont(new Font("Arial", 0, 11));
        btnSpeed3.setToolTipText("Add 3");
        btnSpeed3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpeed3ActionPerformed(evt);
            }
        });
        add(btnSpeed3);

        btnSpeedApagar = new JButton();
        btnSpeedApagar.setBounds(310, 240, 40, 28);
        btnSpeedApagar.setToolTipText("Remove 1");
        btnSpeedApagar.setIcon(new ImageIcon(getClass().getResource("/br/com/pokecalculator/icones/exclude.png")));
        btnSpeedApagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpeedApagarActionPerformed(evt);
            }
        });
        add(btnSpeedApagar);

        btnViewTeam = new JButton("Team");
        btnViewTeam.setBounds(225, 283, 60, 28);
        btnViewTeam.setFont(new Font("Arial", 0, 11));
        btnViewTeam.setToolTipText("View Team");
        btnViewTeam.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewTeamActionPerformed(evt);
            }
        });
        add(btnViewTeam);

        btnExit = new JButton("Exit");
        btnExit.setBounds(290, 283, 60, 28);
        btnExit.setFont(new Font("Arial", 0, 11));
        btnExit.setToolTipText("Exit the system");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        add(btnExit);

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        //menuBar.setFont(new Font("Arial", 0, 11));

        menuView = new JMenu("View");
        menuBar.add(menuView);

        menuOptions = new JMenu("Options");
        menuBar.add(menuOptions);

        menuHelp = new JMenu("Help");
        menuBar.add(menuHelp);

        viewTeam = new JMenuItem("Team Progress");
        viewTeam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewTeamActionPerformed(evt);
            }
        });
        menuView.add(viewTeam);

        optionsDelete = new JMenuItem("Delete");
        optionsDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsDeleteActionPerformed(evt);
            }
        });
        menuOptions.add(optionsDelete);

        optionsExit = new JMenuItem("Exit");
        optionsExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsExitActionPerformed(evt);
            }
        });
        menuOptions.add(optionsExit);

        helpAbout = new JMenuItem("About");
        helpAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpAboutActionPerformed(evt);
            }
        });
        menuHelp.add(helpAbout);

        barraProgresso = new JProgressBar();
        barraProgresso.setBounds(35, 288, 80, 23);
        barraProgresso.setStringPainted(true);
        barraProgresso.setMaximum(510);
        barraProgresso.setForeground(Color.GREEN);
        add(barraProgresso);
    }

    private void btnPesquisarActionPerformed(ActionEvent evt) {
        pesquisar();
    }

    private void btnAdicionarActionPerformed(ActionEvent evt) {
        adicionar();
    }

    private void btnEditarActionPerformed(ActionEvent evt) {
        editar();
    }

    private void btnDeleteActionPerformed(ActionEvent evt) {
        apagar();
    }

    private void btnHp1ActionPerformed(ActionEvent evt) {
        adiciona1Hp();
    }

    private void btnHp2ActionPerformed(ActionEvent evt) {
        adiciona2Hp();
    }

    private void btnHp3ActionPerformed(ActionEvent evt) {
        adiciona3Hp();
    }

    private void btnHpApagarActionPerformed(ActionEvent evt) {
        remove1Hp();
    }

    private void btnAttack1ActionPerformed(ActionEvent evt) {
        adiciona1Attack();
    }

    private void btnAttack2ActionPerformed(ActionEvent evt) {
        adiciona2Attack();
    }

    private void btnAttack3ActionPerformed(ActionEvent evt) {
        adiciona3Attack();
    }

    private void btnAttackApagarActionPerformed(ActionEvent evt) {
        remove1Attack();
    }

    private void btnDefense1ActionPerformed(ActionEvent evt) {
        adiciona1Defense();
    }

    private void btnDefense2ActionPerformed(ActionEvent evt) {
        adiciona2Defense();
    }

    private void btnDefense3ActionPerformed(ActionEvent evt) {
        adiciona3Defense();
    }

    private void btnDefenseApagarActionPerformed(ActionEvent evt) {
        remove1Defense();
    }

    private void btnSpAtt1ActionPerformed(ActionEvent evt) {
        adiciona1SpAtt();
    }

    private void btnSpAtt2ActionPerformed(ActionEvent evt) {
        adiciona2SpAtt();
    }

    private void btnSpAtt3ActionPerformed(ActionEvent evt) {
        adiciona3SpAtt();
    }

    private void btnSpAttApagarActionPerformed(ActionEvent evt) {
        remove1SpAtt();
    }

    private void btnSpDef1ActionPerformed(ActionEvent evt) {
        adiciona1SpDef();
    }

    private void btnSpDef2ActionPerformed(ActionEvent evt) {
        adiciona2SpDef();
    }

    private void btnSpDef3ActionPerformed(ActionEvent evt) {
        adiciona3SpDef();
    }

    private void btnSpDefApagarActionPerformed(ActionEvent evt) {
        remove1SpDef();
    }

    private void btnSpeed1ActionPerformed(ActionEvent evt) {
        adiciona1Speed();
    }

    private void btnSpeed2ActionPerformed(ActionEvent evt) {
        adiciona2Speed();
    }

    private void btnSpeed3ActionPerformed(ActionEvent evt) {
        adiciona3Speed();
    }

    private void btnSpeedApagarActionPerformed(ActionEvent evt) {
        remove1Speed();
    }

    private void viewTeamActionPerformed(ActionEvent evt) {
        TelaEquipe equipe = new TelaEquipe();
        equipe.txtTeamUser.setText(txtCalcUsuario.getText());
        equipe.setVisible(true);
    }

    private void optionsDeleteActionPerformed(ActionEvent evt) {
        apagar();
    }

    private void helpAboutActionPerformed(ActionEvent evt) {
        TelaSobre sobre = new TelaSobre();
        sobre.setVisible(true);
    }

    private void optionsExitActionPerformed(ActionEvent evt) {
        exit();
    }
    
    private void btnViewTeamActionPerformed(ActionEvent evt) {
        TelaEquipe equipe = new TelaEquipe();
        equipe.txtTeamUser.setText(txtCalcUsuario.getText());
        equipe.setVisible(true);
    }

    private void btnExitActionPerformed(ActionEvent evt) {
        exit();
    }

    private JMenuBar menuBar;
    private JMenu menuView;
    private JMenu menuOptions;
    private JMenu menuHelp;
    private JMenuItem viewTeam;
    private JMenuItem optionsDelete;
    private JMenuItem optionsExit;
    private JMenuItem helpAbout;

    public JTextField txtCalcUsuario;
    private JLabel lblPokemon;
    private JTextField txtPokemon;
    private JButton btnPesquisar;
    private JButton btnAdicionar;
    private JButton btnEditar;
    private JButton btnDelete;

    private JLabel lblHp;
    private JFormattedTextField txtHp;
    private JButton btnHp1;
    private JButton btnHp2;
    private JButton btnHp3;
    private JButton btnHpApagar;

    private JLabel lblAttack;
    private JFormattedTextField txtAttack;
    private JButton btnAttack1;
    private JButton btnAttack2;
    private JButton btnAttack3;
    private JButton btnAttackApagar;

    private JLabel lblDefense;
    private JFormattedTextField txtDefense;
    private JButton btnDefense1;
    private JButton btnDefense2;
    private JButton btnDefense3;
    private JButton btnDefenseApagar;

    private JLabel lblSpAtt;
    private JFormattedTextField txtSpAtt;
    private JButton btnSpAtt1;
    private JButton btnSpAtt2;
    private JButton btnSpAtt3;
    private JButton btnSpAttApagar;

    private JLabel lblSpDef;
    private JFormattedTextField txtSpDef;
    private JButton btnSpDef1;
    private JButton btnSpDef2;
    private JButton btnSpDef3;
    private JButton btnSpDefApagar;

    private JLabel lblSpeed;
    private JFormattedTextField txtSpeed;
    private JButton btnSpeed1;
    private JButton btnSpeed2;
    private JButton btnSpeed3;
    private JButton btnSpeedApagar;

    private JProgressBar barraProgresso;
    private JButton btnViewTeam;
    private JButton btnExit;

    private int somaEv;

    public int getSomaEv() {
        somaEv = Integer.parseInt(txtHp.getText()) + Integer.parseInt(txtAttack.getText()) + Integer.parseInt(txtDefense.getText())
                + Integer.parseInt(txtSpAtt.getText()) + Integer.parseInt(txtSpDef.getText()) + Integer.parseInt(txtSpeed.getText());
        return somaEv;
    }
}
