package com.ratan.loginapi.actor.dto;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public class SpecificationBuilder<T> {
    public Specification<T> buildFromFilters(Map<String, Object> filters) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                String field = entry.getKey();
                Object value = entry.getValue();

                if (value instanceof String) {
                    predicate = cb.and(predicate,
                            cb.like(cb.lower(root.get(field)), "%" + value.toString().toLowerCase() + "%"));
                } else {
                    predicate = cb.and(predicate,
                            cb.equal(root.get(field), value));
                }
            }

            return predicate;
        };
    }
}
