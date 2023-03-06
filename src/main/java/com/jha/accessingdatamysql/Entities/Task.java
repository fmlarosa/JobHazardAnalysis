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
public class Task {
    @Id
    @Column(name = "TaskID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer taskID;
  
    public Integer getTaskID() {
        return taskID;
    }

    @NotNull @NotBlank
    private String step;

    //Foreign Key to match Tasks to one JHA
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="JhaID", nullable = false)
    @JsonIgnore
    private JHA jha;
    

    public JHA getJha() {
        return jha;
    }

    public void setJha(JHA jha) {
        this.jha = jha;
    }

    public String getStep() {
        return step;
    }
    
    public void setStep(String step) {
        this.step = step;
    }
    
}