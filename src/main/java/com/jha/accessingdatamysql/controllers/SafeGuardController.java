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
import com.jha.accessingdatamysql.Entities.Safeguard;
import com.jha.accessingdatamysql.repositories.HazardRepository;
import com.jha.accessingdatamysql.repositories.SafeGuardRepository;
import jakarta.validation.Valid;

@RestController 
@RequestMapping(path="/safeGuard") 
public class SafeGuardController {
    @Autowired 
    private HazardRepository hazardRepository;
    @Autowired 
    private SafeGuardRepository safeGuardRepository;

    @GetMapping("/{hazardID}")
    public ResponseEntity<List<Safeguard>> getAllSafeGuardsByHazardID(@PathVariable(value = "hazardID") Integer hazardID) {
      List<Safeguard> safeGuards = safeGuardRepository.findByHazardHazardID(hazardID);
      return new ResponseEntity<>(safeGuards, HttpStatus.OK);
    }

    @PostMapping(path="/{hazardID}") 
    public ResponseEntity<Safeguard> addNewSafeGuard(@PathVariable(value = "hazardID") Integer hazardID,
        @Valid @RequestParam String safetyPrecaution) {
            
            List<Safeguard> safeGuards = safeGuardRepository.findByHazardHazardID(hazardID);
            boolean safeGuardAlreadyAdded = safeGuards.stream().anyMatch(safeGuard -> safetyPrecaution.equals(safeGuard.getSafetyPrecaution()));
            if (safeGuardAlreadyAdded){
              throw new ItemAlreadyExistsException("The precation "  + safetyPrecaution + " already exists for this hazard");
            }

            Hazard hazard = hazardRepository.findByHazardID(hazardID);
            Safeguard newSafeGuard = new Safeguard();
            newSafeGuard.setHazard(hazard);
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
