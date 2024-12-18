package com.geotat.socks.repository;

import com.geotat.socks.enums.Color;
import com.geotat.socks.enums.ComparisonOperator;
import com.geotat.socks.exception.NotValidParametersException;
import com.geotat.socks.model.Socks;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomSocksRepositoryImpl implements CustomSocksRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long findTotalQuantityByCriteria(Color color, Integer cottonPercentage, ComparisonOperator operator) {

        if ((cottonPercentage == null && operator != null) || (cottonPercentage != null && operator == null)) {
            throw new NotValidParametersException("Invalid filter parameters: both cottonPercentage and operator must be specified together.");
        }
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Socks> root = query.from(Socks.class);

        query.select(cb.sum(root.get("quantity")));

        List<Predicate> predicates = new ArrayList<>();

        if (color != null) {
            predicates.add(cb.equal(root.get("color"), color));
        }

        if (cottonPercentage != null && operator != null) {
            switch (operator) {
                case MORE_THAN:
                    predicates.add(cb.greaterThan(root.get("cottonPercentage"), cottonPercentage));
                    break;
                case LESS_THAN:
                    predicates.add(cb.lessThan(root.get("cottonPercentage"), cottonPercentage));
                    break;
                case EQUAL:
                    predicates.add(cb.equal(root.get("cottonPercentage"), cottonPercentage));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid comparison operator: " + operator);
            }
        }
        query.where(predicates.toArray(new Predicate[0]));

        Long result = entityManager.createQuery(query).getSingleResult();
        return result != null ? result : 0L;
    }

    @Override
    public List<Socks> findSocksByCottonPercentageRangeAndSort(Integer minCottonPercentage, Integer maxCottonPercentage, String sortBy, boolean ascending) {

        if (minCottonPercentage != null && maxCottonPercentage != null && minCottonPercentage > maxCottonPercentage) {
            throw new NotValidParametersException("Min cotton percentage cannot be greater than max cotton percentage.");
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Socks> query = cb.createQuery(Socks.class);
        Root<Socks> root = query.from(Socks.class);

        List<Predicate> predicates = new ArrayList<>();


        if (minCottonPercentage != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("cottonPercentage"), minCottonPercentage));
        }

        if (maxCottonPercentage != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("cottonPercentage"), maxCottonPercentage));
        }

        // Устанавливаем предикаты для запроса
        query.where(predicates.toArray(new Predicate[0]));

        // Сортировка
        if (sortBy != null) {
            if (sortBy.equalsIgnoreCase("color")) {
                if (ascending) {
                    query.orderBy(cb.asc(root.get("color")));
                } else {
                    query.orderBy(cb.desc(root.get("color")));
                }
            } else if (sortBy.equalsIgnoreCase("cottonPercentage")) {
                if (ascending) {
                    query.orderBy(cb.asc(root.get("cottonPercentage")));
                } else {
                    query.orderBy(cb.desc(root.get("cottonPercentage")));
                }
            }
        }
        return entityManager.createQuery(query).getResultList();
    }
}
