package edu.basedatos.oracleexample.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;



public class MongoDBConnection {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    public static MongoDatabase connect() {
        try {
            ConnectionString connectionString = new ConnectionString(
                    "mongodb://mico:sanaed@localhost:27017/?authSource=admin"
            );

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();

            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase("testdb");

            // Verificar conexión
            database.listCollectionNames().first();
            System.out.println("✓ Conexión exitosa a MongoDB");

            return database;
        } catch (Exception e) {
            System.err.println("✗ Error al conectar con MongoDB: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static MongoDatabase getDatabase() {
        if (database == null) {
            return connect();
        }
        return database;
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Conexión cerrada");
        }
    }
}
