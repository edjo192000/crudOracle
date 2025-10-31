package edu.basedatos.oracleexample;

import java.sql.*;

public class SimpleOracleTestProfe {
    public static void main(String[] args) {
        String[] urls = {
                "jdbc:oracle:thin:@192.168.11.211:1521:xe",
                "jdbc:oracle:thin:@192.168.11.211:1521/xe"
        };

        for (String url : urls) {
            System.out.println("\n[TEST] Probando: " + url);
            try {
                Class.forName("oracle.jdbc.OracleDriver");
                Connection conn = DriverManager.getConnection(url, "HR", "oracle");
                System.out.println("[SUCCESS] Conexion exitosa con: " + url);
                System.out.println("\n=== TABLAS Y CAMPOS EN EL ESQUEMA HR ===\n");

                // Listar todas las tablas del usuario HR
                Statement stmt = conn.createStatement();
                ResultSet tablesRs = stmt.executeQuery(
                        "SELECT table_name FROM user_tables ORDER BY table_name"
                );

                int tableCount = 0;
                while (tablesRs.next()) {
                    tableCount++;
                    String tableName = tablesRs.getString("table_name");

                    System.out.println("Tabla " + tableCount + ": " + tableName);
                    System.out.println("-".repeat(60));

                    // Obtener columnas de cada tabla
                    Statement colStmt = conn.createStatement();
                    ResultSet columnsRs = colStmt.executeQuery(
                            "SELECT column_name, data_type, data_length, nullable " +
                                    "FROM user_tab_columns " +
                                    "WHERE table_name = '" + tableName + "' " +
                                    "ORDER BY column_id"
                    );

                    System.out.printf("  %-30s %-20s %-10s %-10s%n",
                            "COLUMNA", "TIPO", "LONGITUD", "NULO");
                    System.out.println("  " + "-".repeat(58));

                    while (columnsRs.next()) {
                        String columnName = columnsRs.getString("column_name");
                        String dataType = columnsRs.getString("data_type");
                        String dataLength = columnsRs.getString("data_length");
                        String nullable = columnsRs.getString("nullable");

                        System.out.printf("  %-30s %-20s %-10s %-10s%n",
                                columnName,
                                dataType,
                                dataLength,
                                nullable.equals("Y") ? "SI" : "NO"
                        );
                    }

                    columnsRs.close();
                    colStmt.close();
                    System.out.println();
                }

                tablesRs.close();
                stmt.close();

                if (tableCount == 0) {
                    System.out.println("No hay tablas creadas en el esquema HR");
                } else {
                    System.out.println("========================================");
                    System.out.println("Total de tablas: " + tableCount);
                    System.out.println("========================================");
                }

                conn.close();
                System.out.println("\n[SUCCESS] Prueba completada exitosamente!");
                break;

            } catch (ClassNotFoundException e) {
                System.out.println("[ERROR] Driver de Oracle no encontrado");
                break;

            } catch (Exception e) {
                System.out.println("[ERROR] " + e.getMessage());
            }
        }
    }
}