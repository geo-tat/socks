package com.geotat.socks.repository;

import com.geotat.socks.enums.Color;
import com.geotat.socks.model.Socks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SocksRepository extends JpaRepository<Socks, UUID>, CustomSocksRepository {
    Socks findByColorAndCottonPercentage(Color color, Integer cottonPercentage);
}
