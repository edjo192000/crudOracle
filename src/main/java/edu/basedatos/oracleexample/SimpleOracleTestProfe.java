package edu.basedatos.oracleexample;

import java.sql.Connection;
import java.sql.DriverManager;

public class SimpleOracleTestProfe {
    public static void main(String[] args) {
        String[] urls = {
                "jdbc:oracle:thin:@192.168.11.211:1521:xe",
                "jdbc:oracle:thin:@192.168.11.211:1521/xe"
        };

        for (String url : urls) {
            System.out.println("\n🔍 Probando: " + url);
            try {
                Class.forName("oracle.jdbc.OracleDriver");
                Connection conn = DriverManager.getConnection(url, "HR", "oracle");
                System.out.println("✅ Conexión exitosa con: " + url);
                conn.close();
                break;
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        }
    }
}
