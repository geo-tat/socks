package com.geotat.socks.repository;

import com.geotat.socks.enums.Color;
import com.geotat.socks.enums.ComparisonOperator;
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
    public Long findTotalQuantityByCriteria(Color color, int cottonPercentage, ComparisonOperator operator) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Socks> root = query.from(Socks.class);


        query.select(cb.sum(root.get("quantity")));


        List<Predicate> predicates = new ArrayList<>();

        if (color != null) {
            predicates.add(cb.equal(root.get("color"), color));
        }

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

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getSingleResult();
    }
}
