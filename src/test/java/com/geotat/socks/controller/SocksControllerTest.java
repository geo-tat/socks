package com.geotat.socks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geotat.socks.dto.SocksDtoIn;
import com.geotat.socks.enums.Color;
import com.geotat.socks.enums.ComparisonOperator;
import com.geotat.socks.model.Socks;
import com.geotat.socks.service.SocksServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(SocksController.class)
class SocksControllerTest {

    @MockBean
    SocksServiceImpl service;

    @Autowired
    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testRegisterIncome() throws Exception {
        SocksDtoIn dto = SocksDtoIn.builder()
                .color(Color.BLACK)
                .cottonPercentage(50)
                .quantity(100L)
                .build();

        mvc.perform(post("/api/socks/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(service, times(1)).increaseSocks(any(SocksDtoIn.class));
    }

    @Test
    void testRegisterOutcome() throws Exception {
        SocksDtoIn dto = SocksDtoIn.builder()
                .color(Color.WHITE)
                .cottonPercentage(30)
                .quantity(50L)
                .build();

        mvc.perform(post("/api/socks/outcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(service, times(1)).decreaseSocks(any(SocksDtoIn.class));
    }

    @Test
    void testGetSocks() throws Exception {
        Color color = Color.WHITE;
        Integer cottonPercentage = 50;
        ComparisonOperator operator = ComparisonOperator.MORE_THAN;
        Long expectedResult = 150L;

        when(service.getSocks(color, operator, cottonPercentage)).thenReturn(expectedResult);

        mvc.perform(get("/api/socks")
                        .param("color", color.name())
                        .param("cottonPercentage", String.valueOf(cottonPercentage))
                        .param("operator", operator.name()))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResult.toString()));

        verify(service, times(1)).getSocks(color, operator, cottonPercentage);
    }

    @Test
    void testGetSocksByCottonPercentageRange() throws Exception {
        when(service.getSocksByPercentageRange(anyInt(), anyInt(), anyString(), anyBoolean()))
                .thenReturn(List.of(new Socks()));

        mvc.perform(get("/api/socks/list")
                        .param("minCottonPercentage", "30")
                        .param("maxCottonPercentage", "70")
                        .param("sortBy", "color")
                        .param("ascending", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(service, times(1))
                .getSocksByPercentageRange(30, 70, "color", true);
    }

    @Test
    void testUploadSocksBatch() throws Exception {
        mvc.perform(multipart("/api/socks/batch")
                        .file("file", "test,csv,content".getBytes()))
                .andExpect(status().isOk());

        verify(service, times(1)).uploadSocksBatch(any());
    }

    @Test
    void testUpdateSocks() throws Exception {
        UUID id = UUID.randomUUID();
        SocksDtoIn dto = SocksDtoIn.builder()
                .color(Color.GREY)
                .cottonPercentage(40)
                .quantity(70L)
                .build();

        mvc.perform(put("/api/socks/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(service, times(1)).updateSocks(eq(id), any(SocksDtoIn.class));
    }
}