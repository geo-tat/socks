package com.geotat.socks.repository;

import com.geotat.socks.enums.Color;
import com.geotat.socks.enums.ComparisonOperator;



public interface CustomSocksRepository {
    Long findTotalQuantityByCriteria(Color color, Integer cottonPercentage, ComparisonOperator operator);
}