package com.geotat.socks.repository;

import com.geotat.socks.enums.Color;
import com.geotat.socks.enums.ComparisonOperator;
import com.geotat.socks.model.Socks;

import java.util.List;


public interface CustomSocksRepository {
    Long findTotalQuantityByCriteria(Color color, Integer cottonPercentage, ComparisonOperator operator);
    List<Socks> findSocksByCottonPercentageRangeAndSort(Integer minCottonPercentage, Integer maxCottonPercentage, String sortBy, boolean ascending);
}