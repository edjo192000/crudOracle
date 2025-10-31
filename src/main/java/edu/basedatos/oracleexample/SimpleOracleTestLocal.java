package edu.basedatos.oracleexample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SimpleOracleTestLocal {
    public static void main(String[] args) {

        String[] urls = {
                "jdbc:oracle:thin:@localhost:1521/ORCLPDB1",
                "jdbc:oracle:thin:@localhost:1521:ORCLCDB"
        };

        for (String url : urls) {
            System.out.println("\n[TEST] Probando conexion: " + url);
            try {
                // Cargar el driver de Oracle
                Class.forName("oracle.jdbc.OracleDriver");

                // Intentar conectar
                Connection conn = DriverManager.getConnection(url, "HR", "oracle");
                System.out.println("[SUCCESS] Conexion exitosa con: " + url);

                // Hacer una consulta simple para verificar
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT 'Conexion OK desde HR' AS mensaje, " +
                                "USER AS usuario, " +
                                "SYS_CONTEXT('USERENV', 'CON_NAME') AS container, " +
                                "TO_CHAR(SYSDATE, 'DD/MM/YYYY HH24:MI:SS') AS fecha " +
                                "FROM DUAL"
                );

                if (rs.next()) {
                    System.out.println("\n--- Detalles de la conexion ---");
                    System.out.println("Mensaje: " + rs.getString("mensaje"));
                    System.out.println("Usuario: " + rs.getString("usuario"));
                    System.out.println("Container (PDB): " + rs.getString("container"));
                    System.out.println("Fecha servidor: " + rs.getString("fecha"));
                    System.out.println("-------------------------------\n");
                }

                // Listar todas las tablas del usuario HR
                System.out.println("--- Tablas en el esquema HR ---");
                Statement tablesStmt = conn.createStatement();
                ResultSet tablesRs = tablesStmt.executeQuery(
                        "SELECT table_name, num_rows, last_analyzed " +
                                "FROM user_tables " +
                                "ORDER BY table_name"
                );

                int tableCount = 0;
                boolean tasksFound = false;

                while (tablesRs.next()) {
                    tableCount++;
                    String tableName = tablesRs.getString("table_name");
                    String numRows = tablesRs.getString("num_rows");
                    String lastAnalyzed = tablesRs.getString("last_analyzed");

                    System.out.println(tableCount + ". " + tableName +
                            " (Filas: " + (numRows != null ? numRows : "N/A") +
                            ", Analizada: " + (lastAnalyzed != null ? lastAnalyzed : "Nunca") + ")");

                    if (tableName.equals("TASKS")) {
                        tasksFound = true;
                    }
                }

                if (tableCount == 0) {
                    System.out.println("No hay tablas creadas en el esquema HR");
                } else {
                    System.out.println("\nTotal de tablas: " + tableCount);
                }

                tablesRs.close();
                tablesStmt.close();
                System.out.println("-------------------------------\n");

                if (tasksFound) {
                    System.out.println("[INFO] Tabla TASKS encontrada!");

                    Statement countStmt = conn.createStatement();
                    ResultSet countRs = countStmt.executeQuery("SELECT COUNT(*) AS total FROM TASKS");
                    if (countRs.next()) {
                        int total = countRs.getInt("total");
                        System.out.println("[INFO] Total de tareas en TASKS: " + total);

                        if (total > 0) {
                            System.out.println("[INFO] Listando primeras 5 tareas...");
                            Statement taskStmt = conn.createStatement();
                            ResultSet taskRs = taskStmt.executeQuery(
                                    "SELECT task_id, title, status, priority " +
                                            "FROM TASKS " +
                                            "WHERE ROWNUM <= 5 " +
                                            "ORDER BY task_id"
                            );

                            System.out.println("\n  ID | Titulo | Estado | Prioridad");
                            System.out.println("  " + "-".repeat(50));
                            while (taskRs.next()) {
                                System.out.printf("  %-3d | %-20s | %-12s | %-8s%n",
                                        taskRs.getInt("task_id"),
                                        taskRs.getString("title"),
                                        taskRs.getString("status"),
                                        taskRs.getString("priority")
                                );
                            }
                            taskRs.close();
                            taskStmt.close();
                        }
                    }
                    countRs.close();
                    countStmt.close();
                } else {
                    System.out.println("[WARNING] Tabla TASKS no encontrada.");
                    System.out.println("[TIP] Ejecuta el script: 01_create_tasks_table.sql");
                }

                // Cerrar recursos
                rs.close();
                stmt.close();
                conn.close();

                System.out.println("\n[SUCCESS] Prueba completada exitosamente!");
                System.out.println("[INFO] Tu aplicacion Spring Boot puede conectarse a Oracle");
                break; // Salir del loop si la conexión fue exitosa

            } catch (ClassNotFoundException e) {
                System.out.println("[ERROR] Driver de Oracle no encontrado");
                System.out.println("[TIP] Asegurate de tener ojdbc11.jar en tu classpath");
                System.out.println("[TIP] Revisa la dependencia en build.gradle.kts");
                break;

            } catch (Exception e) {
                System.out.println("[ERROR] " + e.getMessage());

                if (e.getMessage().contains("ORA-12514")) {
                    System.out.println("[TIP] El servicio ORCLPDB1 no esta disponible.");
                    System.out.println("[TIP] Verifica que el PDB este abierto con:");
                    System.out.println("      docker exec -it oracle-db-19c sqlplus sys/oracle@ORCLCDB as sysdba");
                    System.out.println("      ALTER PLUGGABLE DATABASE ORCLPDB1 OPEN;");
                } else if (e.getMessage().contains("ORA-01017")) {
                    System.out.println("[TIP] Usuario o contrasena incorrectos");
                    System.out.println("[TIP] Verifica las credenciales: HR/oracle");
                } else if (e.getMessage().contains("Connection refused")) {
                    System.out.println("[TIP] Oracle no esta corriendo o el puerto 1521 no esta disponible");
                    System.out.println("[TIP] Verifica que el contenedor este corriendo:");
                    System.out.println("      docker ps | grep oracle");
                }
            }
        }
    }
}