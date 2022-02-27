package com.example.tiktokproject.controller;

import com.example.tiktokproject.model.pojo.User;
import com.example.tiktokproject.model.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository repository;

    @Test
    void setupTest() {
        Assert.notNull(mockMvc);
    }

    @Test
    void checkUsernameTest() {
        User u = repository.findByUsername("mihail").orElseThrow();
        assertEquals(u.getUsername(), "mihail");
    }

    @Test
    void setVerifyFieldTest() {
        User u = repository.findByUsername("mihail").orElseThrow();
        u.setVerified(true);
        Assert.isTrue(u.isVerified());
    }

}