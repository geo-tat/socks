package com.geotat.socks.service;

import com.geotat.socks.dto.SocksDtoIn;
import com.geotat.socks.enums.Color;
import com.geotat.socks.enums.ComparisonOperator;
import com.geotat.socks.model.Socks;
import com.geotat.socks.repository.SocksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class SocksServiceIntegrationTest {

    @Autowired
    private SocksServiceImpl socksService;

    @Autowired
    private SocksRepository socksRepository;

    private SocksDtoIn socksDtoIn;

    @BeforeEach
    void setUp() {
        socksDtoIn = SocksDtoIn.builder()
                .color(Color.WHITE)
                .cottonPercentage(80)
                .quantity(100L)
                .build();
    }

    @Test
    void increaseSocksTest() {
        socksService.increaseSocks(socksDtoIn);

        Socks updatedSocks = socksRepository.findByColorAndCottonPercentage(Color.WHITE, 80);

        assertNotNull(updatedSocks);
        assertEquals(100L, updatedSocks.getQuantity());
    }

    @Test
    void decreaseSocksTest() {
        socksService.increaseSocks(socksDtoIn);
        SocksDtoIn decreaseDto = SocksDtoIn.builder()
                .color(Color.WHITE)
                .cottonPercentage(80)
                .quantity(50L)
                .build();

        socksService.decreaseSocks(decreaseDto);

        Socks updatedSocks = socksRepository.findByColorAndCottonPercentage(Color.WHITE, 80);

        assertNotNull(updatedSocks);
        assertEquals(50L, updatedSocks.getQuantity());
    }

    @Test
    void getSocks_shouldReturnTotalQuantity() {
        socksService.increaseSocks(socksDtoIn);

        Long totalQuantity = socksService.getSocks(Color.WHITE, ComparisonOperator.EQUAL, 80);

        assertNotNull(totalQuantity);
        assertEquals(100L, totalQuantity);
    }

    @Test
    void updateSocks_shouldUpdateFields() {
        socksService.increaseSocks(socksDtoIn);
        Socks existingSocks = socksRepository.findByColorAndCottonPercentage(Color.WHITE, 80);
        UUID id = existingSocks.getId();

        SocksDtoIn updateDto = SocksDtoIn.builder()
                .color(Color.BLACK)
                .cottonPercentage(90)
                .quantity(200L)
                .build();

        socksService.updateSocks(id, updateDto);

        Socks updatedSocks = socksRepository.findById(id).orElseThrow();
        assertEquals(Color.BLACK, updatedSocks.getColor());
        assertEquals(90, updatedSocks.getCottonPercentage());
        assertEquals(200L, updatedSocks.getQuantity());
    }
}
