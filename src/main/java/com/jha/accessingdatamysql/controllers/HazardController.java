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
import com.jha.accessingdatamysql.Entities.Hazard;
import com.jha.accessingdatamysql.Entities.Task;
import com.jha.accessingdatamysql.repositories.HazardRepository;
import com.jha.accessingdatamysql.repositories.TaskRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path="/hazard")
public class HazardController {
    @Autowired 
    private TaskRepository taskRepository;
    @Autowired
    private HazardRepository hazardRepository;

    @GetMapping("/{taskID}")
    public ResponseEntity<List<Hazard>> getAllHazardsByTaskID(@PathVariable(value = "taskID") Integer taskID) {
      List<Hazard> hazards = hazardRepository.findByTaskTaskID(taskID);
      return new ResponseEntity<>(hazards, HttpStatus.OK);
    }

    @PostMapping(path="/{taskID}")
    public ResponseEntity<Hazard> addNewHazard(@PathVariable(value = "taskID") Integer taskID,
        @Valid @RequestParam String danger) {
            
        List<Hazard> hazards = hazardRepository.findByTaskTaskID(taskID);
        boolean hazardAlreadyAdded = hazards.stream().anyMatch(hazard -> danger.equals(hazard.getDanger()));
        if (hazardAlreadyAdded){
          throw new ItemAlreadyExistsException("The hazard "  + danger + " already exists for this task");
        }
        Task task = taskRepository.findByTaskID(taskID);
        Hazard newHazard = new Hazard();
        newHazard.setTask(task);
        newHazard.setDanger(danger);
        hazardRepository.save(newHazard);
        return new ResponseEntity<Hazard>(newHazard, HttpStatus.CREATED);
    }

    @PutMapping(path="/{hazardID}") 
    public ResponseEntity<Hazard> updateHazard(@PathVariable(value = "hazardID") Integer hazardID,
    @Valid @RequestParam String danger) {

      Hazard hazard = hazardRepository.findByHazardID(hazardID);
      if (hazard == null){
        throw new ItemNotFoundException("The hazard you are trying to update does not exist");
      }
      hazard.setDanger(danger);
      hazardRepository.save(hazard);
      return new ResponseEntity<Hazard>(hazard, HttpStatus.OK);
    }

    @DeleteMapping(path="/{hazardID}") 
    public ResponseEntity<HttpStatus> deleteHazard(@PathVariable(value = "hazardID") Integer hazardID) throws NotFoundException {
      if (hazardRepository.findByHazardID(hazardID) == null) {
        throw new ItemNotFoundException("The hazard you are trying to delete does not exist");
      }
      hazardRepository.deleteByHazardID(hazardID);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } 
}
