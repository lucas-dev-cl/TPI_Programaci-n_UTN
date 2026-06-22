/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Lila
 */
public class ConexionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/pedidos_db";
    private static final String USER = "root";
    private static final String PASS = System.getenv("PASSWORD_DB") != null ? System.getenv("PASSWORD_DB") : "root";

    public static Connection getConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado.", e);
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

