package com.jha.demo;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.test.web.servlet.MockMvc;
import com.jha.accessingdatamysql.AccessingDataMysqlApplication;
import com.jha.accessingdatamysql.Entities.Hazard;
import com.jha.accessingdatamysql.Entities.JHA;
import com.jha.accessingdatamysql.Entities.Task;
import com.jha.accessingdatamysql.controllers.HazardController;
import com.jha.accessingdatamysql.repositories.HazardRepository;
import com.jha.accessingdatamysql.repositories.TaskRepository;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = HazardController.class)
@ContextConfiguration(classes={AccessingDataMysqlApplication.class})
@WithMockUser
public class HazardControllerTest {
    @Autowired
	private MockMvc mockMvc;

    @MockBean
	private TaskRepository taskRepository;

    @MockBean
	private HazardRepository hazardRepository;

    Hazard hazard1;
    Hazard hazard2;
    Hazard hazard3;
    Task task1;
    Task task2;
    Task task3;
    JHA jha1;
    List<Hazard> listHazard;
    LinkedMultiValueMap<String, String> requestParams;
    
    @BeforeEach
    public void innitTest(){
        JHA jha1 = new JHA();
        task1 = new Task();
        task1.setJha(jha1);
        task1.setStep("TheStep");

        task2 = new Task();
        task2.setJha(jha1);
        task2.setStep("TheStep2");;
        
        hazard1 = new Hazard();
        hazard1.setTask(task1);
        hazard1.setDanger("TheDanger");
        
        hazard2 = new Hazard();
        hazard2.setTask(task1);
        hazard2.setDanger("TheDanger2");

        listHazard = new ArrayList<>();
        listHazard.add(hazard1);
        listHazard.add(hazard2);

        requestParams = new LinkedMultiValueMap<>();
    }

    @Test
    public void testGetAllHazards() throws Exception{
        given(hazardRepository.findByTaskTaskID(1)).willReturn(listHazard);
        
        ResultActions getResponse = mockMvc.perform(get("/hazard/1"));

        getResponse.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.size()", is((2))))
        .andExpect(jsonPath("$.[0].danger").value("TheDanger"))
        .andExpect(jsonPath("$.[1].danger").value("TheDanger2"));
    }

    @Test
    public void testValidPost() throws Exception{
        requestParams.add("danger", "TheDanger3");

        given(hazardRepository.findByTaskTaskID(1)).willReturn(listHazard);
        given(taskRepository.findByTaskID(1)).willReturn(task1);

        ResultActions postResponse = mockMvc.perform(post("/hazard/1")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .params(requestParams));

        postResponse.andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.danger").value("TheDanger3"));
    }

    @Test
    public void testRedundantPost() throws Exception{
        requestParams.add("danger", "TheDanger");

        given(hazardRepository.findByTaskTaskID(1)).willReturn(listHazard);
        given(taskRepository.findByTaskID(1)).willReturn(task1);

        ResultActions postResponse = mockMvc.perform(post("/hazard/1")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .params(requestParams));

        postResponse.andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value("ITEM_ALREADY_EXISTS"))
        .andExpect(jsonPath("$.message").value("The hazard TheDanger already exists for this task"));
    }

    @Test
    public void testValidPut() throws Exception{
        requestParams.add("danger", "TheUpdatedDanger");

        given(hazardRepository.findByHazardID(1)).willReturn(hazard1);

        ResultActions postResponse = mockMvc.perform(put("/hazard/1")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .params(requestParams));

        postResponse.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.danger").value("TheUpdatedDanger"));
    }

    @Test
    public void testInValidPut() throws Exception{
        requestParams.add("danger", "TheUpdatedDanger");

        given(hazardRepository.findByHazardID(1)).willReturn(null);

        ResultActions postResponse = mockMvc.perform(put("/hazard/1")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .params(requestParams));

        postResponse.andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value("ITEM_NOT_FOUND"))
        .andExpect(jsonPath("$.message").value("The hazard you are trying to update does not exist"));
    }

    @Test
    public void testDelete() throws Exception{
        given(hazardRepository.findByHazardID(1)).willReturn(hazard1);

        ResultActions postResponse = mockMvc.perform(delete("/hazard/1")
                                            .contentType(MediaType.APPLICATION_JSON));

        postResponse.andExpect(status().isNoContent());
    }
}
