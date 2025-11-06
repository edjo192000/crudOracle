package edu.basedatos.oracleexample.mongo;

import edu.basedatos.oracleexample.mongo.MongoDBConnection;
import edu.basedatos.oracleexample.mongo.NotificationRepository;
import org.bson.Document;

public class MainMongo {
    public static void main(String[] args) {
        // Conectar
        MongoDBConnection.connect();

        // Crear repositorio
        NotificationRepository repo = new NotificationRepository();

        // ========== CREATE - Notificación de llegada de visitante ==========
        Document metadata = new Document("visitorName", "María González")
                .append("hostName", "Juan Pérez")
                .append("area", "Edificio A - Piso 3");

        String notif1 = repo.create(
                "visitor_arrival",
                "email",
                "juan.perez@utneza.edu.mx",
                "Visitante en recepción",
                "María González ha llegado y solicita acceso al área de TI.",
                "high",
                metadata
        );

        // CREATE - Notificación por WhatsApp
        String notif2 = repo.create(
                "visitor_arrival",
                "whatsapp",
                "+52 555 123 4567",
                "Visitante esperando",
                "María González está en recepción. ¿Autoriza su acceso?",
                "urgent"
        );

        // CREATE - Notificación de alerta de sistema
        String notif3 = repo.create(
                "system",
                "push",
                "admin_user_123",
                "Alerta del sistema",
                "Se detectaron 3 intentos fallidos de acceso no autorizado.",
                "urgent"
        );

        // ========== READ ALL ==========
        System.out.println("\n=== TODAS LAS NOTIFICACIONES ===");
        repo.readAll();

        // ========== READ BY STATUS ==========
        System.out.println("\n=== NOTIFICACIONES PENDIENTES ===");
        repo.readByStatus("pending");

        // ========== READ BY CHANNEL ==========
        System.out.println("\n=== NOTIFICACIONES POR EMAIL ===");
        repo.readByChannel("email");

        // ========== READ BY RECIPIENT ==========
        System.out.println("\n=== NOTIFICACIONES DE JUAN PÉREZ ===");
        repo.readByRecipient("juan.perez@utneza.edu.mx");

        // ========== READ BY PRIORITY ==========
        System.out.println("\n=== NOTIFICACIONES URGENTES ===");
        repo.readByPriority("urgent");

        // ========== READ ONE ==========
        System.out.println("\n=== DETALLE DE NOTIFICACIÓN ===");
        repo.readOne(notif1);

        // ========== UPDATE STATUS ==========
        System.out.println("\n=== ACTUALIZANDO ESTADOS ===");
        repo.markAsSent(notif1);
        repo.markAsDelivered(notif1);
        repo.markAsRead(notif1);

        repo.markAsSent(notif2);
        repo.markAsFailed(notif2, "Número de WhatsApp no válido");

        // ========== UPDATE ==========
        System.out.println("\n=== ACTUALIZANDO CONTENIDO ===");
        repo.update(notif3,
                "Alerta de seguridad CRÍTICA",
                "Se detectaron 5 intentos fallidos. Revisar logs inmediatamente.",
                "urgent"
        );

        // ========== DELETE OLD ==========
        System.out.println("\n=== LIMPIEZA DE NOTIFICACIONES ANTIGUAS ===");
        repo.deleteOldNotifications(30); // Elimina notificaciones de más de 30 días

        // ========== DELETE ONE ==========
        // repo.delete(notif3);

        // Cerrar conexión
        MongoDBConnection.close();
    }
}
