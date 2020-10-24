package br.com.pokecalculator.conexao;

import java.sql.*;
/**
 *
 * @author angel
 */
public class Conexao {

    //método que estabelece conexão com o banco
    public static Connection conector() {
        java.sql.Connection conexao = null;

        // a linha abaixo chama o driver
        String driver = "com.mysql.jdbc.Driver";
        // armazenando informações referentes ao banco
        String url = "jdbc:mysql://localhost:3306/calculadorapokemon";
        String user = "root";
        String password = "";

        // estabelecendo a conexão com o banco
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (Exception e) {
            // System.out.println(e);
            return null;
        }
    }

}
