package com.geotat.socks.service;

import com.geotat.socks.enums.Color;
import com.geotat.socks.model.Socks;
import com.geotat.socks.util.SocksMapper;
import com.geotat.socks.repository.SocksRepository;
import com.geotat.socks.dto.SocksDtoIn;
import com.geotat.socks.enums.ComparisonOperator;
import com.geotat.socks.exception.NotValidParametersException;
import com.geotat.socks.exception.SocksNotEnoughException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
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
            existingSocks.setQuantity(existingSocks.getQuantity() + dto.getQuantity());
            socksRepository.save(existingSocks);
        } else {
            socksRepository.save(entityToSave);
        }
    }

    @Override
    public void decreaseSocks(SocksDtoIn dto) {
        Socks existingSocks = socksRepository.findByColorAndCottonPercentage(
                dto.getColor(), dto.getCottonPercentage());

        if (existingSocks == null)
            throw new NotValidParametersException("Socks not found with specified parameters");

        if (existingSocks.getQuantity() < dto.getQuantity())
            throw new SocksNotEnoughException("Not enough socks in stock");


        existingSocks.setQuantity(existingSocks.getQuantity() - dto.getQuantity());
        socksRepository.save(existingSocks);
    }

@Override
public Long getSocks(Color color, ComparisonOperator operator, int cottonPercentage) {
//
//    Long quantity;
//    switch (operator) {
//        case MORE_THAN:
//            quantity = socksRepository.findTotalQuantityByColorAndCottonPercentageGreaterThan(color, cottonPercentage);
//            break;
//        case LESS_THAN:
//            quantity = socksRepository.findTotalQuantityByColorAndCottonPercentageLessThan(color, cottonPercentage);
//            break;
//        case EQUAL:
//            quantity = socksRepository.findTotalQuantityByColorAndCottonPercentageEquals(color, cottonPercentage);
//            break;
//        default:
//            throw new IllegalArgumentException("Invalid comparison operator: " + operator);
//    }
//    return quantity != null ? quantity : 0;

    Long quantity = socksRepository.findTotalQuantityByCriteria(color, cottonPercentage, operator);
    return quantity != null ? quantity : 0L;
}

@Override
public void updateSocks(UUID id, SocksDtoIn dto) {
    Socks existingSocks = socksRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Socks not found with id: " + id));

    existingSocks.setColor(dto.getColor());
    existingSocks.setCottonPercentage(dto.getCottonPercentage());
    existingSocks.setQuantity(dto.getQuantity());

    socksRepository.save(existingSocks);
}

@Override
public void uploadSocksBatch(MultipartFile file) {

}
}
