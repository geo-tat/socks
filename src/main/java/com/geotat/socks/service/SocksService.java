package com.geotat.socks.service;

import com.geotat.socks.dto.SocksDtoIn;
import com.geotat.socks.enums.Color;
import com.geotat.socks.enums.ComparisonOperator;
import com.geotat.socks.model.Socks;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

public interface SocksService {
    void increaseSocks(SocksDtoIn dto);

    void decreaseSocks(@Valid SocksDtoIn dto);

    Long getSocks(Color color, ComparisonOperator operator, Integer cottonPercentage);

    void updateSocks(UUID id, @Valid SocksDtoIn dto);

    void uploadSocksBatch(MultipartFile file);

    List<Socks> getSocksByPercentageRange(Integer minCottonPercentage, Integer maxCottonPercentage, String sortBy, boolean ascending);
}
