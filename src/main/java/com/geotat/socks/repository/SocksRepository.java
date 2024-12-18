package com.geotat.socks.repository;

import com.geotat.socks.enums.Color;
import com.geotat.socks.model.Socks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SocksRepository extends JpaRepository<Socks, UUID>, CustomSocksRepository {
    Socks findByColorAndCottonPercentage(Color color, Integer cottonPercentage);

    @Query("SELECT SUM(s.quantity) FROM Socks s WHERE s.color = :color AND s.cottonPercentage > :cottonPercentage")
    Long findTotalQuantityByColorAndCottonPercentageGreaterThan(@Param("color") Color color, @Param("cottonPercentage") int cottonPercentage);

    @Query("SELECT SUM(s.quantity) FROM Socks s WHERE s.color = :color AND s.cottonPercentage < :cottonPercentage")
    Long findTotalQuantityByColorAndCottonPercentageLessThan(@Param("color") Color color, @Param("cottonPercentage") int cottonPercentage);

    @Query("SELECT SUM(s.quantity) FROM Socks s WHERE s.color = :color AND s.cottonPercentage = :cottonPercentage")
    Long findTotalQuantityByColorAndCottonPercentageEquals(@Param("color") Color color, @Param("cottonPercentage") int cottonPercentage);
}
