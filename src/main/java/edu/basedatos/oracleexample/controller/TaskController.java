package edu.basedatos.oracleexample.controller;

import edu.basedatos.oracleexample.dto.CreateTaskRequest;
import edu.basedatos.oracleexample.dto.UpdateTaskRequest;
import edu.basedatos.oracleexample.dto.TaskResponse;
import edu.basedatos.oracleexample.domain.Task;
import edu.basedatos.oracleexample.enums.Priority;
import edu.basedatos.oracleexample.enums.Status;
import edu.basedatos.oracleexample.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de tareas
 * Base URL: /api/tasks
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * GET /api/tasks
     * Obtener todas las tareas
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<TaskResponse> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    /**
     * GET /api/tasks/{id}
     * Obtener una tarea por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        TaskResponse task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    /**
     * POST /api/tasks
     * Crear una nueva tarea
     */
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody CreateTaskRequest request) {
        TaskResponse createdTask = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    /**
     * PUT /api/tasks/{id}
     * Actualizar una tarea existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request) {
        TaskResponse updatedTask = taskService.updateTask(id, request);
        return ResponseEntity.ok(updatedTask);
    }

    /**
     * DELETE /api/tasks/{id}
     * Eliminar una tarea
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Tarea eliminada exitosamente");
        response.put("taskId", id.toString());
        return ResponseEntity.ok(response);
    }

    /**
     * PATCH /api/tasks/{id}/complete
     * Marcar una tarea como completada
     */
    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> markAsCompleted(@PathVariable Long id) {
        TaskResponse task = taskService.markAsCompleted(id);
        return ResponseEntity.ok(task);
    }

    // ========== Endpoints de búsqueda ==========

    /**
     * GET /api/tasks/search/status/{status}
     * Buscar tareas por estado
     */
    @GetMapping("/search/status/{status}")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(
            @PathVariable Status status) {
        List<TaskResponse> tasks = taskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }

    /**
     * GET /api/tasks/search/priority/{priority}
     * Buscar tareas por prioridad
     */
    @GetMapping("/search/priority/{priority}")
    public ResponseEntity<List<TaskResponse>> getTasksByPriority(
            @PathVariable Priority priority) {
        List<TaskResponse> tasks = taskService.getTasksByPriority(priority);
        return ResponseEntity.ok(tasks);
    }

}