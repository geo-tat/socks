package com.geotat.socks.service;

import com.geotat.socks.dto.SocksDtoIn;
import com.geotat.socks.enums.Color;
import com.geotat.socks.enums.ComparisonOperator;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.UUID;

public interface SocksService {
    void increaseSocks(SocksDtoIn dto);

    void decreaseSocks(@Valid SocksDtoIn dto);

    Long getSocks(Color color, ComparisonOperator operator, Integer cottonPercentage);

    void updateSocks(UUID id, @Valid SocksDtoIn dto);

    void uploadSocksBatch(MultipartFile file);
}
