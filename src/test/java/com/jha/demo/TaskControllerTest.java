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
import com.jha.accessingdatamysql.Entities.JHA;
import com.jha.accessingdatamysql.Entities.Task;
import com.jha.accessingdatamysql.controllers.TaskController;
import com.jha.accessingdatamysql.repositories.JHARepository;
import com.jha.accessingdatamysql.repositories.TaskRepository;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = TaskController.class)
@ContextConfiguration(classes={AccessingDataMysqlApplication.class})
@WithMockUser
public class TaskControllerTest {

    @Autowired
	private MockMvc mockMvc;

    @MockBean
	private TaskRepository taskRepository;

    @MockBean
	private JHARepository jhaRepository;

    Task task1;
    Task task2;
    Task task3;
    JHA jha1;
    JHA jha2;
    JHA jha3;
    List<Task> listTask;
    LinkedMultiValueMap<String, String> requestParams;
    
    @BeforeEach
    public void innitTest(){
        jha1 = new JHA();
        jha1.setTitle("TheTitle");
        jha1.setAuthor("TheAuthor");

        jha2 = new JHA();
        jha2.setTitle("TheTitle2");
        jha2.setAuthor("TheAuthor2");
        
        task1 = new Task();
        task1.setJha(jha1);
        task1.setStep("TheStep");
        
        task2 = new Task();
        task2.setJha(jha2);
        task2.setStep("TheStep2");

        listTask = new ArrayList<>();
        listTask.add(task1);
        listTask.add(task2);

        requestParams = new LinkedMultiValueMap<>();
        
    }

    @Test
    public void testGetAllTasks() throws Exception{
        
        given(taskRepository.findByJhaTitle("TheTitle")).willReturn(listTask);
        
        ResultActions getResponse = mockMvc.perform(get("/task/TheTitle"));

        getResponse.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.size()", is(2)))
        .andExpect(jsonPath("$.[0].step").value("TheStep"))
        .andExpect(jsonPath("$.[1].step").value("TheStep2"));
    }

    @Test
    public void testValidPost() throws Exception{

        requestParams.add("step", "TheStep3");

        given(taskRepository.findByJhaTitle("TheTitle3")).willReturn(listTask);
        given(jhaRepository.findByTitle("TheTitle3")).willReturn(jha1);

        ResultActions postResponse = mockMvc.perform(post("/task/TheTitle3")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .params(requestParams));

        postResponse.andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.step").value("TheStep3"));
    }

    @Test
    public void testRedundantPost() throws Exception{

        requestParams.add("step", "TheStep");

        given(taskRepository.findByJhaTitle("TheTitle")).willReturn(listTask);
        given(jhaRepository.findByTitle("TheTitle")).willReturn(jha1);

        ResultActions postResponse = mockMvc.perform(post("/task/TheTitle")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .params(requestParams));

        postResponse.andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value("ITEM_ALREADY_EXISTS"))
        .andExpect(jsonPath("$.message").value("The task TheStep already exists for this JHA"));
    }

    @Test
    public void testValidPut() throws Exception{

        requestParams.add("step", "TheUpdatedStep");

        given(taskRepository.findByTaskID(1)).willReturn(task1);

        ResultActions postResponse = mockMvc.perform(put("/task/1")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .params(requestParams));

        postResponse.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.step").value("TheUpdatedStep"));
    }

    @Test
    public void testInValidPut() throws Exception{

        requestParams.add("step", "TheUpdatedStep");

        given(taskRepository.findByTaskID(1)).willReturn(null);

        ResultActions postResponse = mockMvc.perform(put("/task/1")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .params(requestParams));

        postResponse.andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value("ITEM_NOT_FOUND"))
        .andExpect(jsonPath("$.message").value("The task you are trying to update does not exist"));
    }

    @Test
    public void testDelete() throws Exception{
        
        given(taskRepository.findByTaskID(any(Integer.class))).willReturn(task1);

        ResultActions postResponse = mockMvc.perform(delete("/task/1")
                                            .contentType(MediaType.APPLICATION_JSON));

        postResponse.andExpect(status().isNoContent());
    }
    
}
