package com.geotat.socks.dto;

import com.geotat.socks.annotation.Percentage;
import com.geotat.socks.enums.Color;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.PositiveOrZero;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SocksDtoIn {

    @Enumerated(EnumType.STRING)
    @Schema(description = "Цвет", allowableValues = {"""
            WHITE,
            BLACK,
            GREEN,
            YELLOW,
            BLUE,
            GREY,
            DARK_GREY,
            PINK"""})
    private Color color;

    @Percentage
    @Schema(description = "Процент содержания хлопка")
    private Integer cottonPercentage;

    @PositiveOrZero
    @Schema(description = "Количество штук")
    private Long quantity;
}
