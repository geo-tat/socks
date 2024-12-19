package com.geotat.socks.service;

import com.geotat.socks.dto.SocksDtoIn;
import com.geotat.socks.enums.Color;
import com.geotat.socks.enums.ComparisonOperator;
import com.geotat.socks.exception.InvalidFormatFileException;
import com.geotat.socks.exception.NotValidParametersException;
import com.geotat.socks.exception.SocksNotEnoughException;
import com.geotat.socks.model.Socks;
import com.geotat.socks.repository.SocksRepository;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SocksServiceImplTest {

    @InjectMocks
    private SocksServiceImpl socksService;

    @Mock
    private SocksRepository repository;

    private SocksDtoIn socksDto;

    @Mock
    private CSVReader csvReader;
    @Mock
    private MultipartFile file;


    @BeforeEach
    void setUp() {
        socksDto = SocksDtoIn.builder()
                .color(Color.WHITE)
                .cottonPercentage(80)
                .quantity(100L)
                .build();
    }

    @Test
    void testIncreaseSocksWithExistingSocks() {
        Socks existingSocks = Socks.builder()
                .color(Color.WHITE)
                .cottonPercentage(80)
                .quantity(50L)
                .build();

        when(repository.findByColorAndCottonPercentage(socksDto.getColor(), socksDto.getCottonPercentage()))
                .thenReturn(existingSocks);

        socksService.increaseSocks(socksDto);

        verify(repository, times(1)).save(existingSocks);
        assertEquals(150L, existingSocks.getQuantity());
    }

    @Test
    void testIncreaseSocksWithNewSocks() {
        when(repository.findByColorAndCottonPercentage(socksDto.getColor(), socksDto.getCottonPercentage()))
                .thenReturn(null);

        socksService.increaseSocks(socksDto);

        verify(repository, times(1)).save(any(Socks.class));
    }

    @Test
    void testDecreaseSocksWithEnoughQuantity() {
        Socks existingSocks = Socks.builder()
                .color(Color.WHITE)
                .cottonPercentage(80)
                .quantity(150L)
                .build();

        when(repository.findByColorAndCottonPercentage(socksDto.getColor(), socksDto.getCottonPercentage()))
                .thenReturn(existingSocks);

        SocksDtoIn decreaseDto = SocksDtoIn.builder()
                .color(Color.WHITE)
                .cottonPercentage(80)
                .quantity(50L)
                .build();

        socksService.decreaseSocks(decreaseDto);

        verify(repository, times(1)).save(existingSocks);
        assertEquals(100L, existingSocks.getQuantity());
    }

    @Test
    void testDecreaseSocksWithNotEnoughQuantity() {
        Socks existingSocks = Socks.builder()
                .color(Color.WHITE)
                .cottonPercentage(80)
                .quantity(30L)
                .build();

        when(repository.findByColorAndCottonPercentage(socksDto.getColor(), socksDto.getCottonPercentage()))
                .thenReturn(existingSocks);

        SocksDtoIn decreaseDto = SocksDtoIn.builder()
                .color(Color.WHITE)
                .cottonPercentage(80)
                .quantity(50L)
                .build();

        assertThrows(SocksNotEnoughException.class, () -> socksService.decreaseSocks(decreaseDto));
        verify(repository, never()).save(existingSocks);
    }

    @Test
    void testUpdateSocks() {
        UUID socksId = UUID.randomUUID();
        Socks existingSocks = Socks.builder()
                .id(socksId)
                .color(Color.WHITE)
                .cottonPercentage(80)
                .quantity(100L)
                .build();

        when(repository.findById(socksId)).thenReturn(Optional.of(existingSocks));

        SocksDtoIn updateDto = SocksDtoIn.builder()
                .color(Color.BLACK)
                .cottonPercentage(90)
                .quantity(200L)
                .build();

        socksService.updateSocks(socksId, updateDto);

        verify(repository, times(1)).save(existingSocks);
        assertEquals(Color.BLACK, existingSocks.getColor());
        assertEquals(90, existingSocks.getCottonPercentage());
        assertEquals(200L, existingSocks.getQuantity());
    }

    @Test
    void testUploadSocksBatchInvalidFile() throws IOException {
        MultipartFile invalidFile = mock(MultipartFile.class);
        when(invalidFile.getInputStream()).thenThrow(IOException.class);

        assertThrows(InvalidFormatFileException.class, () -> socksService.uploadSocksBatch(invalidFile));
    }

    @Test
    void testDecreaseSocksWhenSocksNotFound() {
        when(repository.findByColorAndCottonPercentage(socksDto.getColor(), socksDto.getCottonPercentage()))
                .thenReturn(null);

        NotValidParametersException exception = assertThrows(NotValidParametersException.class,
                () -> socksService.decreaseSocks(socksDto));

        assertEquals("Socks not found with specified parameters", exception.getMessage());
        verify(repository, never()).save(any(Socks.class));
    }

    @Test
    void testUpdateSocksWhenSocksNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> socksService.updateSocks(id, socksDto));

        assertEquals("Socks not found with id: " + id, exception.getMessage());
        verify(repository, never()).save(any(Socks.class));
    }

    @Test
    void testGetSocksWhenQuantityIsNotNull() {
        Color color = Color.BLACK;
        ComparisonOperator operator = ComparisonOperator.MORE_THAN;
        Integer cottonPercentage = 50;
        Long expectedQuantity = 10L;

        when(repository.findTotalQuantityByCriteria(color, cottonPercentage, operator)).thenReturn(expectedQuantity);

        Long result = socksService.getSocks(color, operator, cottonPercentage);

        assertEquals(expectedQuantity, result);
        verify(repository, times(1)).findTotalQuantityByCriteria(color, cottonPercentage, operator);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testGetSocksWhenQuantityIsNull() {
        Color color = Color.BLACK;
        ComparisonOperator operator = ComparisonOperator.MORE_THAN;
        Integer cottonPercentage = 50;

        when(repository.findTotalQuantityByCriteria(color, cottonPercentage, operator)).thenReturn(null);

        Long result = socksService.getSocks(color, operator, cottonPercentage);

        assertEquals(0L, result);
        verify(repository, times(1)).findTotalQuantityByCriteria(color, cottonPercentage, operator);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testUploadSocksBatchValidFile() throws Exception {

        String header = "Color,CottonPercentage,Quantity";
        String line1 = "WHITE,80,100";
        String line2 = "BLACK,50,200";

        when(repository.findByColorAndCottonPercentage(Color.WHITE, 80)).thenReturn(null);
        when(repository.findByColorAndCottonPercentage(Color.BLACK, 50)).thenReturn(null);


        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream((header + "\n" + line1 + "\n" + line2).getBytes()));

        socksService.uploadSocksBatch(file);

        verify(repository, times(2)).save(any(Socks.class));
    }
}
