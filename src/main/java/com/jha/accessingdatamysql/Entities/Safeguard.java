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
public class Safeguard {
    @Id
    @Column(name = "SafeguardID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer safeguardID;
  
    public Integer getSafeGuardID() {
        return safeguardID;
    }

    @NotNull @NotBlank
    private String safetyPrecaution;

    //Foreign Key to match SafeGuards to one Task
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

    public String getSafetyPrecaution() {
        return safetyPrecaution;
    }
    
    public void setSafetyPrecaution(String safetyPrecaution) {
        this.safetyPrecaution = safetyPrecaution;
    }
}
