package com.jha.accessingdatamysql.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.jha.accessingdatamysql.Entities.Task;
import jakarta.transaction.Transactional;


public interface TaskRepository extends CrudRepository<Task, Integer>{
    List<Task> findByJhaTitle(String title);
    Task findByTaskID(Integer taskID);
    @Transactional
    Integer deleteByTaskID(Integer taskID);
}
