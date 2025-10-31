package edu.basedatos.oracleexample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SimpleOracleTest {
    public static void main(String[] args) {
        // URLs a probar (en orden de prioridad)
        String[] urls = {
                "jdbc:oracle:thin:@localhost:1521/XEPDB1",  // Service Name (recomendado para XE)
                "jdbc:oracle:thin:@localhost:1521:XE"       // SID (legacy)
        };

        for (String url : urls) {
            System.out.println("\n🔍 Probando: " + url);
            try {
                // Cargar el driver de Oracle
                Class.forName("oracle.jdbc.OracleDriver");

                // Intentar conectar
                Connection conn = DriverManager.getConnection(url, "HR", "oracle");
                System.out.println("✅ Conexión exitosa con: " + url);

                // Hacer una consulta simple para verificar
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT 'Conexión OK desde HR' AS mensaje, USER AS usuario FROM DUAL");

                if (rs.next()) {
                    System.out.println("📊 Mensaje: " + rs.getString("mensaje"));
                    System.out.println("👤 Usuario conectado: " + rs.getString("usuario"));
                }

                // Cerrar recursos
                rs.close();
                stmt.close();
                conn.close();

                System.out.println("✅ Prueba completada exitosamente!");
                break; // Salir del loop si la conexión fue exitosa

            } catch (ClassNotFoundException e) {
                System.out.println("❌ Error: Driver de Oracle no encontrado");
                System.out.println("   Asegúrate de tener ojdbc8.jar o ojdbc11.jar en tu classpath");
                break; // Si el driver no está, no tiene sentido seguir probando

            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        }
    }
}