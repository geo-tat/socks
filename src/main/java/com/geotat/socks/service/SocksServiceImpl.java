package com.geotat.socks.service;

import com.geotat.socks.dto.SocksDtoIn;
import com.geotat.socks.enums.Color;
import com.geotat.socks.enums.ComparisonOperator;
import com.geotat.socks.exception.InvalidFormatFileException;
import com.geotat.socks.exception.NotValidParametersException;
import com.geotat.socks.exception.SocksNotEnoughException;
import com.geotat.socks.model.Socks;
import com.geotat.socks.repository.SocksRepository;
import com.geotat.socks.util.SocksMapper;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocksServiceImpl implements SocksService {

    private final SocksRepository socksRepository;

    @Override
    public void increaseSocks(SocksDtoIn dto) {
        Socks entityToSave = SocksMapper.toEntity(dto);

        Socks existingSocks = socksRepository.findByColorAndCottonPercentage(
                dto.getColor(), dto.getCottonPercentage());

        if (existingSocks != null) {
            log.debug("Socks already exist. Updating quantity for color: {}, cottonPercentage: {}",
                    dto.getColor(), dto.getCottonPercentage());
            existingSocks.setQuantity(existingSocks.getQuantity() + dto.getQuantity());
            socksRepository.save(existingSocks);
            log.info("Successfully added. New quantity={}", existingSocks.getQuantity());
        } else {
            socksRepository.save(entityToSave);
            log.info("Successfully added new socks: {}", entityToSave);
        }
    }

    @Override
    public void decreaseSocks(SocksDtoIn dto) {
        Socks existingSocks = socksRepository.findByColorAndCottonPercentage(
                dto.getColor(), dto.getCottonPercentage());

        if (existingSocks == null) {
            log.warn("No socks found with color: {} and cottonPercentage: {}",
                    dto.getColor(), dto.getCottonPercentage());
            throw new NotValidParametersException("Socks not found with specified parameters");
        }
        if (existingSocks.getQuantity() < dto.getQuantity()) {
            log.warn("Not enough socks in stock. Requested: {}, Available: {}",
                    dto.getQuantity(), existingSocks.getQuantity());
            throw new SocksNotEnoughException("Not enough socks in stock");
        }

        existingSocks.setQuantity(existingSocks.getQuantity() - dto.getQuantity());
        socksRepository.save(existingSocks);
        log.info("Successfully decreased socks. New quantity={}", existingSocks.getQuantity());
    }

    @Override
    public Long getSocks(Color color, ComparisonOperator operator, Integer cottonPercentage) {
        Long quantity = socksRepository.findTotalQuantityByCriteria(color, cottonPercentage, operator);
        log.info("Fetched socks count: {}", quantity != null ? quantity : 0L);
        return quantity != null ? quantity : 0L;
    }

    @Override
    public void updateSocks(UUID id, SocksDtoIn dto) {
        Socks existingSocks = socksRepository.findById(id)
                .orElseThrow(() ->{
                    log.warn("Socks not found with ID: {}", id);
                    return new EntityNotFoundException("Socks not found with id: " + id);
                });
        if (dto.getColor() != null)
            existingSocks.setColor(dto.getColor());
        if (dto.getCottonPercentage() != null)
            existingSocks.setCottonPercentage(dto.getCottonPercentage());
        if (dto.getQuantity() != null)
            existingSocks.setQuantity(dto.getQuantity());

        socksRepository.save(existingSocks);
        log.info("Successfully updated socks: {}", existingSocks);
    }

    @Override
    public void uploadSocksBatch(MultipartFile file) {

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] line;
            List<SocksDtoIn> socksList = new ArrayList<>();


            csvReader.skip(1);


            while ((line = csvReader.readNext()) != null) {
                String colorStr = line[0];
                Integer percentage = Integer.parseInt(line[1]);
                Long quantity = Long.parseLong(line[2]);
                Color color = Color.valueOf(colorStr.toUpperCase());

                SocksDtoIn sock = SocksDtoIn.builder()
                        .color(color)
                        .quantity(quantity)
                        .cottonPercentage(percentage)
                        .build();

                socksList.add(sock);
            }

            for (SocksDtoIn dto : socksList) {
                Socks existingSocks = socksRepository.findByColorAndCottonPercentage(
                        dto.getColor(), dto.getCottonPercentage());

                if (existingSocks != null) {
                    existingSocks.setQuantity(existingSocks.getQuantity() + dto.getQuantity());
                    socksRepository.save(existingSocks);
                } else {
                    socksRepository.save(SocksMapper.toEntity(dto));
                }
            }
        } catch (Exception e) {
            throw new InvalidFormatFileException("Error processing the CSV file");
        }
    }

    @Override
    public List<Socks> getSocksByPercentageRange(Integer minCottonPercentage, Integer maxCottonPercentage,
                                                 String sortBy, boolean ascending) {
        return socksRepository.findSocksByCottonPercentageRangeAndSort(minCottonPercentage,
                maxCottonPercentage,sortBy,ascending);
    }
}
