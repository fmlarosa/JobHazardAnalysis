package com.jha.accessingdatamysql.Entities;

import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity 
public class JHA {

  //Primary Key for JHA
  @Id
  @Column(name = "JhaID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer jhaID;


  @Column(unique=true)
  @NotNull @NotBlank
  private String title;



  @NotNull @NotBlank
  private String author;

  //This allows cascading deletions 
  @OneToMany(mappedBy="jha", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
  @JsonIgnore
  private Set<Task> tasks;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }
}
