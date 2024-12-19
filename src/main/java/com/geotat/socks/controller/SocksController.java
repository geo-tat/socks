package com.geotat.socks.controller;

import com.geotat.socks.dto.SocksDtoIn;
import com.geotat.socks.enums.Color;
import com.geotat.socks.enums.ComparisonOperator;
import com.geotat.socks.model.Socks;
import com.geotat.socks.service.SocksService;
import com.geotat.socks.swagger.Documentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Tag(name = "API Socks", description = "API для управления складом носков")
public class SocksController {

    private final SocksService socksService;

    @PostMapping("/income")
    @Operation(
            summary = Documentation.POST_INCOME_SUMMARY,
            description = Documentation.POST_INCOME_DESCRIPTION,
            responses = {
                    @ApiResponse(responseCode = Documentation.HTTP_OK, description = Documentation.OK_RESPONSE_DESCRIPTION),
                    @ApiResponse(responseCode = Documentation.HTTP_BAD_REQUEST, description = Documentation.BAD_REQUEST_DESCRIPTION)
            }
    )
    public ResponseEntity<Void> registerIncome(@RequestBody @Valid SocksDtoIn dto) {
        log.info("Registering income for socks: {}", dto);
        socksService.increaseSocks(dto);
        log.info("Successfully registered income for socks: {}", dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/outcome")
    @Operation(
            summary = Documentation.POST_OUTCOME_SUMMARY,
            description = Documentation.POST_OUTCOME_DESCRIPTION,
            responses = {
                    @ApiResponse(responseCode = Documentation.HTTP_OK, description = Documentation.OK_RESPONSE_DESCRIPTION),
                    @ApiResponse(responseCode = Documentation.HTTP_BAD_REQUEST, description = Documentation.BAD_REQUEST_DESCRIPTION)
            }
    )
    public ResponseEntity<Void> registerOutcome(@RequestBody @Valid SocksDtoIn dto) {
        log.info("Registering outcome for socks: {}", dto);
        socksService.decreaseSocks(dto);
        log.info("Successfully registered outcome for socks: {}", dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(
            summary = Documentation.GET_SOCKS_SUMMARY,
            description = Documentation.GET_SOCKS_DESCRIPTION,
            responses = {
                    @ApiResponse(responseCode = Documentation.HTTP_OK, description = Documentation.OK_RESPONSE_DESCRIPTION),
                    @ApiResponse(responseCode = Documentation.HTTP_BAD_REQUEST, description = Documentation.BAD_REQUEST_DESCRIPTION)
            }
    )
    public ResponseEntity<Long> getSocks(
            @RequestParam(required = false) @Parameter(description = Documentation.PARAMETER_COLOR) Color color,
            @RequestParam(required = false) @Parameter(description = Documentation.PARAMETER_OPERATOR) ComparisonOperator operator,
            @RequestParam(required = false) @Parameter(description = Documentation.PARAMETER_COTTON_PERCENTAGE) Integer cottonPercentage
    ) {
        log.info("Fetching socks with parameters - color: {}, operator: {}, cottonPercentage: {}", color, operator, cottonPercentage);
        Long result = socksService.getSocks(color, operator, cottonPercentage);
        log.info("Fetched socks count: {}", result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/list")
    @Operation(
            summary = Documentation.GET_SOCKS_LIST_SUMMARY,
            description = Documentation.GET_SOCKS_LIST_DESCRIPTION,
            responses = {
                    @ApiResponse(responseCode = Documentation.HTTP_OK, description = Documentation.OK_RESPONSE_DESCRIPTION),
                    @ApiResponse(responseCode = Documentation.HTTP_BAD_REQUEST, description = Documentation.BAD_REQUEST_DESCRIPTION)
            }
    )
    public ResponseEntity<List<Socks>> getSocksByCottonPercentageRange(
            @RequestParam(value = "minCottonPercentage", required = false) Integer minCottonPercentage,
            @RequestParam(value = "maxCottonPercentage", required = false) Integer maxCottonPercentage,
            @RequestParam(value = "sortBy", defaultValue = "cottonPercentage") String sortBy,
            @RequestParam(value = "ascending", defaultValue = "true") boolean ascending
    ) {
        log.info("Fetching socks by cotton percentage range - min: {}, max: {}, sortBy: {}, ascending: {}",
                minCottonPercentage, maxCottonPercentage, sortBy, ascending);
        List<Socks> socks = socksService.getSocksByPercentageRange(minCottonPercentage, maxCottonPercentage, sortBy, ascending);
        log.info("Fetched socks list of size: {}", socks.size());
        return ResponseEntity.ok().body(socks);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = Documentation.PUT_SOCKS_SUMMARY,
            description = Documentation.PUT_SOCKS_DESCRIPTION,
            responses = {
                    @ApiResponse(responseCode = Documentation.HTTP_OK, description = Documentation.OK_RESPONSE_DESCRIPTION),
                    @ApiResponse(responseCode = Documentation.HTTP_BAD_REQUEST, description = Documentation.BAD_REQUEST_DESCRIPTION),
                    @ApiResponse(responseCode = Documentation.HTTP_NOT_FOUND, description = Documentation.NOT_FOUND_DESCRIPTION)
            }
    )
    public ResponseEntity<Void> updateSocks(@PathVariable UUID id, @RequestBody @Valid SocksDtoIn dto) {
        log.info("Updating socks with ID: {}, new data: {}", id, dto);
        socksService.updateSocks(id, dto);
        log.info("Successfully updated socks with ID: {}", id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/batch")
    @Operation(
            summary = Documentation.POST_BATCH_SUMMARY,
            description = Documentation.POST_BATCH_DESCRIPTION,
            responses = {
                    @ApiResponse(responseCode = "200", description = Documentation.OK_RESPONSE_DESCRIPTION),
                    @ApiResponse(responseCode = "400", description = Documentation.POST_BATCH_ERROR_DESCRIPTION)
            }
    )
    public ResponseEntity<Void> uploadSocksBatch(@RequestParam @Parameter(description = "Файл с данными расширения .csv")
                                                 MultipartFile file) {

        log.info("Starting to upload socks batch from file: {}", file.getOriginalFilename());
        socksService.uploadSocksBatch(file);
        log.info("Batch uploaded successfully from file: {}", file.getOriginalFilename());
        return ResponseEntity.ok().build();
    }
}


