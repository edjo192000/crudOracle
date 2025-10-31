package edu.basedatos.oracleexample.dto;

import edu.basedatos.oracleexample.domain.Task;
import edu.basedatos.oracleexample.enums.Priority;
import edu.basedatos.oracleexample.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;


public class CreateTaskRequest {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título no puede exceder 200 caracteres")
    private String title;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String description;

    @NotNull(message = "La prioridad es obligatoria")
    private Priority priority;

    private Status status;

    private LocalDate dueDate;

    @Size(max = 100, message = "El nombre del asignado no puede exceder 100 caracteres")
    private String assignedTo;

    // Constructores
    public CreateTaskRequest() {
        this.status = Status.PENDING;
        this.priority = Priority.MEDIUM;
    }

    public CreateTaskRequest(String title, String description, Priority priority,
                             Status status, LocalDate dueDate, String assignedTo) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.dueDate = dueDate;
        this.assignedTo = assignedTo;
    }

    // Getters y Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
}