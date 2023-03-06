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
import com.jha.accessingdatamysql.Entities.Safeguard;
import com.jha.accessingdatamysql.Entities.Task;
import com.jha.accessingdatamysql.repositories.SafeGuardRepository;
import com.jha.accessingdatamysql.repositories.TaskRepository;
import jakarta.validation.Valid;

@RestController 
@RequestMapping(path="/safeGuard") 
public class SafeGuardController {
    @Autowired 
    private TaskRepository taskRepository;
    @Autowired 
    private SafeGuardRepository safeGuardRepository;

    @GetMapping("/{taskID}")
    public ResponseEntity<List<Safeguard>> getAllSafeGuardsByHazardID(@PathVariable(value = "taskID") Integer taskID) {
      List<Safeguard> safeGuards = safeGuardRepository.findByTaskTaskID(taskID);
      return new ResponseEntity<>(safeGuards, HttpStatus.OK);
    }

    @PostMapping(path="/{taskID}") 
    public ResponseEntity<Safeguard> addNewSafeGuard(@PathVariable(value = "taskID") Integer taskID,
        @Valid @RequestParam String safetyPrecaution) {
            
            List<Safeguard> safeGuards = safeGuardRepository.findByTaskTaskID(taskID);
            boolean safeGuardAlreadyAdded = safeGuards.stream().anyMatch(safeGuard -> safetyPrecaution.equals(safeGuard.getSafetyPrecaution()));
            if (safeGuardAlreadyAdded){
              throw new ItemAlreadyExistsException("The precation "  + safetyPrecaution + " already exists for this hazard");
            }

            Task task = taskRepository.findByTaskID(taskID);
            Safeguard newSafeGuard = new Safeguard();
            newSafeGuard.setTask(task);
            newSafeGuard.setSafetyPrecaution(safetyPrecaution);
            safeGuardRepository.save(newSafeGuard);
            return new ResponseEntity<Safeguard>(newSafeGuard, HttpStatus.CREATED);
    }

    @PutMapping(path="/{safeGuardID}") 
    public ResponseEntity<Safeguard> updateSafeGuard(@PathVariable(value = "safeGuardID") Integer safeGuardID,
    @Valid @RequestParam String safetyPrecaution) {

      Safeguard safeGuard = safeGuardRepository.findBySafeguardID(safeGuardID);
      if (safeGuard == null){
        throw new ItemNotFoundException("The safety precaution you are trying to update does not exist");
      }
      safeGuard.setSafetyPrecaution(safetyPrecaution);
      safeGuardRepository.save(safeGuard);
      return new ResponseEntity<Safeguard>(safeGuard, HttpStatus.OK);
    }

    @DeleteMapping(path="/{safeGuardID}")
    public ResponseEntity<HttpStatus> deleteSafeGuard(@PathVariable(value = "safeGuardID") Integer safeGuardID) throws NotFoundException {
      if (safeGuardRepository.findBySafeguardID(safeGuardID) == null) {
        throw new ItemNotFoundException("The safety precaution you are trying to delete does not exist");
      }
      safeGuardRepository.deleteBySafeguardID(safeGuardID);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } 
}
