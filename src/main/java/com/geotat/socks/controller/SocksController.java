package com.geotat.socks.controller;

import com.geotat.socks.dto.SocksDtoIn;
import com.geotat.socks.enums.Color;
import com.geotat.socks.enums.ComparisonOperator;
import com.geotat.socks.model.Socks;
import com.geotat.socks.service.SocksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/socks")
@RequiredArgsConstructor
public class SocksController {
    private final SocksService socksService;

    @PostMapping("/income")
    public ResponseEntity<Void> registerIncome(@RequestBody @Valid SocksDtoIn dto) {
        socksService.increaseSocks(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/outcome")
    public ResponseEntity<Void> registerOutcome(@RequestBody @Valid SocksDtoIn dto) {
        socksService.decreaseSocks(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Long> getSocks(
            @RequestParam(required = false) Color color,
            @RequestParam(required = false) ComparisonOperator operator,
            @RequestParam(required = false) Integer cottonPercentage
    ) {
        Long result = socksService.getSocks(color, operator, cottonPercentage);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Socks>> getSocksByCottonPercentageRange(
            @RequestParam(value = "minCottonPercentage", required = false) Integer minCottonPercentage,
            @RequestParam(value = "maxCottonPercentage", required = false) Integer maxCottonPercentage,
            @RequestParam(value = "sortBy", defaultValue = "cottonPercentage") String sortBy,
            @RequestParam(value = "ascending", defaultValue = "true") boolean ascending
    ) {
        return ResponseEntity.ok().body(socksService.getSocksByPercentageRange(minCottonPercentage, maxCottonPercentage,
                sortBy, ascending));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSocks(@PathVariable UUID id, @RequestBody @Valid SocksDtoIn dto) {
        socksService.updateSocks(id, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/batch")
    public ResponseEntity<Void> uploadSocksBatch(@RequestParam MultipartFile file) {
        try {
            socksService.uploadSocksBatch(file);

            log.info("Batch uploaded successfully");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Failed to process batch: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
