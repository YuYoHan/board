package com.example.web_sty.controller;

import org.apache.juli.logging.Log;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import javax.transaction.Status;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

// MockMVC를 제어하는 annotation
@AutoConfigureMockMvc
// 이건 디폴트 값이라서 그냥 @SpringBootTest해도 아래와 같은 의미이다.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void hello() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello gracelove"))
                .andDo(print());
    }
}