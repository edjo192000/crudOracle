package edu.basedatos.oracleexample.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationRepository {
    private final MongoCollection<Document> collection;

    public NotificationRepository() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.collection = database.getCollection("notifications");
    }

    // ============== CREATE ==============

    /**
     * Crea una notificación
     * @param type Tipo: "visitor_arrival", "visitor_departure", "access_alert", "system"
     * @param channel Canal: "email", "sms", "push", "whatsapp"
     * @param recipient Destinatario (email, teléfono, userId)
     * @param subject Asunto de la notificación
     * @param message Cuerpo del mensaje
     * @param priority Prioridad: "low", "medium", "high", "urgent"
     */
    public String create(String type, String channel, String recipient,
                         String subject, String message, String priority) {
        Document notification = new Document("type", type)
                .append("channel", channel)
                .append("recipient", recipient)
                .append("subject", subject)
                .append("message", message)
                .append("priority", priority)
                .append("status", "pending")
                .append("sentAt", null)
                .append("deliveredAt", null)
                .append("readAt", null)
                .append("attempts", 0)
                .append("createdAt", new Date())
                .append("updatedAt", new Date());

        collection.insertOne(notification);
        String id = notification.getObjectId("_id").toString();
        System.out.println("📨 Notificación creada [" + channel + "] - ID: " + id);
        return id;
    }

    /**
     * Sobrecarga: Crear notificación con metadata adicional
     */
    public String create(String type, String channel, String recipient,
                         String subject, String message, String priority,
                         Document metadata) {
        Document notification = new Document("type", type)
                .append("channel", channel)
                .append("recipient", recipient)
                .append("subject", subject)
                .append("message", message)
                .append("priority", priority)
                .append("status", "pending")
                .append("metadata", metadata)
                .append("sentAt", null)
                .append("deliveredAt", null)
                .append("readAt", null)
                .append("attempts", 0)
                .append("createdAt", new Date())
                .append("updatedAt", new Date());

        collection.insertOne(notification);
        String id = notification.getObjectId("_id").toString();
        System.out.println("📨 Notificación creada [" + channel + "] - ID: " + id);
        return id;
    }

    // ============== READ ALL ==============

    /**
     * Lee todas las notificaciones (ordenadas por fecha)
     */
    public List<Document> readAll() {
        List<Document> notifications = new ArrayList<>();
        collection.find()
                .sort(Sorts.descending("createdAt"))
                .into(notifications);
        System.out.println("📬 Total de notificaciones: " + notifications.size());
        return notifications;
    }

    /**
     * Lee notificaciones por estado
     */
    public List<Document> readByStatus(String status) {
        List<Document> notifications = new ArrayList<>();
        collection.find(Filters.eq("status", status))
                .sort(Sorts.descending("createdAt"))
                .into(notifications);
        System.out.println("📬 Notificaciones con estado '" + status + "': " + notifications.size());
        return notifications;
    }

    /**
     * Lee notificaciones por canal
     */
    public List<Document> readByChannel(String channel) {
        List<Document> notifications = new ArrayList<>();
        collection.find(Filters.eq("channel", channel))
                .sort(Sorts.descending("createdAt"))
                .into(notifications);
        System.out.println("📬 Notificaciones vía " + channel + ": " + notifications.size());
        return notifications;
    }

    /**
     * Lee notificaciones por destinatario
     */
    public List<Document> readByRecipient(String recipient) {
        List<Document> notifications = new ArrayList<>();
        collection.find(Filters.eq("recipient", recipient))
                .sort(Sorts.descending("createdAt"))
                .into(notifications);
        System.out.println("📬 Notificaciones para " + recipient + ": " + notifications.size());
        return notifications;
    }

    /**
     * Lee notificaciones por prioridad
     */
    public List<Document> readByPriority(String priority) {
        List<Document> notifications = new ArrayList<>();
        collection.find(Filters.eq("priority", priority))
                .sort(Sorts.descending("createdAt"))
                .into(notifications);
        System.out.println("📬 Notificaciones con prioridad '" + priority + "': " + notifications.size());
        return notifications;
    }

    // ============== READ ONE ==============

    /**
     * Lee una notificación específica por ID
     */
    public Document readOne(String id) {
        Document notification = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        if (notification != null) {
            System.out.println("📄 Notificación encontrada: " + notification.getString("subject"));
        } else {
            System.out.println("❌ Notificación no encontrada");
        }
        return notification;
    }

    // ============== UPDATE ==============

    /**
     * Actualiza una notificación completa
     */
    public boolean update(String id, String subject, String message, String priority) {
        long modifiedCount = collection.updateOne(
                Filters.eq("_id", new ObjectId(id)),
                Updates.combine(
                        Updates.set("subject", subject),
                        Updates.set("message", message),
                        Updates.set("priority", priority),
                        Updates.set("updatedAt", new Date())
                )
        ).getModifiedCount();

        boolean updated = modifiedCount > 0;
        System.out.println(updated ? "✓ Notificación actualizada" : "❌ No se actualizó");
        return updated;
    }

    /**
     * Marca notificación como enviada
     */
    public boolean markAsSent(String id) {
        long modifiedCount = collection.updateOne(
                Filters.eq("_id", new ObjectId(id)),
                Updates.combine(
                        Updates.set("status", "sent"),
                        Updates.set("sentAt", new Date()),
                        Updates.inc("attempts", 1),
                        Updates.set("updatedAt", new Date())
                )
        ).getModifiedCount();

        boolean updated = modifiedCount > 0;
        System.out.println(updated ? "✓ Marcada como enviada" : "❌ No se actualizó");
        return updated;
    }

    /**
     * Marca notificación como entregada
     */
    public boolean markAsDelivered(String id) {
        long modifiedCount = collection.updateOne(
                Filters.eq("_id", new ObjectId(id)),
                Updates.combine(
                        Updates.set("status", "delivered"),
                        Updates.set("deliveredAt", new Date()),
                        Updates.set("updatedAt", new Date())
                )
        ).getModifiedCount();

        boolean updated = modifiedCount > 0;
        System.out.println(updated ? "✓ Marcada como entregada" : "❌ No se actualizó");
        return updated;
    }

    /**
     * Marca notificación como leída
     */
    public boolean markAsRead(String id) {
        long modifiedCount = collection.updateOne(
                Filters.eq("_id", new ObjectId(id)),
                Updates.combine(
                        Updates.set("status", "read"),
                        Updates.set("readAt", new Date()),
                        Updates.set("updatedAt", new Date())
                )
        ).getModifiedCount();

        boolean updated = modifiedCount > 0;
        System.out.println(updated ? "✓ Marcada como leída" : "❌ No se actualizó");
        return updated;
    }

    /**
     * Marca notificación como fallida
     */
    public boolean markAsFailed(String id, String errorMessage) {
        long modifiedCount = collection.updateOne(
                Filters.eq("_id", new ObjectId(id)),
                Updates.combine(
                        Updates.set("status", "failed"),
                        Updates.set("error", errorMessage),
                        Updates.inc("attempts", 1),
                        Updates.set("updatedAt", new Date())
                )
        ).getModifiedCount();

        boolean updated = modifiedCount > 0;
        System.out.println(updated ? "✓ Marcada como fallida" : "❌ No se actualizó");
        return updated;
    }

    // ============== DELETE ==============

    /**
     * Elimina una notificación
     */
    public boolean delete(String id) {
        long deletedCount = collection.deleteOne(
                Filters.eq("_id", new ObjectId(id))
        ).getDeletedCount();

        boolean deleted = deletedCount > 0;
        System.out.println(deleted ? "✓ Notificación eliminada" : "❌ No se eliminó");
        return deleted;
    }

    /**
     * Elimina notificaciones antiguas (limpieza)
     * @param daysOld Días de antigüedad
     */
    public long deleteOldNotifications(int daysOld) {
        Date cutoffDate = new Date(System.currentTimeMillis() - (daysOld * 24L * 60 * 60 * 1000));
        long deletedCount = collection.deleteMany(
                Filters.lt("createdAt", cutoffDate)
        ).getDeletedCount();

        System.out.println("🗑️ Notificaciones eliminadas (>" + daysOld + " días): " + deletedCount);
        return deletedCount;
    }
}