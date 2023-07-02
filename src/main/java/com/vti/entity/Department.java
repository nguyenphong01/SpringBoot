package com.vti.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "department")
@Getter
@Setter
public class Department {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 50, unique = true, nullable = false)
    private String name;

    @Column(name = "total_members", nullable = false)
    private Integer totalMembers;

    @Column(name = "type", length = 15, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Type type;

    public enum Type{
        DEVELOPER, TESTER, SCRUM_MASTER, PROJECT_MANAGER
    }

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate createdDate;


    @OneToMany(mappedBy = "department", cascade = {CascadeType.REMOVE, CascadeType.MERGE})   //
    private List<Account> accounts;


}
