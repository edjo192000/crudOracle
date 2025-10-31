package edu.basedatos.oracleexample.service;

import edu.basedatos.oracleexample.dto.CreateTaskRequest;
import edu.basedatos.oracleexample.dto.UpdateTaskRequest;
import edu.basedatos.oracleexample.dto.TaskResponse;
import edu.basedatos.oracleexample.domain.Task;
import edu.basedatos.oracleexample.enums.Priority;
import edu.basedatos.oracleexample.enums.Status;
import edu.basedatos.oracleexample.exception.ResourceNotFoundException;
import edu.basedatos.oracleexample.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de tareas
 * Contiene la lógica de negocio
 */
@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Obtener todas las tareas
     */
    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(TaskResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Obtener una tarea por ID
     */
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con ID: " + id));
        return new TaskResponse(task);
    }

    /**
     * Crear una nueva tarea
     */
    public TaskResponse createTask(CreateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority() != null ? request.getPriority() : Priority.MEDIUM);
        task.setStatus(request.getStatus() != null ? request.getStatus() : Status.PENDING);
        task.setDueDate(request.getDueDate());
        task.setAssignedTo(request.getAssignedTo());

        Task savedTask = taskRepository.save(task);
        return new TaskResponse(savedTask);
    }

    /**
     * Actualizar una tarea existente
     */
    public TaskResponse updateTask(Long id, UpdateTaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con ID: " + id));

        // Actualizar solo los campos que no son nulos en el request
        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        if (request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }
        if (request.getAssignedTo() != null) {
            task.setAssignedTo(request.getAssignedTo());
        }

        Task updatedTask = taskRepository.save(task);
        return new TaskResponse(updatedTask);
    }

    /**
     * Eliminar una tarea
     */
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tarea no encontrada con ID: " + id);
        }
        taskRepository.deleteById(id);
    }

    /**
     * Buscar tareas por estado
     */
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByStatus(Status status) {
        return taskRepository.findByStatus(status)
                .stream()
                .map(TaskResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Buscar tareas por prioridad
     */
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByPriority(Priority priority) {
        return taskRepository.findByPriority(priority)
                .stream()
                .map(TaskResponse::new)
                .collect(Collectors.toList());
    }


    /**
     * Marcar tarea como completada
     */
    public TaskResponse markAsCompleted(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada con ID: " + id));

        task.setStatus(Status.COMPLETED);
        Task updatedTask = taskRepository.save(task);
        return new TaskResponse(updatedTask);
    }

}