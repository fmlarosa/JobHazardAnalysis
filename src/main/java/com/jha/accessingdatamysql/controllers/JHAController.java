package com.jha.accessingdatamysql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.jha.accessingdatamysql.repositories.JHARepository;
import jakarta.validation.Valid;

//Allows CRUD functionality for JHA
@RestController 
@RequestMapping(path="/jha")
public class JHAController {
  @Autowired 
  private JHARepository jhaRepository;

  @PostMapping(path="/add") 
  public ResponseEntity<JHA> addNewUser(@Valid @RequestParam String title,
      @Valid @RequestParam String author) {
        if (jhaRepository.findByTitle(title) != null){
          throw new ItemAlreadyExistsException(title + " already exists in database");
        }
          JHA newJha = new JHA();
          newJha.setTitle(title);
          newJha.setAuthor(author);
          jhaRepository.save(newJha);
          return new ResponseEntity<JHA>(newJha, HttpStatus.CREATED); 
  }

  @GetMapping(path="/all")
  public Iterable<JHA> getAllUsers() {
    return jhaRepository.findAll();
  }

  @GetMapping(path="/{Title}")
  public ResponseEntity<JHA> getJHA(@PathVariable("Title") String title) {
    if ((title == null) || jhaRepository.findByTitle(title) == null) {
      throw new ItemNotFoundException(title + " does not exist");
    }
      return new ResponseEntity<JHA>(jhaRepository.findByTitle(title), HttpStatus.OK);
  }
  
  @DeleteMapping(path="/{Title}")
  public ResponseEntity<String> deleteJHA(@PathVariable("Title") String title) {
    if ((title == null) || jhaRepository.findByTitle(title) == null) {
      throw new ItemNotFoundException(title + " Does not exist");
    }
    jhaRepository.deleteByTitle(title);
    return new ResponseEntity<String>(title + " has been deleted", HttpStatus.NO_CONTENT);
  }

  @PutMapping(path="/{Title}/Author")
  public ResponseEntity<JHA> putJhaAuthor(@PathVariable("Title") String title, @RequestParam String author) {
    JHA existJHA = jhaRepository.findByTitle(title);
    if ((existJHA == null)) {
      throw new ItemNotFoundException(title + " does not exist");
    }
    existJHA.setAuthor(author);
    return new ResponseEntity<JHA>(existJHA, HttpStatus.OK);
  }
}