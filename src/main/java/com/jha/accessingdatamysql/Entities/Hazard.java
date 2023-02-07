package com.jha.accessingdatamysql.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Hazard {
    @Id
    @Column(name = "HazardID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer hazardID;
  
    public Integer getHazardID() {
        return hazardID;
    }

    @NotNull @NotBlank
    private String danger;

    //Foreign Key to match Hazards to one Task
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="TaskID", nullable = false)
    @JsonIgnore
    private Task task;
    

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getDanger() {
        return danger;
    }
    
    public void setDanger(String danger) {
        this.danger = danger;
    }
}
