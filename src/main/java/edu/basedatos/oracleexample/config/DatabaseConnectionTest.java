package edu.basedatos.oracleexample.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DatabaseConnectionTest implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseConnectionTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        try {
            // Test 1: Verificar versión de Oracle
            String version = jdbcTemplate.queryForObject(
                    "SELECT BANNER FROM V$VERSION WHERE ROWNUM = 1",
                    String.class
            );
            log.info("✅ Conexión exitosa a Oracle Database");
            log.info("📌 Versión: {}", version);

            // Test 2: Verificar usuario actual
            String currentUser = jdbcTemplate.queryForObject(
                    "SELECT USER FROM DUAL",
                    String.class
            );
            log.info("👤 Usuario conectado: {}", currentUser);

            // Test 3: Contar tablas del esquema
            Integer tableCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM USER_TABLES",
                    Integer.class
            );
            log.info("📊 Número de tablas en el esquema: {}", tableCount);

            // Test 4: Listar primeras 5 tablas
            log.info("📋 Primeras tablas del esquema:");
            jdbcTemplate.queryForList(
                    "SELECT TABLE_NAME FROM USER_TABLES WHERE ROWNUM <= 5 ORDER BY TABLE_NAME"
            ).forEach(row -> log.info("   - {}", row.get("TABLE_NAME")));

        } catch (Exception e) {
            log.error("❌ Error al conectar con Oracle: {}", e.getMessage());
            log.error("Detalles del error:", e);
        }
    }
}
