package com.geotat.socks.util;

import com.geotat.socks.dto.SocksDtoIn;
import com.geotat.socks.model.Socks;

public class SocksMapper {

    public static Socks toEntity(SocksDtoIn dto) {
        return Socks.builder()
                .color(dto.getColor())
                .cottonPercentage(dto.getCottonPercentage())
                .quantity(dto.getQuantity())
                .build();
    }

}
