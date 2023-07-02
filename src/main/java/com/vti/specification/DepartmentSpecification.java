package com.vti.specification;

import com.vti.entity.Department;
import com.vti.form.DepartmentFilterForm;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class DepartmentSpecification {
    public static Specification<Department> buildWhere(DepartmentFilterForm form) {
        if (form == null) {
            return null;
        }
        return hasCreatedDateEqual(form.getCreatedDate())
                .and(hasCreatedDateGreaterThanOrEqualTo(form.getMinCreatedDate()))
                .and(hasCreatedDateLessThanOrEqualTo(form.getMaxCreatedDate()))
                .and(hasCreatedYearGreaterThanOrEqualTo(form.getMinCreatedYear()))
                .and(hasTypeEqual(form.getType()));

    }

    private static Specification<Department> hasCreatedDateEqual(LocalDate createdDate) {
        return (root, query, builder) -> {
            if (createdDate == null) {
                return null;
            }
            return builder.equal(root.get("createdDate").as(LocalDate.class), createdDate);
        };
    }

    private static Specification<Department> hasCreatedDateGreaterThanOrEqualTo(LocalDate minCreatedDate) {
        return (root, query, builder) -> {
            if (minCreatedDate == null) {
                return null;
            }
            return builder.greaterThanOrEqualTo(root.get("createdDate").as(LocalDate.class), minCreatedDate);
        };
    }

    private static Specification<Department> hasCreatedDateLessThanOrEqualTo(LocalDate maxCreatedDate) {
        return (root, query, builder) -> {
            if (maxCreatedDate == null) {
                return null;
            }
            return builder.lessThanOrEqualTo(root.get("createdDate").as(LocalDate.class), maxCreatedDate);
        };
    }
    private static Specification<Department> hasCreatedYearGreaterThanOrEqualTo(Integer minCreatedYear) {
        return (root, query, builder) -> {
            if (minCreatedYear == null) {
                return null;
            }
            return builder.greaterThanOrEqualTo(
                    builder.function("YEAR", Integer.class, root.get("createdDate")),
                    minCreatedYear
            );
        };
    }
    private static Specification<Department> hasTypeEqual(Department.Type type) {
        return (root, query, builder) -> {
            if (type == null) {
                return null;
            }
            return builder.equal(root.get("type"), type);
        };
    }
}
