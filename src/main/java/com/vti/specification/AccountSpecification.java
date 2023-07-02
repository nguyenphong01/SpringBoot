package com.vti.specification;

import com.vti.entity.Account;
import com.vti.form.AccountFilterForm;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class AccountSpecification {
    public static Specification<Account> buildWhere(AccountFilterForm form){
        if (form == null){
            return null;
        }
        return hasUsernameLike(form.getSearch()).or(hasDepartmentNameLike(form.getSearch())).and(hasIdGreaterThanOrEqualTo(form.getMinId()).and(hasIdLessThanOrEqualTo(form.getMaxId())));
    }

    private static Specification<Account> hasUsernameLike(String search){
        return (root, query, builder) -> {
            if (!StringUtils.hasText(search)){
                return null;
            }
            return builder.like(root.get("username"), "%" + search.trim() + "%");
        };
    }

    private static Specification<Account> hasDepartmentNameLike(String search){
        return (root, query, builder) -> {
            if (!StringUtils.hasText(search)){
                return null;
            }
            return builder.like(root.get("department").get("name"), "%" + search.trim() + "%");
        };
    }

    private static Specification<Account> hasIdGreaterThanOrEqualTo(Integer minId) {
        return (root, query, builder) -> {
            if (minId == null) {
                return null;
            }
            return builder.greaterThanOrEqualTo(root.get("id"), minId);
        };
    }

    private static Specification<Account> hasIdLessThanOrEqualTo(Integer maxId) {
        return (root, query, builder) -> {
            if (maxId == null) {
                return null;
            }
            return builder.lessThanOrEqualTo(root.get("id"), maxId);
        };
    }
}
