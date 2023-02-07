package com.jha.accessingdatamysql.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jha.Exceptions.ItemAlreadyExistsException;
import com.jha.Exceptions.ItemNotFoundException;
import com.jha.accessingdatamysql.Entities.JHA;
import com.jha.accessingdatamysql.Entities.Task;
import com.jha.accessingdatamysql.repositories.JHARepository;
import com.jha.accessingdatamysql.repositories.TaskRepository;
import jakarta.validation.Valid;

@RestController 
@RequestMapping(path="/task") 
public class TaskController {

    @Autowired 
    private JHARepository jhaRepository;
    @Autowired 
    private TaskRepository taskRepository;

    @GetMapping("/{title}")
    public ResponseEntity<List<Task>> getAllTasksByJhaTitle(@PathVariable(value = "title") String title) {

      List<Task> tasks = taskRepository.findByJhaTitle(title);
      return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping(path="/{title}") 
    public ResponseEntity<Task> addNewTask(@PathVariable(value = "title") String title,
        @Valid @RequestParam String step) {

          List<Task> tasks = taskRepository.findByJhaTitle(title);
          boolean stepAlreadyAdded = tasks.stream().anyMatch(task -> step.equals(task.getStep()));
          if (stepAlreadyAdded){
            throw new ItemAlreadyExistsException("The task "  + step + " already exists for this JHA");
          }
          JHA jha = jhaRepository.findByTitle(title);
          Task newTask = new Task();
          newTask.setJha(jha);
          newTask.setStep(step); 
          taskRepository.save(newTask);
          return new ResponseEntity<Task>(newTask, HttpStatus.CREATED);
    }

    @PutMapping(path="/{taskID}") 
    public ResponseEntity<Task> updateTask(@PathVariable(value = "taskID") Integer taskID,
    @Valid @RequestParam String step) {

      Task task = taskRepository.findByTaskID(taskID);
      if (task == null){
        throw new ItemNotFoundException("The task you are trying to update does not exist");
      }
      task.setStep(step);
      taskRepository.save(task);
      return new ResponseEntity<Task>(task, HttpStatus.OK);
    }

    @DeleteMapping(path="/{taskID}") 
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable(value = "taskID") Integer taskID) throws NotFoundException {
      if (taskRepository.findByTaskID(taskID) == null) {
        throw new ItemNotFoundException("The task you are trying to delete does not exist");
      }
      taskRepository.deleteByTaskID(taskID);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } 
}
