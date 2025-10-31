package edu.basedatos.oracleexample.repository;

import edu.basedatos.oracleexample.domain.Task;
import edu.basedatos.oracleexample.enums.Priority;
import edu.basedatos.oracleexample.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Task
 * Spring Data JPA genera automáticamente la implementación
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Buscar tareas por estado
     */
    List<Task> findByStatus(Status status);

    /**
     * Buscar tareas por prioridad
     */
    List<Task> findByPriority(Priority priority);

}