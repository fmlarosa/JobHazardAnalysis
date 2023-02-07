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
import com.jha.accessingdatamysql.controllers.JHAController;
import com.jha.accessingdatamysql.repositories.JHARepository;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


//Some very basic unit tests for the API, I would have liked to test more extensively but I ran out of time
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = JHAController.class)
@ContextConfiguration(classes={AccessingDataMysqlApplication.class})
@WithMockUser
public class JHAControllerTest {
    @Autowired
	private MockMvc mockMvc;

    @MockBean
	private JHARepository jhaRepository;

    JHA jha1;
    JHA jha2;
    JHA jha3;
    List<JHA> listJha;
    LinkedMultiValueMap<String, String> requestParams;

    @BeforeEach
    public void innitTest(){
        jha1 = new JHA();
        jha1.setTitle("TheTitle");
        jha1.setAuthor("TheAuthor");

        jha2 = new JHA();
        jha2.setTitle("TheTitle2");
        jha2.setAuthor("TheAuthor2");
        
        jha3 = new JHA();
        jha3.setTitle("TheTitle3");
        jha3.setAuthor("TheAuthor3");

        listJha = new ArrayList<>();
        listJha.add(jha1);
        listJha.add(jha2);

        requestParams = new LinkedMultiValueMap<>();
        requestParams.add("title", "TheTitle3");
        requestParams.add("author", "TheAuthor3");
    }

    @Test
    public void testGetAll() throws Exception{
        
        given(jhaRepository.findAll()).willReturn(listJha);
        
        ResultActions getResponse = mockMvc.perform(get("/jha/all"));

        getResponse.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.size()", is(2)))
        .andExpect(jsonPath("$.[0].title").value("TheTitle"))
        .andExpect(jsonPath("$.[1].author").value("TheAuthor2"));
    }

    @Test
    public void testValidGetByTitle() throws Exception{
    
        given(jhaRepository.findByTitle("TheTitle")).willReturn(jha1);

        ResultActions getResponse = mockMvc.perform(get("/jha/TheTitle"));

        getResponse.andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("TheTitle"))
        .andExpect(jsonPath("$.author").value("TheAuthor"));

    }

    @Test
    public void testInvalidGetByTitle() throws Exception{
        
        given(jhaRepository.findByTitle("TheTitle")).willReturn(jha1);

        ResultActions getResponse = mockMvc.perform(get("/jha/ABadTitle"));

        getResponse.andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value("ITEM_NOT_FOUND"))
        .andExpect(jsonPath("$.message").value("ABadTitle does not exist"));
    }

    @Test
    public void testValidPost() throws Exception{

        given(jhaRepository.save(any(JHA.class))).willReturn(jha3);

        ResultActions postResponse = mockMvc.perform(post("/jha/add")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .params(requestParams));

        postResponse.andExpect(status().isCreated())
        .andExpect(jsonPath("$.title").value("TheTitle3"))
        .andExpect(jsonPath("$.author").value("TheAuthor3"));
    }

    @Test
    public void testRedundantPost() throws Exception{
        
        given(jhaRepository.findByTitle(any(String.class))).willReturn(jha3);

        ResultActions postResponse = mockMvc.perform(post("/jha/add")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .params(requestParams));

        postResponse.andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value("ITEM_ALREADY_EXISTS"))
        .andExpect(jsonPath("$.message").value("TheTitle3 already exists in database"));
    }

    @Test
    public void testValidPut() throws Exception{
        
    
        requestParams.remove("author");
        requestParams.add("author","UpdatedAuthor");
        
        given(jhaRepository.findByTitle(any(String.class))).willReturn(jha3);

        ResultActions postResponse = mockMvc.perform(put("/jha/TheTitle3/Author")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .params(requestParams));

        postResponse.andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("TheTitle3"))
        .andExpect(jsonPath("$.author").value("UpdatedAuthor"));
    }

    @Test
    public void testNonExistingPut() throws Exception{
        requestParams.remove("author");
        requestParams.add("author","UpdatedAuthor");
        
        given(jhaRepository.findByTitle(any(String.class))).willReturn(null);

        ResultActions postResponse = mockMvc.perform(put("/jha/TheTitle3/Author")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .params(requestParams));

        postResponse.andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value("ITEM_NOT_FOUND"))
        .andExpect(jsonPath("$.message").value("TheTitle3 does not exist"));
    }

    @Test
    public void testDelete() throws Exception{
        
        given(jhaRepository.findByTitle(any(String.class))).willReturn(jha3);

        ResultActions postResponse = mockMvc.perform(delete("/jha/TheTitle3")
                                            .contentType(MediaType.APPLICATION_JSON));

        postResponse.andExpect(status().isNoContent());
    }
        
}
