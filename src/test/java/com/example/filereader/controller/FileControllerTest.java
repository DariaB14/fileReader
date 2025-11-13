package com.example.filereader.controller;

import com.example.filereader.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileController.class)
class FileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @Test
    void findNMinWithValidInput() throws Exception {
        when(fileService.findNMinNumber("test.xlsx", 3)).thenReturn(5);

        mockMvc.perform(post("/api/file/search")
                        .param("path", "test.xlsx")
                        .param("n", "3"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void findNMinNumberWithInvalidNParameter() throws Exception {
        mockMvc.perform(post("/api/file/search")
                        .param("path", "test.xlsx")
                        .param("n", "null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findNMinNumberWhereNIsGreaterThanFile() throws Exception {
        String message ="N cannot be greater than the number of elements";
        when(fileService.findNMinNumber("test.xlsx", 3))
                .thenThrow(new IllegalArgumentException(message));

        mockMvc.perform(post("/api/file/search")
                        .param("path", "test.xlsx")
                        .param("n", "3"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(message));
    }

    @Test
    void findNMinNumberWithInternalException() throws Exception {
        when(fileService.findNMinNumber("test.xlsx", 3)).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/api/file/search")
                        .param("path", "test.xlsx")
                        .param("n", "3"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal error"));
    }
}